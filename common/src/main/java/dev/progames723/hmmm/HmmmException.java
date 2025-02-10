package dev.progames723.hmmm;

import dev.progames723.hmmm.internal.CallerSensitive;
import dev.progames723.hmmm.utils.ReflectUtil;

public class HmmmException extends RuntimeException {
	private final Class<?> classAtFault;
	
	@CallerSensitive
	public HmmmException() {
		super("Class at fault: %s".formatted(ReflectUtil.CALLER_CLASS.getCallerClass()));
		classAtFault = ReflectUtil.CALLER_CLASS.getCallerClass();
	}
	
	@CallerSensitive
	public HmmmException(String message) {
		super("Class at fault: %s ".formatted(ReflectUtil.CALLER_CLASS.getCallerClass()) + message);
		classAtFault = ReflectUtil.CALLER_CLASS.getCallerClass();
	}
	
	@CallerSensitive
	public HmmmException(String message, Throwable cause) {
		super("Class at fault: %s ".formatted(ReflectUtil.CALLER_CLASS.getCallerClass()) + message, cause);
		classAtFault = ReflectUtil.CALLER_CLASS.getCallerClass();
	}
	
	@CallerSensitive
	public HmmmException(Throwable cause) {
		super("Class at fault: %s".formatted(ReflectUtil.CALLER_CLASS.getCallerClass()), cause);
		classAtFault = ReflectUtil.CALLER_CLASS.getCallerClass();
	}
	
	@CallerSensitive
	public HmmmException(Class<?> clazz) {
		super(clazz == null ? "" : "Class at fault: %s ".formatted(clazz));
		classAtFault = clazz;
	}
	
	@CallerSensitive
	public HmmmException(Class<?> clazz, String message) {
		super(clazz == null ? "" : "Class at fault: %s ".formatted(clazz) + message);
		classAtFault = clazz;
	}
	
	@CallerSensitive
	public HmmmException(Class<?> clazz, String message, Throwable cause) {
		super(clazz == null ? "" : "Class at fault: %s ".formatted(clazz) + message, cause);
		classAtFault = clazz;
	}
	
	@CallerSensitive
	public HmmmException(Class<?> clazz, Throwable cause) {
		super(clazz == null ? "" : "Class at fault: %s ".formatted(clazz), cause);
		classAtFault = clazz;
	}
	
	public Class<?> getClassAtFault() {
		return classAtFault;
	}
}
