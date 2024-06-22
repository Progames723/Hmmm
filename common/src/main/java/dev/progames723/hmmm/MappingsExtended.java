package dev.progames723.hmmm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface MappingsExtended extends Mappings<String, String> {
	String map(String a);
	
	String mapClassName(Class<?> a);
	
	String mapField(Field a, String descriptor);
	
	String mapMethod(Method a, String descriptor);
	
	String unmap(String b);
	
	String unmapClassName(Class<?> b);
	
	String unmapField(Field b, String descriptor);
	
	String unmapMethod(Method b, String descriptor);
}
