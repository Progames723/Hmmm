package dev.progames723.hmmm;

import dev.architectury.event.EventHandler;
import dev.architectury.utils.EnvExecutor;
import dev.progames723.hmmm.event.api.Events;
import dev.progames723.hmmm.utils.PlatformUtil;
import dev.progames723.hmmm.utils.TestUtil;
import io.github.classgraph.ClassGraph;
import org.burningwave.core.assembler.StaticComponentContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.io.IOException;

@SuppressWarnings("JavaReflectionMemberAccess")
public class HmmmLibrary {
	public static final String MOD_ID = "hmmm";
	public static final Logger LOGGER = LoggerFactory.getLogger("HmmmLibrary");
	public static final Marker TEST = MarkerFactory.getMarker("Test");
	public static final Marker REFLECT = MarkerFactory.getMarker("Reflection");
	public static final Marker EVENT = MarkerFactory.getMarker("Event");
	public static final boolean TEST_ARG;
	
	static {
		boolean temp_test;
		try {
			temp_test = EnvExecutor.getEnvSpecific(() -> () -> {
				try {return net.minecraft.client.main.Main.class.getDeclaredField("test_hmmm").getBoolean(null);
				} catch (Exception e) {return false;}}, () -> () -> {
				try {return net.minecraft.server.Main.class.getDeclaredField("test_hmmm").getBoolean(null);
				} catch (Exception e) {return false;}
			});
		} catch (AssertionError e) {
			temp_test = false;
		}
		TEST_ARG = temp_test;
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
		ClassGraph.CIRCUMVENT_ENCAPSULATION = ClassGraph.CircumventEncapsulationMethod.JVM_DRIVER;
		StaticComponentContainer.JVMInfo.getVersion();
		LOGGER.info("Initializing HmmmLibrary");
		//TODO add better dependencies
		EventHandler.init();
		LOGGER.info("Running system architecture: {}", PlatformUtil.getArchitecture());
		if (TEST_ARG) TestUtil.testAll();
		Events.startEventRegistration();
		LOGGER.info("Initialized HmmmLibrary!");
	}
}
