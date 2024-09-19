package dev.progames723.hmmm.utils;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.MappingsImpl;
import org.burningwave.core.classes.*;

import java.lang.reflect.*;
import java.security.AccessController;
import java.security.AllPermission;
import java.security.PrivilegedAction;
import java.util.*;

/**
 * only useful for mods without mixins, otherwise redundant
 */
@SuppressWarnings({"unused", "removal"})
public class ReflectUtil {
	private ReflectUtil() {throw new RuntimeException();}
	
	public static Class<?> getClass(String path, String name) {
		return getClass(path.replace('/', '.') + "." + name);
	}
	
	public static Class<?> getInnerClass(String path, String name) {
		return getClass(path.replace('/', '.') + "$" + name);
	}
	
	public static Class<?> getClass(String path) {
		try {
			return Class.forName(path);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace(System.err);
			return null;
		}
	}
	
	public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... types) {
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
	
	public static Object invokeConstructor(Constructor<?> constructor, Object... args) {
		try {
			return constructor.newInstance(args);
		}
		catch (ReflectiveOperationException e) {
			e.printStackTrace(System.err);
		}
		return args;
	}
	
	public static <T> List<T> getFields(Class<?> clazz, Class<T> type) {
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
	
	public static List<Field> getFields(Class<?> type) {
		List<Field> result = new ArrayList<>();
		
		Class<?> clazz = type;
		while (clazz != null && clazz != Object.class) {
			if (!result.isEmpty()) {
				result.addAll(Arrays.asList(clazz.getDeclaredFields()));
			}
			else {
				Collections.addAll(result, clazz.getDeclaredFields());
			}
			clazz = clazz.getSuperclass();
		}
		
		return result;
	}
	
	public static void tryToMakeItAccessible(AccessibleObject object) {
//		throwExceptionIfWrongClassPackage(checkClassPackage(object.getClass()));
		AccessibleObject finalObject = object;
		PrivilegedAction<AccessibleObject> action = () -> {
			finalObject.setAccessible(true);
			return finalObject;
		};
		object = AccessController.doPrivileged(action);//just to be safe
		try {
			object.setAccessible(true);
		} catch (SecurityException | InaccessibleObjectException e) {
			try {
				AccessController.doPrivileged(action, AccessController.getContext(), new AllPermission());
			} catch (InaccessibleObjectException | SecurityException exc) {
				//last attempt
				try {
					if (object instanceof Method method) Bypass.METHODS.setAccessible(method, true);
					if (object instanceof Field field) Bypass.FIELDS.setAccessible(field, true);
					if (object instanceof Constructor<?> constructor) Bypass.CONSTRUCTORS.setAccessible(constructor, true);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
				throw new RuntimeException("Impossible to set accessible");
			}
		}
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
	
	public static Field getField(Class<?> clazz, String fieldName) {
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
	
	public static Object getFieldValue(Object from, String fieldName) {
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
	
	public static boolean setFieldValue(Object of, String fieldName, Object value) {
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
	
	public static Method getMethod(Class<?> clazz, String methodName, Class<?>... o) {
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
	
	public static Object invokeMethod(Method method, Object object, Object... param) {
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
	
	public static Class<?> getCallerClass() {//should work
		return StackWalker.getInstance(Set.of(StackWalker.Option.values())).getCallerClass();
	}
	
	public static String getMethodSignature(Method method) {
		String signature;
		try {
			Method signatureMethod = Method.class.getDeclaredMethod("getGenericSignature");
			signatureMethod.setAccessible(true);
			signature = (String) signatureMethod.invoke(method);
			if (signature != null) return signature;
		} catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ignored) {}
		StringBuilder stringBuilder = new StringBuilder("(");
		for (Class<?> c : method.getParameterTypes()) stringBuilder.append((signature = Array.newInstance(c, 0).toString()), 1, signature.indexOf("@"));
		return stringBuilder.append(")").append(method.getReturnType() == void.class ? "V" : (signature = Array.newInstance(method.getReturnType(), 0).toString()).substring(1, signature.indexOf("@"))).toString();
	}
	
	public static String getFieldSignature(Field field) {
		String signature;
		try {
			Method signatureMethod = Field.class.getDeclaredMethod("getGenericSignature");
			signatureMethod.setAccessible(true);
			signature = (String) signatureMethod.invoke(field);
			if (signature != null) return signature;
		} catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ignored) {}
		return field.getType() == void.class ? "V" : (signature = Array.newInstance(field.getType(), 0).toString()).substring(1, signature.indexOf("@"));
	}
	
	@ExpectPlatform
	private static MappingsImpl getModLoaderSpecificMappingsImpl() {
		return null;
	}
	
	public static MappingsImpl getMappingsImpl() {
		MappingsImpl mappings = getModLoaderSpecificMappingsImpl();
		//every modern mod loader should be supported!
		if (mappings == null) throw new AssertionError("Method wasnt transformed!");
		return mappings;
	}
	
	public static void bypassModuleReflectionRestrictions(Module moduleToBypass) {
		if (!HmmmLibrary.unsafeReflect) throw new RuntimeException("Please use the \"enable_unsafe_reflection_hmmm\" flag in minecraft launch arguments to access this");
		
		
		String[] packagesToOpen = moduleToBypass.getPackages().toArray(new String[0]);
		
		Class<? extends Module> module = moduleToBypass.getClass();
		Method theBypass = Bypass.METHODS.findFirstAndMakeItAccessible(module, "implAddExportsOrOpens", String.class, Module.class, boolean.class, boolean.class);
		for (String s : packagesToOpen) {
			Bypass.METHODS.invoke(
				moduleToBypass,//obvious
				theBypass,
				//actual params now
				/*package name*/s,
				/*module*/Bypass.FIELDS.<Module>getStatic(Bypass.FIELDS.findOneAndMakeItAccessible(Module.class, "EVERYONE_MODULE")),
				/*open*/true,
				/*syncVM*/true
			);
		}
	}
	
	public static class Bypass {//well mostly
		public static final Methods METHODS;
		public static final Fields FIELDS;
		public static final Constructors CONSTRUCTORS;
		public static final Classes CLASSES;
		public static final Members MEMBERS;
		
		static {
			if (HmmmLibrary.unsafeReflect) {
				METHODS = Methods.create();
				FIELDS = Fields.create();
				CONSTRUCTORS = Constructors.create();
				CLASSES = Classes.create();
				MEMBERS = Members.create();
			} else {
				METHODS = null;
				FIELDS = null;
				CONSTRUCTORS = null;
				CLASSES = null;
				MEMBERS = null;
			}
		}
	}
}
