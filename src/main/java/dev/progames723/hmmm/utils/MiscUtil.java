package dev.progames723.hmmm.utils;

import dev.progames723.hmmm.HmmmException;

public class MiscUtil {
	public static void instantiationOfUtilClass() {
		throw new HmmmException(ReflectUtil.getCallerOfCaller().getDeclaringClass(), "Tried to instantiate a util class! Class instantiated: " + ReflectUtil.getCaller().getDeclaringClass());
	}
}
