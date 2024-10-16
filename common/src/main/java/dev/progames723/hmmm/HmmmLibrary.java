package dev.progames723.hmmm;

import dev.architectury.event.EventHandler;
import dev.architectury.utils.EnvExecutor;
import dev.progames723.hmmm.utils.NativeUtil;
import dev.progames723.hmmm.utils.PlatformUtil;
import dev.progames723.hmmm.utils.TestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.io.IOException;

@SuppressWarnings("JavaReflectionMemberAccess")
public class HmmmLibrary {
	public static final String MOD_ID = "hmmm";
	public static final Logger LOGGER = LoggerFactory.getLogger("HmmmLibrary");
	public static final Marker NATIVE = MarkerFactory.getMarker("Native");
	public static final Marker TEST = MarkerFactory.getMarker("Test");
	public static final Marker REFLECT = MarkerFactory.getMarker("Reflection");
	public static final boolean TEST_ARG;
	public static final boolean UNSAFE_REFLECT;
	
	static {
		boolean temp_test;
		boolean temp_reflect;
		try {
			temp_test = EnvExecutor.getEnvSpecific(() -> () -> {
				try {return net.minecraft.client.main.Main.class.getDeclaredField("test_hmmm").getBoolean(null);
				} catch (Exception e) {return false;}}, () -> () -> {
				try {return net.minecraft.server.Main.class.getDeclaredField("test_hmmm").getBoolean(null);
				} catch (Exception e) {return false;}
			});
			temp_reflect = EnvExecutor.getEnvSpecific(() -> () -> {
				try {return net.minecraft.client.main.Main.class.getDeclaredField("enableUnsafeReflect").getBoolean(null);
				} catch (Exception e) {return false;}}, () -> () -> {
				try {return net.minecraft.server.Main.class.getDeclaredField("enableUnsafeReflect").getBoolean(null);
				} catch (Exception e) {return false;}
			});
		} catch (AssertionError e) {
			temp_test = false;
			temp_reflect = false;
		}
		TEST_ARG = temp_test;
		UNSAFE_REFLECT = temp_reflect;
	}
	
	public static void main(String[] args) {
		java.util.logging.Logger.getLogger("Hmmm Library").severe("This file cannot run this way");
		java.util.logging.Logger.getLogger("Hmmm Library").severe("Press enter to exit... ");
		try {
			System.in.read();
		} catch (IOException e) {
			throw new HmmmException(HmmmLibrary.class, e);
		}
		System.exit(-1);
	}
	
	public static void init() {
		NativeUtil.init();
		LOGGER.info("Initializing HmmmLibrary");
		EventHandler.init();
		LOGGER.info("Running system architecture: {}", PlatformUtil.getArchitecture());
		if (TEST_ARG) TestUtil.testAll();
		LOGGER.info("Initialized HmmmLibrary!");
	}
}
