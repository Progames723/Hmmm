package dev.progames723.hmmm;

import dev.architectury.event.EventHandler;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import dev.progames723.hmmm.utils.MathUtil;
import dev.progames723.hmmm.utils.PlatformUtil;
import dev.progames723.hmmm.utils.ReflectUtil;
import dev.progames723.hmmm.utils.TestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.io.IOException;

public class HmmmLibrary {
	public static final String MOD_ID = "hmmm";
	public static final Logger LOGGER = LoggerFactory.getLogger("HmmmLibrary");
	public static final Marker NATIVE = MarkerFactory.getMarker("Native");
	public static final Marker JAVA_COMMAND = MarkerFactory.getMarker("Java Command");
	public static final Marker TEST = MarkerFactory.getMarker("Test");
	private static final boolean testArg;
	
	static {
		boolean tempTestArg;
		try {
			tempTestArg = Platform.getEnvironment() == Env.CLIENT ? ReflectUtil.getField(net.minecraft.client.main.Main.class, "test_hmmm").getBoolean(null) : ReflectUtil.getField(net.minecraft.server.Main.class, "test_hmmm").getBoolean(null);
		} catch (IllegalAccessException e) {
			tempTestArg = false;
		}
		testArg = tempTestArg;
	}
	
	public static void main(String[] args) {
		System.out.println("This cannot run this way");
		System.out.println("Press enter to exit... ");
		try {
			System.in.read();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		System.exit(-1);
	}
	
	public static void init() {
		LOGGER.info("Initializing HmmmLibrary");
		EventHandler.init();
		MathUtil.loadLibrary();
		LOGGER.info("Running system architecture: {}", PlatformUtil.getArchitecture());
		if (testArg) TestUtil.testAll();
		LOGGER.info("Initialized HmmmLibrary!");
	}
}
