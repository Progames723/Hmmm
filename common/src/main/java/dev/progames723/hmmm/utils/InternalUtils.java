package dev.progames723.hmmm.utils;

import dev.progames723.hmmm.HmmmException;
import dev.progames723.hmmm.internal.CallerSensitive;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.Annotation;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

public class InternalUtils {
	@SuppressWarnings("removal")
	private static final ClassLoader systemClassLoader = AccessController.doPrivileged((PrivilegedAction<ClassLoader>) ClassLoader::getSystemClassLoader, AccessController.getContext(), new RuntimePermission("getClassLoader"));
	
	private InternalUtils() {MiscUtil.instantiationOfUtilClass(ReflectUtil.CALLER_CLASS.getCallerClass());}
	
	@CallerSensitive
	public static <T> List<Class<T>> scanClassesFor(Class<T> classToScanFor, ScanType type, boolean ignoreExceptions) {
		CallerSensitive.Utils.throwExceptionIfNotAllowed(ReflectUtil.CALLER_CLASS.getCallerClass());
		if (classToScanFor == null) throw new HmmmException(ReflectUtil.CALLER_CLASS.getCallerClass(), new NullPointerException("argument cannot be null!"));
		try (ScanResult result = new ClassGraph()
			.rejectPaths("java", "javax", "com.sun", "sun", "org.jetbrains", "jdk")//the basics
			.overrideClassLoaders(systemClassLoader)//me when system class loader
			.enableAllInfo()
			.removeTemporaryFilesAfterScan()
			.scan()) {
			return switch (type) {
				case SUPER_CLASS -> result.getSuperclasses(classToScanFor).loadClasses(classToScanFor, ignoreExceptions);
				case SUB_CLASSES -> result.getSubclasses(classToScanFor).loadClasses(classToScanFor, ignoreExceptions);
				case INTERFACE_IMPL -> result.getClassesImplementing(classToScanFor).loadClasses(classToScanFor, ignoreExceptions);
				default -> throw new HmmmException(ReflectUtil.CALLER_CLASS.getCallerClass(), "Malformed enum!");
			};
		}
	}
	
	@CallerSensitive
	public static <T> List<Class<T>> scanClassesFor(Class<T> classToScanFor, ScanType type) {
		CallerSensitive.Utils.throwExceptionIfNotAllowed(ReflectUtil.CALLER_CLASS.getCallerClass());
		if (classToScanFor == null) throw new HmmmException(ReflectUtil.CALLER_CLASS.getCallerClass(), new NullPointerException("argument cannot be null!"));
		return scanClassesFor(classToScanFor, type, false);
	}
	
	@CallerSensitive
	public static List<Class<?>> scanForAnnotatedClassesWith(Class<? extends Annotation> annotation) {
		CallerSensitive.Utils.throwExceptionIfNotAllowed(ReflectUtil.CALLER_CLASS.getCallerClass());
		if (annotation == null) throw new HmmmException(ReflectUtil.CALLER_CLASS.getCallerClass(), new NullPointerException("argument cannot be null!"));
		try (ScanResult result = new ClassGraph()
			.rejectPaths("java", "javax", "com.sun", "sun", "org.jetbrains", "jdk")//the basics
			.overrideClassLoaders(systemClassLoader)//me when system class loader
			.enableAllInfo()
			.removeTemporaryFilesAfterScan()
			.scan()) {
			return result.getClassesWithAnnotation(annotation).loadClasses();
		}
	}
	
	@ApiStatus.Internal
	public enum ScanType {
		SUPER_CLASS,
		SUB_CLASSES,
		INTERFACE_IMPL
	}
}
