package dev.progames723.hmmm;

import dev.progames723.hmmm.internal.CallerSensitive;
import dev.progames723.hmmm.utils.ReflectUtil;

public class HmmmError extends Error {
	private final Class<?> classAtFault;
	
	@CallerSensitive
	public HmmmError() {
		super("Class at fault: %s".formatted(ReflectUtil.CALLER_CLASS.getCallerClass()));
		classAtFault = ReflectUtil.CALLER_CLASS.getCallerClass();
	}
	
	@CallerSensitive
	public HmmmError(String message) {
		super("Class at fault: %s ".formatted(ReflectUtil.CALLER_CLASS.getCallerClass()) + message);
		classAtFault = ReflectUtil.CALLER_CLASS.getCallerClass();
	}
	
	@CallerSensitive
	public HmmmError(String message, Throwable cause) {
		super("Class at fault: %s ".formatted(ReflectUtil.CALLER_CLASS.getCallerClass()) + message, cause);
		classAtFault = ReflectUtil.CALLER_CLASS.getCallerClass();
	}
	
	@CallerSensitive
	public HmmmError(Throwable cause) {
		super("Class at fault: %s".formatted(ReflectUtil.CALLER_CLASS.getCallerClass()), cause);
		classAtFault = ReflectUtil.CALLER_CLASS.getCallerClass();
	}
	
	@CallerSensitive
	public HmmmError(Class<?> clazz) {
		super(clazz == null ? "" : "Class at fault: %s ".formatted(clazz));
		classAtFault = clazz;
	}
	
	@CallerSensitive
	public HmmmError(Class<?> clazz, String message) {
		super(clazz == null ? "" : "Class at fault: %s ".formatted(clazz) + message);
		classAtFault = clazz;
	}
	
	@CallerSensitive
	public HmmmError(Class<?> clazz, String message, Throwable cause) {
		super(clazz == null ? "" : "Class at fault: %s ".formatted(clazz) + message, cause);
		classAtFault = clazz;
	}
	
	@CallerSensitive
	public HmmmError(Class<?> clazz, Throwable cause) {
		super(clazz == null ? "" : "Class at fault: %s ".formatted(clazz), cause);
		classAtFault = clazz;
	}
	
	public Class<?> getClassAtFault() {
		return classAtFault;
	}
}
