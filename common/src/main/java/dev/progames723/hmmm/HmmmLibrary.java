package dev.progames723.hmmm;

import dev.architectury.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("nullable")
public class HmmmLibrary {
	public static final String MOD_ID = "hmmm";
	public static final Logger LOGGER = LoggerFactory.getLogger("HmmmLibrary");
	
	public static void init() {
		LOGGER.info("Initialized HmmmLibrary!");
		initializeEvents();
	}
	
	public static void initializeEvents(){
		EventHandler.init();
	}
}
