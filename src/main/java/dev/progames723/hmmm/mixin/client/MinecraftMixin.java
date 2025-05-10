package dev.progames723.hmmm.mixin.client;

import dev.progames723.hmmm.interface_injection.PlayerAttachments;
import dev.progames723.hmmm.obj_attach.AttachmentProvider;
import dev.progames723.hmmm.obj_attach.AttachmentSync;
import dev.progames723.hmmm.obj_attach.SynchronizedAttachment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	@Shadow @Nullable public LocalPlayer player;
	
	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;tick()V"))
	private void injectBeforeSyncBack(CallbackInfo ci) {
		if (player == null) return;
		for (AttachmentProvider provider : ((PlayerAttachments) player).getPlayerAttachments()) {
			if (!(provider instanceof SynchronizedAttachment sync)) continue;
			AttachmentSync.sendToServer(sync, player);
		}
	}
}
