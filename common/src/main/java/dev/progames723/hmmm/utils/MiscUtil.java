package dev.progames723.hmmm.utils;

import dev.progames723.hmmm.HmmmException;

public class MiscUtil {
	public static void instantiationOfUtilClass(Class<?> caller) {
		throw new HmmmException(caller, "Tried to instantiate a util class! Class instantiated: " + ReflectUtil.STACK_WALKER.getCallerClass().getSimpleName());
	}
}
