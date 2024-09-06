package dev.progames723.hmmm.utils;

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
	public static void loadLibrary(String absolutePath) {
		File file = new File(absolutePath);
		if (!file.isAbsolute()) throw new RuntimeException("File path not absolute!");
		File tempDirectory;
		try {
			String tmpDirsLocation = System.getProperty("java.io.tmpdir");
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
			//i will support only if i get a mac for free or other people compile
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
	}
}
