package dev.progames723.hmmm.code_gen;

import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;

import java.util.List;

public record ClassDefinition(
	String className,
	@Nullable String signature,
	@Nullable String superName, //set to null if you dont have a super class or just extend Object
	String[] interfaces,
	int version,
	int access,
	List<AnnotationDefinition> annotations,
	List<FieldDefinition> fields,
	List<MethodDefinition> methods,
	boolean createDefaultConstructor
) {
	public ClassDefinition(
		String className,
		@Nullable String signature,
		@Nullable String superName, //same here
		String[] interfaces,
		int access,
		List<AnnotationDefinition> annotations,
		List<FieldDefinition> fields,
		List<MethodDefinition> methods,
		boolean createDefaultConstructor
	) {
		this(className, signature, superName, interfaces, Opcodes.V1_8, access, annotations, fields, methods, createDefaultConstructor);
	}
}
