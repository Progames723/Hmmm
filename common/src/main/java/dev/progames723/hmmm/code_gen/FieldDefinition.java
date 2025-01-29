package dev.progames723.hmmm.code_gen;

import java.util.List;

public record FieldDefinition(
	String name,
	String descriptor,
	int access,
	List<AnnotationDefinition> annotations,
	Object initialFieldValue
) {}
