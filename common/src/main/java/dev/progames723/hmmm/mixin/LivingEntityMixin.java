package dev.progames723.hmmm.mixin;

import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.event.LivingEvents;
import dev.progames723.hmmm.event.utils.DoubleValue;
import dev.progames723.hmmm.event.utils.TripleValue;
import dev.progames723.hmmm.events.event.DamagedEvent;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow @Final private Map<MobEffect, MobEffectInstance> activeEffects;
	
	@Shadow protected abstract void onEffectUpdated(MobEffectInstance arg, boolean bl, Entity arg2);
	
	@Shadow protected abstract void onEffectAdded(MobEffectInstance arg, Entity arg2);
	
	@Shadow protected abstract void onEffectRemoved(MobEffectInstance arg);
	
	@Shadow @Nullable public abstract MobEffectInstance removeEffectNoUpdate(MobEffect arg);
	
	@Unique private final LivingEntity hmmm$instance = (LivingEntity) (Object) this;
	
	@Unique private static DamageSource hmmm$tempDamageSource = null;
	
	@Unique private static float hmmm$hurtTempDamage = -1.0f;
	
	@Unique private static float hmmm$damagedTempDamage = -1.0f;
	
	@Unique private static MobEffectInstance hmmm$effectAppliedTempEffect = null;
	
	@Unique private static MobEffectInstance hmmm$effectAddedTempEffect = null;
	
	public LivingEntityMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}
	
	@Unique
	private float sanitizeFloat(@Nullable Float value) {
		if (value == null) {
			return -1;
		}
		if (value.isInfinite() && value == Float.POSITIVE_INFINITY) {
			return Float.MAX_VALUE;
		} else if (value.isInfinite() && value == Float.NEGATIVE_INFINITY) {
			return Float.MIN_VALUE;
		} else if (value.isNaN()) {
			return -1;
		}
		return value;
	}
	
	@Inject(method = "hurt", at = @At(value = "HEAD"), cancellable = true)
	private void livingHurt(final DamageSource damageSource, final float f, CallbackInfoReturnable<Boolean> cir) {
		TripleValue<Boolean, DamageSource, Float> tripleValue = LivingEvents.LIVING_HURT.invoker().livingHurt(hmmm$instance, damageSource, f);
		Constructor<DamagedEvent> constructor;
		try {
			constructor = DamagedEvent.class.getDeclaredConstructor(LivingEntity.class, Entity.class, Entity.class, float.class);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
		constructor.setAccessible(true);
		DamagedEvent damagedEvent;
		try {
			damagedEvent = constructor.newInstance(hmmm$instance, damageSource.getEntity(), damageSource.getDirectEntity(), f);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		HmmmLibrary.LOGGER.info("DamagedEvent invoked! Damage before: {}, after: {}", f, damagedEvent.getDamage());
		hmmm$tempDamageSource = tripleValue.getB();
		hmmm$hurtTempDamage = sanitizeFloat(tripleValue.getC());
		if (!tripleValue.getA() || tripleValue.getA() == null) {
			cir.setReturnValue(false);
		}
		if (tripleValue.getC() == null || (hmmm$hurtTempDamage <= 0.0f && hmmm$hurtTempDamage != -1.0f)) {
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
	private float livingHurtModify(final float damage) {
		if (hmmm$hurtTempDamage != damage && hmmm$hurtTempDamage != -1) {
			return hmmm$hurtTempDamage;
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
	private DamageSource livingHurtModify(final DamageSource damageSource) {
		if (hmmm$tempDamageSource != damageSource && hmmm$tempDamageSource != null) {
			return hmmm$tempDamageSource;
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
	private void livingDamaged(final DamageSource damageSource, final float f, CallbackInfo ci) {
		if (hmmm$instance.getUUID().equals(new UUID(0L, 0L))) {
			hmmm$damagedTempDamage = sanitizeFloat(f);
			return;
		}
		DoubleValue<Boolean, Float> doubleValue = LivingEvents.LIVING_DAMAGED.invoker().livingDamaged(hmmm$instance, damageSource, f);
		//a damage source cannot be altered that late
		hmmm$damagedTempDamage = sanitizeFloat(doubleValue.getB());
		if (!doubleValue.getA() || doubleValue.getA() == null) {
			ci.cancel();
		}
		if (doubleValue.getB() == null || (hmmm$damagedTempDamage <= 0.0f && hmmm$damagedTempDamage != -1.0f)) {
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
	private float livingDamagedModify(final float f) {
		if (hmmm$damagedTempDamage != f && hmmm$damagedTempDamage != -1) {
			return hmmm$damagedTempDamage;
		}
		return f;
	}
	
	@Inject(method = "tick", at = @At(value = "HEAD"))
	private void livingEarlyTick(CallbackInfo ci) {
		LivingEvents.LIVING_EARLY_TICK.invoker().livingEarlyTick(hmmm$instance);
	}
	
	@Inject(method = "tick", at = @At(value = "TAIL"))
	private void livingLateTick(CallbackInfo ci) {
		LivingEvents.LIVING_LATE_TICK.invoker().livingLateTick(hmmm$instance);
	}
	
	@ModifyArg(method = "tickEffects", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
	private Object livingEffectTick(final Object key) {
		if (key instanceof MobEffectInstance instance) {
			LivingEvents.LIVING_EFFECT_TICK.invoker().livingEffectTick(hmmm$instance, instance);
		}
		return key;//doesnt modify anything
	}
	
	@ModifyArg(
			method = "tickEffects",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/LivingEntity;onEffectRemoved(Lnet/minecraft/world/effect/MobEffectInstance;)V"
			)
	)
	private MobEffectInstance livingBeforeEffectExpired(final MobEffectInstance arg) {
		LivingEvents.LIVING_BEFORE_EFFECT_EXPIRED.invoker().livingBeforeEffectExpired(hmmm$instance, arg);
		return arg;
	}
	
	@Inject(method = "canBeAffected", at = @At(value = "HEAD"), cancellable = true)
	private void beforeEffectApplied(final MobEffectInstance mobEffectInstance, CallbackInfoReturnable<Boolean> cir) {
		DoubleValue<Boolean, MobEffectInstance> the = LivingEvents.LIVING_BEFORE_EFFECT_APPLIED.invoker().livingBeforeEffectApplied(hmmm$instance, mobEffectInstance);
		hmmm$effectAppliedTempEffect = the.getB();
		if (the.getA() != null) {
			cir.setReturnValue(the.getA());
		}
	}
	
	@ModifyVariable(
			method = "canBeAffected",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/LivingEntity;getMobType()Lnet/minecraft/world/entity/MobType;"
			),
			argsOnly = true
	)
	private MobEffectInstance beforeEffectAppliedModify(final MobEffectInstance value) {
		if (hmmm$effectAppliedTempEffect != null && hmmm$effectAppliedTempEffect != value) {
			return hmmm$effectAppliedTempEffect;
		}
		return value;
	}
	
	@Inject(
			method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z",
			at = @At(value = "HEAD"),
			cancellable = true
	)
	private void beforeEffectAdded(MobEffectInstance mobEffectInstance, @Nullable Entity entity, CallbackInfoReturnable<Boolean> cir) {
		DoubleValue<Boolean, MobEffectInstance> the = LivingEvents.LIVING_BEFORE_EFFECT_ADDED.invoker().livingBeforeEffectAdded(hmmm$instance, mobEffectInstance);
		hmmm$effectAddedTempEffect = the.getB();
		if (the.getA() != null) {
			if (the.getA()) {
				if (hmmm$effectAddedTempEffect != null && hmmm$effectAddedTempEffect != mobEffectInstance) {
					mobEffectInstance = hmmm$effectAddedTempEffect;//modify if returns true
				}
				MobEffectInstance mobeffectinstance = this.activeEffects.get(mobEffectInstance.getEffect());
				if (mobeffectinstance == null) {
					this.activeEffects.put(mobEffectInstance.getEffect(), mobEffectInstance);
					this.onEffectAdded(mobEffectInstance, entity);
				} else if (mobeffectinstance.update(mobEffectInstance)) {
					this.onEffectUpdated(mobeffectinstance, true, entity);
				}
				mobEffectInstance.onEffectStarted(hmmm$instance);
				cir.setReturnValue(true);
			}
			cir.setReturnValue(the.getA());
		}
	}
	
	@ModifyArg(
			method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/LivingEntity;canBeAffected(Lnet/minecraft/world/effect/MobEffectInstance;)Z"
			)
	)
	private MobEffectInstance beforeEffectAddedModify(final MobEffectInstance arg) {
		if (hmmm$effectAddedTempEffect != null && hmmm$effectAddedTempEffect != arg) {
			return hmmm$effectAddedTempEffect;
		}
		return arg;
	}
	
	@Inject(
			method = "removeEffect",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/LivingEntity;removeEffectNoUpdate(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/world/effect/MobEffectInstance;"
			),
			cancellable = true
	)
	private void beforeEffectRemoved(MobEffect mobEffect, CallbackInfoReturnable<Boolean> cir) {
		MobEffectInstance mobEffectInstance = this.activeEffects.get(mobEffect);
		Boolean the = LivingEvents.LIVING_BEFORE_EFFECT_REMOVED.invoker().livingBeforeEffectRemoved(hmmm$instance, mobEffectInstance);
		if (the != null){
			if (the) {
				MobEffectInstance mobeffectinstance = this.removeEffectNoUpdate(mobEffect);
				if (mobeffectinstance != null) {
					this.onEffectRemoved(mobeffectinstance);
					cir.setReturnValue(true);
				} else {
					cir.setReturnValue(false);
				}
			}
			cir.setReturnValue(the);
		}
	}
}
