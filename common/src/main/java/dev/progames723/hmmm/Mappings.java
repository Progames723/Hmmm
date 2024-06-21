package dev.progames723.hmmm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface Mappings<A, B> {
	A map(B b);
	
	A mapClassName(Class<?> b);
	
	A mapField(Field b, String descriptor);
	
	A mapMethod(Method b, String descriptor);
	
	B unmap(A a);
	
	B unmapClassName(Class<?> a);
	
	B unmapField(Field a, String descriptor);
	
	B unmapMethod(Method a, String descriptor);
}
