package dev.progames723.hmmm.obj_attach;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;

public interface PlayerAwareAttachment extends AttachmentProvider {
	/**
	 * THE PLAYER FIELD MUST BE TRANSIENT
	 */
	Player getAttachedPlayer();
	
	@ApiStatus.Internal
	//should only be used by the PlayerMixin
	void setPlayer(Player player);
}
