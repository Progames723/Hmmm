package dev.progames723.hmmm.utils;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.progames723.hmmm.HmmmError;
import dev.progames723.hmmm.HmmmException;
import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.MappingsImpl;
import dev.progames723.hmmm.internal.CallerSensitive;
import org.burningwave.core.classes.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;
import java.security.AccessController;
import java.security.AllPermission;
import java.security.PrivilegedAction;
import java.util.*;
import java.util.function.Supplier;

/**
 * only useful for mods without mixins, otherwise redundant
 */
@SuppressWarnings({"unused", "removal", "deprecation"})
public class ReflectUtil {
	private static boolean reflectionUsed = false;
	public static final StackWalker STACK_WALKER = StackWalker.getInstance(Set.of(StackWalker.Option.values()));
	public static final StackWalker CALLER_CLASS = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
	public static final Supplier<Class<?>> TRUE_CALLER_CLASS = () -> STACK_WALKER.walk(stack -> stack.map(StackWalker.StackFrame::getDeclaringClass).skip(4).findFirst().orElseThrow());
	
	@CallerSensitive
	@ApiStatus.Internal
	public static CallerSensitive getInstance() {
		Class<?> caller = CALLER_CLASS.getCallerClass();
		if (!caller.getPackageName().startsWith("dev.progames723.hmmm")) {
			throw new HmmmError(caller, "tried to access internal method!");
		}
		Supplier<? extends RuntimeException> supplier = () -> new HmmmException(caller, "this should never happen!");
		String methodName = CALLER_CLASS.walk(stackFrameStream -> stackFrameStream.filter(stackFrame -> stackFrame.getDeclaringClass() == caller).findFirst().orElseThrow(supplier).getMethodName());
		Class<?>[] params = CALLER_CLASS.walk(stackFrameStream -> stackFrameStream.filter(stackFrame -> stackFrame.getDeclaringClass() == caller).findFirst().orElseThrow(supplier).getMethodType().parameterArray());
		Method method;
		CallerSensitive instance;
		try {
			method = caller.getDeclaredMethod(methodName, params);
			boolean accessible = method.isAccessible();
			if (!accessible) method.setAccessible(true);
			instance = method.getDeclaredAnnotation(CallerSensitive.class);
			method.setAccessible(accessible);
		} catch (Exception e) {
			throw supplier.get();
		}
		if (instance == null) {
			throw supplier.get();
		}
		return instance;
	}
	
	@CallerSensitive
	@ApiStatus.Internal
	public static CallerSensitive getInstance(Class<?> caller) {
		String requiredPackage = "dev.progames723.hmmm";
		if (!CALLER_CLASS.getCallerClass().getPackageName().startsWith("dev.progames723.hmmm")) {
			throw new HmmmError(CALLER_CLASS.getCallerClass(), "tried to access internal method!");
		}
		Supplier<? extends RuntimeException> supplier = () -> new HmmmException(caller, "this should never happen!");
		String methodName = CALLER_CLASS.walk(stackFrameStream -> stackFrameStream.filter(stackFrame -> stackFrame.getDeclaringClass() == caller).findFirst().orElseThrow(supplier).getMethodName());
		Class<?>[] params = CALLER_CLASS.walk(stackFrameStream -> stackFrameStream.filter(stackFrame -> stackFrame.getDeclaringClass() == caller).findFirst().orElseThrow(supplier).getMethodType().parameterArray());
		Method method;
		CallerSensitive instance;
		try {
			method = caller.getDeclaredMethod(methodName, params);
			boolean accessible = method.isAccessible();
			if (!accessible) method.setAccessible(true);
			instance = method.getDeclaredAnnotation(CallerSensitive.class);
			method.setAccessible(accessible);
		} catch (Exception e) {
			throw supplier.get();
		}
		if (instance == null) {
			throw supplier.get();
		}
		return instance;
	}
	
	private ReflectUtil() {MiscUtil.instantiationOfUtilClass(CALLER_CLASS.getCallerClass());}
	
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
			warnOnReflection(CALLER_CLASS.getCallerClass());
			throwExceptionIfNotOpenForReflection(CALLER_CLASS.getCallerClass(), clazz);
			Constructor<?> constructor = clazz.getDeclaredConstructor(types);
			tryToMakeItAccessible(constructor);
			return constructor;
		}
		catch (ReflectiveOperationException e) {
			e.printStackTrace(System.err);
		}
		return null;
	}
	
	public static Object invokeConstructor(Constructor<?> constructor, Object... args) {
		warnOnReflection(CALLER_CLASS.getCallerClass());
		try {
			throwExceptionIfNotOpenForReflection(CALLER_CLASS.getCallerClass(), constructor.getDeclaringClass());
			tryToMakeItAccessible(constructor);
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
	
	public static List<Method> getMethods(Class<?> type) {
		List<Method> result = new ArrayList<>();
		
		Class<?> clazz = type;
		while (clazz != null && clazz != Object.class) {
			if (!result.isEmpty()) {
				result.addAll(Arrays.asList(clazz.getDeclaredMethods()));
			}
			else {
				Collections.addAll(result, clazz.getDeclaredMethods());
			}
			clazz = clazz.getSuperclass();
		}
		
		return result;
	}
	
	public static void tryToMakeItAccessible(AccessibleObject object) {
		warnOnReflection(CALLER_CLASS.getCallerClass());
		AccessibleObject finalObject = object;
		PrivilegedAction<AccessibleObject> action = () -> {
			finalObject.setAccessible(true);
			return finalObject;
		};
		object = AccessController.doPrivileged(action);//just to be safe
		try {
			object.setAccessible(true);
			return;
		} catch (Exception ignored) {}
		try {
			AccessController.doPrivileged(action, AccessController.getContext(), new AllPermission());
		} catch (Exception exc) {
			//last attempt
			//effectively forces availability
			try {
				if (object instanceof Method method) Bypass.METHODS.setAccessible(method, true);
				if (object instanceof Field field) Bypass.FIELDS.setAccessible(field, true);
				if (object instanceof Constructor<?> constructor) Bypass.CONSTRUCTORS.setAccessible(constructor, true);
			} catch (Exception ex) {
				throw new HmmmException("Impossible to set accessible", ex);
			}
			throw new HmmmException("Impossible to set accessible");
		}
	}
	
	private static void throwExceptionIfNotOpenForReflection(Class<?> caller, Class<?> target) {
		if (target.getModule().isOpen(target.getPackageName(), caller.getModule()))
			throw new HmmmError(caller, "Module not open for reflection!");
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
		}
		catch (NoSuchFieldException e) {
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
			warnOnReflection(CALLER_CLASS.getCallerClass());
			Class<?> clazz = from instanceof Class<?> ? (Class<?>) from : from.getClass();
			throwExceptionIfNotOpenForReflection(CALLER_CLASS.getCallerClass(), clazz);
			Field field = getField(clazz, fieldName);
			if (field == null) return null;
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
			warnOnReflection(CALLER_CLASS.getCallerClass());
			boolean isStatic = of instanceof Class;
			Class<?> clazz = isStatic ? (Class<?>) of : of.getClass();
			
			throwExceptionIfNotOpenForReflection(CALLER_CLASS.getCallerClass(), clazz);
			
			Field field = getField(clazz, fieldName);
			if (field == null) return false;
			
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
				throw new HmmmException(e);
			} else {
				return getMethod(superClass, methodName);
			}
		}
	}
	
	public static Object invokeMethod(Method method, Object object, Object... param) {
		warnOnReflection(CALLER_CLASS.getCallerClass());
		throwExceptionIfNotOpenForReflection(CALLER_CLASS.getCallerClass(), method.getDeclaringClass());
		tryToMakeItAccessible(method);
		try {
			return method.invoke(object, param);
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace(System.err);
		}
		return null;
	}
	
	/**
	 * @deprecated please dont use this as it's easy to mixin and change the return value of this method <p>
	 * use {@link ReflectUtil#CALLER_CLASS}'s {@link StackWalker#getCallerClass()} instead!
	 * @return a {@link Class} that called the method that invoked this method
	 */
	@Deprecated
	public static Class<?> getCallerClass() {//should work
		try {
			return CALLER_CLASS.walk(stack -> stack.map(StackWalker.StackFrame::getDeclaringClass).skip(3).findFirst().orElseThrow());
		} catch (Exception e) {
			throw new HmmmError(ReflectUtil.class, "Something went wrong when getting caller class! Please do not inject this into #main() methods!");
		}
	}
	
	public static String getMethodSignature(Method method) {
		warnOnReflection(CALLER_CLASS.getCallerClass());
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
		warnOnReflection(CALLER_CLASS.getCallerClass());
		String signature;
		try {
			Method signatureMethod = Field.class.getDeclaredMethod("getGenericSignature");
			tryToMakeItAccessible(signatureMethod);
			signature = (String) signatureMethod.invoke(field);
			if (signature != null) return signature;
		} catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ignored) {}
		return field.getType() == void.class ? "V" : (signature = Array.newInstance(field.getType(), 0).toString()).substring(1, signature.indexOf("@"));
	}
//
	@ExpectPlatform
	private static MappingsImpl getModLoaderSpecificMappingsImpl() {
		return null;
	}
	
	public static MappingsImpl getMappingsImpl() {
		MappingsImpl mappings = getModLoaderSpecificMappingsImpl();
		//every modern mod loader should be supported!
		if (mappings == null) throw new HmmmError("Method wasnt transformed!");
		return mappings;
	}
	
	public static void bypassModuleReflectionRestrictions(Module moduleToBypass) {
		if (!HmmmLibrary.UNSAFE_REFLECT) throw new HmmmException("Please use the \"enable_unsafe_reflection_hmmm\" flag in minecraft launch arguments to access this");
		warnOnReflection(CALLER_CLASS.getCallerClass());
		
		assert Bypass.FIELDS != null; assert Bypass.CONSTRUCTORS != null; assert Bypass.METHODS != null; assert Bypass.CLASSES != null; assert Bypass.MEMBERS != null;
		
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
		@Nullable public static final Methods METHODS;
		@Nullable public static final Fields FIELDS;
		@Nullable public static final Constructors CONSTRUCTORS;
		@Nullable public static final Classes CLASSES;
		@Nullable public static final Members MEMBERS;
		
		static {
			if (HmmmLibrary.UNSAFE_REFLECT) {
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
		
		private Bypass() {MiscUtil.instantiationOfUtilClass(ReflectUtil.CALLER_CLASS.getCallerClass());}
	}
}
