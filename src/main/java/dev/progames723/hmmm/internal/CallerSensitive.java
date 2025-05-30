package dev.progames723.hmmm.internal;

import dev.progames723.hmmm.HmmmError;
import dev.progames723.hmmm.HmmmException;
import dev.progames723.hmmm.utils.MiscUtil;
import dev.progames723.hmmm.utils.ReflectUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface CallerSensitive {
	/**
	 * overrides forbidden classes
	 */
	Class<?>[] allowedClasses() default {};
	
	/**
	 * overrides forbidden packages
	 */
	String[] allowedPackages() default {};
	
	Class<?>[] forbiddenClasses() default {};
	
	String[] forbiddenPackages() default {};
	
	class Utils {
		private Utils() {MiscUtil.instantiationOfUtilClass();}
		
		@CallerSensitive
		public static void throwExceptionIfNotAllowed() {
			StackWalker.StackFrame frame = ReflectUtil.getCaller();
			Class<?> caller = ReflectUtil.getCallerOfCaller().getDeclaringClass();
			Method method;
			try {
				method = frame.getDeclaringClass().getMethod(frame.getMethodName(), frame.getMethodType().parameterArray());
			} catch (Exception e) {
				throw new HmmmError(null, "JVM stack is unreliable. Termination Required", e);
			}
			CallerSensitive instance = method.getAnnotation(CallerSensitive.class);
			if (instance == null) throw new HmmmException("Must have @CallerSensitive annotation!");
			if (caller == null) throw new HmmmException();
			
			String packageName = caller.getPackageName();
			
			boolean isInModule = packageName.startsWith("dev.progames723.hmmm");
			var h = new Object() {
				public boolean isForbidden = false;
				public boolean isAllowed = false;
			};
			
			h.isForbidden = List.of(instance.forbiddenClasses()).contains(caller);
			if (!h.isForbidden) {
				List.of(instance.forbiddenPackages()).forEach(string -> {
					if (h.isForbidden) return;
					if (packageName.startsWith(string)) h.isForbidden = true;
				});
			}
			h.isAllowed = List.of(instance.allowedClasses()).contains(caller);
			if (!h.isAllowed) {
				List.of(instance.allowedPackages()).forEach(string -> {
					if (h.isAllowed) return;
					if (packageName.startsWith(string)) h.isAllowed = true;
				});
			}
			
			if ((h.isAllowed && !h.isForbidden) || isInModule) return;
			
			throw new HmmmException(caller, new IllegalAccessException("Wrong class tried to access internal method!"));
		}
	}
}
