package dev.progames723.hmmm.utils;

import dev.progames723.hmmm.HmmmException;
import dev.progames723.hmmm.internal.CallerSensitive;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.Annotation;
import java.util.List;

@SuppressWarnings("unchecked")
public class InternalUtils {
	private InternalUtils() {MiscUtil.instantiationOfUtilClass();}
	
	@CallerSensitive
	public static <T> List<Class<T>> scanClassesForGenerics(Class<T> classToScanFor, ScanType type, boolean ignoreExceptions) {
		CallerSensitive.Utils.throwExceptionIfNotAllowed();
		if (classToScanFor == null) throw new HmmmException(new NullPointerException("argument cannot be null!"));
		try (ScanResult result = new ClassGraph()
			.rejectPaths("java", "javax", "com.sun", "sun", "org.jetbrains", "jdk")//the basics
			.overrideClassLoaders(ReflectUtil.getCaller().getDeclaringClass().getClassLoader())
			.enableAllInfo()
			.removeTemporaryFilesAfterScan()
			.scan()) {
			return switch (type) {
				case SUPER_CLASS -> {
					List<Class<T>> clsList = result.getSuperclasses(classToScanFor).loadClasses(classToScanFor, ignoreExceptions);
					if (!clsList.isEmpty()) yield clsList;
					try {
						yield (List<Class<T>>) (Object) result.getSuperclasses(classToScanFor).loadClasses(ignoreExceptions);
					} catch (ClassCastException e) {
						yield clsList;
					}
				}
				case SUB_CLASSES -> {
					List<Class<T>> clsList = result.getSubclasses(classToScanFor).loadClasses(classToScanFor, ignoreExceptions);
					if (!clsList.isEmpty()) yield clsList;
					try {
						yield (List<Class<T>>) (Object) result.getSubclasses(classToScanFor).loadClasses(ignoreExceptions);
					} catch (ClassCastException e) {
						yield clsList;
					}
				}
				case INTERFACE_IMPL -> {
					List<Class<T>> clsList = result.getClassesImplementing(classToScanFor).loadClasses(classToScanFor, ignoreExceptions);
					if (!clsList.isEmpty()) yield clsList;
					try {
						yield (List<Class<T>>) (Object) result.getClassesImplementing(classToScanFor).loadClasses(ignoreExceptions);
					} catch (ClassCastException e) {
						yield clsList;
					}
				}
				default -> throw new HmmmException("Malformed enum!");
			};
		}
	}
	
	@CallerSensitive
	public static <T> List<Class<T>> scanClassesForGenerics(Class<T> classToScanFor, ScanType type) {
		CallerSensitive.Utils.throwExceptionIfNotAllowed();
		if (classToScanFor == null) throw new HmmmException(new NullPointerException("argument cannot be null!"));
		return scanClassesForGenerics(classToScanFor, type, false);
	}
	
	@CallerSensitive
	public static List<Class<?>> scanClassesFor(Class<?> classToScanFor, ScanType type, boolean ignoreExceptions) {
		CallerSensitive.Utils.throwExceptionIfNotAllowed();
		if (classToScanFor == null) throw new HmmmException(new NullPointerException("argument cannot be null!"));
		try (ScanResult result = new ClassGraph()
			.rejectPaths("java", "javax", "com.sun", "sun", "org.jetbrains", "jdk")//the basics
			.overrideClassLoaders(ReflectUtil.getCaller().getDeclaringClass().getClassLoader())
			.enableAllInfo()
			.removeTemporaryFilesAfterScan()
			.scan()) {
			return switch (type) {
				case SUPER_CLASS -> result.getSuperclasses(classToScanFor).loadClasses(ignoreExceptions);
				case SUB_CLASSES -> result.getSubclasses(classToScanFor).loadClasses(ignoreExceptions);
				case INTERFACE_IMPL -> result.getClassesImplementing(classToScanFor).loadClasses(ignoreExceptions);
				default -> throw new HmmmException("Malformed enum!");
			};
		}
	}
	
	@CallerSensitive
	public static List<Class<?>> scanClassesFor(Class<?> classToScanFor, ScanType type) {
		CallerSensitive.Utils.throwExceptionIfNotAllowed();
		if (classToScanFor == null) throw new HmmmException(new NullPointerException("argument cannot be null!"));
		return scanClassesFor(classToScanFor, type, false);
	}
	
	@CallerSensitive
	public static List<Class<?>> scanForAnnotatedClassesWith(Class<? extends Annotation> annotation) {
		CallerSensitive.Utils.throwExceptionIfNotAllowed();
		if (annotation == null) throw new HmmmException(new NullPointerException("argument cannot be null!"));
		try (ScanResult result = new ClassGraph()
			.rejectPaths("java", "javax", "com.sun", "sun", "org.jetbrains", "jdk")//the basics
			.overrideClassLoaders(ReflectUtil.getCaller().getDeclaringClass().getClassLoader())
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
