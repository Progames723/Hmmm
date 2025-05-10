package dev.progames723.hmmm.mixin.events;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.progames723.hmmm.event.api.Event;
import dev.progames723.hmmm.event.api.Events;
import dev.progames723.hmmm.event.events.LivingEntityEvent;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
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

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	//? if < 1.21.1
	@Shadow @Final private Map<MobEffect, MobEffectInstance> activeEffects;
	
	//? if >= 1.21.1
	/*@Shadow @Final private Map<Holder<MobEffect>, MobEffectInstance> activeEffects;*/
	
	@Shadow protected abstract void onEffectUpdated(MobEffectInstance arg, boolean bl, Entity arg2);
	
	@Shadow protected abstract void onEffectAdded(MobEffectInstance arg, Entity arg2);
	
	//? if < 1.21.1
	@Shadow protected abstract void onEffectRemoved(MobEffectInstance arg);
	
	//? if >= 1.21.1
	/*@Shadow protected abstract void onEffectsRemoved(Collection<MobEffectInstance> par1);*/
	
	//? if < 1.21.1
	@Shadow @Nullable public abstract MobEffectInstance removeEffectNoUpdate(MobEffect arg);
	
	//? if >= 1.21.1
	/*@Shadow @Nullable public abstract MobEffectInstance removeEffectNoUpdate(Holder<MobEffect> par1);*/
	
	@Shadow public abstract void remove(RemovalReason reason);
	
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
		if (value == null || value.isNaN()) return -1;
		
		if (value == Float.POSITIVE_INFINITY) return Float.MAX_VALUE;
		else if (value == Float.NEGATIVE_INFINITY) return Float.MIN_NORMAL;
		
		return value;
	}
	
	@SuppressWarnings("unchecked")
	@Unique
	private <T> T sanitize(T value, T defaultValue) {
		if (value instanceof Double d) return (T) (Object) sanitizeFloat(d.floatValue());
		if (value instanceof Float f) return (T) (Object) sanitizeFloat(f);
		return value == null ? defaultValue : value;
	}
	
	//? if < 1.21.4 {
	@Inject(method = "hurt", at = @At(value = "HEAD"), cancellable = true)
	private void livingHurt(final DamageSource damageSource, final float f, CallbackInfoReturnable<Boolean> cir) {
		LivingEntityEvent.Hurt event = new LivingEntityEvent.Hurt(hmmm$instance, damageSource, f);
		Events.invokeEvent(event);
		if (event.isCancelled()) {
			cir.setReturnValue(false);
		}
		hmmm$hurtTempDamage = event.getDamage();
		hmmm$tempDamageSource = event.getDamageSource();
		if (hmmm$hurtTempDamage <= 0.0f && hmmm$hurtTempDamage != -1.0f) {
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
	//?} else {
	/*@Inject(method = "hurtServer", at = @At(value = "HEAD"), cancellable = true)
	private void livingHurt(ServerLevel level, DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
		LivingEntityEvent.Hurt event = new LivingEntityEvent.Hurt(hmmm$instance, damageSource, f);
		Events.invokeEvent(event);
		if (event.isCancelled()) {
			cir.setReturnValue(false);
		}
		hmmm$hurtTempDamage = event.getDamage();
		hmmm$tempDamageSource = event.getDamageSource();
		if (hmmm$hurtTempDamage <= 0.0f && hmmm$hurtTempDamage != -1.0f) {
			cir.setReturnValue(false);
		}
	}
	
	@ModifyVariable(
		method = "hurtServer",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;isInvulnerableTo(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;)Z"
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
		method = "hurtServer",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;isInvulnerableTo(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;)Z"
		),
		argsOnly = true
	)
	private DamageSource livingHurtModify(final DamageSource damageSource) {
		if (hmmm$tempDamageSource != damageSource && hmmm$tempDamageSource != null) {
			return hmmm$tempDamageSource;
		}
		return damageSource;
	}
	*///?}
	
	@Inject(
			method = "actuallyHurt",
			at = @At(
				value = "INVOKE_ASSIGN",
				target = "Ljava/lang/Math;max(FF)F"
			),
			cancellable = true
	)
	//? if < 1.21.4
	private void livingDamaged(final DamageSource damageSource, final float f, CallbackInfo ci) {
	//? if >= 1.21.4
	/*private void livingDamaged(ServerLevel level, DamageSource damageSource, float f, CallbackInfo ci) {*/
//		DoubleValue<Boolean, Float> doubleValue = LivingEvents.LIVING_DAMAGED.invoker().livingDamaged(hmmm$instance, damageSource, f);
		LivingEntityEvent.Damaged event = new LivingEntityEvent.Damaged(hmmm$instance, damageSource, f);
		Events.invokeEvent(event);
		hmmm$damagedTempDamage = event.getDamage();
		if (event.isCancelled()) {
			ci.cancel();
		}
		if (hmmm$damagedTempDamage <= 0.0f && hmmm$damagedTempDamage != -1.0f) {
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
		Events.invokeEvent(new LivingEntityEvent.EarlyTick(hmmm$instance));
	}
	
	@Inject(method = "tick", at = @At(value = "TAIL"))
	private void livingLateTick(CallbackInfo ci) {
		Events.invokeEvent(new LivingEntityEvent.LateTick(hmmm$instance));
	}
	
	@ModifyArg(method = "tickEffects", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
	private Object livingEffectTick(final Object key) {
		if (key instanceof MobEffectInstance instance) {
			Events.invokeEvent(new LivingEntityEvent.EffectTick(hmmm$instance, instance));
		}
		return key;//doesnt modify anything
	}
	
	//? if < 1.21.4 {
	@ModifyArg(
			method = "tickEffects",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/LivingEntity;onEffectRemoved(Lnet/minecraft/world/effect/MobEffectInstance;)V"
			)
	)
	private MobEffectInstance livingBeforeEffectExpired(final MobEffectInstance arg) {
		Events.invokeEvent(new LivingEntityEvent.BeforeEffectExpired(hmmm$instance, arg));
		return arg;
	}
	//?} else {
	/*@ModifyArg(
		method = "tickEffects",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;onEffectsRemoved(Ljava/util/Collection;)V"
		)
	)
	private Collection<MobEffectInstance> livingBeforeEffectExpired(Collection<MobEffectInstance> par1) {
		List<MobEffectInstance> list = (List<MobEffectInstance>) par1;
		Events.invokeEvent(new LivingEntityEvent.BeforeEffectExpired(hmmm$instance, list.getFirst()));
		return par1;
	}
	*///?}
	
	@Inject(method = "canBeAffected", at = @At(value = "HEAD"), cancellable = true)
	private void beforeEffectApplied(final MobEffectInstance mobEffectInstance, CallbackInfoReturnable<Boolean> cir) {
		LivingEntityEvent.BeforeEffectApplied event = Events.invokeEvent(new LivingEntityEvent.BeforeEffectApplied(hmmm$instance, mobEffectInstance));
		if (event.getEffectInstance() == null) return;
		hmmm$effectAppliedTempEffect = null;
		switch (event.getEventResult()) {
			case FAILURE -> cir.setReturnValue(false);
			case SUCCESS -> {
				cir.setReturnValue(true);
				hmmm$effectAppliedTempEffect = sanitize(event.getEffectInstance(), mobEffectInstance);
			}
		}
	}
	
	@ModifyVariable(
			method = "canBeAffected",
			at = @At(
					value = "INVOKE",
					//? if < 1.21.1 {
					target = "Lnet/minecraft/world/entity/LivingEntity;getMobType()Lnet/minecraft/world/entity/MobType;"
					//?} else {
					/*target = "Lnet/minecraft/world/entity/LivingEntity;getType()Lnet/minecraft/world/entity/EntityType;"
					*///?}
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
		LivingEntityEvent.BeforeEffectAdded event = new LivingEntityEvent.BeforeEffectAdded(hmmm$instance, mobEffectInstance);
		Events.invokeEvent(event);
		if (event.getEffectInstance() == null) return;
		hmmm$effectAppliedTempEffect = null;
		hmmm$effectEventResult = event.getEventResult();
		hmmm$effectAddedTempEffect = sanitize(event.getEffectInstance(), mobEffectInstance);
	}
	
	private Event.EventResult hmmm$effectEventResult;
	
	@ModifyExpressionValue(
		method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;canBeAffected(Lnet/minecraft/world/effect/MobEffectInstance;)Z"
		)
	)
	private boolean modifyReturn(boolean original) {
		if (hmmm$effectEventResult == null) return original;
		Event.EventResult tempEventResult = hmmm$effectEventResult;
		hmmm$effectEventResult = null;
		return switch (tempEventResult) {
			case SUCCESS -> true;
			case FAILURE -> false;
			default -> original;
		};
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
	
	//? if < 1.21.1 {
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
		LivingEntityEvent.BeforeEffectRemoved event = new LivingEntityEvent.BeforeEffectRemoved(hmmm$instance, mobEffectInstance);
		if (event.getEventResult() == Event.EventResult.SUCCESS) {
			MobEffectInstance mobeffectinstance = this.removeEffectNoUpdate(mobEffect);
			if (mobeffectinstance != null) {
				this.onEffectRemoved(mobeffectinstance);
				cir.setReturnValue(true);
			} else cir.setReturnValue(false);
		} else if (event.getEventResult() == Event.EventResult.FAILURE) {
			cir.setReturnValue(false);
		}
	}
	//?} else {
	/*@Inject(
		method = "removeEffect",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;removeEffectNoUpdate(Lnet/minecraft/core/Holder;)Lnet/minecraft/world/effect/MobEffectInstance;"
		),
		cancellable = true
	)
	private void beforeEffectRemoved(Holder<MobEffect> effect, CallbackInfoReturnable<Boolean> cir) {
		MobEffectInstance mobEffectInstance = this.activeEffects.get(effect);
		LivingEntityEvent.BeforeEffectRemoved event = new LivingEntityEvent.BeforeEffectRemoved(hmmm$instance, mobEffectInstance);
		if (event.getEventResult() == Event.EventResult.SUCCESS) {
			MobEffectInstance mobeffectinstance = this.removeEffectNoUpdate(effect);
			if (mobeffectinstance != null) {
				this.onEffectsRemoved(List.of(mobeffectinstance));
				cir.setReturnValue(true);
			} else cir.setReturnValue(false);
		} else if (event.getEventResult() == Event.EventResult.FAILURE) {
			cir.setReturnValue(false);
		}
	}
	*///?}
}
