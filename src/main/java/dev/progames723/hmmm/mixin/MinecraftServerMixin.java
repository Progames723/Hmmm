package dev.progames723.hmmm.mixin;

import dev.progames723.hmmm.interface_injection.PlayerAttachments;
import dev.progames723.hmmm.obj_attach.AttachmentProvider;
import dev.progames723.hmmm.obj_attach.AttachmentSync;
import dev.progames723.hmmm.obj_attach.SynchronizedAttachment;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin extends ReentrantBlockableEventLoop<TickTask> {
	@Shadow private PlayerList playerList;
	@Unique
	@SuppressWarnings({"unused", "FieldCanBeLocal"})
	private static MinecraftServer hmmm$serverInstance = null;
	
	public MinecraftServerMixin(String name) {
		super(name);
	}
	
	@Inject(method = "spin", at = @At("RETURN"))
	private static <S extends MinecraftServer> void spinInject(Function<Thread, S> threadFunction, CallbackInfoReturnable<S> cir) {
		hmmm$serverInstance = cir.getReturnValue();
	}
	
	//? if > 1.20.1 {
	/*@Inject(
		method = "tickChildren",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/network/PlayerChunkSender;sendNextChunks(Lnet/minecraft/server/level/ServerPlayer;)V"
		),
		locals = LocalCapture.CAPTURE_FAILSOFT
	)
	private void tickChildrenInject(BooleanSupplier hasTimeLeft, CallbackInfo ci, ProfilerFiller profilerFiller, Iterator<?> var3, ServerPlayer player) {
		for (AttachmentProvider provider : ((PlayerAttachments) player).getPlayerAttachments()) {
			if (!(provider instanceof SynchronizedAttachment sync)) continue;
			AttachmentSync.sendToClient(sync, player);
		}
	}
	*///?} else {
	@Inject(
		method = "tickChildren",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/network/ServerConnectionListener;tick()V"
		)
	)
	private void tickChildrenInject(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
		for (ServerPlayer player : playerList.getPlayers()) {
			for (AttachmentProvider provider : ((PlayerAttachments) player).getPlayerAttachments()) {
				if (!(provider instanceof SynchronizedAttachment sync)) continue;
				AttachmentSync.sendToClient(sync, player);
			}
		}
	}
	//?}
}
