package dev.progames723.hmmm.event;

import dev.architectury.event.Event;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;

public class LivingEvents {
	public static Event<LivingHurt> LIVING_HURT = EventFactoryUtil.createTripleValue(new TripleValue<>(true, new DamageSource(Holder.direct(new DamageType("generic", 0.0f))), 1.0f));
	
	public static Event<LivingDamaged> LIVING_DAMAGED = EventFactoryUtil.createDoubleValue(new DoubleValue<>(true, 1.0f));
	
	@FunctionalInterface
	public interface LivingHurt {
		TripleValue<Boolean, DamageSource, Float> livingHurt(LivingEntity entity, DamageSource source, float amount);
	}
	
	@FunctionalInterface
	public interface LivingDamaged {
		DoubleValue<Boolean, Float> livingDamaged(LivingEntity entity, DamageSource source, float amount);
	}
}
