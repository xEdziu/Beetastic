package xedziu.beetastic.honey;

import java.util.Arrays;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import xedziu.beetastic.Beetastic;

public enum NectarProfile {
	WILDFLOWER("wildflower", null),
	DANDELION("dandelion", flowerTag("dandelion")),
	CORNFLOWER("cornflower", flowerTag("cornflower"));

	private final String path;
	private final Identifier id;
	private final TagKey<Block> flowerTag;

	NectarProfile(String path, TagKey<Block> flowerTag) {
		this.path = path;
		this.id = Beetastic.id(path);
		this.flowerTag = flowerTag;
	}

	public Identifier id() {
		return this.id;
	}

	public String honeyBottleTranslationKey() {
		return "item.beetastic." + this.path + "_honey_bottle";
	}

	public String honeycombTranslationKey() {
		return "item.beetastic." + this.path + "_honeycomb";
	}

	public Identifier honeyBottleModel() {
		return Beetastic.id(this.path + "_honey_bottle");
	}

	public Identifier honeycombModel() {
		return Beetastic.id(this.path + "_honeycomb");
	}

	public boolean isWildflower() {
		return this == WILDFLOWER;
	}

	public static NectarProfile fromFlower(BlockState flower) {
		return Arrays.stream(values())
			.filter(profile -> profile.flowerTag != null && flower.is(profile.flowerTag))
			.findFirst()
			.orElse(WILDFLOWER);
	}

	public static NectarProfile fromId(Identifier id) {
		return Arrays.stream(values())
			.filter(profile -> profile.id.equals(id))
			.findFirst()
			.orElse(WILDFLOWER);
	}

	private static TagKey<Block> flowerTag(String path) {
		return TagKey.create(Registries.BLOCK, Beetastic.id("nectar_sources/" + path));
	}
}
