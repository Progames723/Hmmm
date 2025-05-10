package dev.progames723.hmmm.mixin;

import dev.progames723.hmmm.networking.HmmmNetworking;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Connection.class)
public abstract class ConnectionMixin {
	//? if <= 1.20.1 {
	@Inject(
		method = "genericsFtw",
		at = @At("HEAD"),
		cancellable = true
	)
	private static <T extends PacketListener> void customPacketInject(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
		if (!(packet instanceof HmmmNetworking.MessagePacket messagePacket)) return;
		if (listener instanceof HmmmNetworking.MessagePacket.MessagePacketListener messagePacketListener)
			messagePacket.handle(messagePacketListener);
		else messagePacket.handle(null);
		ci.cancel();
	}
	//?}
}
