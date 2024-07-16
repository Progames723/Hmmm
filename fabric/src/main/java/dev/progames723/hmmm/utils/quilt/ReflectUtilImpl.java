package dev.progames723.hmmm.utils.quilt;

import dev.progames723.hmmm.MappingsImpl;
import dev.progames723.hmmm.fabric.MappingsFabricImpl;

public class ReflectUtilImpl {
	public static MappingsImpl getModLoaderSpecificMappingsImpl() {
		return new MappingsFabricImpl();
	}
}
