package dev.progames723.hmmm.utils;

import dev.progames723.hmmm.GMP;
import dev.progames723.hmmm.HmmmLibrary;
import net.minecraft.Util;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

public class NativeUtil {
	private static boolean initialized = false;
	private static boolean works = true;
	
	public static void loadLibrary(String absolutePath) {
		File file = new File(absolutePath);
		if (!file.isAbsolute()) throw new RuntimeException("File path not absolute!");
		File tempDirectory;
		try {
			Path path = Paths.get(FileUtils.getTempDirectory().getAbsolutePath(), String.valueOf(Instant.now().getEpochSecond()));
			tempDirectory = Files.createDirectory(path).toFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		try {
			String toLoad = Files.copy(file.toPath(), tempDirectory.toPath().resolve(file.toPath().getFileName())).toFile().getAbsolutePath();
			System.load(toLoad);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		tempDirectory.deleteOnExit();
	}
	
	public static void loadLibrary(InputStream inputStream, String fileName) throws IOException {
		String tempDirLocation = System.getProperty("java.io.tmpdir");
		char dirChar = '/';
		switch (Util.getPlatform()) {
			case WINDOWS -> {
				fileName = fileName.contains(".dll") ? fileName : fileName + ".dll";
				dirChar = '\\';
			}
			case LINUX -> fileName = fileName.contains(".so") ? fileName : fileName + ".so";
			//i will support only if i get a mac for free or other people compile it for me
//			case OSX -> fileName = fileName.contains(".dylib") ? fileName : fileName + ".dylib";
			default -> throw new RuntimeException("Unsupported os!");
		}
		String fileLocation = tempDirLocation + dirChar + "hmmm-temp-" + Instant.now().getEpochSecond();
		String fileLocationWithFileName = fileLocation + dirChar + fileName;
		File actualFile = new File(fileLocationWithFileName);
		File workingDirectory = new File(fileLocation);
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(fileLocationWithFileName);
		} catch (IOException e) {
			workingDirectory.mkdir();
			actualFile.createNewFile();
			//trying again
			outputStream = new FileOutputStream(fileLocationWithFileName);
		}
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
		InputStream windowsLibraryX64 = Thread.currentThread().getContextClassLoader().getResourceAsStream("native_libs/test/libHmmm.dll");
		
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
//					NativeUtil.loadLibrary(windowsLibraryX64, "libHmmm.dll");
				    System.load("E:\\IdeaProjects\\hmmm library\\nativeLibSrc\\hmmm.dll");
					PlatformUtil.initArchitecture(PlatformUtil.Architecture.X64);
				} catch (UnsatisfiedLinkError e) {
					HmmmLibrary.LOGGER.error(HmmmLibrary.NATIVE, "damn", e);
					PlatformUtil.initArchitecture(PlatformUtil.Architecture.getArchitecture());
					works = false;
				}/* catch (IOException e) {
					works = false;
					HmmmLibrary.LOGGER.error("Encountered an IO exception", e);
					PlatformUtil.initArchitecture(PlatformUtil.Architecture.getArchitecture());
				}*/
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
		try {
			GMP.init();
		} catch (UnsatisfiedLinkError e) {
			HmmmLibrary.LOGGER.error(HmmmLibrary.NATIVE, "the fuck", e);
		}
		try {
			MathUtil.init();
		} catch (UnsatisfiedLinkError e) {
			HmmmLibrary.LOGGER.error(HmmmLibrary.NATIVE, "the fuck", e);
		}
	}
}
