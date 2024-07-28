package dev.progames723.hmmm.utils;

import sun.misc.Unsafe;

@SuppressWarnings("all")
public class CrashJVMUtil {
	public static void accessingReservedRAM() {Unsafe.getUnsafe().putAddress(0x0L, 0);}//crashes jvm on windows 100%
	
	public static void stackOverflow() {stackOverflow();}
	
	public static void OOM() {
		double[] theCrash = new double[(int) Double.MAX_VALUE];
	}
}
