package dev.progames723.hmmm.utils;

import dev.progames723.hmmm.GMPWrapper;
import dev.progames723.hmmm.HmmmLibrary;
import net.minecraft.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;

public class NativeUtil {
	private static boolean initialized = false;
	private static boolean works = true;
	private static final String PATH;
	
	static {
		if (System.getProperty("java.library.path") == null) {
			System.setProperty("java.library.path", System.getProperty("java.io.tmpdir") + "/hmmm-temp-" + Instant.now().getEpochSecond() + "/");
		}
		PATH = System.getProperty("java.library.path");
	}
	
	public static void loadLibrary(InputStream inputStream, String fileName) throws IOException {
		char dirChar = '/';
		switch (Util.getPlatform()) {
			case WINDOWS -> {
				fileName = fileName.contains(".dll") ? fileName : fileName + ".dll";
				dirChar = '\\';
			}
			case LINUX -> fileName = fileName.contains(".so") ? fileName : fileName + ".so";
			//i will support MacOS only if i get a mac for free or other people compile it for me
//			case OSX -> fileName = fileName.contains(".dylib") ? fileName : fileName + ".dylib";
			default -> throw new RuntimeException("Unsupported os!");
		}
		String fileLocationWithFileName = PATH + dirChar + fileName;
		File actualFile = new File(fileLocationWithFileName);
		File workingDirectory = new File(PATH);
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(actualFile);
		} catch (IOException e) {
			workingDirectory.mkdir();
			actualFile.createNewFile();
			//trying again
			outputStream = new FileOutputStream(actualFile);
		}
		inputStream.readAllBytes();
		inputStream.transferTo(outputStream);
		outputStream.close();
		inputStream.close();
		try {
			System.load(fileLocationWithFileName);
		} catch (UnsatisfiedLinkError e) {
			actualFile.delete();
			workingDirectory.delete();
			throw e;
		}
		actualFile.deleteOnExit();
		workingDirectory.deleteOnExit();
	}
	
	public static void init() {
		if (initialized) return;
		initialized = true;
		InputStream linuxLibraryX64 = Thread.currentThread().getContextClassLoader().getResourceAsStream("native_libs/mathUtil/linux/x64/libmathUtil.so");
		InputStream windowsLibraryX64 = Thread.currentThread().getContextClassLoader().getResourceAsStream("native_libs/test/hmmm.dll");
		
		assert linuxLibraryX64 != null; assert windowsLibraryX64 != null;
		
		switch (Util.getPlatform()) {
			case LINUX -> {
				try {
					NativeUtil.loadLibrary(linuxLibraryX64, "libHmmm.so");
//				    System.load(linuxLibraryX64.getAbsolutePath());
					PlatformUtil.initArchitecture(PlatformUtil.Architecture.X64);
				} catch (UnsatisfiedLinkError e) {
					PlatformUtil.initArchitecture(PlatformUtil.Architecture.getArchitecture());
					HmmmLibrary.LOGGER.error(HmmmLibrary.NATIVE, "damn", e);
					works = false;
				} catch (IOException e) {
					works = false;
					HmmmLibrary.LOGGER.error("Encountered an IO exception", e);
					PlatformUtil.initArchitecture(PlatformUtil.Architecture.getArchitecture());
				}
			}
			case WINDOWS -> {
				try {
					NativeUtil.loadLibrary(windowsLibraryX64, "hmmm.dll");
//				    System.loadLibrary("hmmm");
					PlatformUtil.initArchitecture(PlatformUtil.Architecture.X64);
				} catch (UnsatisfiedLinkError e) {
					HmmmLibrary.LOGGER.error(HmmmLibrary.NATIVE, "damn", e);
					PlatformUtil.initArchitecture(PlatformUtil.Architecture.getArchitecture());
					works = false;
				} catch (IOException e) {
					works = false;
					HmmmLibrary.LOGGER.error("Encountered an IO exception", e);
					PlatformUtil.initArchitecture(PlatformUtil.Architecture.getArchitecture());
				}
			}
			default -> {
				HmmmLibrary.LOGGER.error(HmmmLibrary.NATIVE, "Unsupported OS!");
				works = false;
			}
		}
		try {
			linuxLibraryX64.close();
			windowsLibraryX64.close();
		} catch (IOException e) {
			HmmmLibrary.LOGGER.error(HmmmLibrary.NATIVE, "Cannot close some file's stream!", e);
		}
		if (!works) return;
		GMPWrapper.testGMP();
		try {
			MathUtil.fastPow(1, 1);
		} catch (UnsatisfiedLinkError e) {
			HmmmLibrary.LOGGER.error(HmmmLibrary.NATIVE, "the fuck", e);
		}
	}
}
