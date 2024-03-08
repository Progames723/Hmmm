package dev.progames723.hmmm;

import dev.architectury.event.EventHandler;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.progames723.hmmm.event.LivingEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HmmmLibrary {
	public static final String MOD_ID = "hmmm";
	public static final Logger LOGGER = LoggerFactory.getLogger("HmmmLibrary");
	public static void init() {
		LOGGER.info("Initializing HmmmLibrary");
		initializeEvents();
		PlayerEvent.PICKUP_ITEM_PRE.register((player, itemEntity, item) -> {
			return EventResult.pass();
		});
		LivingEvents.BEFORE_LIVING_HURT.register((level, entity, source, amount) -> {
			return amount;
		});
	}
	public static void initializeEvents(){
		EventHandler.init();
	}
}
