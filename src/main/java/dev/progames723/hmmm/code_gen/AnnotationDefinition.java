package dev.progames723.hmmm.code_gen;

import java.util.Map;

public record AnnotationDefinition(
	String descriptor,
	Map<String, Object> values
) {}