//? if forge {
/*package dev.progames723.hmmm.platforms.forge;

import cpw.mods.modlauncher.api.INameMappingService;
import dev.progames723.hmmm.ReflectionMappings;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.spongepowered.asm.bridge.RemapperAdapterFML;

public class ReflectionMappingsForge extends ReflectionMappings {
	private static final RemapperAdapterFML remapper = (RemapperAdapterFML) RemapperAdapterFML.create();
	
	public ReflectionMappingsForge() {super();}
	
	@Override
	public String unmapClassName(String className) {
		return ObfuscationReflectionHelper.remapName(INameMappingService.Domain.CLASS, className);
	}
	
	@Override
	public String unmapField(String className, String field, String descriptor) {
		return ObfuscationReflectionHelper.remapName(INameMappingService.Domain.FIELD, field);
	}
	
	@Override
	public String unmapMethod(String className, String method, String descriptor) {
		return ObfuscationReflectionHelper.remapName(INameMappingService.Domain.METHOD, method);
	}
	
	@Override
	public String mapClassName(String className) {
		return remapper.map(className);
	}
	
	@Override
	public String mapField(String className, String field, String descriptor) {
		return remapper.mapFieldName(className, field, descriptor);
	}
	
	@Override
	public String mapMethod(String className, String method, String descriptor) {
		return remapper.mapMethodName(className, method, descriptor);
	}
}
*///?}