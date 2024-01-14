package dev.progames723.hmmm.fabric.event;

import dev.progames723.hmmm.HmmmLibrary;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.Arrays;

public class LivingDamagedCallback {

	public static final Event<LivingDamagedCallback.livingDamaged> EVENT = EventFactory.createArrayBacked(LivingDamagedCallback.livingDamaged.class, callbacks -> (level, entity, damageSource, damageAmount) -> {
		HmmmLibrary.LOGGER.info(Arrays.toString(callbacks));
		for (LivingDamagedCallback.livingDamaged callback : callbacks) {
			HmmmLibrary.LOGGER.info("Phase 2");
			float newDamage = callback.onLivingDamaged(level, entity, damageSource, damageAmount);
			if (newDamage != damageAmount) {
				HmmmLibrary.LOGGER.info("Phase 3(modified damage)");
				return newDamage;
			}
		}
		HmmmLibrary.LOGGER.info("Phase 4");
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
