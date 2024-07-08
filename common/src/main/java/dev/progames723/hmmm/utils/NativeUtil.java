package dev.progames723.hmmm.utils;

import dev.architectury.platform.Platform;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

public class NativeUtil {
	private static final int MIN_PREFIX_LENGTH = 3;
	public static final String NATIVE_FOLDER_PATH_PREFIX = "lib_temp";
	
	private static File temporaryDir;

	private NativeUtil() {}
	
	public static void loadLibraryFromJar(String path) {
		String fileName = getFileName(path);
		
		if (temporaryDir == null) {
			temporaryDir = createTempDirectory();
			temporaryDir.deleteOnExit();
		}
		
		File temp = new File(temporaryDir, fileName);
		
		try (InputStream is = NativeUtil.class.getResourceAsStream(path)) {
			Files.copy(is, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException | NullPointerException e) {
			temp.delete();
			if (e instanceof NullPointerException) throw new RuntimeException(new FileNotFoundException("File " + path + " was not found inside JAR."));
			throw new RuntimeException(e);
		}
		
		try {
			System.load(temp.getAbsolutePath());
		} finally {
			if (isPosixCompliant()) temp.delete();
			else temp.deleteOnExit();
		}
	}
	
	private static @NotNull String getFileName(String path) {
		if (path == null || !new File(path).isAbsolute()) {
			throw new IllegalArgumentException("The path has to be absolute.");
		}
		
		String[] parts = path.split("/");
		String filename = (parts.length > 1) ? parts[parts.length - 1] : null;
		
		if (filename == null || filename.length() < MIN_PREFIX_LENGTH) {
			throw new IllegalArgumentException("The filename has to be at least %s characters long.".formatted(MIN_PREFIX_LENGTH));
		}
		return filename;
	}
	
	private static boolean isPosixCompliant() {
		try {
			return FileSystems.getDefault().supportedFileAttributeViews().contains("posix");
		} catch (FileSystemNotFoundException | ProviderNotFoundException | SecurityException e) {
			return false;
		}
	}
	
	private static File createTempDirectory() {
		String tempDir = Platform.getConfigFolder().toString();
		tempDir = tempDir.charAt(tempDir.length() - 1) == '/' ? tempDir.concat("temp") : tempDir.concat("/temp");
		
		File generatedDir = new File(tempDir, NativeUtil.NATIVE_FOLDER_PATH_PREFIX + System.nanoTime());
		
		if (!generatedDir.mkdir()) throw new RuntimeException(new IOException("Failed to create temp directory " + generatedDir.getName()));
		
		return generatedDir;
	}
}
