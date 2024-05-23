package dev.progames723.hmmm;

import dev.architectury.event.EventHandler;
import dev.progames723.hmmm.event.DoubleValue;
import dev.progames723.hmmm.event.LivingEvents;
import dev.progames723.hmmm.event.TripleValue;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HmmmLibrary {
	public static final String MOD_ID = "hmmm";
	public static final Logger LOGGER = LoggerFactory.getLogger("HmmmLibrary");
	
	public static void init() {
		LOGGER.info("Initialized HmmmLibrary!");
		EventHandler.init();
		LivingEvents.LIVING_HURT.register((living, source, amount) -> {
			DamageSource newSource = source;
			float newAmount = amount;
			if (amount <= 5f) {
				newSource = living.damageSources().fellOutOfWorld();
				newAmount = 5f;
			}
			if (living instanceof Player) {
//				LOGGER.info("{} damage calculated", MinecraftDamageReduction.damageReduction(newAmount, newSource, living));
			}
			return new TripleValue<>(true, newSource, newAmount);
		});
		LivingEvents.LIVING_DAMAGED.register((living, source, amount) -> {
			if (living instanceof Player) {
				LOGGER.info("{} damage dealt", amount);
			}
			return new DoubleValue<>(true, amount);
		});
	}
}
