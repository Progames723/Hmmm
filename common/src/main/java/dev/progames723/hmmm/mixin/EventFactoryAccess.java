package dev.progames723.hmmm.mixin;

import dev.architectury.event.EventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

@Mixin(EventFactory.class)
public interface EventFactoryAccess {
	@Invoker(value = "invokeMethod", remap = false)
	@SuppressWarnings("unchecked")
	static <T, R> R invokeMethod(T listener, Method method, Object[] args) throws Throwable {
		return (R) MethodHandles.lookup().unreflect(method).bindTo(listener).invokeWithArguments(args);
	}
}
