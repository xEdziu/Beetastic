package xedziu.beetastic;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xedziu.beetastic.attachment.BeetasticAttachments;
import xedziu.beetastic.clientcontent.BeetasticCreativeTabs;
import xedziu.beetastic.component.BeetasticComponents;
import xedziu.beetastic.honey.HiveHarvestHandler;

public class Beetastic implements ModInitializer {
	public static final String MOD_ID = "beetastic";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		BeetasticComponents.initialize();
		BeetasticAttachments.initialize();
		BeetasticCreativeTabs.initialize();
		HiveHarvestHandler.initialize();

		LOGGER.info("Beetastic initialized");
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
