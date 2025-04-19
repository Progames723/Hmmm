package dev.progames723.hmmm.mixin;

import dev.progames723.hmmm.obj_attach.AttachmentProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.storage.PlayerDataStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
	@Shadow @Final private PlayerDataStorage playerIo;
	
	@Inject(method = "load", at = @At("RETURN"))
	private void injectLoad(ServerPlayer player, CallbackInfoReturnable<CompoundTag> cir) {
		AttachmentProvider.tryLoadAttachmentsForPlayer(player, ((PlayerDataStorageAccessor) playerIo).getPlayerDir(), null);
	}
}
