package dev.progames723.hmmm.code_gen;

import dev.progames723.hmmm.HmmmException;
import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.utils.ReflectUtil;
import org.burningwave.core.classes.Methods;
import org.objectweb.asm.*;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class RuntimeClassGenerator {
	private static boolean DEBUG = false;//set manually or through reflection, dont leave as true in production
	
	private static final GeneratedClassLoader loader = new GeneratedClassLoader();
	
	private static final class GeneratedClassLoader extends ClassLoader {}
	
	@SuppressWarnings("unchecked")
	public static <T> Class<? extends T> generateClass(ClassDefinition clazz) {
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		
		//yes im forced to use the class loader that loaded the interface to even think about casting
		//and dont ask about this
		ClassLoader casting = null;
		Class<? extends T> cls = null;
		String superName = clazz.superName();
		if (superName == null) {
			List<String> interfaces = List.of(clazz.interfaces());
			if (interfaces.isEmpty()) {
				cls = (Class<? extends T>) Object.class;
				casting = loader;
			} else {
				HmmmException stored = null;
				for (String string : interfaces) {
					try {
						cls = (Class<? extends T>) Class.forName(string.replace('/', '.'));
						break;
					} catch (Exception e) {
						if (stored != null) stored.addSuppressed(e);
						else (stored = new HmmmException(null, "Parent exception")).addSuppressed(e);
					}
				}
				if (cls == null) //noinspection ConstantValue
					throw stored != null ? stored : new HmmmException(null, "impossible");
			}
		} else {
			try {
				cls = (Class<? extends T>) Class.forName(superName.replace('/', '.'));
			} catch (Exception e) {
				throw new HmmmException(ReflectUtil.CALLER_CLASS.getCallerClass(), e);
			}
		}
		if (casting == null) casting = cls.getClassLoader();
		Method defineClassMethod = Methods.create().findFirstAndMakeItAccessible(
			casting.getClass(),
			"defineClass",
			String.class, byte[].class, int.class, int.class
		);
		assert defineClassMethod != null : "impossible";
		
		//it did work actually
		cw.visit(
			clazz.version(),
			clazz.access(),
			clazz.className(),
			clazz.signature(),
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
		debug(bytes);
		Class<? extends T> generated;
		try {
			generated = (Class<? extends T>) defineClassMethod.invoke(casting, clazz.className().replace('/', '.'), bytes, 0, bytes.length);
		} catch (ClassCastException e) {
			throw new HmmmException(ReflectUtil.CALLER_CLASS.getCallerClass(), "bro you used the wrong interface/super class", e);
		} catch (Exception e) {
			if (!DEBUG) {
				try {
					HmmmLibrary.LOGGER.error("Unknown class loading exception! Forcing debug for better error message...", e);
					DEBUG = true;
					debug(bytes);
				} finally {
					DEBUG = false;
				}
			}
			throw new HmmmException(ReflectUtil.CALLER_CLASS.getCallerClass(), e);
		}
		return generated;
	}
	
	public static <T> T generate(ClassDefinition clazz, Class<?>[] paramTypes, Object... args) {
		Class<? extends T> cls = generateClass(clazz);
		Constructor<? extends T> constructor;
		try {
			constructor = cls.getDeclaredConstructor(paramTypes);
		} catch (Exception e) {
			throw new HmmmException(ReflectUtil.CALLER_CLASS.getCallerClass(), e);
		}
		try {
			constructor.setAccessible(true);
			return constructor.newInstance(args);
		} catch (Exception e) {
			throw new HmmmException(ReflectUtil.CALLER_CLASS.getCallerClass(), e);
		} finally {
			constructor.setAccessible(false);
		}
	}
	
	private static void debug(byte[] bytes) {
		if (!DEBUG) return;
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(new CheckClassAdapter(null), 0);
		Path path = Paths.get(classReader.getClassName() + ".class");
		try {
			Files.createDirectories(path.getParent());
			Files.write(path, bytes);
		} catch (IOException e) {
			throw new HmmmException((Class<?>) null, e);
		}
	}
	
	private static void generateField(ClassWriter cw, FieldDefinition field) {
		FieldVisitor fv = cw.visitField(
			field.access(),
			field.name(),
			field.descriptor(),
			field.signature(),
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
			method.signature(),
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
