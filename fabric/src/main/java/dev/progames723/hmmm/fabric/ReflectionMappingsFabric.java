package dev.progames723.hmmm.fabric;

import dev.progames723.hmmm.ReflectionMappings;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.fabricmc.loader.impl.util.mappings.MixinIntermediaryDevRemapper;

public class ReflectionMappingsFabric extends ReflectionMappings {
	private static final MappingResolver resolver = FabricLoader.getInstance().getMappingResolver();
	
	private static final MixinIntermediaryDevRemapper remapper = new MixinIntermediaryDevRemapper(FabricLauncherBase.getLauncher().getMappingConfiguration().getMappings(), "named", "intermediary");
	
	public ReflectionMappingsFabric() {super();}
	
	@Override
	public String mapClassName(String className) {
		return resolver.mapClassName("intermediary", className);
	}
	
	@Override
	public String mapField(String className, String field, String descriptor) {
		return resolver.mapFieldName("intermediary", className, field, descriptor);
	}
	
	@Override
	public String mapMethod(String className, String method, String descriptor) {
		return resolver.mapMethodName("intermediary", className, method, descriptor);
	}
	
	@Override
	public String unmapClassName(String className) {
		return resolver.unmapClassName("named", className);
	}
	
	@Override
	public String unmapField(String className, String field, String descriptor) {
		return remapper.mapFieldName(className, field, descriptor);
	}
	
	@Override
	public String unmapMethod(String className, String method, String descriptor) {
		return remapper.mapMethodName(className, method, descriptor);
	}
}
