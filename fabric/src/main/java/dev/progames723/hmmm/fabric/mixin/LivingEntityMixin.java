package dev.progames723.hmmm.fabric.mixin;

import dev.progames723.hmmm.fabric.event.LivingEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	private LivingEntityMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}
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
}
