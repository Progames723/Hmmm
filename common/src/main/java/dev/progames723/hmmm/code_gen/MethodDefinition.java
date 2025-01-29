package dev.progames723.hmmm.code_gen;

import org.objectweb.asm.MethodVisitor;

import java.util.List;
import java.util.function.Consumer;

public record MethodDefinition(
	String name,
	String descriptor,
	int access,
	List<AnnotationDefinition> annotations,
	String[] exceptions,
	Consumer<MethodVisitor> instructions
) {}