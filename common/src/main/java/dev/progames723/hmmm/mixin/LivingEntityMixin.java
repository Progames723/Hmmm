package dev.progames723.hmmm.mixin;

import dev.progames723.hmmm.event.DoubleValue;
import dev.progames723.hmmm.event.LivingEvents;
import dev.progames723.hmmm.event.TripleValue;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export = true)
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow public abstract boolean hurt(DamageSource arg, float g);
	
	@Unique private final LivingEntity instance = (LivingEntity) (Object) this;
	
	@Unique private DamageSource tempDamageSource = null;
	
	@NotNull @Unique private Float hurtTempDamage = -1.0f;
	
	@NotNull @Unique private Float damagedTempDamage = -1.0f;
	
	public LivingEntityMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}
	
	@Inject(
			method = "hurt",
			at = @At(
					value = "HEAD"
			),
			cancellable = true
	)
	private void livingHurt(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
		TripleValue<Boolean, DamageSource, Float> tripleValue = LivingEvents.LIVING_HURT.invoker().livingHurt(instance, damageSource, f);
		tempDamageSource = tripleValue.getB();
		hurtTempDamage = sanitizeFloat(tripleValue.getC());
		if (!tripleValue.getA() || tripleValue.getA() == null) {
			cir.setReturnValue(false);
		}
		if (tripleValue.getC() == null || tripleValue.getC() <= 0.0f) {
			cir.setReturnValue(false);
		}
	}
	
	@ModifyVariable(
			method = "hurt",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/LivingEntity;isInvulnerableTo(Lnet/minecraft/world/damagesource/DamageSource;)Z"
			),
			argsOnly = true
	)
	private float livingHurtModify(float damage) {
		if (hurtTempDamage != damage && hurtTempDamage != -1) {
			return hurtTempDamage;
		}
		return damage;
	}
	
	@ModifyVariable(
			method = "hurt",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/LivingEntity;isInvulnerableTo(Lnet/minecraft/world/damagesource/DamageSource;)Z"
			),
			argsOnly = true
	)
	private DamageSource livingHurtModify(DamageSource damageSource) {
		if (tempDamageSource != damageSource && tempDamageSource != null) {
			damageSource = tempDamageSource;
		}
		return damageSource;
	}
	
	@Inject(
			method = "actuallyHurt",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/LivingEntity;getAbsorptionAmount()F",
					ordinal = 1
			),
			cancellable = true
	)
	private void livingDamaged(DamageSource damageSource, float f, CallbackInfo ci) {
		DoubleValue<Boolean, Float> doubleValue = LivingEvents.LIVING_DAMAGED.invoker().livingDamaged(instance, damageSource, f);
		//a damage source cannot be altered that late
		damagedTempDamage = sanitizeFloat(doubleValue.getB());
		if (!doubleValue.getA() || doubleValue.getA() == null) {
			ci.cancel();
		}
		if (doubleValue.getB() == null || doubleValue.getB() <= 0.0f) {
			ci.cancel();
		}
	}
	
	@ModifyVariable(
			method = "actuallyHurt",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/LivingEntity;setAbsorptionAmount(F)V",
					ordinal = 0
			),
			argsOnly = true
	)
	private float livingDamagedModify(float f) {
		if (damagedTempDamage != f && damagedTempDamage != -1) {
			return damagedTempDamage;
		}
		return f;
	}
	
	@Unique
	private float sanitizeFloat(@Nullable Float value) {
		if (value == null) {
			return 0;
		}
		if (value.isInfinite() && value == Float.POSITIVE_INFINITY) {
			return Float.MAX_VALUE;
		} else if (value.isInfinite() && value == Float.NEGATIVE_INFINITY) {
			return Float.MIN_VALUE;
		} else if (value.isNaN()) {
			return 0;
		}
		return value;
	}
}
