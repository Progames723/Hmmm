package dev.progames723.hmmm.utils;

import dev.progames723.hmmm.MappingsImpl;

import static dev.progames723.hmmm.HmmmLibrary.LOGGER;
import static dev.progames723.hmmm.HmmmLibrary.TEST;

public class TestUtil {//tests for the mod
	private static final MappingsImpl mappings = ReflectUtil.getMappingsImpl();
	
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
