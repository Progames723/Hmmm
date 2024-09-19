package dev.progames723.hmmm;

import dev.progames723.hmmm.utils.ReflectUtil;

public class HmmmException extends RuntimeException {
	private final Class<?> classAtFault;
	
	public HmmmException() {
		super("Class at fault: %s\n".formatted(ReflectUtil.getCallerClass()));
		classAtFault = ReflectUtil.getCallerClass();
	}
	
	public HmmmException(String message) {
		super("Class at fault: %s\n".formatted(ReflectUtil.getCallerClass()) + message);
		classAtFault = ReflectUtil.getCallerClass();
	}
	
	public HmmmException(String message, Throwable cause) {
		super("Class at fault: %s\n".formatted(ReflectUtil.getCallerClass()) + message, cause);
		classAtFault = ReflectUtil.getCallerClass();
	}
	
	public HmmmException(Throwable cause) {
		super("Class at fault: %s\n".formatted(ReflectUtil.getCallerClass()), cause);
		classAtFault = ReflectUtil.getCallerClass();
	}
	
	public HmmmException(Class<?> clazz) {
		super("Class at fault: %s\n".formatted(ReflectUtil.getCallerClass()));
		classAtFault = clazz;
	}
	
	public HmmmException(Class<?> clazz, String message) {
		super("Class at fault: %s\n".formatted(ReflectUtil.getCallerClass()) + message);
		classAtFault = clazz;
	}
	
	public HmmmException(Class<?> clazz, String message, Throwable cause) {
		super("Class at fault: %s\n".formatted(clazz) + message, cause);
		classAtFault = clazz;
	}
	
	public HmmmException(Class<?> clazz, Throwable cause) {
		super("Class at fault: %s\n".formatted(ReflectUtil.getCallerClass()), cause);
		classAtFault = clazz;
	}
	
	public Class<?> getClassAtFault() {
		return classAtFault;
	}
}
