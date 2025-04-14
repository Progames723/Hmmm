package dev.progames723.hmmm;

import dev.progames723.hmmm.internal.CallerSensitive;
import dev.progames723.hmmm.utils.ReflectUtil;

public class HmmmError extends Error {
	private final Class<?> classAtFault;
	
	@CallerSensitive
	public HmmmError() {
		super("Class at fault: %s.".formatted(ReflectUtil.getCallerOfCaller().getDeclaringClass()));
		classAtFault = ReflectUtil.getCallerOfCaller().getDeclaringClass();
	}
	
	@CallerSensitive
	public HmmmError(String message) {
		super("Class at fault: %s.\nException message: ".formatted(ReflectUtil.getCallerOfCaller().getDeclaringClass()) + message);
		classAtFault = ReflectUtil.getCallerOfCaller().getDeclaringClass();
	}
	
	@CallerSensitive
	public HmmmError(String message, Throwable cause) {
		super("Class at fault: %s.\nException message: ".formatted(ReflectUtil.getCallerOfCaller().getDeclaringClass()) + message, cause);
		classAtFault = ReflectUtil.getCallerOfCaller().getDeclaringClass();
	}
	
	@CallerSensitive
	public HmmmError(Throwable cause) {
		super("Class at fault: %s.".formatted(ReflectUtil.getCallerOfCaller().getDeclaringClass()), cause);
		classAtFault = ReflectUtil.getCallerOfCaller().getDeclaringClass();
	}
	
	@CallerSensitive
	public HmmmError(Class<?> clazz) {
		super(clazz == null ? "" : "Class at fault: %s.\nException message: ".formatted(clazz));
		classAtFault = clazz;
	}
	
	@CallerSensitive
	public HmmmError(Class<?> clazz, String message) {
		super(clazz == null ? "" : "Class at fault: %s.\nException message: ".formatted(clazz) + message);
		classAtFault = clazz;
	}
	
	@CallerSensitive
	public HmmmError(Class<?> clazz, String message, Throwable cause) {
		super(clazz == null ? "" : "Class at fault: %s.\nException message: ".formatted(clazz) + message, cause);
		classAtFault = clazz;
	}
	
	@CallerSensitive
	public HmmmError(Class<?> clazz, Throwable cause) {
		super(clazz == null ? "" : "Class at fault: %s".formatted(clazz), cause);
		classAtFault = clazz;
	}
	
	public Class<?> getClassAtFault() {
		return classAtFault;
	}
}
