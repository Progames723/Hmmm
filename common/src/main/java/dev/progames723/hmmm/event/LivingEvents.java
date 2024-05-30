package dev.progames723.hmmm.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.progames723.hmmm.event.utils.DoubleValue;
import dev.progames723.hmmm.event.utils.EventFactoryUtil;
import dev.progames723.hmmm.event.utils.TripleValue;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class LivingEvents {
	public static Event<Interfaces.LivingHurt> LIVING_HURT = EventFactoryUtil.createTripleValue(new TripleValue<>(true, null, -1.0f));
	
	public static Event<Interfaces.LivingDamaged> LIVING_DAMAGED = EventFactoryUtil.createDoubleValue(new DoubleValue<>(true, -1.0f));
	
	public static Event<Interfaces.LivingEarlyTick> LIVING_EARLY_TICK = EventFactory.createLoop();
	
	public static Event<Interfaces.LivingLateTick> LIVING_LATE_TICK = EventFactory.createLoop();
	
	public static Event<Interfaces.LivingEffectTick> LIVING_EFFECT_TICK = EventFactory.createLoop();
	
	public static Event<Interfaces.LivingBeforeEffectExpired> LIVING_BEFORE_EFFECT_EXPIRED = EventFactory.createLoop();
	
	public static Event<Interfaces.LivingBeforeEffectApplied> LIVING_BEFORE_EFFECT_APPLIED = EventFactoryUtil.createDoubleValue(new DoubleValue<>(null, null));
	
	public static Event<Interfaces.LivingBeforeEffectAdded> LIVING_BEFORE_EFFECT_ADDED = EventFactoryUtil.createDoubleValue(new DoubleValue<>(null, null));
	
	public static Event<Interfaces.LivingBeforeEffectRemoved> LIVING_BEFORE_EFFECT_REMOVED = EventFactoryUtil.createNullableBoolean(null);
	
	//interfaces
	public static class Interfaces {
		public interface LivingHurt {TripleValue<Boolean, DamageSource, Float> livingHurt(LivingEntity entity, DamageSource source, float amount);}
		
		public interface LivingDamaged {DoubleValue<Boolean, Float> livingDamaged(LivingEntity entity, DamageSource source, float amount);}
		
		public interface LivingEarlyTick {void livingEarlyTick(LivingEntity entity);}
		
		public interface LivingLateTick {void livingLateTick(LivingEntity entity);}
		
		public interface LivingEffectTick {void livingEffectTick(LivingEntity entity, MobEffectInstance instance);}
		
		public interface LivingBeforeEffectExpired {void livingBeforeEffectExpired(LivingEntity entity, MobEffectInstance instance);}
		
		public interface LivingBeforeEffectApplied {DoubleValue<Boolean, MobEffectInstance> livingBeforeEffectApplied(LivingEntity entity, MobEffectInstance instance);}
		
		public interface LivingBeforeEffectAdded {DoubleValue<Boolean, MobEffectInstance> livingBeforeEffectAdded(LivingEntity entity, MobEffectInstance instance);}
		
		public interface LivingBeforeEffectRemoved {Boolean livingBeforeEffectRemoved(LivingEntity entity, MobEffectInstance instance);}
	}
	//because why not
}
