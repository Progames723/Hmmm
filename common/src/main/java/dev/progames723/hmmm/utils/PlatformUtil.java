package dev.progames723.hmmm.utils;

import net.minecraft.Util;

public class PlatformUtil {
	private static final Util.OS os = Util.getPlatform();
	private final Architecture architecture;//will be loaded from MathUtil(trial and error method)
	private static PlatformUtil instance;
	
	private PlatformUtil(Architecture architecture) {
		this.architecture = architecture;
	}
	
	public static Util.OS getRunningOS() {
		return os;
	}
	
	public static Architecture getArchitecture() {
		if (instance == null) instance = new PlatformUtil(Architecture.getArchitecture());
		return instance.architecture;
	}
	
	static void initArchitecture(Architecture architecture) {
		if (instance == null) instance = new PlatformUtil(architecture);
	}
	
	/**
	 * Every architecture that is supported by jre(jdk) 17
	 */
	public enum Architecture {
		X86(true),
		X64(true),
		AARCH64(false),
		ARM(false),
		SPARCV9(false),
		S390X(false),
		RISCV64(false),
		PPC64(false),
		PPC64LE(false),
		UNKNOWN(false);
		
		Architecture(boolean isSupported) {this.isSupported = isSupported;}
		
		final boolean isSupported;
		
		public static Architecture getArchitecture() {
			return getArchitecture(System.getProperty("os.arch"));
		}
		
		public static Architecture getArchitecture(String arch) {//the list that i compiled myself
			return switch (arch) {
				case "x64", "x86_64", "amd64" -> Architecture.X64;
				case "x86", "x86_32", "i386", "i486", "i586", "i686" -> Architecture.X86;
				case "aarch64", "armv8", "armv9" -> Architecture.AARCH64;
				case "armv7", "arm" -> Architecture.ARM;
				default -> Architecture.UNKNOWN;
			};
		}
	}
}
