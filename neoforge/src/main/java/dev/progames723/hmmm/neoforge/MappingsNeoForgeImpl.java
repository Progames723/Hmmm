package dev.progames723.hmmm.neoforge;

import cpw.mods.modlauncher.api.INameMappingService;
import dev.progames723.hmmm.MappingsImpl;
import net.neoforged.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

public class MappingsNeoForgeImpl extends MappingsImpl {
	
	public MappingsNeoForgeImpl(Collection<String> in, Collection<String> out) {
		super(in, out);
	}
	
	public MappingsNeoForgeImpl(String[] in, String[] out) {
		super(in, out);
	}
	
	public MappingsNeoForgeImpl(Enumeration<String> in, Enumeration<String> out) {
		super(in, out);
	}
	
	public MappingsNeoForgeImpl(Iterator<String> in, Iterator<String> out) {
		super(in, out);
	}
	
	public MappingsNeoForgeImpl(Map<String, String> mappingMap) {
		super(mappingMap);
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
		throw new UnsupportedOperationException("Unmapping not supported by neoforge!(or at least i dont know any method to do that)");
	}
	
	@Override
	public String unmapField(Field a, String descriptor) {
		throw new UnsupportedOperationException("Unmapping not supported by neoforge!(or at least i dont know any method to do that)");
	}
	
	@Override
	public String unmapMethod(Method a, String descriptor) {
		throw new UnsupportedOperationException("Unmapping not supported by neoforge!(or at least i dont know any method to do that)");
	}
}
