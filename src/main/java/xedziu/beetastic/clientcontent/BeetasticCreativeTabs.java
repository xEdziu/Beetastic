package xedziu.beetastic.clientcontent;

import java.util.Arrays;
import java.util.List;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import xedziu.beetastic.component.NectarProducts;
import xedziu.beetastic.honey.NectarProfile;

public final class BeetasticCreativeTabs {
	private static final ResourceKey<CreativeModeTab> FOOD_AND_DRINKS = ResourceKey.create(
		Registries.CREATIVE_MODE_TAB,
		Identifier.fromNamespaceAndPath("minecraft", "food_and_drinks")
	);
	private static final ResourceKey<CreativeModeTab> INGREDIENTS = ResourceKey.create(
		Registries.CREATIVE_MODE_TAB,
		Identifier.fromNamespaceAndPath("minecraft", "ingredients")
	);

	private BeetasticCreativeTabs() {
	}

	public static void initialize() {
		CreativeModeTabEvents.modifyOutputEvent(FOOD_AND_DRINKS).register(output ->
			output.insertAfter(Items.HONEY_BOTTLE, honeyBottles(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
		);
		CreativeModeTabEvents.modifyOutputEvent(INGREDIENTS).register(output ->
			output.insertAfter(Items.HONEYCOMB, honeycombs(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
		);
	}

	private static List<ItemStack> honeyBottles() {
		return varietalProfiles().stream().map(NectarProducts::honeyBottle).toList();
	}

	private static List<ItemStack> honeycombs() {
		return varietalProfiles().stream().map(profile -> NectarProducts.honeycomb(profile, 1)).toList();
	}

	private static List<NectarProfile> varietalProfiles() {
		return Arrays.stream(NectarProfile.values()).filter(profile -> !profile.isWildflower()).toList();
	}
}
