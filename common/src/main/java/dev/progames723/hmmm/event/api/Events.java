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
import java.util.function.Function;

@SuppressWarnings({"unchecked", "StringConcatenationArgumentToLogCall"})//i have to format the string to be able to display the exception
public final class Events {
	private static boolean finalized;
	private static final Set<Class<? extends Event>> registeredEvents = new HashSet<>();
	private static final Map<Class<? extends Event>, TreeSet<Listener<Event>>> eventListeners = new HashMap<>();
	private static final Function<Event, Thread> asyncEventExecutor = (event) -> new Thread(() -> {
		TreeSet<Listener<Event>> listeners = eventListeners.get(event.getClass());
		try {
			assert listeners != null : "event not registered!";
		} catch (AssertionError e) {
			HmmmLibrary.LOGGER.debug("Async event(%s) executor caught an error".formatted(event.toString()), e);
			throw e;
		}
		for (Listener<Event> listener : listeners) listener.invoke(event);
	}, "Async event(%s) executor".formatted(event.toString()));
	
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
	
	private static Set<Listener<Event>> getEveryListener(Class<? extends Event> eventClass) {
		final var h = new Object() {
			final Set<Listener<Event>> set = eventListeners.get(eventClass);
		};
		eventListeners.forEach((cls, listeners) -> {
			if (!eventClass.isAssignableFrom(cls)) return;
			if (listeners == null || listeners.isEmpty()) return;
			h.set.addAll(listeners);
		});
		return h.set;
	}
	
	public static synchronized <E extends Event> E invokeEvent(E event) {
		Set<Listener<Event>> listeners = getEveryListener(event.getClass());
		try {
			assert listeners != null : "unregistered event!";
		} catch (AssertionError e) {
			throw new HmmmException((Class<?>) null, e);
		}
		if (AsyncEvent.class.isAssignableFrom(event.getClass()) && ((AsyncEvent) event).isAsync()) {
			//run asynchronously
			asyncEventExecutor.apply(event).start();
		} else {
			for (Listener<? extends Event> listener : listeners) {
				((Listener<E>) listener).invoke(event);
			}
		}
		return event;
	}
	
	public static synchronized void registerEventListenerClass(Class<?> eventHandlerClass) {
		List<Method> methods = List.of(eventHandlerClass.getDeclaredMethods());
		for (Method method : methods) {
			try {
				registerEventListener(new Listener<>(method));
			} catch (Exception e) {
				HmmmLibrary.LOGGER.debug(HmmmLibrary.EVENT, "Listener {} cannot be registered\n{}", method, e.toString());
			}
		}
	}
	
	public static synchronized void unregisterEventListenerClass(Class<?> eventHandlerClass) {
		List<Method> methods = List.of(eventHandlerClass.getDeclaredMethods());
		for (Method method : methods) {
			try {
				unregisterEventListener(new Listener<>(method));
			} catch (Exception e) {
				HmmmLibrary.LOGGER.debug(HmmmLibrary.EVENT, "Listener {} cannot be unregistered\n{}", method, e.toString());
			}
		}
	}
	
	public static synchronized void registerEventListener(Listener<Event> listener) {
		Class<? extends Event> event = checkIfCorrectMethod(listener.method);
		TreeSet<Listener<Event>> listeners = eventListeners.get(event);
		try {
			assert listeners != null : "unregistered event!";
		} catch (AssertionError e) {
			throw new HmmmException((Class<?>) null, e);
		}
		if (!listeners.add(listener)) throw new HmmmException(null, "Listener already registered!");
		eventListeners.put(event, listeners);
	}
	
	public static synchronized void unregisterEventListener(Listener<? extends Event> listener) {
		Class<? extends Event> event = checkIfCorrectMethod(listener.method);
		TreeSet<Listener<Event>> listeners = eventListeners.get(event);
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
	public static synchronized void startEventRegistration() {
		if (finalized) return;
		finalized = true;
		CallerSensitive.Utils.throwExceptionIfNotAllowed(ReflectUtil.CALLER_CLASS.getCallerClass());
		HmmmLibrary.LOGGER.debug("Registering events...");
		List<Class<?>> events = InternalUtils.scanClassesFor(Event.class, InternalUtils.ScanType.SUB_CLASSES, true);
		if (!events.isEmpty()) events.add(Event.class);
		for (Class<?> event : events) {
			try {
				registerEvent((Class<? extends Event>) event);
			} catch (Exception e) {
				HmmmLibrary.LOGGER.error(HmmmLibrary.EVENT, "Failed to register %s event!".formatted(event), e);
			}
		}
		HmmmLibrary.LOGGER.debug("Scanning for registering callbacks... ");
		List<Class<RegisterEventCallback>> list = InternalUtils.scanClassesForGenerics(RegisterEventCallback.class, InternalUtils.ScanType.INTERFACE_IMPL, true);
		if (!list.isEmpty()) HmmmLibrary.LOGGER.debug("Scan successful, entries: {}.", list); else HmmmLibrary.LOGGER.debug("No callback entries have been found.");
		list.forEach(cls -> {
			try {
				Constructor<? extends RegisterEventCallback> constructor = cls.getDeclaredConstructor();
				constructor.setAccessible(true);
				constructor.newInstance().registrationStart();
				constructor.setAccessible(false);
			} catch (Exception e) {
				HmmmLibrary.LOGGER.error(HmmmLibrary.EVENT, "failed to instantiate %s! Make sure to have a public no args constructor!".formatted(cls), e);
			}
		});
		List<Class<?>> list1 = InternalUtils.scanForAnnotatedClassesWith(AutoRegisterEvents.class);
		list1.forEach(Events::registerEventListenerClass);
	}
}
