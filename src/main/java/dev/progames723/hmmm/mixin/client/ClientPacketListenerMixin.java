package dev.progames723.hmmm.mixin.client;

import dev.progames723.hmmm.networking.HmmmNetworking;
import net.minecraft.client.multiplayer.ClientPacketListener;
//? if > 1.20.1 {
/*import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
*///?}
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
	//? if > 1.20.1 {
	/*@Inject(
		method = "handleCustomPayload",
		at = @At("HEAD"),
		cancellable = true
	)
	private void handleCustomPayloadInject(CustomPacketPayload payload, CallbackInfo ci) {
		if (!(payload instanceof HmmmNetworking.MessagePacketPayload packetPayload)) return;
		HmmmNetworking.iterateThroughHandlers(packetPayload);
		ci.cancel();
	}
	*///?}
}
