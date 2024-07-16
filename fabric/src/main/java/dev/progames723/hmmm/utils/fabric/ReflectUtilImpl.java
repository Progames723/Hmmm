package dev.progames723.hmmm.utils.fabric;

import dev.progames723.hmmm.MappingsImpl;
import dev.progames723.hmmm.fabric.MappingsFabricImpl;

public class ReflectUtilImpl {
	public static MappingsImpl getModLoaderSpecificMappingsImpl() {
		return new MappingsFabricImpl();
	}
}
