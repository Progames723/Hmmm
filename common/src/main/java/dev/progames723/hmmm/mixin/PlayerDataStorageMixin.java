package dev.progames723.hmmm.mixin;

import dev.progames723.hmmm.obj_attach.AttachmentProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.PlayerDataStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(PlayerDataStorage.class)
public abstract class PlayerDataStorageMixin {
	@Shadow @Final private File playerDir;
	
	@Inject(method = "save", at = @At("HEAD"))
	private void injectSave(Player player, CallbackInfo ci) {
		AttachmentProvider.saveAllAttachments(player, playerDir, null);
	}
}
