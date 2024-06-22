package dev.progames723.hmmm.fabric;

import dev.progames723.hmmm.MappingsImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.fabricmc.loader.impl.util.mappings.MixinIntermediaryDevRemapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

public class MappingsFabricImpl extends MappingsImpl {
	private static final MappingResolver resolver = FabricLoader.getInstance().getMappingResolver();
	
	private static final MixinIntermediaryDevRemapper remapper = new MixinIntermediaryDevRemapper(FabricLauncherBase.getLauncher().getMappingConfiguration().getMappings(), "named", "intermediary");
	
	public MappingsFabricImpl() {
		super();
	}
	
	public MappingsFabricImpl(Collection<String> in, Collection<String> out) {
		super(in, out);
	}
	
	public MappingsFabricImpl(String[] in, String[] out) {
		super(in, out);
	}
	
	public MappingsFabricImpl(Enumeration<String> in, Enumeration<String> out) {
		super(in, out);
	}
	
	public MappingsFabricImpl(Iterator<String> in, Iterator<String> out) {
		super(in, out);
	}
	
	public MappingsFabricImpl(Map<String, String> mappingMap) {
		super(mappingMap);
	}
	
	@Override
	public String mapClassName(Class<?> a) {
		return resolver.mapClassName("intermediary", a.getName());
	}
	
	@Override
	public String mapField(Field a, String descriptor) {
		return resolver.mapFieldName("intermediary", a.getDeclaringClass().getName(), a.getName(), descriptor);
	}
	
	@Override
	public String mapMethod(Method a, String descriptor) {
		return resolver.mapMethodName("intermediary", a.getDeclaringClass().getName(), a.getName(), descriptor);
	}
	
	@Override
	public String unmapClassName(Class<?> b) {
		return resolver.unmapClassName("intermediary", b.getName());
	}
	
	@Override
	public String unmapField(Field b, String descriptor) {
		return remapper.mapFieldName(b.getDeclaringClass().getName(), b.getName(), descriptor);
	}
	
	@Override
	public String unmapMethod(Method b, String descriptor) {
		return remapper.mapMethodName(b.getDeclaringClass().getName(), b.getName(), descriptor);
	}
}
