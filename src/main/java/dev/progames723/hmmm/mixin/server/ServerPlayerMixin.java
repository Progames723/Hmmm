package dev.progames723.hmmm.mixin.server;

import dev.progames723.hmmm.interface_injection.PlayerAttachments;
import dev.progames723.hmmm.mixin.PlayerMixin;
import dev.progames723.hmmm.obj_attach.PersistentAttachment;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends PlayerMixin {
	protected ServerPlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}
	
	@Inject(
		method = "restoreFrom",
		at = @At("HEAD")
	)
	private void restoreFromInject(ServerPlayer that, boolean keepEverything, CallbackInfo ci) {
		((PlayerAttachments) that).getPlayerAttachments().forEach(attachmentProvider -> {
			if (!(attachmentProvider instanceof PersistentAttachment persistentAttachment)) return;
			persistentAttachment.onCopy(that, (ServerPlayer) (Object) this, !keepEverything);
		});
	}
}
