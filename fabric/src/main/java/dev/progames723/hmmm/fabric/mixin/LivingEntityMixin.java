package dev.progames723.hmmm.fabric.mixin;

import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.fabric.event.LivingDamagedCallback;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export = true)
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

//	@ModifyVariable(
//			method = "actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V",
//			at = @At(value = "INVOKE",
//					target = "Ljava/lang/Math;max(FF)F",
//					ordinal = 0),
//			ordinal = 0,
//			argsOnly = true
//	)
//	private float onLivingDamaged(float f, DamageSource damageSource, float damage){
//		LivingEntity livingEntity = (LivingEntity)(Object)this;
//		Level level = livingEntity.level();
//
//		float newDamage = LivingDamagedCallback.EVENT.invoker().onLivingDamaged(level, livingEntity, damageSource, f);
//
//		if (newDamage != f){
//			return newDamage;
//		}
//
//		return f;
//	}
	@ModifyVariable(method = "hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At("HEAD"), argsOnly = true)
	private float modifyDamageTaken(float originalValue, DamageSource source, float amount) {
		LivingEntity entity = (LivingEntity)(Object)this;
		Level level = entity.level();
		float newValue = LivingDamagedCallback.EVENT.invoker().onLivingDamaged(level, entity, source, originalValue);
		HmmmLibrary.LOGGER.info("modified damage: " + originalValue + ", unmodified damage: " + newValue);

		return newValue;
	}
}
