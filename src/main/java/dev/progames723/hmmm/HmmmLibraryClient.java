package dev.progames723.hmmm;

import dev.progames723.hmmm.event.api.AutoRegisterEvents;
import dev.progames723.hmmm.interface_injection.PlayerAttachments;
import dev.progames723.hmmm.misc.ClientOnly;
import dev.progames723.hmmm.networking.HmmmNetworking;
import dev.progames723.hmmm.obj_attach.AttachmentSync;
import dev.progames723.hmmm.obj_attach.SynchronizedAttachment;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

//$ clientOnly
@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
@AutoRegisterEvents
public class HmmmLibraryClient implements ClientOnly {
	public static void init() {
		HmmmNetworking.registerS2CMessageHandler(payload -> {
			if (!payload.message().getID().equals(AttachmentSync.ATTACHMENT_SYNC_ID)) return;
			if (!(payload.message() instanceof HmmmNetworking.S2CMessage message)) return;
			FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
			message.writeToBuf(buf);
			SynchronizedAttachment provider = AttachmentSync.readData(buf);
			if (provider == null) {
				HmmmLibrary.LOGGER.error("Bad packet!", new HmmmException(null, "Bad packet!"));
				return;
			}
			Player player = Minecraft.getInstance().player;
			if (player == null) return;
			((PlayerAttachments) player).addPlayerAttachment(provider);
		});
	}
}
