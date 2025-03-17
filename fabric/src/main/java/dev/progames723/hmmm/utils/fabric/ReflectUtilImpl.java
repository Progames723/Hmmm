package dev.progames723.hmmm.utils.fabric;

import dev.progames723.hmmm.ReflectionMappingsImpl;
import dev.progames723.hmmm.fabric.ReflectionMappingsFabricImpl;

public class ReflectUtilImpl {
	public static ReflectionMappingsImpl getModLoaderSpecificMappingsImpl() {
		return new ReflectionMappingsFabricImpl();
	}
}
