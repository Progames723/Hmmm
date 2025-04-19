package dev.progames723.hmmm.interface_injection;

import dev.progames723.hmmm.HmmmError;
import dev.progames723.hmmm.obj_attach.AttachmentProvider;
import net.minecraft.world.entity.player.Player;

import java.util.Set;

public interface PlayerAttachments {
	default Set<AttachmentProvider> getAttachments() {
		throw new HmmmError(Player.class, "Not transformed!");
	}
	
	default boolean hasAttachment(Class<? extends AttachmentProvider> clazz) {
		throw new HmmmError(Player.class, "Not transformed!");
	}
	
	default void addAttachment(AttachmentProvider attachment) {
		throw new HmmmError(Player.class, "Not transformed!");
	}
}
