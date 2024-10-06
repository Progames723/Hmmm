package dev.progames723.hmmm.utils.forge;

import dev.progames723.hmmm.MappingsImpl;
import dev.progames723.hmmm.forge.MappingsNeoForgeImpl;

public class ReflectUtilImpl {
	public static MappingsImpl getModLoaderSpecificMappingsImpl() {
		return new MappingsNeoForgeImpl();
	}
}
