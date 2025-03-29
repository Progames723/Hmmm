package dev.progames723.hmmm;

import com.sun.jna.Platform;
import dev.progames723.hmmm.event.api.Events;
import dev.progames723.hmmm.internal.CallerSensitive;
import dev.progames723.hmmm.utils.ReflectUtil;
import dev.progames723.hmmm.utils.TestUtil;
import dev.progames723.hmmm_natives.NativeReflectUtils;
import io.github.classgraph.ClassGraph;
import org.burningwave.core.assembler.StaticComponentContainer;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.io.IOException;

public class HmmmLibrary {
	public static final String MOD_ID = "hmmm";
	public static final Logger LOGGER = LoggerFactory.getLogger("Hmmm Library");
	public static final Marker TEST = MarkerFactory.getMarker("Test");
	public static final Marker REFLECT = MarkerFactory.getMarker("Reflection");
	public static final Marker EVENT = MarkerFactory.getMarker("Event");
	private static boolean TEST_ARG = false;
	
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
	
	@ApiStatus.Internal
	@CallerSensitive
	public static void preInit() {
		CallerSensitive.Utils.throwExceptionIfNotAllowed(ReflectUtil.CALLER_CLASS.getCallerClass());
		new NativeReflectUtils();//init
		ClassGraph.CIRCUMVENT_ENCAPSULATION = ClassGraph.CircumventEncapsulationMethod.JVM_DRIVER;
		StaticComponentContainer.JVMInfo.getVersion();
		LOGGER.info("Initializing Hmmm Library");
		LOGGER.info("Running system architecture: {}", Platform.ARCH);
		LOGGER.info("Initializing dependencies...");
	}
	
	@ApiStatus.Internal
	@CallerSensitive
	public static void init() {
		CallerSensitive.Utils.throwExceptionIfNotAllowed(ReflectUtil.CALLER_CLASS.getCallerClass());
		if (TEST_ARG) TestUtil.testAll();
		LOGGER.info("Starting event registration...");
		Events.startEventRegistration();
		LOGGER.info("Initialized Hmmm Library!");
	}
}
