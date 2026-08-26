package xedziu.beetastic.honey;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.mojang.serialization.Codec;

import net.minecraft.resources.Identifier;

public record NectarBatch(List<Identifier> contributions) {
	public static final int MAX_CONTRIBUTIONS = 5;
	public static final int DOMINANCE_THRESHOLD = 3;
	public static final NectarBatch EMPTY = new NectarBatch(List.of());
	public static final Codec<NectarBatch> CODEC = Identifier.CODEC.listOf()
		.xmap(NectarBatch::new, NectarBatch::contributions);

	public NectarBatch {
		contributions = List.copyOf(
			contributions.subList(0, Math.min(MAX_CONTRIBUTIONS, contributions.size()))
		);
	}

	public NectarBatch recordDelivery(int currentHoneyLevel, NectarProfile profile) {
		List<Identifier> updated = new ArrayList<>(this.contributions);
		while (updated.size() < Math.min(currentHoneyLevel, MAX_CONTRIBUTIONS)) {
			updated.add(NectarProfile.WILDFLOWER.id());
		}

		if (updated.size() < MAX_CONTRIBUTIONS) {
			updated.add(profile.id());
		}

		return new NectarBatch(updated);
	}

	public NectarBatch completeForHarvest() {
		List<Identifier> completed = new ArrayList<>(this.contributions);
		Identifier fallback = completed.isEmpty()
			? NectarProfile.WILDFLOWER.id()
			: completed.getLast();

		while (completed.size() < MAX_CONTRIBUTIONS) {
			completed.add(fallback);
		}

		return new NectarBatch(completed);
	}

	public NectarProfile dominantProfile() {
		Map<Identifier, Long> counts = this.contributions.stream()
			.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		return counts.entrySet().stream()
			.filter(entry -> entry.getValue() >= DOMINANCE_THRESHOLD)
			.max(Map.Entry.comparingByValue(Comparator.naturalOrder()))
			.map(Map.Entry::getKey)
			.map(NectarProfile::fromId)
			.orElse(NectarProfile.WILDFLOWER);
	}
}
