package dev.progames723.hmmm.mixin;

import com.google.common.collect.Maps;
import dev.architectury.event.EventResult;
import dev.progames723.hmmm.event.LivingEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Final @Shadow private Map<MobEffect, MobEffectInstance> activeEffects = Maps.newHashMap();
	private LivingEntityMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}
	@ModifyVariable(
			method = "actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V",
			at = @At(
					value = "INVOKE",
					target = "Ljava/lang/Math;max(FF)F",
					ordinal = 0
			),
			ordinal = 0,
			argsOnly = true
	)
	private float onLivingDamaged(float f, DamageSource damageSource, float damage){
		LivingEntity livingEntity = (LivingEntity) (Object) this;
		Level level = livingEntity.level();
		float newValue = LivingEvents.BEFORE_LIVING_DAMAGED.invoker().onLivingDamaged(
				level, livingEntity, damageSource, f);
		if (newValue <= 0){
            newValue = 0;
        }
		return newValue;
	}
	@ModifyVariable(
			method = "hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z",
			at = @At("HEAD"),
			argsOnly = true
	)
	private float onLivingHurt(float originalValue, DamageSource source, float amount) {
		LivingEntity entity = (LivingEntity) (Object) this;
		Level level = entity.level();
		float newValue = LivingEvents.BEFORE_LIVING_HURT.invoker().onLivingHurt(level, entity, source, originalValue);
		if (newValue <= 0){
			newValue = 0;
		}
		return newValue;
	}
	@Inject(
			method = "hurt",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/LivingEntity;isSleeping()Z"
			),
			cancellable = true
	)
	private void beforeDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		LivingEntity entity = (LivingEntity) (Object) this;
		Level level = entity.level();
		if (!LivingEvents.BEFORE_LIVING_HURT_CANCELLABLE.invoker().onLivingHurtCancellable(level, entity, source, amount)) {
			cir.setReturnValue(false);
		}
	}
	@Inject(
			method = "tick",
			at = @At(value = "HEAD")
	)
	private void livingTick(CallbackInfo ci){
		LivingEntity entity = (LivingEntity) (Object) this;
		LivingEvents.ON_LIVING_TICK.invoker().onLivingTick(entity.level(), entity);
	}
	@Inject(
			method = "tickEffects",
			at = @At(
					value = "HEAD"
			)
	)
	private void tickEffect(CallbackInfo ci){
		LivingEntity entity = (LivingEntity) (Object) this;
		MobEffectInstance mobEffectInstance;
		while (this.activeEffects.values().iterator().hasNext()) {
			mobEffectInstance = this.activeEffects.values().iterator().next();
			MobEffect mobEffect = mobEffectInstance.getEffect();
			LivingEvents.ON_LIVING_EFFECT_TICK.invoker().onLivingEffectTick(entity.level(), entity, mobEffectInstance, mobEffect);
		}
	}
	@Inject(
			method = "tickEffects",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/Iterator;remove()V"
			)
	)
	private void effectExpired(CallbackInfo ci){
		LivingEntity entity = (LivingEntity) (Object) this;
		MobEffectInstance mobEffectInstance;
		while (this.activeEffects.values().iterator().hasNext()) {
			mobEffectInstance = this.activeEffects.values().iterator().next();
			MobEffect mobEffect = mobEffectInstance.getEffect();
			LivingEvents.ON_LIVING_EFFECT_EXPIRED.invoker().onLivingEffectExpired(entity.level(), entity, mobEffectInstance, mobEffect);
		}
	}
	@Inject(
			method = "canBeAffected",
			at = @At(
					value = "HEAD"
			),
			cancellable = true
	)
	private void beforeEffectApplied(MobEffectInstance mobEffectInstance, CallbackInfoReturnable<Boolean> cir) {
		LivingEntity entity = (LivingEntity) (Object) this;
		MobEffect mobEffect = mobEffectInstance.getEffect();
		EventResult the = LivingEvents.LIVING_BEFORE_EFFECT_APPLIED.invoker().livingBeforeEffectApplied(entity.level(), entity, mobEffectInstance, mobEffect);
		if (the.interruptsFurtherEvaluation() && the.value() == null) {
			cir.setReturnValue(false);
		} else if (the.interruptsFurtherEvaluation()) {
			cir.setReturnValue(the.value());
		}
	}
	@Inject(
			method = "onEffectRemoved",
			at = @At(
					value = "HEAD"
			),
			cancellable = true
	)
	private void beforeEffectRemoved(MobEffectInstance mobEffectInstance, CallbackInfo ci) {
		LivingEntity entity = (LivingEntity) (Object) this;
		MobEffect mobEffect = mobEffectInstance.getEffect();
		if (!LivingEvents.LIVING_BEFORE_EFFECT_REMOVED.invoker().livingBeforeEffectRemoved(entity.level(), entity, mobEffectInstance, mobEffect)){
			ci.cancel();
		}
	}
	@Inject(
			method = "onEffectAdded",
			at = @At(
					value = "HEAD"
			),
			cancellable = true
	)
	private void beforeEffectAdded(MobEffectInstance mobEffectInstance, Entity entity2, CallbackInfo ci) {
		LivingEntity entity = (LivingEntity) (Object) this;
		MobEffect mobEffect = mobEffectInstance.getEffect();
		if (!LivingEvents.LIVING_BEFORE_EFFECT_ADDED.invoker().livingBeforeEffectAdded(entity.level(), entity, mobEffectInstance, mobEffect)) {
			ci.cancel();
		}
	}
}
