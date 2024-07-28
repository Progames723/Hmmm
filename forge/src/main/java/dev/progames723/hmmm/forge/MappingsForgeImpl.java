package dev.progames723.hmmm.forge;

import cpw.mods.modlauncher.api.INameMappingService;
import dev.progames723.hmmm.MappingsImpl;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.spongepowered.asm.bridge.RemapperAdapterFML;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MappingsForgeImpl extends MappingsImpl {
	public MappingsForgeImpl() {
		super();
	}
	
	@Override
	public String mapClassName(Class<?> b) {
		return ObfuscationReflectionHelper.remapName(INameMappingService.Domain.CLASS, b.getName());
	}
	
	@Override
	public String mapField(Field b, String descriptor) {
		return ObfuscationReflectionHelper.remapName(INameMappingService.Domain.FIELD, b.getName());
	}
	
	@Override
	public String mapMethod(Method b, String descriptor) {
		return ObfuscationReflectionHelper.remapName(INameMappingService.Domain.METHOD, b.getName());
	}
	
	@Override
	public String unmapClassName(Class<?> a) {
		return RemapperAdapterFML.create().map(a.getName());
	}
	
	@Override
	public String unmapField(Field a, String descriptor) {
		return RemapperAdapterFML.create().mapFieldName(a.getDeclaringClass().getName(), a.getName(), descriptor);
	}
	
	@Override
	public String unmapMethod(Method a, String descriptor) {
		return RemapperAdapterFML.create().mapMethodName(a.getDeclaringClass().getName(), a.getName(), descriptor);
	}
}
