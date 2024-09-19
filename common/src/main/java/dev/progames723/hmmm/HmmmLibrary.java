package dev.progames723.hmmm;

import dev.architectury.event.EventHandler;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import dev.progames723.hmmm.event.LivingEvents;
import dev.progames723.hmmm.event.utils.DoubleValue;
import dev.progames723.hmmm.event.utils.TripleValue;
import dev.progames723.hmmm.utils.MathUtil;
import dev.progames723.hmmm.utils.MinecraftUtil;
import dev.progames723.hmmm.utils.PlatformUtil;
import dev.progames723.hmmm.utils.TestUtil;
import net.minecraft.world.entity.player.Player;
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
	public static final boolean testArg;
	public static final boolean unsafeReflect;
	
	static {
		boolean tempTestArg = false;
		boolean tempUnsafeReflect = false;
		try {
			tempUnsafeReflect = Platform.getEnvironment() == Env.CLIENT ? net.minecraft.client.main.Main.class.getDeclaredField("enableUnsafeReflect").getBoolean(null) : net.minecraft.server.Main.class.getDeclaredField("enableUnsafeReflect").getBoolean(null);
			tempTestArg = Platform.getEnvironment() == Env.CLIENT ? net.minecraft.client.main.Main.class.getDeclaredField("test_hmmm").getBoolean(null) : net.minecraft.server.Main.class.getDeclaredField("test_hmmm").getBoolean(null);
		} catch (Exception ignored) {}
		testArg = tempTestArg;
		unsafeReflect = tempUnsafeReflect;
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
		LivingEvents.LIVING_HURT.register((entity, source, amount) -> {
			if (entity instanceof Player player) {
				LOGGER.info("Player hurt: {} damage, {} calulated", amount, MinecraftUtil.DamageReduction.test(amount, source, player));
			}
			return new TripleValue<>(true, source, amount);
		});
		LivingEvents.LIVING_DAMAGED.register((entity, source, amount) -> {
			if (entity instanceof Player) {
				LOGGER.info("Player hurt: {} actual", amount);
			}
			return new DoubleValue<>(true, amount);
		});
		if (testArg) TestUtil.testAll();
		LOGGER.info("Initialized HmmmLibrary!");
	}
}
