package dev.progames723.hmmm.event;

import com.google.common.reflect.AbstractInvocationHandler;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import dev.progames723.hmmm.mixin.EventFactoryInvoker;
import dev.progames723.hmmm.mixin.LivingEntityMixin;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

public class LivingEvents {
	private LivingEvents() {} // non-instantiable because it shouldn't be
	/**
	 * Triggered before {@code LivingEntity} is damaged, before damage is reduced, can be cancelled by returning {@code false}.
	 * @see LivingEntityMixin
	 */
	public static final Event<LivingHurt> BEFORE_LIVING_HURT = EventFactory.of(listeners -> (LivingHurt) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{LivingHurt.class}, new AbstractInvocationHandler() {@Override protected Object handleInvocation(@NotNull Object proxy, @NotNull Method method, Object @NotNull [] args) throws Throwable {for (var listener : listeners) {var result = Objects.requireNonNull(EventFactoryInvoker.invokeMethod(listener, method, args));if (result instanceof Long longRes) {if (longRes > 0){return longRes;}} else if (result instanceof Integer intRes) {if (intRes > 0){return intRes;}} else if (result instanceof Float floatRes) {if (floatRes > 0){return floatRes;}} else if (result instanceof Double doubleRes) {if (doubleRes > 0){return doubleRes;}}}return 0.0f;}}));
	/**
	 * Triggered before {@code LivingEntity} is damaged, but after damage is reduced.<p>
	 * Cancelling the event WILL NOT RESTORE THE DURABILITY and etc
	 * @see LivingEntityMixin
	 */
	public static final Event<LivingDamaged> BEFORE_LIVING_DAMAGED = EventFactory.of(listeners -> (LivingDamaged) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{LivingDamaged.class}, new AbstractInvocationHandler() {@Override protected Object handleInvocation(@NotNull Object proxy, @NotNull Method method, Object @NotNull [] args) throws Throwable {for (var listener : listeners) {var result = Objects.requireNonNull(EventFactoryInvoker.invokeMethod(listener, method, args));if (result instanceof Long longRes) {if (longRes > 0){return longRes;}} else if (result instanceof Integer intRes) {if (intRes > 0){return intRes;}} else if (result instanceof Float floatRes) {if (floatRes > 0){return floatRes;}} else if (result instanceof Double doubleRes) {if (doubleRes > 0){return doubleRes;}}}return 0.0f;}}));
	/**
	 * Triggered before {@code LivingEntity} is damaged, can be cancelled by returning {@code false}.
	 * @see LivingEntityMixin
	 */
	public static final Event<LivingHurtCancellable> BEFORE_LIVING_HURT_CANCELLABLE = EventFactory.of(listeners -> (LivingHurtCancellable) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{LivingHurtCancellable.class}, new AbstractInvocationHandler() {@Override protected Object handleInvocation(@NotNull Object proxy, @NotNull Method method, Object @NotNull [] args) throws Throwable {for (var listener : listeners) {boolean result = Objects.requireNonNull(EventFactoryInvoker.invokeMethod(listener, method, args));if (!result){return false;}}return true;}}));
	/**
	 * Triggered before {@code LivingEntity} is ticked, can not be cancelled.
	 * @see LivingEntityMixin
	 */
	public static final Event<LivingTick> ON_LIVING_TICK = EventFactory.createLoop();
	/**
	 * Ticks absolutely every effect on {@link LivingEntity}, can not be cancelled.
	 * @see LivingEntityMixin
	 */
	public static final Event<LivingEffectTick> ON_LIVING_EFFECT_TICK = EventFactory.createLoop();
	/**
	 * Triggered before an effect on a {@code LivingEntity} is expired, can not be cancelled.
	 * @see LivingEntityMixin
	 */
	public static final Event<LivingEffectExpired> ON_LIVING_EFFECT_EXPIRED = EventFactory.createLoop();
	/**
	 * Triggered before an effect is applied on a {@code LivingEntity}, can be cancelled.
	 * @see LivingEntityMixin
	 */
	public static final Event<BeforeEffectApplied> LIVING_BEFORE_EFFECT_APPLIED = EventFactory.createEventResult();
	/**
	 * Triggered before an effect is added to a {@code LivingEntity}, can be cancelled by returning {@code false}.
	 * @see LivingEntityMixin
	 */
	public static final Event<BeforeEffectAdded> LIVING_BEFORE_EFFECT_ADDED = EventFactory.of(listeners -> (BeforeEffectAdded) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{BeforeEffectAdded.class}, new AbstractInvocationHandler() {@Override protected Object handleInvocation(@NotNull Object proxy, @NotNull Method method, Object @NotNull [] args) throws Throwable {for (var listener : listeners) {boolean result = Objects.requireNonNull(EventFactoryInvoker.invokeMethod(listener, method, args));if (!result) {return false;}}return true;}}));
	/**
	 * Triggered before an effect is removed from a {@code LivingEntity}, can be cancelled by returning {@code false}.
	 * @see LivingEntityMixin
	 */
	public static final Event<BeforeEffectRemoved> LIVING_BEFORE_EFFECT_REMOVED = EventFactory.of(listeners -> (BeforeEffectRemoved) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{BeforeEffectRemoved.class}, new AbstractInvocationHandler() {@Override protected Object handleInvocation(@NotNull Object proxy, @NotNull Method method, Object @NotNull [] args) throws Throwable {for (var listener : listeners) {boolean result = Objects.requireNonNull(EventFactoryInvoker.invokeMethod(listener, method, args));if (!result) {return false;}}return true;}}));
	//does this really interest you?
	//i dont think so!
	@FunctionalInterface
	public interface LivingTick {
		void onLivingTick(Level level, LivingEntity entity);
	}
	@FunctionalInterface
	public interface LivingEffectTick {
		void onLivingEffectTick(Level level, LivingEntity entity, MobEffectInstance mobEffectInstance, MobEffect mobEffect);
	}
	@FunctionalInterface
	public interface LivingEffectExpired {
		void onLivingEffectExpired(Level level, LivingEntity entity, MobEffectInstance mobEffectInstance, MobEffect mobEffect);
	}
	@FunctionalInterface
	public interface BeforeEffectApplied {
		EventResult livingBeforeEffectApplied(Level level, LivingEntity entity, MobEffectInstance mobEffectInstance, MobEffect mobEffect);
	}
	@FunctionalInterface
	public interface BeforeEffectAdded {
		boolean livingBeforeEffectAdded(Level level, LivingEntity entity, MobEffectInstance mobEffectInstance, MobEffect mobEffect);
	}
	@FunctionalInterface
	public interface BeforeEffectRemoved {
		boolean livingBeforeEffectRemoved(Level level, LivingEntity entity, MobEffectInstance mobEffectInstance, MobEffect mobEffect);
	}
	@FunctionalInterface
	public interface LivingDamaged {
		float onLivingDamaged(Level level, LivingEntity entity, DamageSource damageSource, float damageAmount);
	}
	@FunctionalInterface
	public interface LivingHurt {
		float onLivingHurt(Level level, LivingEntity entity, DamageSource damageSource, float damageAmount);
	}
	@FunctionalInterface
	public interface LivingHurtCancellable {
		boolean onLivingHurtCancellable(Level level, LivingEntity entity, DamageSource damageSource, float damageAmount);
	}
}
