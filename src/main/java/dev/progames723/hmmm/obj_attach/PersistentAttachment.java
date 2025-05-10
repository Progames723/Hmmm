package dev.progames723.hmmm.obj_attach;

import dev.progames723.hmmm.interface_injection.PlayerAttachments;
import net.minecraft.world.entity.player.Player;

public interface PersistentAttachment extends AttachmentProvider {
	default boolean isPersistent() {
		return true;
	}
	
	default boolean copyOnDeath() {
		return false;
	}
	
	default void onCopy(Player original, Player other, boolean wasDeath) {
		if (!isPersistent()) return;
		if (wasDeath && !copyOnDeath()) return;
		PersistentAttachment persistentAttachment = ((PlayerAttachments) original).getPlayerAttachment(this.getClass()).orElse(null);
		if (persistentAttachment == null) return;
		((PlayerAttachments) other).addPlayerAttachment(persistentAttachment);
		if (this instanceof PlayerAwareAttachment playerAwareAttachment)
			playerAwareAttachment.setPlayer(other);
	}
}
