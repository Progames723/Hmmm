package dev.progames723.hmmm.obj_attach;

import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.annotations.Temporary;
import dev.progames723.hmmm.networking.HmmmNetworking;
import dev.progames723.hmmm.utils.MiscUtil;
import dev.progames723.hmmm.utils.SerializationUtil;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Consumer;
import java.util.function.Function;

public class AttachmentSync {
	private AttachmentSync() {MiscUtil.instantiationOfUtilClass();}
	
	public static final ResourceLocation ATTACHMENT_SYNC_ID = HmmmLibrary.createRL(HmmmLibrary.MOD_ID, "attachment_sync");
	
	private static final Function<SynchronizedAttachment, Consumer<FriendlyByteBuf>> funnyFunction = sync -> buf -> {
		byte[] data;
		try {
			data = SerializationUtil.serializeAndCompressObject(sync);
		} catch (Exception e) {
			data = new byte[] {-1};
		}
		buf.writeByteArray(data);
		byte[] classData;
		try {
			classData = SerializationUtil.serializeAndCompressObject(sync.getClass());
		} catch (Exception e) {
			classData = new byte[] {-1};
		}
		buf.writeByteArray(classData);
	};
	
	public static void sendToClient(SynchronizedAttachment sync, ServerPlayer player) {
		if (!sync.canSync()) return;
		HmmmNetworking.S2CSend(new HmmmNetworking.S2CMessage(player, ATTACHMENT_SYNC_ID, funnyFunction.apply(sync)));
	}
	
	//$ clientOnly
	@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
	public static void sendToServer(SynchronizedAttachment sync, LocalPlayer player) {
		if (!sync.canSyncBack()) return;
		HmmmNetworking.C2SSend(new HmmmNetworking.C2SMessage(player.getUUID(), ATTACHMENT_SYNC_ID, funnyFunction.apply(sync)));
	}
	
	@SuppressWarnings("unchecked")
	public static SynchronizedAttachment readData(FriendlyByteBuf buf) {
		try {
			byte[] data = buf.readByteArray();
			if (data.length == 1) throw new IllegalArgumentException("Invalid object data!");
			byte[] classData = buf.readByteArray();
			if (classData.length == 1) throw new IllegalArgumentException("Invalid class data!");
			Class<? extends SynchronizedAttachment> cls = (Class<? extends SynchronizedAttachment>) SerializationUtil.decompressAndDeserializeObject(classData, Class.class);
			return SerializationUtil.decompressAndDeserializeObject(data, cls);
		} catch (Exception e) {
			HmmmLibrary.LOGGER.error("Deserialization failed!", e);
			return null;
		}
	}
}
