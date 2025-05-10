package dev.progames723.hmmm.mixin;

import dev.progames723.hmmm.HmmmException;
import dev.progames723.hmmm.interface_injection.PlayerAttachments;
import dev.progames723.hmmm.obj_attach.AttachmentProvider;
import dev.progames723.hmmm.obj_attach.PlayerAwareAttachment;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements PlayerAttachments {
	@Unique
	private final Set<AttachmentProvider> attachmentProviders = new HashSet<>();
	
	protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}
	
	@Override
	public Set<? extends AttachmentProvider> getPlayerAttachments() {
		attachmentProviders.remove(null);
		return attachmentProviders;
	}
	
	@Override
	public void addPlayerAttachment(AttachmentProvider attachment) {
		if (attachment == null) throw new HmmmException("Cannot add null attachments!");
		attachmentProviders.add(attachment);
		if (attachment instanceof PlayerAwareAttachment single)
			single.setPlayer((Player) (Object) this);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends AttachmentProvider> Optional<T> getPlayerAttachment(Class<T> clazz) {
		try {
			return Optional.ofNullable((T) attachmentProviders.stream().filter(provider -> provider.getClass() == clazz).findFirst().orElse(null));
		} catch (Exception ignored) {
			return Optional.empty();
		}
	}
}
