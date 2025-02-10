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
	class Utils {
		private Utils() {MiscUtil.instantiationOfUtilClass(ReflectUtil.CALLER_CLASS.getCallerClass());}
		
		@CallerSensitive
		public static void throwExceptionIfNotAllowed(Class<?> caller, List<Class<?>> allowedClasses, List<String> allowedPackages, List<Class<?>> forbiddenClasses, List<String> forbiddenPackages) {
			ReflectUtil.getInstance();
			if (caller == null) throw new HmmmException(ReflectUtil.CALLER_CLASS.getCallerClass());
			
			String packageName = caller.getPackageName();
			
			boolean isInModule = packageName.startsWith("dev.progames723.hmmm");
			
			if (forbiddenClasses.contains(caller)) throw new HmmmException(caller);
			if (forbiddenPackages.contains(packageName)) throw new HmmmException(caller);
			
			if (allowedClasses.contains(caller)) return;
			if (allowedPackages.contains(packageName)) return;
			
			if (!isInModule) throw new HmmmException(caller);
		}
	}
}
