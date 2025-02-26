package dev.progames723.hmmm.utils;

import dev.progames723.hmmm.HmmmException;
import dev.progames723.hmmm.internal.CallerSensitive;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

import java.lang.annotation.Annotation;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

public class InternalUtils {
	@SuppressWarnings("removal")
	private static final ClassLoader systemClassLoader = AccessController.doPrivileged((PrivilegedAction<ClassLoader>) ClassLoader::getSystemClassLoader, AccessController.getContext(), new RuntimePermission("getClassLoader"));
	
	private InternalUtils() {MiscUtil.instantiationOfUtilClass(ReflectUtil.CALLER_CLASS.getCallerClass());}
	
	@SuppressWarnings("unchecked")
	@CallerSensitive
	public static <T> List<Class<?>> scanClassesFor(Class<T> classToScanFor) {
		CallerSensitive.Utils.throwExceptionIfNotAllowed(ReflectUtil.CALLER_CLASS.getCallerClass());
		if (classToScanFor == null) throw new HmmmException(ReflectUtil.CALLER_CLASS.getCallerClass(), new NullPointerException("argument cannot be null!"));
		boolean isAnnotation = classToScanFor.isAnnotation();
		boolean isSuperClass = !classToScanFor.isInterface();
		try (ScanResult result = new ClassGraph()
			.rejectPaths("java", "javax", "com.sun", "sun", "org.jetbrains", "jdk")//the basics
			.overrideClassLoaders(systemClassLoader)//me when system class loader
			.enableAllInfo()
			.removeTemporaryFilesAfterScan()
			.scan()) {
			if (isAnnotation)
				return result.getClassesWithAnnotation((Class<? extends Annotation>) classToScanFor).loadClasses();
			
			if (isSuperClass)
				return result.getSubclasses(classToScanFor).loadClasses();
			
			return result.getClassesImplementing(classToScanFor).loadClasses();
		}
	}
}
