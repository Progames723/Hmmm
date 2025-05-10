package dev.progames723.hmmm.code_gen;

import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.MethodVisitor;

import java.util.List;
import java.util.function.Consumer;

public record MethodDefinition(
	String name,
	@Nullable String signature,
	String descriptor,
	int access,
	List<AnnotationDefinition> annotations,
	String[] exceptions,
	Consumer<MethodVisitor> instructions
) {}