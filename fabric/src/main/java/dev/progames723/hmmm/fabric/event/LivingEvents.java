package dev.progames723.hmmm.fabric.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class LivingEvents {
	private LivingEvents() {}
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
	public static final Event<LivingEvents.effectTick> ON_LIVING_EFFECT_TICK = EventFactory.createArrayBacked(LivingEvents.effectTick.class, callbacks -> (entity, effect, effectInstance) -> {
		for (LivingEvents.effectTick callback : callbacks) {
			callback.onEffectTick(entity, effect, effectInstance);
		}
	});
	@FunctionalInterface
	public interface effectTick {
		void onEffectTick(Entity entity, MobEffect effect, MobEffectInstance effectInstance);
	}
	@FunctionalInterface
	public interface livingDamaged {
		float onLivingDamaged(Level level, Entity entity, DamageSource damageSource, float damageAmount);
	}
	@FunctionalInterface
	public interface livingHurt {
		float onLivingHurt(Level level, Entity entity, DamageSource damageSource, float damageAmount);
	}
	@FunctionalInterface
	public interface livingHurtCancellable{
		boolean onLivingHurtCancellable(Level level, Entity entity, DamageSource damageSource, float damageAmount);
	}
}
