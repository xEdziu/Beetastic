package xedziu.beetastic.component;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

import xedziu.beetastic.Beetastic;

public final class BeetasticComponents {
	public static final DataComponentType<Identifier> NECTAR_PROFILE = Registry.register(
		BuiltInRegistries.DATA_COMPONENT_TYPE,
		Beetastic.id("nectar_profile"),
		DataComponentType.<Identifier>builder()
			.persistent(Identifier.CODEC)
			.networkSynchronized(Identifier.STREAM_CODEC)
			.build()
	);

	private BeetasticComponents() {
	}

	public static void initialize() {
		// Loading this class registers its component types.
	}
}
