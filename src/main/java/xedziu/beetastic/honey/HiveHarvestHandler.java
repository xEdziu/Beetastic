package xedziu.beetastic.honey;

import java.util.List;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Util;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

import xedziu.beetastic.attachment.BeetasticAttachments;
import xedziu.beetastic.component.NectarProducts;

public final class HiveHarvestHandler {
	private HiveHarvestHandler() {
	}

	public static void initialize() {
		UseBlockCallback.EVENT.register(HiveHarvestHandler::harvest);
	}

	private static InteractionResult harvest(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
		if (player.isSpectator()) {
			return InteractionResult.PASS;
		}

		BlockPos pos = hitResult.getBlockPos();
		BlockState state = level.getBlockState(pos);
		ItemStack heldStack = player.getItemInHand(hand);
		if (!(state.getBlock() instanceof BeehiveBlock beehiveBlock)
			|| state.getValue(BeehiveBlock.HONEY_LEVEL) < NectarBatch.MAX_CONTRIBUTIONS
			|| (!heldStack.is(Items.GLASS_BOTTLE) && !heldStack.is(Items.SHEARS))) {
			return InteractionResult.PASS;
		}

		if (!(level instanceof ServerLevel serverLevel)) {
			return InteractionResult.SUCCESS;
		}

		if (!(level.getBlockEntity(pos) instanceof BeehiveBlockEntity hive)) {
			return InteractionResult.PASS;
		}

		NectarBatch batch = hive.getAttachedOrCreate(BeetasticAttachments.HIVE_NECTAR_BATCH).completeForHarvest();
		NectarProfile profile = batch.dominantProfile();
		Item usedItem = heldStack.getItem();

		if (heldStack.is(Items.SHEARS)) {
			Block.popResource(serverLevel, pos, NectarProducts.honeycomb(profile, 3));
			level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
			heldStack.hurtAndBreak(1, player, hand.asEquipmentSlot());
			level.gameEvent(player, GameEvent.SHEAR, pos);
		} else {
			heldStack.shrink(1);
			level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
			giveOrDrop(player, hand, heldStack, NectarProducts.honeyBottle(profile));
			level.gameEvent(player, GameEvent.FLUID_PICKUP, pos);
		}

		player.awardStat(Stats.ITEM_USED.get(usedItem));
		hive.setAttached(BeetasticAttachments.HIVE_NECTAR_BATCH, NectarBatch.EMPTY);
		hive.setChanged();

		if (CampfireBlock.isSmokeyPos(level, pos)) {
			beehiveBlock.resetHoneyLevel(level, state, pos);
		} else {
			angerNearbyBees(level, pos);
			beehiveBlock.releaseBeesAndResetHoneyLevel(
				level,
				state,
				pos,
				player,
				BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY
			);
		}

		return InteractionResult.SUCCESS;
	}

	private static void giveOrDrop(Player player, InteractionHand hand, ItemStack emptiedBottleStack, ItemStack honeyBottle) {
		if (emptiedBottleStack.isEmpty()) {
			player.setItemInHand(hand, honeyBottle);
		} else if (!player.getInventory().add(honeyBottle)) {
			player.drop(honeyBottle, false);
		}
	}

	private static void angerNearbyBees(Level level, BlockPos pos) {
		AABB area = new AABB(pos).inflate(8.0, 6.0, 8.0);
		List<Bee> bees = level.getEntitiesOfClass(Bee.class, area);
		List<Player> players = level.getEntitiesOfClass(Player.class, area);
		if (players.isEmpty()) {
			return;
		}

		for (Bee bee : bees) {
			if (bee.getTarget() == null) {
				bee.setTarget(Util.getRandom(players, level.getRandom()));
			}
		}
	}
}
