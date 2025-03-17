package dev.progames723.hmmm.utils.forge;

import dev.progames723.hmmm.ReflectionMappingsImpl;
import dev.progames723.hmmm.forge.ReflectionMappingsNeoForgeImpl;

public class ReflectUtilImpl {
	public static ReflectionMappingsImpl getModLoaderSpecificMappingsImpl() {
		return new ReflectionMappingsNeoForgeImpl();
	}
}
