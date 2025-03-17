package dev.progames723.hmmm;

public abstract class ReflectionMappingsImpl {
	public ReflectionMappingsImpl() {}
	
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
