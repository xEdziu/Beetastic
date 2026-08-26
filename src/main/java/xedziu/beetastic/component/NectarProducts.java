package xedziu.beetastic.component;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import xedziu.beetastic.honey.NectarProfile;

public final class NectarProducts {
	private NectarProducts() {
	}

	public static ItemStack honeyBottle(NectarProfile profile) {
		return applyProfile(new ItemStack(Items.HONEY_BOTTLE), profile, true);
	}

	public static ItemStack honeycomb(NectarProfile profile, int count) {
		return applyProfile(new ItemStack(Items.HONEYCOMB, count), profile, false);
	}

	private static ItemStack applyProfile(ItemStack stack, NectarProfile profile, boolean bottle) {
		if (profile.isWildflower()) {
			return stack;
		}

		stack.set(BeetasticComponents.NECTAR_PROFILE, profile.id());
		stack.set(
			DataComponents.ITEM_NAME,
			Component.translatable(bottle ? profile.honeyBottleTranslationKey() : profile.honeycombTranslationKey())
		);
		stack.set(DataComponents.ITEM_MODEL, bottle ? profile.honeyBottleModel() : profile.honeycombModel());
		return stack;
	}
}
