package dev.progames723.hmmm.fabric.mixin;

import com.google.common.collect.Maps;
import dev.progames723.hmmm.fabric.event.LivingEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	private LivingEntityMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}
	@Final @Shadow private final Map<MobEffect, MobEffectInstance> activeEffects = Maps.newHashMap();
	@Shadow protected void onEffectUpdated(MobEffectInstance mobEffectInstance, boolean bl, @Nullable Entity entity) {}
	@Shadow protected void onEffectRemoved(MobEffectInstance mobEffectInstance) {}
	@ModifyVariable(
			method = "actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V",
			at = @At(value = "INVOKE",
					target = "Ljava/lang/Math;max(FF)F",
					ordinal = 0),
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
		float newValue = LivingEvents.BEFORE_LIVING_HURT.invoker().onLivingHurt(
				level, entity, source, originalValue);
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
			method = "tickEffects",
			at = @At(value = "HEAD")
	)
	private void livingEffectTick(CallbackInfo ci){
		Iterator<MobEffect> iterator = this.activeEffects.keySet().iterator();
		while(iterator.hasNext()) {
			MobEffect mobEffect = iterator.next();
			MobEffectInstance mobEffectInstance = this.activeEffects.get(mobEffect);
			LivingEvents.ON_LIVING_EFFECT_TICK.invoker().onEffectTick((LivingEntity) (Object) this, mobEffect, mobEffectInstance);
			if (!mobEffectInstance.tick((LivingEntity) (Object) this, () -> {
				this.onEffectUpdated(mobEffectInstance, true, null);
			})) {
				if (!this.level().isClientSide) {
					iterator.remove();
					this.onEffectRemoved(mobEffectInstance);
				}
			} else if (mobEffectInstance.getDuration() % 600 == 0) {
				this.onEffectUpdated(mobEffectInstance, false, null);
			}
		}
	}
}
