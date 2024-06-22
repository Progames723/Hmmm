package dev.progames723.hmmm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface MappingsExtended extends Mappings<String, String> {
	String map(String b);
	
	String mapClassName(Class<?> b);
	
	String mapField(Field b, String descriptor);
	
	String mapMethod(Method b, String descriptor);
	
	String unmap(String a);
	
	String unmapClassName(Class<?> a);
	
	String unmapField(Field a, String descriptor);
	
	String unmapMethod(Method a, String descriptor);
}
