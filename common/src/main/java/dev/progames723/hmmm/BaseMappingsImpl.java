package dev.progames723.hmmm;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

public class BaseMappingsImpl extends MappingsImpl {
	public BaseMappingsImpl(Collection<String> in, Collection<String> out) {
		super(in, out);
	}
	
	public BaseMappingsImpl(String[] in, String[] out) {
		super(in, out);
	}
	
	public BaseMappingsImpl(Enumeration<String> in, Enumeration<String> out) {
		super(in, out);
	}
	
	public BaseMappingsImpl(Iterator<String> in, Iterator<String> out) {
		super(in, out);
	}
	
	public BaseMappingsImpl(Map<String, String> mappingMap) {
		super(mappingMap);
	}
	
	@Override
	public String map(String string) {
		if (!super.from.contains(string)) return "";
		else if (!this.to.contains(string)) return "";
		else return super.map(string);
	}
	
	@Override
	public String unmap(String string) {
		if (!super.from.contains(string)) return "";
		else if (!this.to.contains(string)) return "";
		else return super.unmap(string);
	}
	
	@Override
	public String mapClassName(String className) {
		throw new UnsupportedOperationException("Please use ReflectUtil#getMappingsImpl!");
	}
	
	@Override
	public String mapField(String className, String field, String descriptor) {
		throw new UnsupportedOperationException("Please use ReflectUtil#getMappingsImpl!");
	}
	
	@Override
	public String mapMethod(String className, String method, String descriptor) {
		return "";
	}
	
	@Override
	public String unmapClassName(String className) {
		throw new UnsupportedOperationException("Please use ReflectUtil#getMappingsImpl!");
	}
	
	@Override
	public String unmapField(String className, String field, String descriptor) {
		throw new UnsupportedOperationException("Please use ReflectUtil#getMappingsImpl!");
	}
	
	@Override
	public String unmapMethod(String className, String method, String descriptor) {
		throw new UnsupportedOperationException("Please use ReflectUtil#getMappingsImpl!");
	}
}
