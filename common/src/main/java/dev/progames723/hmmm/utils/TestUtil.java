package dev.progames723.hmmm.utils;

import dev.progames723.hmmm.ReflectionMappingsImpl;

import static dev.progames723.hmmm.HmmmLibrary.LOGGER;
import static dev.progames723.hmmm.HmmmLibrary.TEST;

public class TestUtil {//tests for the mod
	private TestUtil() {MiscUtil.instantiationOfUtilClass(ReflectUtil.CALLER_CLASS.getCallerClass());}
	
	private static final ReflectionMappingsImpl mappings = ReflectUtil.getReflectionMappingsImpl();
	
	public static void testAll() {
		testRemapping();
	}
	
	public static void testRemapping() {
		String mappedClassName = mappings.mapClassName("net.minecraft.world.entity.LivingEntity");
		String unmappedClassName = mappings.unmapClassName(mappedClassName);
		boolean passed = !mappedClassName.equals(unmappedClassName);
		LOGGER.info(TEST, "Remapping test passed: {}", passed);
	}
}
