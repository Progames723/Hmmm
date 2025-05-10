package dev.progames723.hmmm.code_gen;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public record FieldDefinition(
	String name,
	@Nullable String signature,
	String descriptor,
	int access,
	List<AnnotationDefinition> annotations,
	Object initialFieldValue
) {}
