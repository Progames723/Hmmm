package dev.progames723.hmmm.utils;

import dev.progames723.hmmm.HmmmException;
import dev.progames723.hmmm.internal.CallerSensitive;
import dev.progames723.hmmm.misc.ClientOnly;
import dev.progames723.hmmm.xplat.XplatProvider;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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
					List<Class<T>> clsList = filter(result.getSuperclasses(classToScanFor)).loadClasses(classToScanFor, ignoreExceptions);
					if (!clsList.isEmpty()) yield clsList;
					try {
						yield (List<Class<T>>) (Object) filter(result.getSuperclasses(classToScanFor)).loadClasses(ignoreExceptions);
					} catch (ClassCastException e) {
						yield clsList;
					}
				}
				case SUB_CLASSES -> {
					List<Class<T>> clsList = filter(result.getSubclasses(classToScanFor)).loadClasses(classToScanFor, ignoreExceptions);
					if (!clsList.isEmpty()) yield clsList;
					try {
						yield (List<Class<T>>) (Object) filter(result.getSubclasses(classToScanFor)).loadClasses(ignoreExceptions);
					} catch (ClassCastException e) {
						yield clsList;
					}
				}
				case INTERFACE_IMPL -> {
					List<Class<T>> clsList = filter(result.getClassesImplementing(classToScanFor)).loadClasses(classToScanFor, ignoreExceptions);
					if (!clsList.isEmpty()) yield clsList;
					try {
						yield (List<Class<T>>) (Object) filter(result.getClassesImplementing(classToScanFor)).loadClasses(ignoreExceptions);
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
				case SUPER_CLASS -> filter(result.getSuperclasses(classToScanFor)).loadClasses(ignoreExceptions);
				case SUB_CLASSES -> filter(result.getSubclasses(classToScanFor)).loadClasses(ignoreExceptions);
				case INTERFACE_IMPL -> filter(result.getClassesImplementing(classToScanFor)).loadClasses(ignoreExceptions);
				default -> throw new HmmmException("Malformed enum!");
			};
		}
	}
	
	@CallerSensitive
	private static ClassInfoList filter(ClassInfoList list) {
		if (ReflectUtil.getCallerOfCaller().getDeclaringClass() == XplatProvider.class) return list;
		list.removeIf(classInfo -> XplatUtils.getSide().isServer() && classInfo.implementsInterface(ClientOnly.class));
		return list;
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
			return filter(result.getClassesWithAnnotation(annotation)).loadClasses();
		}
	}
	
	private static final Function<String, ClassInfoList.ClassInfoFilter> filterFunction = string -> classInfo -> {
		if (string.isEmpty()) return true;
		return classInfo.getName().startsWith(string);
	};
	
	@CallerSensitive
	public static List<Class<?>> getClassesForString(String string) {
		CallerSensitive.Utils.throwExceptionIfNotAllowed();
		if (string == null) throw new HmmmException("argument cannot be null!");
		try (ScanResult result = new ClassGraph()
			.acceptPackages(string)
			.scan()) {
			return filter(result.getAllClasses()).filter(filterFunction.apply(string)).loadClasses();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}
	
	@CallerSensitive
	public static List<Class<?>> getClassesForString(String string, Class<?> classToScanFor, ScanType type, boolean ignoreExceptions) {
		CallerSensitive.Utils.throwExceptionIfNotAllowed();
		if (string == null) throw new HmmmException("argument cannot be null!");
		try (ScanResult result = new ClassGraph()
			.rejectPaths("java", "javax", "com.sun", "sun", "org.jetbrains", "jdk")//the basics
			.overrideClassLoaders(ReflectUtil.getCaller().getDeclaringClass().getClassLoader())
			.enableAllInfo()
			.removeTemporaryFilesAfterScan()
			.scan()) {
			return switch (type) {
				case SUB_CLASSES ->
					filter(result.getSubclasses(classToScanFor).filter(filterFunction.apply(string))).loadClasses(ignoreExceptions);
				case INTERFACE_IMPL ->
					filter(result.getClassesImplementing(classToScanFor).filter(filterFunction.apply(string))).loadClasses(ignoreExceptions);
				case SUPER_CLASS -> new ArrayList<>();
				default -> throw new HmmmException("Malformed enum!");
			};
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}
	
	@ApiStatus.Internal
	public enum ScanType {
		SUPER_CLASS,
		SUB_CLASSES,
		INTERFACE_IMPL
	}
}
