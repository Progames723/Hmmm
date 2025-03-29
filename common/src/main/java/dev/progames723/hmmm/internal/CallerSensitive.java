package dev.progames723.hmmm.internal;

import dev.progames723.hmmm.HmmmException;
import dev.progames723.hmmm.utils.MiscUtil;
import dev.progames723.hmmm.utils.ReflectUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface CallerSensitive {
	Class<?>[] allowedClasses() default {};
	
	String[] allowedPackages() default {};
	
	/**
	 * overrides allowed classes
	 */
	Class<?>[] forbiddenClasses() default {};
	
	/**
	 * overrides allowed packages
	 */
	String[] forbiddenPackages() default {};
	
	class Utils {
		private Utils() {MiscUtil.instantiationOfUtilClass(ReflectUtil.CALLER_CLASS.getCallerClass());}
		
		@CallerSensitive
		public static void throwExceptionIfNotAllowed(Class<?> caller) {
			CallerSensitive instance = ReflectUtil.getInstance();
			if (caller == null || instance == null) throw new HmmmException(ReflectUtil.CALLER_CLASS.getCallerClass());
			
			String packageName = caller.getPackageName();
			
			boolean isInModule = packageName.startsWith("dev.progames723");
			var h = new Object() {
				public boolean isForbidden = false;
				public boolean isAllowed = false;
			};
			
			h.isForbidden = List.of(instance.forbiddenClasses()).contains(caller);
			if (!h.isForbidden) {
				List.of(instance.forbiddenPackages()).forEach(string -> {
					if (packageName.startsWith(string)) h.isForbidden = true;
				});
			}
			h.isAllowed = List.of(instance.allowedClasses()).contains(caller);
			if (!h.isAllowed) {
				List.of(instance.allowedPackages()).forEach(string -> {
					if (packageName.startsWith(string)) h.isAllowed = true;
				});
			}
			
			if (h.isForbidden) throw new HmmmException(caller, new IllegalAccessException("Wrong class tried to access internal method!"));
			
			if (h.isAllowed) return;
			
			if (!isInModule) throw new HmmmException(caller, new IllegalAccessException("Wrong class tried to access internal method!"));
		}
	}
}
