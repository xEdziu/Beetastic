package xedziu.beetastic.honey;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import xedziu.beetastic.attachment.BeetasticAttachments;

public final class NectarDelivery {
	private NectarDelivery() {
	}

	public static void record(Bee bee) {
		if (!(bee.level() instanceof ServerLevel level)) {
			return;
		}

		BlockPos hivePos = bee.getHivePos();
		if (hivePos == null || !(level.getBlockEntity(hivePos) instanceof BeehiveBlockEntity hive)) {
			return;
		}

		BlockState hiveState = level.getBlockState(hivePos);
		if (!hiveState.hasProperty(BeehiveBlock.HONEY_LEVEL)) {
			return;
		}

		int honeyLevel = hiveState.getValue(BeehiveBlock.HONEY_LEVEL);
		if (honeyLevel >= NectarBatch.MAX_CONTRIBUTIONS) {
			return;
		}

		NectarProfile profile = NectarProfile.WILDFLOWER;
		BlockPos flowerPos = bee.getSavedFlowerPos();
		if (flowerPos != null && level.isLoaded(flowerPos)) {
			profile = NectarProfile.fromFlower(level.getBlockState(flowerPos));
		}

		NectarBatch current = hive.getAttachedOrCreate(BeetasticAttachments.HIVE_NECTAR_BATCH);
		hive.setAttached(BeetasticAttachments.HIVE_NECTAR_BATCH, current.recordDelivery(honeyLevel, profile));
		hive.setChanged();
	}
}
