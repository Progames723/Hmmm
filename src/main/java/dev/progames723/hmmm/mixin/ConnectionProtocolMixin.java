package dev.progames723.hmmm.mixin;

import dev.progames723.hmmm.networking.HmmmNetworking;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ConnectionProtocol.class)
public abstract class ConnectionProtocolMixin {
	//? if <= 1.20.1 {
	@Inject(
		method = "getProtocolForPacket",
		at = @At("HEAD"),
		cancellable = true
	)
	private static void customPacketProtocolInject(Packet<?> packet, CallbackInfoReturnable<ConnectionProtocol> cir) {
		if (!(packet instanceof HmmmNetworking.MessagePacket)) return;
		cir.setReturnValue(ConnectionProtocol.PLAY);
	}
	//?}
}
