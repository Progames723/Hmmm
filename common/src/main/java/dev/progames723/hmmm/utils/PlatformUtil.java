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
		return instance.architecture;
	}
	
	static void initArchitecture(Architecture architecture) {
		if (instance == null) new PlatformUtil(architecture);
	}
	
	/**
	 * Every architecture that is supported by jre(jdk) 17
	 */
	public enum Architecture {
		X86(false),
		X64(true),
		AARCH64(true),
		ARM(false),
		SPARCV9(true),
		S390X(true),
		RISCV64(true),
		PPC64(true),
		PPC64LE(true),
		UNKNOWN(false);
		
		Architecture(boolean isSupported) {
			this.isSupported = isSupported;
		}
		
		final boolean isSupported;
		
		public static String getStringArchitecture() {
			return System.getProperty("os.name");
		}
	}
}
