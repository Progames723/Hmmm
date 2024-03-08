package dev.progames723.hmmm.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.progames723.hmmm.mixin.LivingEntityMixin;
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
		private static EventLogic result(@Nullable EventLogic eventLogic) {//i hope this will be used
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
	}
	
	
	/**
	 * Triggered before {@code LivingEntity} is damaged, before damage is reduced, can be cancelled by returning {@code false}.
	 * @see LivingEntityMixin
	 */
	public static final Event<livingHurt> BEFORE_LIVING_HURT = EventFactory.createEventResult();
	/**
	 * Triggered before {@code LivingEntity} is damaged, but after damage is reduced.<p>
	 * Cancelling the event WILL NOT RESTORE THE DURABILITY and etc
	 * @see LivingEntityMixin
	 */
	public static final Event<livingDamaged> BEFORE_LIVING_DAMAGED = EventFactory.createEventResult();
	/**
	 * Triggered before {@code LivingEntity} is damaged, can be cancelled by returning {@code false}.
	 * @see LivingEntityMixin
	 */
	public static final Event<livingHurtCancellable> BEFORE_LIVING_HURT_CANCELLABLE = EventFactory.createEventResult();
	/**
	 * Triggered before {@code LivingEntity} is ticked, can not be cancelled.
	 * @see LivingEntityMixin
	 */
	public static final Event<livingTick> ON_LIVING_TICK = EventFactory.createLoop();
	/**
	 * Ticks absolutely every effect on {@link LivingEntity}, can not be cancelled.
	 * @see LivingEntityMixin
	 */
	public static final Event<livingEffectTick> ON_LIVING_EFFECT_TICK = EventFactory.createLoop();
	/**
	 * Triggered before an effect on a {@code LivingEntity} is expired, can not be cancelled.
	 * @see LivingEntityMixin
	 */
	public static final Event<livingEffectExpired> ON_LIVING_EFFECT_EXPIRED = EventFactory.createLoop();
	/**
	 * Triggered before {@code LivingEntity} is applied, can be cancelled by returning {@link EventLogic#NO}, <p>
	 * {@link EventLogic#YES} ignores minecraft's effect applying logic and {@link EventLogic#DEFAULT} uses minecraft's logic
	 * @see LivingEntityMixin
	 */
	public static final Event<beforeEffectApplied> LIVING_BEFORE_EFFECT_APPLIED = EventFactory.createEventResult();
	/**
	 * Triggered before an effect is added to a {@code LivingEntity}, can be cancelled by returning {@code false}.
	 * @see LivingEntityMixin
	 */
	public static final Event<beforeEffectAdded> LIVING_BEFORE_EFFECT_ADDED = EventFactory.createEventResult();
	/**
	 * Triggered before an effect is removed from a {@code LivingEntity}, can be cancelled by returning {@code false}.
	 * @see LivingEntityMixin
	 */
	public static final Event<beforeEffectRemoved> LIVING_BEFORE_EFFECT_REMOVED = EventFactory.createEventResult();
	//does this really interest you?
	//i dont think so!
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
