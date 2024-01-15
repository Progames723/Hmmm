package dev.progames723.hmmm.fabric.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class BeforeLivingDamagedCallback {
	/**
	 * Event for modifying or cancelling damage before any damage reduction mitigations are applied.
	 * <p>
	 * {@code Level} is already included for less boilerplate.
	 * <p>
	 * {@link dev.progames723.hmmm.fabric.mixin.LivingEntityMixin} is used for this.
	 * <p>
	 * Almost the same as {@link dev.progames723.hmmm.fabric.event.BeforeLivingHurtCallback} but triggers before damage is reduced
	 */
	public static final Event<BeforeLivingDamagedCallback.livingDamaged> EVENT = EventFactory.createArrayBacked(BeforeLivingDamagedCallback.livingDamaged.class, callbacks -> (level, entity, damageSource, damageAmount) -> {
		for (BeforeLivingDamagedCallback.livingDamaged callback : callbacks) {
			float newDamage = callback.onLivingDamaged(level, entity, damageSource, damageAmount);
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
	public interface livingDamaged {
		float onLivingDamaged(Level level, Entity entity, DamageSource damageSource, float damageAmount);
	}
}
