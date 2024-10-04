package dev.progames723.hmmm;

import dev.progames723.hmmm.utils.ReflectUtil;

public class HmmmError extends Error {
	private final Class<?> classAtFault;
	
	public HmmmError() {
		super("Class at fault: %s\n".formatted(ReflectUtil.getCallerClass()));
		classAtFault = ReflectUtil.getCallerClass();
	}
	
	public HmmmError(String message) {
		super("Class at fault: %s\n".formatted(ReflectUtil.getCallerClass()) + message);
		classAtFault = ReflectUtil.getCallerClass();
	}
	
	public HmmmError(String message, Throwable cause) {
		super("Class at fault: %s\n".formatted(ReflectUtil.getCallerClass()) + message, cause);
		classAtFault = ReflectUtil.getCallerClass();
	}
	
	public HmmmError(Throwable cause) {
		super("Class at fault: %s\n".formatted(ReflectUtil.getCallerClass()), cause);
		classAtFault = ReflectUtil.getCallerClass();
	}
	
	public HmmmError(Class<?> clazz) {
		super("Class at fault: %s\n".formatted(ReflectUtil.getCallerClass()));
		classAtFault = clazz;
	}
	
	public HmmmError(Class<?> clazz, String message) {
		super("Class at fault: %s\n".formatted(ReflectUtil.getCallerClass()) + message);
		classAtFault = clazz;
	}
	
	public HmmmError(Class<?> clazz, String message, Throwable cause) {
		super("Class at fault: %s\n".formatted(clazz) + message, cause);
		classAtFault = clazz;
	}
	
	public HmmmError(Class<?> clazz, Throwable cause) {
		super("Class at fault: %s\n".formatted(ReflectUtil.getCallerClass()), cause);
		classAtFault = clazz;
	}
	
	public Class<?> getClassAtFault() {
		return classAtFault;
	}
}
