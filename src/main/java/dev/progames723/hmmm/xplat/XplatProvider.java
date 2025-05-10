package dev.progames723.hmmm.xplat;

import dev.progames723.hmmm.HmmmException;
import dev.progames723.hmmm.utils.InternalUtils;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * only really useful for multiplatform projects with arch loom
 */
public interface XplatProvider {
	@SuppressWarnings("unchecked")
	static <T extends XplatProvider> T tryFindInstance(Class<T> XplatClass, boolean isInterface) {
		List<Class<?>> classes = InternalUtils.scanClassesFor(
			XplatClass,
			isInterface ? InternalUtils.ScanType.INTERFACE_IMPL : InternalUtils.ScanType.SUB_CLASSES,
			true
		);
		if (classes.size() != 1) throw new HmmmException("Xplat found %s instances instead of expected 1! Instances: %s".formatted(classes.size(), classes));
		Constructor<T> constructor;
		T instance;
		Class<T> cls;
		try {
			cls = (Class<T>) classes.get(0);
		} catch (ClassCastException e) {
			throw new HmmmException("Class could not be cast to the impl", e);
		}
		if (cls.isInterface())
			throw new HmmmException("Xplat provider found an interface impl! Interfaces cannot be used as platform implementations!");
		try {
			constructor = cls.getDeclaredConstructor();
			instance = constructor.newInstance();
		} catch (NoSuchMethodException e) {
			throw new HmmmException("Expected a no-args constructor in Xplat impl, found %s instead!".formatted((Object[]) cls.getDeclaredConstructors()), e);
		} catch (Exception e) {
			throw new HmmmException("Failure to construct a new Xplat impl instance!", e);
		}
		return instance;
	}
}
