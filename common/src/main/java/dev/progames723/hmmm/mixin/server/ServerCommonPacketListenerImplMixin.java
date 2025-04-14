package dev.progames723.hmmm.mixin.server;

import dev.progames723.hmmm.networking.HmmmNetworking;
import net.minecraft.network.protocol.common.ServerCommonPacketListener;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommonPacketListenerImpl.class)
public abstract class ServerCommonPacketListenerImplMixin implements ServerCommonPacketListener {
	@Inject(
		method = "handleCustomPayload",
		at = @At("HEAD"),
		cancellable = true
	)
	public void handleCustomPayloadInject(ServerboundCustomPayloadPacket packet, CallbackInfo ci) {
		if (!(packet.payload() instanceof HmmmNetworking.MessagePacketPayload packetPayload)) return;
		HmmmNetworking.iterateThroughHandlers(packetPayload);
		ci.cancel();
	}
}
