package dev.progames723.hmmm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
	public String mapClassName(Class<?> a) {
		throw new UnsupportedOperationException("Please use ReflectUtil#getMappingsImpl!");
	}
	
	@Override
	public String mapField(Field a, String descriptor) {
		throw new UnsupportedOperationException("Please use ReflectUtil#getMappingsImpl!");
	}
	
	@Override
	public String mapMethod(Method a, String descriptor) {
		throw new UnsupportedOperationException("Please use ReflectUtil#getMappingsImpl!");
	}
	
	@Override
	public String unmapClassName(Class<?> b) {
		throw new UnsupportedOperationException("Please use ReflectUtil#getMappingsImpl!");
	}
	
	@Override
	public String unmapField(Field b, String descriptor) {
		throw new UnsupportedOperationException("Please use ReflectUtil#getMappingsImpl!");
	}
	
	@Override
	public String unmapMethod(Method b, String descriptor) {
		throw new UnsupportedOperationException("Please use ReflectUtil#getMappingsImpl!");
	}
}
