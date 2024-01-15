package dev.progames723.hmmm.fabric.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class BeforeLivingHurtCallback {
	/**
	 * Event for modifying or cancelling damage after damage reduction is applied.
	 * <p>
	 * {@code Level} is already included for less boilerplate.
	 * <p>
	 * {@link dev.progames723.hmmm.fabric.mixin.LivingEntityMixin} is used for this.
	 * <p>
	 * Almost the same as {@link dev.progames723.hmmm.fabric.event.BeforeLivingDamagedCallback} but triggers after damage is reduced
	 */
	public static final Event<BeforeLivingHurtCallback.livingHurt> EVENT = EventFactory.createArrayBacked(BeforeLivingHurtCallback.livingHurt.class, callbacks -> (level, entity, damageSource, damageAmount) -> {
		for (BeforeLivingHurtCallback.livingHurt callback : callbacks) {
			float newDamage = callback.onLivingHurt(level, entity, damageSource, damageAmount);
			if (newDamage != damageAmount) {
				return newDamage;
			}
		}
		if (damageAmount <= 0){
			damageAmount = 0;
		}
		return damageAmount;
	});

	@FunctionalInterface
	public interface livingHurt {
		float onLivingHurt(Level level, Entity entity, DamageSource damageSource, float damageAmount);
	}
}
