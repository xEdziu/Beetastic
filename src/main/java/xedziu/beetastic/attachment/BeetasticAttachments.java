package xedziu.beetastic.attachment;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

import net.minecraft.network.codec.ByteBufCodecs;

import xedziu.beetastic.Beetastic;
import xedziu.beetastic.honey.NectarBatch;

public final class BeetasticAttachments {
	public static final AttachmentType<NectarBatch> HIVE_NECTAR_BATCH = AttachmentRegistry.create(
		Beetastic.id("hive_nectar_batch"),
		builder -> builder
			.initializer(() -> NectarBatch.EMPTY)
			.persistent(NectarBatch.CODEC)
			.syncWith(ByteBufCodecs.fromCodecWithRegistries(NectarBatch.CODEC), AttachmentSyncPredicate.all())
	);

	private BeetasticAttachments() {
	}

	public static void initialize() {
		// Loading this class registers its attachment types.
	}
}
