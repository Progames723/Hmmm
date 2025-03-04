package dev.progames723.hmmm.event.api;

import dev.progames723.hmmm.HmmmException;
import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.internal.CallerSensitive;
import dev.progames723.hmmm.utils.InternalUtils;
import dev.progames723.hmmm.utils.MiscUtil;
import dev.progames723.hmmm.utils.ReflectUtil;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

//TODO finish
@SuppressWarnings({"unchecked"}) //i have to format the string to be able to display the exception
public final class Events {
	private static final Set<Class<? extends Event>> registeredEvents = new HashSet<>();
	private static final Map<Class<? extends Event>, TreeSet<Listener<? extends Event>>> eventListeners = new HashMap<>();
	
	private Events() {MiscUtil.instantiationOfUtilClass(ReflectUtil.CALLER_CLASS.getCallerClass());}
	
	public static final class Listener<E extends Event> implements Comparable<Listener<E>> {
		private final Method method;
		private final EventListener listener;
		
		public Listener(Method method) {
			this(method, method.getAnnotation(EventListener.class));
		}
		
		public Listener(Method method, EventListener listener) {
			if (method == null || listener == null) throw new HmmmException(ReflectUtil.CALLER_CLASS.getCallerClass(), new NullPointerException("Arguments must not be null!"));
			checkIfCorrectMethod(method);//just for the exception
			this.method = method;
			this.listener = listener;
		}
		
		public void invoke(E event) {
			if (event.isCancelled() && !this.listener.receiveEventWhenCancelled()) return;
			try {
				method.setAccessible(true);
				method.invoke(null, event);
			} catch (Exception e) {
				HmmmLibrary.LOGGER.error(HmmmLibrary.EVENT, "Method(%s, from %s) threw an exception when handling event!".formatted(method, method.getDeclaringClass()), e);
			} finally {
				method.setAccessible(false);
			}
		}
		
		@Override
		public int compareTo(@NotNull Events.Listener<E> o) {
			return listener.priority().compareTo(o.listener.priority());
		}
	}
	
	private static Set<Listener<? extends Event>> getEveryListener(Class<? extends Event> eventClass) {
		final var h = new Object() {
			final Set<Listener<? extends Event>> set = eventListeners.get(eventClass);
		};
		eventListeners.forEach((cls, listeners) -> {
			if (!eventClass.isAssignableFrom(cls)) return;
			if (listeners == null || listeners.isEmpty()) return;
			h.set.addAll(listeners);
		});
		return h.set;
	}
	
	public static synchronized <E extends Event> E invokeEvent(E event) {
		Set<Listener<? extends Event>> listeners = getEveryListener(event.getClass());
		try {
			assert listeners != null : "unregistered event!";
		} catch (AssertionError e) {
			throw new HmmmException((Class<?>) null, e);
		}
		for (Listener<? extends Event> listener : listeners) {
			((Listener<E>) listener).invoke(event);
		}
		return event;
	}
	
	public static synchronized void registerEventListenerClass(Class<?> eventHandlerClass) {
		List<Method> methods = List.of(eventHandlerClass.getDeclaredMethods());
		for (Method method : methods) {
			try {
				registerEventListener(new Listener<>(method));
			} catch (Exception e) {
				HmmmLibrary.LOGGER.debug(HmmmLibrary.EVENT, "method {} cannot be registered\n{}", method, e.toString());
			}
		}
	}
	
	public static synchronized void unregisterEventListenerClass(Class<?> eventHandlerClass) {
		List<Method> methods = List.of(eventHandlerClass.getDeclaredMethods());
		for (Method method : methods) {
			try {
				unregisterEventListener(new Listener<>(method));
			} catch (Exception e) {
				HmmmLibrary.LOGGER.debug(HmmmLibrary.EVENT, "method {} cannot be unregistered\n{}", method, e.toString());
			}
		}
	}
	
	public static synchronized void registerEventListener(Listener<? extends Event> listener) {
		Class<? extends Event> event = checkIfCorrectMethod(listener.method);
		TreeSet<Listener<? extends Event>> listeners = eventListeners.get(event);
		try {
			assert listeners != null : "unregistered event!";
		} catch (AssertionError e) {
			throw new HmmmException((Class<?>) null, e);
		}
		if (!listeners.add(listener)) throw new HmmmException(null, "Method already registered!");
		eventListeners.put(event, listeners);
	}
	
	public static synchronized void unregisterEventListener(Listener<? extends Event> listener) {
		Class<? extends Event> event = checkIfCorrectMethod(listener.method);
		TreeSet<Listener<? extends Event>> listeners = eventListeners.get(event);
		try {
			assert listeners != null : "unregistered event!";
		} catch (AssertionError e) {
			throw new HmmmException((Class<?>) null, e);
		}
		listeners.remove(listener);
		eventListeners.put(event, listeners);
	}
	
	private static Class<? extends Event> checkIfCorrectMethod(Method eventHandler) {
		List<HmmmException> list = new LinkedList<>();
		if (!Modifier.isStatic(eventHandler.getModifiers())) list.add(new HmmmException(null, "method must be static!"));
		if (eventHandler.getAnnotation(EventListener.class) == null) list.add(new HmmmException(null, "method must have an EventListener annotation!"));
		if (eventHandler.getParameterTypes().length != 1) list.add(new HmmmException(null, "method has malformed(or incorrect) arguments"));
		if (!Event.class.isAssignableFrom(eventHandler.getParameterTypes()[0])) list.add(new HmmmException(null, "expected %s(or subclass) in argument, got %s".formatted(Event.class, eventHandler.getParameterTypes()[0])));
		if (!list.isEmpty()) {
			list.add(0, new HmmmException(null, "Cannot use incorrect methods!"));
			HmmmException exception = null;
			for (HmmmException e : list) {
				if (exception == null) exception = e;
				else exception.addSuppressed(e);
			}
			throw exception;
		}
		return (Class<? extends Event>) eventHandler.getParameterTypes()[0];
	}
	
	public static synchronized void registerEvent(Class<? extends Event> event) {
		if (!registeredEvents.add(event)) throw new HmmmException(ReflectUtil.CALLER_CLASS.getCallerClass(), "Event already exists!");
		if (eventListeners.put(event, new TreeSet<>(Comparator.comparingInt(value -> -value.listener.priority().ordinal()))) != null) throw new HmmmException(ReflectUtil.CALLER_CLASS.getCallerClass(), "Event already exists! also this should never happen");
	}
	
	public static synchronized void unregisterEvent(Class<? extends Event> event) {
		registeredEvents.remove(event);
		eventListeners.remove(event);
	}
	
	@ApiStatus.Internal
	@CallerSensitive
	public static void startEventRegistration() {
		CallerSensitive.Utils.throwExceptionIfNotAllowed(ReflectUtil.CALLER_CLASS.getCallerClass());
		List<Class<RegisterEventCallback>> list = InternalUtils.scanClassesFor(RegisterEventCallback.class, InternalUtils.ScanType.INTERFACE_IMPL, true);
		list.forEach(cls -> {
			try {
				Constructor<? extends RegisterEventCallback> constructor = cls.getDeclaredConstructor();
				constructor.setAccessible(true);
				constructor.newInstance().registrationStart();
				constructor.setAccessible(false);
			} catch (Exception e) {
				HmmmLibrary.LOGGER.error(HmmmLibrary.EVENT, "failed to instantiate %s!".formatted(cls), e);
			}
		});
		List<Class<?>> list1 = InternalUtils.scanForAnnotatedClassesWith(AutoRegisterEvents.class);
		list1.forEach(Events::registerEventListenerClass);
	}
}
