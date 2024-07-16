package dev.progames723.hmmm.utils.neoforge;

import dev.progames723.hmmm.MappingsImpl;
import dev.progames723.hmmm.neoforge.MappingsNeoForgeImpl;

public class ReflectUtilImpl {
	public static MappingsImpl getModLoaderSpecificMappingsImpl() {
		return new MappingsNeoForgeImpl();
	}
}
