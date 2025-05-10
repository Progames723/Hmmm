package dev.progames723.hmmm;

import dev.progames723.hmmm.xplat.XplatProvider;

public abstract class ReflectionMappings implements XplatProvider {
	private static ReflectionMappings instance;
	
	public ReflectionMappings() {}
	
	public static ReflectionMappings getInstance() {
		if (instance == null) instance = XplatProvider.tryFindInstance(ReflectionMappings.class, false);
		return instance;
	}
	
	/**
	 * mapping a class to obfuscated(whatever is used)
	 */
	public abstract String mapClassName(String className);
	
	/**
	 * mapping a field to obfuscated(whatever is used)
	 */
	public abstract String mapField(String className, String field, String descriptor);
	
	/**
	 * mapping a method to obfuscated(whatever is used)
	 */
	public abstract String mapMethod(String className, String method, String descriptor);
	
	/**
	 * unmapping a class from obfuscated(whatever is used)
	 */
	public abstract String unmapClassName(String className);
	
	/**
	 * unmapping a field from obfuscated(whatever is used)
	 */
	public abstract String unmapField(String className, String field, String descriptor);
	
	/**
	 * unmapping a method from obfuscated(whatever is used)
	 */
	public abstract String unmapMethod(String className, String method, String descriptor);
}
