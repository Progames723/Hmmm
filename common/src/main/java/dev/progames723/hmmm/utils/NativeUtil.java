package dev.progames723.hmmm.utils;

import com.sun.jna.Platform;
import dev.progames723.hmmm.GMP;
import dev.progames723.hmmm.GMPWrapper;
import dev.progames723.hmmm.HmmmException;
import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.internal.CallerSensitive;
import net.minecraft.Util;
import org.burningwave.core.classes.Methods;
import org.jetbrains.annotations.ApiStatus;

import java.io.*;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ReassignedVariable")
public class NativeUtil {
	private static boolean initialized = false;
	static boolean works = true;
	private static final String PATH;
	
	static {
		if (System.getProperty("java.library.path") == null) {
			System.setProperty("java.library.path", System.getProperty("java.io.tmpdir") + "/hmmm-temp-" + Instant.now().getEpochSecond() + "/");
		}
		PATH = System.getProperty("java.library.path");
	}
	
	public static void loadLibrary(InputStream stream, String fileName) throws IOException {
		char dirChar = '/';
		final String unmodifiedFilename = fileName;
		switch (Util.getPlatform()) {
			case WINDOWS -> {
				fileName = fileName.contains(".dll") ? fileName : fileName + ".dll";
				dirChar = '\\';
			}
			case LINUX -> {
				fileName = fileName.startsWith("lib") ? fileName : "lib" + fileName;
				fileName = fileName.contains(".so") ? fileName : fileName + ".so";
			}
			//i will support MacOS only if i get a mac for free or other people compile it for me
//			case OSX -> {
//				fileName = fileName.startsWith("lib") ? fileName : "lib" + fileName;
//				fileName = fileName.contains(".dylib") ? fileName : fileName + ".dylib";
//			}
			default -> throw new RuntimeException("Unsupported os!");
		}
		File actualFile = new File(PATH + dirChar + fileName);
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(actualFile);
		} catch (FileNotFoundException e) {
			actualFile.createNewFile();
			outputStream = new FileOutputStream(actualFile);
		}
		stream.transferTo(outputStream);
		try {
			System.loadLibrary(fileName);
		} catch (UnsatisfiedLinkError e) {
			loadLibraryFallback(stream, unmodifiedFilename);
			return;
		}
		stream.close();
	}
	
	public static void loadLibraryFallback(InputStream stream, String fileName) throws IOException {
		char dirChar = '/';
		switch (Util.getPlatform()) {
			case WINDOWS -> {
				fileName = fileName.contains(".dll") ? fileName : fileName + ".dll";
				dirChar = '\\';
			}
			case LINUX -> {
				fileName = fileName.startsWith("lib") ? fileName : "lib" + fileName;
				fileName = fileName.contains(".so") ? fileName : fileName + ".so";
			}
			//i will support MacOS only if i get a mac for free or other people compile it for me
//			case OSX -> {
//				fileName = fileName.startsWith("lib") ? fileName : "lib" + fileName;
//				fileName = fileName.contains(".dylib") ? fileName : fileName + ".dylib";
//			}
			default -> throw new RuntimeException("Unsupported os!");
		}
		File actualFile = new File(PATH + dirChar + fileName);
		try {
			System.load(actualFile.getAbsolutePath());
		} catch (UnsatisfiedLinkError e) {
			HmmmLibrary.LOGGER.error(HmmmLibrary.NATIVE, "Unable to load library: %s".formatted(fileName), e);
			works = false;
		}
		stream.close();
	}
	
	public static void init() {
		if (initialized) return;
		initialized = true;
		Methods methods = Methods.create();
		Method method;
		try {
			method = ClassLoader.class.getDeclaredMethod("getBuiltinPlatformClassLoader");
		} catch (NoSuchMethodException e) {
			works = false;
			throw new HmmmException(e);
		}
		methods.setAccessible(method, true);
		ClassLoader classLoader = methods.invoke(null, method);
		Map<String, InputStream> libraryStreams = new HashMap<>(5);
		if (Platform.isIntel()) {
			if (Platform.isLinux()) {
				libraryStreams.put("MathUtil", classLoader.getResourceAsStream("native_libs/mathUtil/linux/x64/libMathUtil.so"));
				libraryStreams.put("GMP", classLoader.getResourceAsStream("native_libs/GMP/linux/x64/libGMP.so"));
				libraryStreams.put("NativeReflectUtil", classLoader.getResourceAsStream("native_libs/nativeReflectUtil/linux/x64/libNativeReflectUtil.so"));
			}
			if (Platform.isWindows()) {
				libraryStreams.put("MathUtil", classLoader.getResourceAsStream("native_libs/mathUtil/windows/x64/MathUtil.dll"));
				libraryStreams.put("GMP", classLoader.getResourceAsStream("native_libs/GMP/windows/x64/GMP.dll"));
				libraryStreams.put("NativeReflectUtil", classLoader.getResourceAsStream("native_libs/nativeReflectUtil/windows/x64/NativeReflectUtil.dll"));
			}
		}
		libraryStreams.forEach((string, inputStream) -> {
			try {
				loadLibrary(inputStream, string);
				inputStream.close();
			} catch (IOException e) {
				HmmmLibrary.LOGGER.error(HmmmLibrary.NATIVE, "An IOException occured!", e);
				works = false;
			}
		});
		libraryStreams.clear();
		if (!works) return;
		GMPWrapper.testGMP();
		try {
			MathUtil.fastPow(1, 1);
		} catch (UnsatisfiedLinkError e) {
			HmmmLibrary.LOGGER.error(HmmmLibrary.NATIVE, "the fuck", e);
			works = false;
		}
	}
	
	@CallerSensitive(allowedClasses = GMP.class)
	@ApiStatus.Internal
	public static void setWorks(boolean value) {
		CallerSensitive.Utils.throwExceptionIfNotAllowed(ReflectUtil.CALLER_CLASS.getCallerClass());
		works = value;
	}
}
