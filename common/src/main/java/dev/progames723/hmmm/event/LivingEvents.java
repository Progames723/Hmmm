package dev.progames723.hmmm.event;

import dev.architectury.event.Event;
import dev.progames723.hmmm.event.events.LivingEntityEvent;
import dev.progames723.hmmm.event.utils.DoubleValue;
import dev.progames723.hmmm.event.utils.EventFactoryUtil;
import dev.progames723.hmmm.event.utils.TripleValue;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;

public class LivingEvents {
	public static Event<LivingHurt> LIVING_HURT = EventFactoryUtil.createEvent(new LivingEntityEvent<TripleValue<Boolean, DamageSource, Float>>(false, new TripleValue<>(true, null, -1.0f), null));
	public static Event<LivingDamaged> LIVING_DAMAGED = EventFactoryUtil.createEvent(new LivingEntityEvent<>(false, new DoubleValue<>(true, -1.0f), null));
	public static Event<LivingEarlyTick> LIVING_EARLY_TICK = EventFactoryUtil.createVoidEvent(new LivingEntityEvent<Void>(true, null, null));
	public static Event<LivingLateTick> LIVING_LATE_TICK = EventFactoryUtil.createVoidEvent(new LivingEntityEvent<Void>(true, null, null));
	public static Event<LivingEffectTick> LIVING_EFFECT_TICK = EventFactoryUtil.createVoidEvent(new LivingEntityEvent<MobEffectInstance>(true, null, null));
	public static Event<LivingBeforeEffectExpired> LIVING_BEFORE_EFFECT_EXPIRED = EventFactoryUtil.createVoidEvent(new LivingEntityEvent<Void>(true, null, null));
	public static Event<LivingBeforeEffectApplied> LIVING_BEFORE_EFFECT_APPLIED = EventFactoryUtil.createEvent(new LivingEntityEvent<>(false, new DoubleValue<>(null, null), null));
	public static Event<LivingBeforeEffectAdded> LIVING_BEFORE_EFFECT_ADDED = EventFactoryUtil.createEvent(new LivingEntityEvent<>(false, new DoubleValue<>(null, null), null));
	public static Event<LivingBeforeEffectRemoved> LIVING_BEFORE_EFFECT_REMOVED = EventFactoryUtil.createVoidEvent(new LivingEntityEvent<Void>(true, null, null));
	
	//interfaces
	public interface LivingHurt {void livingHurt(LivingEntityEvent<DoubleValue<DamageSource, Float>> event);}
	public interface LivingDamaged {void livingDamaged(LivingEntityEvent<Float> event);}
	public interface LivingEarlyTick {void livingEarlyTick(LivingEntityEvent<Void> event);}
	public interface LivingLateTick {void livingLateTick(LivingEntityEvent<Void> event);}
	public interface LivingEffectTick {void livingEffectTick(LivingEntityEvent<MobEffectInstance> event);}
	public interface LivingBeforeEffectExpired {void livingBeforeEffectExpired(LivingEntityEvent<MobEffectInstance> event);}
	public interface LivingBeforeEffectApplied {void livingBeforeEffectApplied(LivingEntityEvent<MobEffectInstance> event);}
	public interface LivingBeforeEffectAdded {void livingBeforeEffectAdded(LivingEntityEvent<MobEffectInstance> event);}
	public interface LivingBeforeEffectRemoved {void livingBeforeEffectRemoved(LivingEntityEvent<MobEffectInstance> event);}
	//because why not
}
