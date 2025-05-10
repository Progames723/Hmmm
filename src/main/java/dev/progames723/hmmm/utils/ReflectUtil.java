package dev.progames723.hmmm.utils;

import dev.progames723.hmmm.HmmmException;
import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.internal.CallerSensitive;
import dev.progames723.hmmm_natives.NativeReflectUtils;

import java.lang.reflect.*;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;
import java.util.stream.Collectors;

/**
 * only useful for mods without mixins, otherwise redundant
 */
@SuppressWarnings({"unused", "removal"})
public class ReflectUtil {
	private static boolean reflectionUsed = false;
	private static final StackWalker STACK_WALKER = StackWalker.getInstance(Set.of(StackWalker.Option.values()));
	/**
	 * throws exception if caller is jni
	 */
	private static final StackWalker CALLER_CLASS = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
	
	private ReflectUtil() {MiscUtil.instantiationOfUtilClass();}
	
	public static Class<?> getClass(String path, String name) {
		return getClass(path.replace('/', '.') + "." + name);
	}
	
	public static Class<?> getInnerClass(String path, String name) {
		return getClass(path.replace('/', '.') + "$" + name);
	}
	
	public static Class<?> getClass(String path) {
		try {
			return Class.forName(path);
		} catch (ClassNotFoundException e) {
			HmmmLibrary.LOGGER.error(HmmmLibrary.REFLECT, "Reflection error!", e);
			return null;
		}
	}
	
	public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... types) {
		try {
			warnOnReflection(getCaller().getDeclaringClass());
			Constructor<?> constructor = clazz.getDeclaredConstructor(types);
			tryToMakeItAccessible(constructor);
			return constructor;
		} catch (ReflectiveOperationException e) {
			HmmmLibrary.LOGGER.error(HmmmLibrary.REFLECT, "Reflection error!", e);
		}
		return null;
	}
	
	public static Object invokeConstructor(Constructor<?> constructor, Object... args) {
		warnOnReflection(getCaller().getDeclaringClass());
		try {
			tryToMakeItAccessible(constructor);
			return constructor.newInstance(args);
		} catch (ReflectiveOperationException e) {
			HmmmLibrary.LOGGER.error(HmmmLibrary.REFLECT, "Reflection error!", e);
		}
		return args;
	}
	
	public static <T> List<T> getFields(Class<?> clazz, Class<T> type) {
		List<T> list = new ArrayList<>();
		
		for (Field field : ReflectUtil.getFields(clazz)) {
			if (!field.getDeclaringClass().equals(clazz)) continue;
			if (!Modifier.isStatic(field.getModifiers())) continue;
			if (!type.isAssignableFrom(field.getType())) continue;
			if (!field.trySetAccessible()) tryToMakeItAccessible(field);
			
			try {
				list.add(type.cast(field.get(null)));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				HmmmLibrary.LOGGER.error(HmmmLibrary.REFLECT, "Reflection error!", e);
			}
		}
		
		return list;
	}
	
	public static List<Field> getFields(Class<?> type) {
		List<Field> result = new ArrayList<>();
		
		Class<?> clazz = type;
		while (clazz != null && clazz != Object.class) {
			if (!result.isEmpty()) {
				result.addAll(Arrays.asList(clazz.getDeclaredFields()));
			} else {
				Collections.addAll(result, clazz.getDeclaredFields());
			}
			clazz = clazz.getSuperclass();
		}
		
		return result;
	}
	
	public static List<Method> getMethods(Class<?> type) {
		List<Method> result = new ArrayList<>();
		
		Class<?> clazz = type;
		while (clazz != null && clazz != Object.class) {
			if (!result.isEmpty()) {
				result.addAll(Arrays.asList(clazz.getDeclaredMethods()));
			} else {
				Collections.addAll(result, clazz.getDeclaredMethods());
			}
			clazz = clazz.getSuperclass();
		}
		
		return result;
	}
	
	public static void tryToMakeItAccessible(final AccessibleObject object) {
		warnOnReflection(getCaller().getDeclaringClass());
		PrivilegedAction<AccessibleObject> action = () -> {
			object.setAccessible(true);
			return object;
		};
		AccessController.doPrivileged(action);//just to be safe
		try {
			object.setAccessible(true);
			return;
		} catch (Exception ignored) {}
		try {
			AccessController.doPrivileged(action, AccessController.getContext(), new ReflectPermission("suppressAccessChecks"));
		} catch (Exception exc) {
			//last attempt
			//effectively forces availability
			try {
				NativeReflectUtils.forceSetAccessible(object, true);
			} catch (Exception ex) {
				throw new HmmmException("Impossible to set accessible", ex);
			}
		}
	}
	
	private static void warnOnReflection(Class<?> caller) {
		if (!reflectionUsed) {
			reflectionUsed = true;
			HmmmLibrary.LOGGER.warn(HmmmLibrary.REFLECT, "Reflection used! Only shows up once every run. Do not report issues caused by reflection. Caller: %s".formatted(caller));
		}
	}
	
	public static Field getField(Class<?> clazz, String fieldName) {
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			Class<?> superClass = clazz.getSuperclass();
			if (superClass == null) {
				throw new HmmmException(e);
			} else {
				return getField(superClass, fieldName);
			}
		}
	}
	
	public static Object getFieldValue(Object from, String fieldName) {
		try {
			warnOnReflection(getCaller().getDeclaringClass());
			Class<?> clazz = from instanceof Class<?> ? (Class<?>) from : from.getClass();
			Field field = getField(clazz, fieldName);
			if (field == null) return null;
			tryToMakeItAccessible(field);
			return field.get(from);
		} catch (IllegalAccessException e) {
			HmmmLibrary.LOGGER.error(HmmmLibrary.REFLECT, "Reflection error!", e);
		}
		return null;
	}
	
	public static boolean setFieldValue(Object of, String fieldName, Object value) {
		try {
			warnOnReflection(getCaller().getDeclaringClass());
			boolean isStatic = of instanceof Class;
			Class<?> clazz = isStatic ? (Class<?>) of : of.getClass();
			
			Field field = getField(clazz, fieldName);
			if (field == null) return false;
			
			tryToMakeItAccessible(field);
			field.set(isStatic ? null : of, value);
			return true;
		} catch (IllegalAccessException e) {
			HmmmLibrary.LOGGER.error(HmmmLibrary.REFLECT, "Reflection error!", e);
		}
		return false;
	}
	
	public static Method getMethod(Class<?> clazz, String methodName, Class<?>... o) {
		try {
			return clazz.getDeclaredMethod(methodName, o);
		} catch (NoSuchMethodException e) {
			Class<?> superClass = clazz.getSuperclass();
			if (superClass == null) {
				throw new HmmmException(e);
			} else {
				return getMethod(superClass, methodName);
			}
		}
	}
	
	public static Object invokeMethod(Method method, Object object, Object... param) {
		warnOnReflection(getCaller().getDeclaringClass());
		tryToMakeItAccessible(method);
		try {
			return method.invoke(object, param);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			HmmmLibrary.LOGGER.error(HmmmLibrary.REFLECT, "Reflection error!", e);
		}
		return null;
	}
	
	/**
	 * @return a {@link java.lang.StackWalker.StackFrame} of the caller
	 */
	@CallerSensitive
	public static StackWalker.StackFrame getCallerOfCaller() {//should work
		try {
			List<StackWalker.StackFrame> list = CALLER_CLASS.walk(stack -> stack.limit(4).skip(1).collect(Collectors.toList()));
			StackWalker.StackFrame caller = list.get(0);
			StackWalker.StackFrame returned = list.get(2);
			try {
				if (caller.getDeclaringClass().getDeclaredMethod(caller.getMethodName(), caller.getMethodType().parameterArray())
					.isAnnotationPresent(CallerSensitive.class)
				) return returned;
			} catch (NoSuchMethodException e) {
				if (caller.getDeclaringClass().getDeclaredConstructor(caller.getMethodType().parameterArray())
					.isAnnotationPresent(CallerSensitive.class)
				) return returned;
			}
			throw new HmmmException(caller.getDeclaringClass() != null ? caller.getDeclaringClass() : Class.forName(caller.getClassName()), "Caller should have @CallerSensitive annotation!");
		} catch (HmmmException e) {
			throw e;
		} catch (Exception e) {
			throw new HmmmException(ReflectUtil.class, "Something went wrong when getting the stack frame!", e);
		}
	}
	
	/**
	 * @return a {@link java.lang.StackWalker.StackFrame} of the nth caller
	 */
	@CallerSensitive
	public static StackWalker.StackFrame getCallerOfNthCaller(int n) {//should work
		if (n < 0) {
			HmmmLibrary.LOGGER.error("Invalid argument for getCallerOnNthCaller!");
			return null;
		}
		try {
			List<StackWalker.StackFrame> list = CALLER_CLASS.walk(stack -> stack.limit(n + 2).skip(1).collect(Collectors.toList()));
			StackWalker.StackFrame caller = list.get(0);
			StackWalker.StackFrame returned = list.get(n);
			try {
				if (caller.getDeclaringClass().getDeclaredMethod(caller.getMethodName(), caller.getMethodType().parameterArray())
					.isAnnotationPresent(CallerSensitive.class)
				) return returned;
			} catch (NoSuchMethodException e) {
				if (caller.getDeclaringClass().getDeclaredConstructor(caller.getMethodType().parameterArray())
					.isAnnotationPresent(CallerSensitive.class)
				) return returned;
			}
			throw new HmmmException(caller.getDeclaringClass() != null ? caller.getDeclaringClass() : Class.forName(caller.getClassName()), "Caller should have @CallerSensitive annotation!");
		} catch (HmmmException e) {
			throw e;
		} catch (Exception e) {
			throw new HmmmException(ReflectUtil.class, "Something went wrong when getting the stack frame!", e);
		}
	}
	
	/**
	 * @return a {@link java.lang.StackWalker.StackFrame} of the caller
	 */
	@CallerSensitive
	public static StackWalker.StackFrame getCaller() {//should work
		try {
			List<StackWalker.StackFrame> list = CALLER_CLASS.walk(stack -> stack.limit(4).skip(1).collect(Collectors.toList()));
			StackWalker.StackFrame caller = list.get(0);
			StackWalker.StackFrame returned = list.get(1);
			try {
				if (caller.getDeclaringClass().getDeclaredMethod(caller.getMethodName(), caller.getMethodType().parameterArray())
					.isAnnotationPresent(CallerSensitive.class)
				) return returned;
			} catch (NoSuchMethodException e) {
				if (caller.getDeclaringClass().getDeclaredConstructor(caller.getMethodType().parameterArray())
					.isAnnotationPresent(CallerSensitive.class)
				) return returned;
			}
			throw new HmmmException(caller.getDeclaringClass() != null ? caller.getDeclaringClass() : Class.forName(caller.getClassName()), "Caller should have @CallerSensitive annotation!");
		} catch (HmmmException e) {
			throw e;
		} catch (Exception e) {
			throw new HmmmException(ReflectUtil.class, "Something went wrong when getting the stack frame!", e);
		}
	}
	
	public static boolean checkCallers(StackWalker.StackFrame first, StackWalker.StackFrame second) {
		return first.toStackTraceElement().equals(second.toStackTraceElement());
	}
	
	/**
	 * @return a {@link java.lang.StackWalker.StackFrame} of the true caller(doesnt skip Method#invoke() and etc)
	 */
	@CallerSensitive
	public static StackWalker.StackFrame getTrueCallerOfTrueCaller() {//should work
		try {
			List<StackWalker.StackFrame> list = CALLER_CLASS.walk(stack -> stack.limit(4).skip(1).collect(Collectors.toList()));
			StackWalker.StackFrame trueCaller = list.get(0);
			StackWalker.StackFrame returned = list.get(2);
			try {
				if (trueCaller.getDeclaringClass().getDeclaredMethod(trueCaller.getMethodName(), trueCaller.getMethodType().parameterArray())
					.isAnnotationPresent(CallerSensitive.class)
				) return returned;
			} catch (NoSuchMethodException e) {
				if (trueCaller.getDeclaringClass().getDeclaredConstructor(trueCaller.getMethodType().parameterArray())
					.isAnnotationPresent(CallerSensitive.class)
				) return returned;
			}
			throw new HmmmException(trueCaller.getDeclaringClass() != null ? trueCaller.getDeclaringClass() : Class.forName(trueCaller.getClassName()), "Caller should have @CallerSensitive annotation!");
		} catch (HmmmException e) {
			throw e;
		} catch (Exception e) {
			throw new HmmmException(ReflectUtil.class, "Something went wrong when getting the stack frame!", e);
		}
	}
	
	/**
	 * @return a {@link java.lang.StackWalker.StackFrame} of the nth true caller(doesnt skip Method#invoke() and etc)
	 */
	@CallerSensitive
	public static StackWalker.StackFrame getTrueCallerOfNthTrueCaller(int n) {//should work
		if (n < 0) {
			HmmmLibrary.LOGGER.error("Invalid argument for getTrueCallerOnNthTrueCaller!");
			return null;
		}
		try {
			List<StackWalker.StackFrame> list = CALLER_CLASS.walk(stack -> stack.limit(n + 2).skip(1).collect(Collectors.toList()));
			StackWalker.StackFrame trueCaller = list.get(0);
			StackWalker.StackFrame returned = list.get(n);
			try {
				if (trueCaller.getDeclaringClass().getDeclaredMethod(trueCaller.getMethodName(), trueCaller.getMethodType().parameterArray())
					.isAnnotationPresent(CallerSensitive.class)
				) return returned;
			} catch (NoSuchMethodException e) {
				if (trueCaller.getDeclaringClass().getDeclaredConstructor(trueCaller.getMethodType().parameterArray())
					.isAnnotationPresent(CallerSensitive.class)
				) return returned;
			}
			throw new HmmmException(trueCaller.getDeclaringClass() != null ? trueCaller.getDeclaringClass() : Class.forName(trueCaller.getClassName()), "Caller should have @CallerSensitive annotation!");
		} catch (HmmmException e) {
			throw e;
		} catch (Exception e) {
			throw new HmmmException(ReflectUtil.class, "Something went wrong when getting the stack frame!", e);
		}
	}
	
	/**
	 * @return a {@link java.lang.StackWalker.StackFrame} of the true caller(doesnt skip Method#invoke() and etc)
	 */
	@CallerSensitive
	public static StackWalker.StackFrame getTrueCaller() {//should work
		try {
			List<StackWalker.StackFrame> list = CALLER_CLASS.walk(stack -> stack.limit(4).skip(1).collect(Collectors.toList()));
			StackWalker.StackFrame trueCaller = list.get(0);
			StackWalker.StackFrame returned = list.get(1);
			try {
				if (trueCaller.getDeclaringClass().getDeclaredMethod(trueCaller.getMethodName(), trueCaller.getMethodType().parameterArray())
					.isAnnotationPresent(CallerSensitive.class)
				) return returned;
			} catch (NoSuchMethodException e) {
				if (trueCaller.getDeclaringClass().getDeclaredConstructor(trueCaller.getMethodType().parameterArray())
					.isAnnotationPresent(CallerSensitive.class)
				) return returned;
			}
			throw new HmmmException(trueCaller.getDeclaringClass() != null ? trueCaller.getDeclaringClass() : Class.forName(trueCaller.getClassName()), "Caller should have @CallerSensitive annotation!");
		} catch (HmmmException e) {
			throw e;
		} catch (Exception e) {
			throw new HmmmException(ReflectUtil.class, "Something went wrong when getting the stack frame!", e);
		}
	}
	
	public static String getMethodSignature(Method method) {
		warnOnReflection(getCaller().getDeclaringClass());
		String signature;
		try {
			Method signatureMethod = Method.class.getDeclaredMethod("getGenericSignature");
			tryToMakeItAccessible(signatureMethod);
			signature = (String) signatureMethod.invoke(method);
			if (signature != null) return signature;
		} catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ignored) {}
		StringBuilder stringBuilder = new StringBuilder("(");
		for (Class<?> c : method.getParameterTypes()) stringBuilder.append((signature = Array.newInstance(c, 0).toString()), 1, signature.indexOf("@"));
		return stringBuilder.append(")").append(method.getReturnType() == void.class ? "V" : (signature = Array.newInstance(method.getReturnType(), 0).toString()).substring(1, signature.indexOf("@"))).toString();
	}
	
	public static String getFieldSignature(Field field) {
		warnOnReflection(getCaller().getDeclaringClass());
		String signature;
		try {
			Method signatureMethod = Field.class.getDeclaredMethod("getGenericSignature");
			tryToMakeItAccessible(signatureMethod);
			signature = (String) signatureMethod.invoke(field);
			if (signature != null) return signature;
		} catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ignored) {}
		return field.getType() == void.class ? "V" : (signature = Array.newInstance(field.getType(), 0).toString()).substring(1, signature.indexOf("@"));
	}
}
