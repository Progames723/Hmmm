package dev.progames723.hmmm.fabric.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class LivingEvents {
	private LivingEvents() {}
	public enum EventLogic {
		YES,
		NO,
		DEFAULT;
	}
	
	/**
	 * Made because funny and throws an exception when wrong enum. <p>
	 * Also did i mention that this uses the {@code switch}, {@code case}?
	 * @param eventLogic an {@link EventLogic} enum
	 * @return a new {@link EventLogic} enum
	 */
	private static EventLogic result(@Nullable EventLogic eventLogic) {
		if (eventLogic == null){
			throw new IllegalArgumentException("Wrong " + EventLogic.class.getName() + " enum! " +
					"Imagine actually using null instead of the enum");
		} else {
			switch (eventLogic){
				case NO -> {
					return EventLogic.NO;
				}
				case YES -> {
					return EventLogic.YES;
				}
				case DEFAULT -> {
					return EventLogic.DEFAULT;
				}
				default -> throw new IllegalArgumentException("Wrong " + EventLogic.class.getName() + " enum!");
			}
		}
	}
	/**
	 * Triggered before {@code LivingEntity} is damaged, before damage is reduced.<p>
	 * {@link  Level} is already included for less boilerplate!
	 * @see dev.progames723.hmmm.fabric.mixin.LivingEntityMixin
	 */
	public static final Event<LivingEvents.livingHurt> BEFORE_LIVING_HURT = EventFactory.createArrayBacked(LivingEvents.livingHurt.class, callbacks -> (level, entity, damageSource, damageAmount) -> {
		for (LivingEvents.livingHurt callback : callbacks) {
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
	/**
	 * Triggered before {@code LivingEntity} is damaged, but after damage is reduced.<p>
	 * Cancelling the event WILL NOT RESTORE THE DURABILITY and etc
	 * {@link Level} is already included for less boilerplate!
	 * @see dev.progames723.hmmm.fabric.mixin.LivingEntityMixin
	 */
	public static final Event<LivingEvents.livingDamaged> BEFORE_LIVING_DAMAGED = EventFactory.createArrayBacked(LivingEvents.livingDamaged.class, callbacks -> (level, entity, damageSource, damageAmount) -> {
		for (LivingEvents.livingDamaged callback : callbacks) {
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
	/**
	 * Triggered before {@code LivingEntity} is damaged, can be cancelled by returning false.<p>
	 * {@link Level} is already included for less boilerplate!
	 * @see dev.progames723.hmmm.fabric.mixin.LivingEntityMixin
	 */
	public static final Event<LivingEvents.livingHurtCancellable> BEFORE_LIVING_HURT_CANCELLABLE = EventFactory.createArrayBacked(LivingEvents.livingHurtCancellable.class, callbacks -> (level, entity, damageSource, damageAmount) -> {
		for (LivingEvents.livingHurtCancellable callback : callbacks) {
			boolean eventCancel = callback.onLivingHurtCancellable(level, entity, damageSource, damageAmount);
			if (!eventCancel) {
				return false;
			}
		}
		return true;
	});
	public static final Event<LivingEvents.livingTick> ON_LIVING_TICK = EventFactory.createArrayBacked(LivingEvents.livingTick.class, callbacks -> (level, entity) -> {
		for (LivingEvents.livingTick callback : callbacks) {
			callback.onLivingTick(level, entity);
		}
	});
	/**
	 * Ticks absolutely every effect on {@link LivingEntity}
	 */
	public static final Event<LivingEvents.livingEffectTick> ON_LIVING_EFFECT_TICK = EventFactory.createArrayBacked(LivingEvents.livingEffectTick.class, callbacks -> (level, entity, mobEffectInstance, mobEffect) -> {
		for (LivingEvents.livingEffectTick callback : callbacks) {
			callback.onLivingEffectTick(level, entity, mobEffectInstance, mobEffect);
		}
	});
	public static final Event<LivingEvents.livingEffectExpired> ON_LIVING_EFFECT_EXPIRED = EventFactory.createArrayBacked(LivingEvents.livingEffectExpired.class, callbacks -> (level, entity, mobEffectInstance, mobEffect) -> {
		for (LivingEvents.livingEffectExpired callback : callbacks) {
			callback.onLivingEffectExpired(level, entity, mobEffectInstance, mobEffect);
		}
	});
	public static final Event<LivingEvents.beforeEffectApplied> LIVING_BEFORE_EFFECT_APPLIED = EventFactory.createArrayBacked(LivingEvents.beforeEffectApplied.class, callbacks -> (level, entity, mobEffectInstance, mobEffect) -> {
		for (LivingEvents.beforeEffectApplied callback : callbacks) {
			EventLogic eventLogic = callback.livingBeforeEffectApplied(level, entity, mobEffectInstance, mobEffect);
			return result(eventLogic);
		}
		return EventLogic.DEFAULT;
	});
	public static final Event<LivingEvents.beforeEffectAdded> LIVING_BEFORE_EFFECT_ADDED = EventFactory.createArrayBacked(LivingEvents.beforeEffectAdded.class, callbacks -> (level, entity, mobEffectInstance, mobEffect) -> {
		for (LivingEvents.beforeEffectAdded callback : callbacks) {
			boolean eventCancel = callback.livingBeforeEffectAdded(level, entity, mobEffectInstance, mobEffect);
			if (!eventCancel){
				return true;
			}
		}
		return false;
	});
	public static final Event<LivingEvents.beforeEffectRemoved> LIVING_BEFORE_EFFECT_REMOVED = EventFactory.createArrayBacked(LivingEvents.beforeEffectRemoved.class, callbacks -> (level, entity, mobEffectInstance, mobEffect) -> {
		for (LivingEvents.beforeEffectRemoved callback : callbacks) {
			boolean eventCancel = callback.livingBeforeEffectRemoved(level, entity, mobEffectInstance, mobEffect);
			if (!eventCancel){
				return true;
			}
		}
		return false;
	});
	@FunctionalInterface
	public interface livingTick {
		void onLivingTick(Level level, LivingEntity entity);
	}
	@FunctionalInterface
	public interface livingEffectTick {
		void onLivingEffectTick(Level level, LivingEntity entity, MobEffectInstance mobEffectInstance, MobEffect mobEffect);
	}
	@FunctionalInterface
	public interface livingEffectExpired {
		void onLivingEffectExpired(Level level, LivingEntity entity, MobEffectInstance mobEffectInstance, MobEffect mobEffect);
	}
	@FunctionalInterface
	public interface beforeEffectApplied {
		EventLogic livingBeforeEffectApplied(Level level, LivingEntity entity, MobEffectInstance mobEffectInstance, MobEffect mobEffect);
	}
	@FunctionalInterface
	public interface beforeEffectAdded {
		boolean livingBeforeEffectAdded(Level level, LivingEntity entity, MobEffectInstance mobEffectInstance, MobEffect mobEffect);
	}
	@FunctionalInterface
	public interface beforeEffectRemoved {
		boolean livingBeforeEffectRemoved(Level level, LivingEntity entity, MobEffectInstance mobEffectInstance, MobEffect mobEffect);
	}
	@FunctionalInterface
	public interface livingDamaged {
		float onLivingDamaged(Level level, LivingEntity entity, DamageSource damageSource, float damageAmount);
	}
	@FunctionalInterface
	public interface livingHurt {
		float onLivingHurt(Level level, LivingEntity entity, DamageSource damageSource, float damageAmount);
	}
	@FunctionalInterface
	public interface livingHurtCancellable{
		boolean onLivingHurtCancellable(Level level, LivingEntity entity, DamageSource damageSource, float damageAmount);
	}
}
