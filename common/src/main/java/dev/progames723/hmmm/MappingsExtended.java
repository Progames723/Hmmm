package dev.progames723.hmmm;

public interface MappingsExtended extends Mappings<String, String> {
	String map(String a);
	
	String mapClassName(String className);
	
	String mapField(String className, String field, String descriptor);
	
	String mapMethod(String className, String method, String descriptor);
	
	String unmap(String b);
	
	String unmapClassName(String className);
	
	String unmapField(String className, String field, String descriptor);
	
	String unmapMethod(String className, String method, String descriptor);
}
