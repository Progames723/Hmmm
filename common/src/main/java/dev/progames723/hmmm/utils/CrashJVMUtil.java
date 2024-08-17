package dev.progames723.hmmm.utils;

import sun.misc.Unsafe;

@SuppressWarnings("all")
public class CrashJVMUtil {
	//kills the jvm without any way to recover it
	@KillsIt(true)
	public static void accessingReservedRAM() {Unsafe.getUnsafe().putAddress(0xffffffffL, 0);}//crashes jvm on windows 100%
	
	//Causes a StackOverflowError, it can be caught maybe?
	@KillsIt(false)
	public static void stackOverflow() {stackOverflow();}
	
	//Causes an OutOfMemoryError, it can be caught maybe?
	@KillsIt(false)
	public static void OOM() {new double[Integer.MAX_VALUE].clone();}
	
	private @interface KillsIt {boolean value();}
}
