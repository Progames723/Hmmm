package dev.progames723.hmmm.utils.neoforge;

import dev.progames723.hmmm.ReflectionMappingsImpl;
import dev.progames723.hmmm.neoforge.ReflectionMappingsNeoForgeImpl;

public class ReflectUtilImpl {
	public static ReflectionMappingsImpl getModLoaderSpecificMappingsImpl() {
		return new ReflectionMappingsNeoForgeImpl();
	}
}
