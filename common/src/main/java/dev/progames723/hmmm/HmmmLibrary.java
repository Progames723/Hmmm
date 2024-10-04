package dev.progames723.hmmm;

import dev.architectury.event.EventHandler;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import dev.progames723.hmmm.utils.MathUtil;
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
	public static final Marker JAVA_COMMAND = MarkerFactory.getMarker("Java Command");
	public static final Marker TEST = MarkerFactory.getMarker("Test");
	public static final Marker REFLECT = MarkerFactory.getMarker("Reflection");
	public static final boolean TEST_ARG;
	public static final boolean UNSAFE_REFLECT;
	
	static {
		boolean tempTestArg = false;
		boolean tempUnsafeReflect = false;
		try {
			//forced to use reflection man
			tempUnsafeReflect = Platform.getEnvironment() == Env.CLIENT ? net.minecraft.client.main.Main.class.getDeclaredField("enableUnsafeReflect").getBoolean(null) : net.minecraft.server.Main.class.getDeclaredField("enableUnsafeReflect").getBoolean(null);
			tempTestArg = Platform.getEnvironment() == Env.CLIENT ? net.minecraft.client.main.Main.class.getDeclaredField("test_hmmm").getBoolean(null) : net.minecraft.server.Main.class.getDeclaredField("test_hmmm").getBoolean(null);
		} catch (Exception ignored) {}
		TEST_ARG = tempTestArg;
		UNSAFE_REFLECT = tempUnsafeReflect;
	}
	
	public static void main(String[] args) {
		java.util.logging.Logger.getLogger("Hmmm Library").severe("This cannot run this way");
		java.util.logging.Logger.getLogger("Hmmm Library").severe("Press enter to exit... ");
		try {
			System.in.read();
		} catch (IOException e) {
			throw new HmmmException(HmmmLibrary.class, e);
		}
		System.exit(-1);
	}
	
	public static void init() {
		LOGGER.info("Initializing HmmmLibrary");
		EventHandler.init();
		MathUtil.loadLibrary();
		LOGGER.info("Running system architecture: {}", PlatformUtil.getArchitecture());
		if (TEST_ARG) TestUtil.testAll();
		LOGGER.info("Initialized HmmmLibrary!");
	}
}
