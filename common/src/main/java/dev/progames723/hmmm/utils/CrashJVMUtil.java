package dev.progames723.hmmm.utils;

import dev.progames723.hmmm.HmmmLibrary;
import sun.misc.Unsafe;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SuppressWarnings("all")
public class CrashJVMUtil {
	private CrashJVMUtil() {MiscUtil.instantiationOfUtilClass(ReflectUtil.CALLER_CLASS.getCallerClass());}
	
	private static final boolean hasPermission;
	
	static {
		boolean temp = true;
		try {
			System.getSecurityManager().checkPermission(new RuntimePermission("exitVM"));
		} catch (Exception e) {
			temp = false;
		}
		hasPermission = temp;
	}
	
	//kills the jvm without any way to recover it
	@KillsIt(true)
	public static void accessingReservedRAM() {
		if (!hasPermission) {
			HmmmLibrary.LOGGER.error("Unable to crash due to unsufficient permissions");
			return;
		}
		Unsafe.getUnsafe().putAddress(0xffffffffL, 0);
	}//crashes jvm on windows 100%
	
	//Causes a StackOverflowError, it can be caught?
	@KillsIt(false)
	public static void stackOverflow() {
		if (!hasPermission){
			HmmmLibrary.LOGGER.error("Unable to crash due to unsufficient permissions");
			return;
		}
		stackOverflow();
	}
	
	//Causes an OutOfMemoryError, it can be caught?
	@KillsIt(false)
	public static void OOM() {
		if (!hasPermission){
			HmmmLibrary.LOGGER.error("Unable to crash due to unsufficient permissions");
			return;
		}
		double[] arr = new double[Integer.MAX_VALUE];
	}
	
	@Retention(RetentionPolicy.SOURCE)
	private @interface KillsIt {boolean value();}
}
