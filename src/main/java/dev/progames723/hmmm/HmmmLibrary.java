package dev.progames723.hmmm;

import com.mojang.brigadier.CommandDispatcher;
import com.sun.jna.Platform;
import dev.progames723.hmmm.config.v0.Json5Config;
import dev.progames723.hmmm.event.api.AutoRegisterEvents;
import dev.progames723.hmmm.event.api.Event;
import dev.progames723.hmmm.event.api.EventListener;
import dev.progames723.hmmm.event.api.Events;
import dev.progames723.hmmm.event.events.server.CommandRegistrationEvent;
import dev.progames723.hmmm.interface_injection.PlayerAttachments;
import dev.progames723.hmmm.internal.CallerSensitive;
import dev.progames723.hmmm.misc.IterableMap;
import dev.progames723.hmmm.misc.commands.AttachmentsCommand;
import dev.progames723.hmmm.misc.commands.ConfigCommand;
import dev.progames723.hmmm.misc.commands.JavaEvalCommand;
import dev.progames723.hmmm.networking.HmmmNetworking;
import dev.progames723.hmmm.obj_attach.AttachmentSync;
import dev.progames723.hmmm.obj_attach.SynchronizedAttachment;
import dev.progames723.hmmm.utils.MinecraftUtil;
import dev.progames723.hmmm_natives.NativeReflectUtils;
import io.github.classgraph.ClassGraph;
import io.netty.buffer.Unpooled;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@AutoRegisterEvents
public class HmmmLibrary {
	public static final String MOD_ID = "hmmm";
	public static final Logger LOGGER = LoggerFactory.getLogger("Hmmm Library");
	public static final Marker REFLECT = MarkerFactory.getMarker("Reflection");
	public static final Marker EVENT = MarkerFactory.getMarker("Event");
	
	@ApiStatus.Internal
	@CallerSensitive
	public static void preInit() {
		CallerSensitive.Utils.throwExceptionIfNotAllowed();
		ClassGraph.CIRCUMVENT_ENCAPSULATION = ClassGraph.CircumventEncapsulationMethod.JVM_DRIVER;
		new NativeReflectUtils();//init libraries
		LOGGER.info("Initializing Hmmm Library");
		registerPacketListeners();
		LOGGER.info("Running system architecture: {}", Platform.ARCH);
		LOGGER.info("Initializing dependencies...");
	}
	
	@ApiStatus.Internal
	@CallerSensitive
	public static void init() {
		CallerSensitive.Utils.throwExceptionIfNotAllowed();
		LOGGER.info("Starting event registration...");
		Events.startEventRegistration();
		LOGGER.info("Initialized Hmmm Library!");
	}
	
	public static HmmmConfig getConfig() {
		return HmmmConfig.instance;
	}
	
	@EventListener(priority = Event.EventPriority.HIGHEST)
	public static void onCommandRegister(CommandRegistrationEvent event) {
		CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
		AttachmentsCommand.register(dispatcher);
		if (HmmmLibrary.getConfig().canEvalJavaCode())
			JavaEvalCommand.register(dispatcher);
		ConfigCommand.register(dispatcher);
	}
	
	public static ResourceLocation createRL(String namespace, String path) {
		//? if > 1.20.4 {
		/*return ResourceLocation.fromNamespaceAndPath(namespace, path);
		*///?} else {
		return new ResourceLocation(namespace, path);
		//?}
	}
	
	private static void registerPacketListeners() {
		HmmmNetworking.registerC2SMessageHandler(payload -> {
			if (!payload.message().getID().equals(AttachmentSync.ATTACHMENT_SYNC_ID)) return;
			if (!(payload.message() instanceof HmmmNetworking.C2SMessage message)) return;
			FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
			message.writeToBuf(buf);
			SynchronizedAttachment provider = AttachmentSync.readData(buf);
			if (provider == null) {
				HmmmLibrary.LOGGER.error("Bad packet!", new HmmmException(null, "Bad packet!"));
				return;
			}
			MinecraftServer server = MinecraftUtil.Server.getServerInstance();
			if (server == null) return;
			Player player = server.getPlayerList().getPlayer(message.getSender());
			if (player == null) return;
			((PlayerAttachments) player).addPlayerAttachment(provider);
		});
	}
	
	public static final class HmmmConfig extends Json5Config {
		private static final AtomicLong instancesCount = new AtomicLong();
		private static final HmmmConfig instance = new HmmmConfig();
		
		private HmmmConfig() {
			super("hmmm-library", "hmmm", false);
		}
		
		@Override
		public IllegalStateException onlyOneInstanceAllowed() {
			if (instancesCount.get() > 0) return new IllegalStateException("Singleton config instantiated more than once!");
			instancesCount.incrementAndGet();
			return null;
		}
		
		@Override
		protected Map<String, String> createDefaultConfigValues(FileType type) {
			return IterableMap.ofEntries(
				Map.entry("eval_code", "false"),
				Map.entry("can_eval", "")
			);
		}
		
		@Override
		protected void writeComments(Writer writer, FileType type) throws IOException {
			writer.write("""
				// I am not responsible if you fuck up and enable code
				// evaluation on a server and if it does something bad.
				// Enable only if required(and maybe troll your friends)
    
				// For "can_eval" enter player names, separated by a " "(space)
				// character that can use the command.
				// Case-sensitive!!!
				""");
		}
		
		public boolean canEvalJavaCode() {
			return getBoolean("eval_code", false);
		}
		
		public List<String> getCanEval() {
			String s = getConfigValue("can_eval", "");
			if (s == null) return List.of();
			return List.of(s.split(" "));
		}
	}
}
