package dev.progames723.hmmm.mixin.client;

import dev.progames723.hmmm.networking.HmmmNetworking;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.network.protocol.common.ClientCommonPacketListener;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientCommonPacketListenerImpl.class)
public abstract class ClientCommonPacketListenerImplMixin implements ClientCommonPacketListener {
	@Inject(
		method = "handleCustomPayload(Lnet/minecraft/network/protocol/common/ClientboundCustomPayloadPacket;)V",
		at = @At("HEAD"),
		cancellable = true
	)
	private void handleCustomPayloadInject(ClientboundCustomPayloadPacket packet, CallbackInfo ci) {
		if (!(packet.payload() instanceof HmmmNetworking.MessagePacketPayload packetPayload)) return;
		HmmmNetworking.iterateThroughHandlers(packetPayload);
		ci.cancel();
	}
}
