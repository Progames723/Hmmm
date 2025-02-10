package dev.progames723.hmmm.utils;

import net.minecraft.Util;

public class PlatformUtil {
	private static final Util.OS os = Util.getPlatform();
	private static final Architecture architecture = Architecture.getArchitecture();
	
	private PlatformUtil() {MiscUtil.instantiationOfUtilClass(ReflectUtil.CALLER_CLASS.getCallerClass());}
	
	public static Util.OS getRunningOS() {
		return os;
	}
	
	public static Architecture getArchitecture() {
		return architecture;
	}
	
	/**
	 * Every architecture that is supported by jre(jdk) 17
	 */
	public enum Architecture {
		X86,
		X64,
		AARCH64,
		ARM,
		SPARCV9,
		S390X,
		RISCV64,
		PPC64,
		PPC64LE,
		UNKNOWN;
		
		public static Architecture getArchitecture() {
			return getArchitecture(System.getProperty("os.arch"));
		}
		
		public static Architecture getArchitecture(String arch) {//the list that i compiled myself
			return switch (arch.toLowerCase()) {
				case "x64", "x86_64", "amd64" -> Architecture.X64;
				case "x86", "x86_32", "i386", "i486", "i586", "i686" -> Architecture.X86;
				case "aarch64", "armv8", "armv9" -> Architecture.AARCH64;
				case "armv7", "arm" -> Architecture.ARM;
				default -> Architecture.UNKNOWN;
			};
		}
	}
}
