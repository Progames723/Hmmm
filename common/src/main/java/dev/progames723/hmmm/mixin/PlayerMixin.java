package dev.progames723.hmmm.mixin;

import dev.progames723.hmmm.HmmmException;
import dev.progames723.hmmm.interface_injection.PlayerAttachments;
import dev.progames723.hmmm.obj_attach.AttachmentProvider;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;

import java.util.HashSet;
import java.util.Set;

@Mixin(Player.class)
public class PlayerMixin implements PlayerAttachments {
	private final Set<AttachmentProvider> attachmentProviders = new HashSet<>();
	
	@Override
	public Set<AttachmentProvider> getAttachments() {
		attachmentProviders.remove(null);
		return attachmentProviders;
	}
	
	@Override
	public void addAttachment(AttachmentProvider attachment) {
		if (attachment == null) throw new HmmmException("Cannot add null attachments!");
		attachmentProviders.add(attachment);
	}
	
	@Override
	public boolean hasAttachment(Class<? extends AttachmentProvider> clazz) {
		return attachmentProviders.stream().anyMatch(attachmentProvider -> attachmentProvider.getClass() == clazz);
	}
}
