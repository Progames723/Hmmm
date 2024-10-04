package dev.progames723.hmmm.internal;

import dev.progames723.hmmm.HmmmException;
import dev.progames723.hmmm.utils.ReflectUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface CallerSensitive {
	//my own spin on caller sensitive shit
	
	/**
	 * prioritized over forbidden!!!
	 * wildcards allowed
	 * @return overridden allowed caller classes
	 */
	Class<?>[] allowedClasses() default {};
	
	/**
	 * wildcards allowed
	 * @return overridden forbidden caller classes
	 */
	Class<?>[] forbiddenClasses() default {};
	
	/**
	 * prioritized over forbidden!!!
	 * wildcards allowed
	 * @return overridden allowed caller packages
	 */
	String[] allowedPackages() default {};
	
	/**
	 * wildcards allowed
	 * @return overridden forbidden caller packages
	 */
	String[] forbiddenPackages() default {};
	
	class Utils {
		private Utils() {}
		
		public static void throwExceptionIfNotAllowed(Class<?> caller) {
			CallerSensitive instance = ReflectUtil.getCallerClass().getAnnotation(CallerSensitive.class);
			
			String packageName = caller.getPackageName();
			//list things
			List<Class<?>> allowedClasses = List.of(instance.allowedClasses());
			List<String> allowedPackages = List.of(instance.allowedPackages());
			List<Class<?>> forbiddenClasses = List.of(instance.forbiddenClasses());
			List<String> forbiddenPackages = List.of(instance.forbiddenPackages());
			
			boolean isInModule = packageName.startsWith("dev.progames723.hmmm");
			
			if (allowedClasses.contains(caller)) return;
			if (allowedPackages.contains(packageName)) return;
			
			if (!isInModule) throw new HmmmException(caller);
			
			if (forbiddenClasses.contains(caller)) throw new HmmmException(caller);
			if (forbiddenPackages.contains(packageName)) throw new HmmmException(caller);
			
		}
	}
}
