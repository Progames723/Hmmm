package dev.progames723.hmmm.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;

/**
 * only useful for mods without mixins, otherwise redundant
 * okay its useless if you have mixins lol
 */
@SuppressWarnings("unused")
public class ReflectUtil {
	private ReflectUtil() {throw new RuntimeException();}
	
	public static Class<?> getClass(@NotNull String path, @NotNull String name) {
		return getClass(path + "." + name);
	}
	
	public static Class<?> getInnerClass(@NotNull String path, @NotNull String name) {
		return getClass(path + "$" + name);
	}
	
	public static Class<?> getClass(@NotNull String path) {
		try {
			return Class.forName(path);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace(System.err);
			return null;
		}
	}
	
	public static Constructor<?> getConstructor(@NotNull Class<?> clazz, Class<?>... types) {
		try {
			Constructor<?> constructor = clazz.getDeclaredConstructor(types);
			throwExceptionIfWrongClassPackage(checkClassPackage(constructor.getClass()));
			tryToMakeItAccessible(constructor);
			return constructor;
		}
		catch (ReflectiveOperationException e) {
			e.printStackTrace(System.err);
		}
		return null;
	}
	
	public static Object invokeConstructor(@NotNull Constructor<?> constructor, Object... args) {
		try {
			return constructor.newInstance(args);
		}
		catch (ReflectiveOperationException e) {
			e.printStackTrace(System.err);
		}
		return args;
	}
	
	@NotNull
	public static <T> List<T> getFields(@NotNull Class<?> clazz, @NotNull Class<T> type) {
		List<T> list = new ArrayList<>();
		
		for (Field field : ReflectUtil.getFields(clazz)) {
			if (!field.getDeclaringClass().equals(clazz)) continue;
			if (!Modifier.isStatic(field.getModifiers())) continue;
			if (!Modifier.isFinal(field.getModifiers())) continue;
			if (!type.isAssignableFrom(field.getType())) continue;
			if (!field.trySetAccessible()) continue;
			
			try {
				list.add(type.cast(field.get(null)));
			}
			catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace(System.err);
			}
		}
		
		return list;
	}
	
	@NotNull
	public static List<Field> getFields(@NotNull Class<?> type) {
		List<Field> result = new ArrayList<>();
		
		Class<?> clazz = type;
		while (clazz != null && clazz != Object.class) {
			if (!result.isEmpty()) {
				result.addAll(0, Arrays.asList(clazz.getDeclaredFields()));
			}
			else {
				Collections.addAll(result, clazz.getDeclaredFields());
			}
			clazz = clazz.getSuperclass();
		}
		
		return result;
	}
	
	public static void tryToMakeItAccessible(@NotNull AccessibleObject object) {
		throwExceptionIfWrongClassPackage(checkClassPackage(object.getClass()));
		@NotNull AccessibleObject finalObject = object;
		PrivilegedAction<AccessibleObject> action = () -> {
			finalObject.setAccessible(true);
			return finalObject;
		};
		object = AccessController.doPrivileged(action);//just to be safe
		try {
			object.setAccessible(true);
		} catch (SecurityException | InaccessibleObjectException e) {
			try {
				action.run();
			} catch (InaccessibleObjectException | SecurityException exception) {
				throw new RuntimeException(exception);//nah too bad if it didnt work
			}
		}
	}
	
	private static void test() {
		System.out.println("Test passed ig");
	}
	
	
	private static boolean checkClassPackage(String className) {
		Class<?> clazz;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		return !(className.startsWith("java") || className.startsWith("com.sun") || className.startsWith("javax") || className.startsWith("jdk") || className.startsWith("sun")) && !clazz.isHidden();
	}
	
	private static boolean checkClassPackage(Class<?> clazz) {
		return checkClassPackage(clazz.getName()) && !clazz.isHidden();
	}
	
	private static void throwExceptionIfWrongClassPackage(boolean checkClassPackageResult) {
		if (checkClassPackageResult) {
			throw new RuntimeException(new IllegalCallerException("Cannot access java's packages"));
		}
	}
	
	public static Field getField(@NotNull Class<?> clazz, @NotNull String fieldName) {
		try {
			return clazz.getDeclaredField(fieldName);
		}
		catch (NoSuchFieldException e) {
			Class<?> superClass = clazz.getSuperclass();
			if (superClass == null) {
				throw new RuntimeException(e);
			} else {
				return getField(superClass, fieldName);
			}
		}
	}
	
	public static Object getFieldValue(@NotNull Object from, @NotNull String fieldName) {
		try {
			Class<?> clazz = from instanceof Class<?> ? (Class<?>) from : from.getClass();
			Field field = getField(clazz, fieldName);
			if (field == null) return null;
			
			throwExceptionIfWrongClassPackage(checkClassPackage(field.getClass()));
			tryToMakeItAccessible(field);
			return field.get(from);
		}
		catch (IllegalAccessException e) {
			e.printStackTrace(System.err);
		}
		return null;
	}
	
	public static boolean setFieldValue(@NotNull Object of, @NotNull String fieldName, @Nullable Object value) {
		try {
			boolean isStatic = of instanceof Class;
			Class<?> clazz = isStatic ? (Class<?>) of : of.getClass();
			
			Field field = getField(clazz, fieldName);
			if (field == null) return false;
			
			throwExceptionIfWrongClassPackage(checkClassPackage(field.getClass()));
			tryToMakeItAccessible(field);
			field.set(isStatic ? null : of, value);
			return true;
		}
		catch (IllegalAccessException e) {
			e.printStackTrace(System.err);
		}
		return false;
	}
	
	public static Method getMethod(@NotNull Class<?> clazz, @NotNull String methodName, @NotNull Class<?>... o) {
		try {
			return clazz.getDeclaredMethod(methodName, o);
		}
		catch (NoSuchMethodException e) {
			Class<?> superClass = clazz.getSuperclass();
			if (superClass == null) {
				throw new RuntimeException(e);
			} else {
				return getMethod(superClass, methodName);
			}
		}
	}
	
	public static Object invokeMethod(@NotNull Method method, @Nullable Object object, @Nullable Object... param) {
		throwExceptionIfWrongClassPackage(checkClassPackage(method.getClass()));
		tryToMakeItAccessible(method);
		try {
			return method.invoke(object, param);
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace(System.err);
		}
		return null;
	}
	
	public static Class<?> getCallerClass() {
		try {
			return StackWalker.getInstance(Set.of(StackWalker.Option.values())).getCallerClass();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
