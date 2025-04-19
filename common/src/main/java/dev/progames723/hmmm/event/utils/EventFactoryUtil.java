package dev.progames723.hmmm.event.utils;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.progames723.hmmm.mixin.EventFactoryAccess;

import java.lang.reflect.Proxy;
import java.util.Objects;

@Deprecated(forRemoval = true)
@SuppressWarnings({"unchecked", "unused"})
public final class EventFactoryUtil {//no fabric port ig :(
	private EventFactoryUtil() {throw new RuntimeException();}
	
	@SafeVarargs
	public static <T> Event<T> createBoolean(boolean defaultReturn, T... typeGetter) {
		if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
		return createBoolean(defaultReturn, (Class<T>) typeGetter.getClass().getComponentType());
	}
	
	public static <T> Event<T> createBoolean(boolean defaultReturn, Class<T> clazz) {
		return EventFactory.of(listeners -> (T) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
			for (var listener : listeners) return (boolean) Objects.requireNonNull(EventFactoryAccess.invokeMethod(listener, method, args));
			return defaultReturn;
		}));
	}
	
	@SafeVarargs
	public static <T> Event<T> createNullableBoolean(Boolean defaultReturn, T... typeGetter) {
		if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
		return createNullableBoolean(defaultReturn, (Class<T>) typeGetter.getClass().getComponentType());
	}
	
	public static <T> Event<T> createNullableBoolean(Boolean defaultReturn, Class<T> clazz) {
		return EventFactory.of(listeners -> (T) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
			for (var listener : listeners) return (Boolean) EventFactoryAccess.invokeMethod(listener, method, args);
			return defaultReturn;
		}));
	}
	
	@SafeVarargs
	public static <T> Event<T> createDouble(double defaultReturn, T... typeGetter) {
		if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
		return createDouble(defaultReturn, (Class<T>) typeGetter.getClass().getComponentType());
	}
	
	public static <T> Event<T> createDouble(double defaultReturn, Class<T> clazz) {
		return EventFactory.of(listeners -> (T) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
			for (var listener : listeners) return (double) Objects.requireNonNull(EventFactoryAccess.invokeMethod(listener, method, args));
			return defaultReturn;
		}));
	}
	
	@SafeVarargs
	public static <T> Event<T> createFloat(float defaultReturn, T... typeGetter) {
		if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
		return createFloat(defaultReturn, (Class<T>) typeGetter.getClass().getComponentType());
	}
	
	public static <T> Event<T> createFloat(float defaultReturn, Class<T> clazz) {
		return EventFactory.of(listeners -> (T) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
			for (var listener : listeners) return (float) Objects.requireNonNull(EventFactoryAccess.invokeMethod(listener, method, args));
			return defaultReturn;
		}));
	}
	
	@SafeVarargs
	public static <T> Event<T> createDoubleValue(DoubleValue<?, ?> defaultReturn, T... typeGetter) {
		if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
		return createDoubleValue(defaultReturn, (Class<T>) typeGetter.getClass().getComponentType());
	}
	
	public static <T> Event<T> createDoubleValue(DoubleValue<?, ?> defaultReturn, Class<T> clazz) {
		return EventFactory.of(listeners -> (T) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
			for (var listener : listeners) {
				DoubleValue<?, ?> doubleValue = (DoubleValue<?, ?>) Objects.requireNonNull(EventFactoryAccess.invokeMethod(listener, method, args));
				if (!isGood(doubleValue.getA(), defaultReturn.getA())) {throw new NullPointerException();}
				if (!isGood(doubleValue.getB(), defaultReturn.getB())) {throw new NullPointerException();}
				return doubleValue;
			}
			return defaultReturn;
		}));
	}
	
	@SafeVarargs
	public static <T> Event<T> createTripleValue(TripleValue<?, ?, ?> defaultReturn, T... typeGetter) {
		if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
		return createTripleValue(defaultReturn, (Class<T>) typeGetter.getClass().getComponentType());
	}
	
	public static <T> Event<T> createTripleValue(TripleValue<?, ?, ?> defaultReturn, Class<T> clazz) {
		return EventFactory.of(listeners -> (T) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
			for (var listener : listeners) {
				TripleValue<?, ?, ?> tripleValue = (TripleValue<?, ?, ?>) Objects.requireNonNull(EventFactoryAccess.invokeMethod(listener, method, args));
				if (!isGood(tripleValue.getA(), defaultReturn.getA())) {throw new NullPointerException();}
				if (!isGood(tripleValue.getB(), defaultReturn.getB())) {throw new NullPointerException();}
				if (!isGood(tripleValue.getC(), defaultReturn.getC())) {throw new NullPointerException();}
				return tripleValue;
			}
			return defaultReturn;
		}));
	}
	
	@SafeVarargs
	public static <T> Event<T> createQuadrupleValue(QuadrupleValue<?, ?, ?, ?> defaultReturn, T... typeGetter) {
		if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
		return createQuadrupleValue(defaultReturn, (Class<T>) typeGetter.getClass().getComponentType());
	}
	
	public static <T> Event<T> createQuadrupleValue(QuadrupleValue<?, ?, ?, ?> defaultReturn, Class<T> clazz) {
		return EventFactory.of(listeners -> (T) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
			for (var listener : listeners) {
				QuadrupleValue<?, ?, ?, ?> quadrupleValue = (QuadrupleValue<?, ?, ?, ?>) Objects.requireNonNull(EventFactoryAccess.invokeMethod(listener, method, args));
				if (!isGood(quadrupleValue.getA(), defaultReturn.getA())) {throw new NullPointerException();}
				if (!isGood(quadrupleValue.getB(), defaultReturn.getB())) {throw new NullPointerException();}
				if (!isGood(quadrupleValue.getC(), defaultReturn.getC())) {throw new NullPointerException();}
				if (!isGood(quadrupleValue.getD(), defaultReturn.getD())) {throw new NullPointerException();}
				return quadrupleValue;
			}
			return defaultReturn;
		}));
	}
	
	private static boolean isGood(Object value, Object defaultValue) {
		if (defaultValue == null) return true;
		return value != null;
	}
}
