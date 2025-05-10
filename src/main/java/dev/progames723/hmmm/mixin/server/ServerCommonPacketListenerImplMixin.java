package dev.progames723.hmmm.mixin.server;

import dev.progames723.hmmm.networking.HmmmNetworking;
//? if > 1.20.1 {
/*import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
*///?}
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(/*? if > 1.20.1 {*//*ServerCommonPacketListenerImpl.class*//*?} else {*/ServerGamePacketListenerImpl.class/*?}*/)
public abstract class ServerCommonPacketListenerImplMixin {
	//? if > 1.20.1 {
	/*@Inject(
		method = "handleCustomPayload",
		at = @At("HEAD"),
		cancellable = true
	)
	public void handleCustomPayloadInject(ServerboundCustomPayloadPacket packet, CallbackInfo ci) {
		if (!(packet.payload() instanceof HmmmNetworking.MessagePacketPayload packetPayload)) return;
		HmmmNetworking.iterateThroughHandlers(packetPayload);
		ci.cancel();
	}
	*///?}
}
