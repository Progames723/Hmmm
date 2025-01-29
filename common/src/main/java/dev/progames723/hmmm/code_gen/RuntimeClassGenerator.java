package dev.progames723.hmmm.code_gen;

import org.objectweb.asm.*;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RuntimeClassGenerator {
	private static boolean DEBUG = false;//set manually or through reflection, dont leave as true in production
	
	private static final GeneratedClassLoader loader = new GeneratedClassLoader();
	
	private static final class GeneratedClassLoader extends ClassLoader {
		public Class<?> defineClass(String className, byte[] data) {return defineClass(className, data, 0, data.length);}
	}
	
	public static Class<?> generateClass(ClassDefinition clazz) {
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		
		//the class thing uhh idfk what im doing so i will now pray for it to start working correctly on my first try
		cw.visit(
			clazz.version(),
			clazz.access(),
			clazz.className(),
			null,
			clazz.superName() != null ? clazz.superName() : "java/lang/Object",
			clazz.interfaces()
		);
		
		//class annotation things
		for (AnnotationDefinition annotation : clazz.annotations()) {
			AnnotationVisitor av = cw.visitAnnotation(
				annotation.descriptor(),
				true
			);
			annotation.values().forEach(av::visit);
			av.visitEnd();
		}
		
		//fields yeah
		for (FieldDefinition field : clazz.fields()) {
			generateField(cw, field);
		}
		
		//default constructor lol
		if (clazz.createDefaultConstructor()) {
			generateDefaultConstructor(cw, clazz.superName());
		}
		
		//methods lol
		for (MethodDefinition method : clazz.methods()) {
			generateMethod(cw, method);
		}
		
		cw.visitEnd();
		byte[] bytes = cw.toByteArray();
		if (DEBUG) {
			ClassReader classReader = new ClassReader(bytes);
			classReader.accept(new CheckClassAdapter(null), 0);
			Path path = Paths.get(classReader.getClassName() + ".class");
			try {
				Files.createDirectories(path.getParent());
				Files.write(path, bytes);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return loader.defineClass(clazz.className().replace('/', '.'), bytes);
	}
	
	private static void generateField(ClassWriter cw, FieldDefinition field) {
		FieldVisitor fv = cw.visitField(
			field.access(),
			field.name(),
			field.descriptor(),
			null,
			field.initialFieldValue()
		);
		for (AnnotationDefinition annotation : field.annotations()) {
			AnnotationVisitor av = fv.visitAnnotation(
				annotation.descriptor(),
				true
			);
			annotation.values().forEach(av::visit);
			av.visitEnd();
		}
		fv.visitEnd();
	}
	
	private static void generateDefaultConstructor(ClassWriter cw, String superName) {
		MethodVisitor mv = cw.visitMethod(
			Opcodes.ACC_PUBLIC,
			"<init>",
			"()V",
			null,
			null
		);
		mv.visitCode();
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		if (superName == null) superName = "java/lang/Object";
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, superName, "<init>", "()V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0); //method code reuse
		mv.visitEnd();
	}
	
	private static void generateMethod(ClassWriter cw, MethodDefinition method) {
		MethodVisitor mv = cw.visitMethod(
			method.access(),
			method.name(),
			method.descriptor(),
			null,
			method.exceptions()
		);
		for (AnnotationDefinition annotation : method.annotations()) {
			AnnotationVisitor av = mv.visitAnnotation(
				annotation.descriptor(),
				true
			);
			annotation.values().forEach(av::visit);
			av.visitEnd();
		}
		mv.visitCode();
		method.instructions().accept(mv);
		mv.visitMaxs(0, 0); //we dont need to do shit lol
		mv.visitEnd();
	}
}
