package dev.progames723.hmmm.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.progames723.hmmm.mixin.EventFactoryAccess;

import java.lang.reflect.Proxy;
import java.util.Objects;

@SuppressWarnings({"unchecked"})
public class EventFactoryUtil {//no fabric port ig :(
	@SafeVarargs
	public static <T> Event<T> createBoolean(boolean defaultReturn, T... typeGetter) {
		if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
		return createBoolean(defaultReturn, (Class<T>) typeGetter.getClass().getComponentType());
	}
	
	public static <T> Event<T> createBoolean(boolean defaultReturn, Class<T> clazz) {
		return EventFactory.of(listeners -> (T) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
			for (var listener : listeners) {
				return (boolean) Objects.requireNonNull(EventFactoryAccess.invokeMethod(listener, method, args));
			}
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
			for (var listener : listeners) {
				return (double) Objects.requireNonNull(EventFactoryAccess.invokeMethod(listener, method, args));
			}
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
			for (var listener : listeners) {
				return (float) Objects.requireNonNull(EventFactoryAccess.invokeMethod(listener, method, args));
			}
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
				if (doubleValue.getA() == null ^ defaultReturn.getA() == null) {
					throw new NullPointerException();
				}
				if (doubleValue.getB() == null ^ defaultReturn.getB() == null) {
					throw new NullPointerException();
				}
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
				if (tripleValue.getA() == null ^ defaultReturn.getA() == null) {
					throw new NullPointerException();
				}
				if (tripleValue.getB() == null ^ defaultReturn.getB() == null) {
					throw new NullPointerException();
				}
				if (tripleValue.getC() == null ^ defaultReturn.getC() == null) {
					throw new NullPointerException();
				}
				return tripleValue;
			}
			return defaultReturn;
		}));
	}
}
