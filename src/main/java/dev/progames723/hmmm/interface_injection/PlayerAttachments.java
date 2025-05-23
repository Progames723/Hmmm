package dev.progames723.hmmm.interface_injection;

import dev.progames723.hmmm.HmmmError;
import dev.progames723.hmmm.obj_attach.AttachmentProvider;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;
import java.util.Set;

public interface PlayerAttachments {
	default Set<? extends AttachmentProvider> getPlayerAttachments() {
		throw new HmmmError(Player.class, "Not transformed!");
	}
	
	default <T extends AttachmentProvider> Optional<T> getPlayerAttachment(Class<T> clazz) {
		throw new HmmmError(Player.class, "Not transformed!");
	}
	
	default void addPlayerAttachment(AttachmentProvider attachment) {
		throw new HmmmError(Player.class, "Not transformed!");
	}
}
