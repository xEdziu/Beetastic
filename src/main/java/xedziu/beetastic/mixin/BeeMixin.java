package xedziu.beetastic.mixin;

import net.minecraft.world.entity.animal.bee.Bee;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import xedziu.beetastic.honey.NectarDelivery;

@Mixin(Bee.class)
public abstract class BeeMixin {
	@Inject(method = "dropOffNectar", at = @At("HEAD"))
	private void beetastic$recordNectarDelivery(CallbackInfo info) {
		NectarDelivery.record((Bee)(Object)this);
	}
}
