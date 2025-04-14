package dev.progames723.hmmm.event.events;

import dev.progames723.hmmm.event.api.Event;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public abstract class LivingEntityEvent extends Event {
	private final LivingEntity entity;
	
	protected LivingEntityEvent(LivingEntity entity, boolean isCancellable) {
		super(isCancellable);
		this.entity = entity;
	}
	
	public final LivingEntity getEntity() {
		return entity;
	}
	
	private abstract static class Damage extends LivingEntityEvent {
		private float damage;
		
		public Damage(LivingEntity entity, float initialDamage) {
			super(entity, true);
			damage = initialDamage;
		}
		
		public Damage(LivingEntity entity) {
			this(entity, 0.0f);
		}
		
		public float getDamage() {
			return damage;
		}
		
		public void setDamage(float damage) {
			if (canChangeEvent())
				this.damage = damage;
		}
	}
	
	/**
	 * the opposite to {@link Hurt}
	 */
	public static final class Damaged extends Damage {
		public Damaged(LivingEntity entity, float initialDamage) {
			super(entity, initialDamage);
		}
		
		public Damaged(LivingEntity entity) {
			super(entity);
		}
	}
	
	/**
	 * earliest damage event, you can alter the {@link DamageSource} because no damage had been dealt yet
	 */
	public static final class Hurt extends Damage {
		private DamageSource damageSource;
		
		public Hurt(LivingEntity entity, DamageSource damageSource, float initialDamage) {
			super(entity, initialDamage);
			this.damageSource = damageSource;
		}
		
		public Hurt(LivingEntity entity, DamageSource damageSource) {
			this(entity, damageSource, 0.0f);
		}
		
		@Nullable
		public DamageSource getDamageSource() {
			return damageSource;
		}
		
		public void setDamageSource(DamageSource damageSource) {
			if (canChangeEvent())
				this.damageSource = damageSource;
		}
	}
	
	public static final class EarlyTick extends LivingEntityEvent {
		public EarlyTick(LivingEntity entity) {
			super(entity, false);
		}
	}
	
	public static final class LateTick extends LivingEntityEvent {
		public LateTick(LivingEntity entity) {
			super(entity, false);
		}
	}
	
	private abstract static class Effect extends LivingEntityEvent {
		protected MobEffectInstance effectInstance;
		
		public Effect(LivingEntity entity, MobEffectInstance effectInstance, boolean isCancellable) {
			super(entity, isCancellable);
			this.effectInstance = effectInstance;
		}
		
		public MobEffectInstance getEffectInstance() {
			return effectInstance;
		}
		
		protected void setEffectInstance(MobEffectInstance effectInstance) {
			//NO-OP
		}
	}
	
	public static final class EffectTick extends Effect {
		public EffectTick(LivingEntity entity, MobEffectInstance effectInstance) {
			super(entity, effectInstance, false);
		}
	}
	
	public static final class BeforeEffectExpired extends Effect {
		public BeforeEffectExpired(LivingEntity entity, MobEffectInstance effectInstance) {
			super(entity, effectInstance, false);
		}
	}
	
	public static final class BeforeEffectApplied extends Effect implements HasEventResult {
		private EventResult eventResult = EventResult.PASS;
		
		public BeforeEffectApplied(LivingEntity entity, MobEffectInstance effectInstance) {
			super(entity, effectInstance, true);
		}
		
		@Override
		public void setEffectInstance(MobEffectInstance effectInstance) {
			if (canChangeEvent()) this.effectInstance = effectInstance;
		}
		
		@Override
		public EventResult getEventResult() {
			return eventResult;
		}
		
		@Override
		public void setEventResult(EventResult eventResult) {
			if (canChangeEvent()) this.eventResult = eventResult;
		}
	}
	
	public static final class BeforeEffectAdded extends Effect {
		public BeforeEffectAdded(LivingEntity entity, MobEffectInstance effectInstance) {
			super(entity, effectInstance, true);
		}
		
		@Override
		public void setEffectInstance(MobEffectInstance effectInstance) {
			if (canChangeEvent()) this.effectInstance = effectInstance;
		}
	}
	
	public static final class BeforeEffectRemoved extends Effect implements HasEventResult {
		private EventResult eventResult = EventResult.PASS;
		
		public BeforeEffectRemoved(LivingEntity entity, MobEffectInstance effectInstance) {
			super(entity, effectInstance, true);
		}
		
		@Override
		public void setEffectInstance(MobEffectInstance effectInstance) {
			if (canChangeEvent()) this.effectInstance = effectInstance;
		}
		
		@Override
		public EventResult getEventResult() {
			return eventResult;
		}
		
		@Override
		public void setEventResult(EventResult eventResult) {
			if (canChangeEvent()) this.eventResult = eventResult;
		}
	}
}
