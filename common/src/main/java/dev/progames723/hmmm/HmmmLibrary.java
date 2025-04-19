package dev.progames723.hmmm;

import com.mojang.brigadier.CommandDispatcher;
import com.sun.jna.Platform;
import dev.progames723.hmmm.event.api.AutoRegisterEvents;
import dev.progames723.hmmm.event.api.Event;
import dev.progames723.hmmm.event.api.EventListener;
import dev.progames723.hmmm.event.api.Events;
import dev.progames723.hmmm.event.events.client.RenderEvent;
import dev.progames723.hmmm.event.events.server.CommandRegistrationEvent;
import dev.progames723.hmmm.internal.CallerSensitive;
import dev.progames723.hmmm.misc.DoubleTicker;
import dev.progames723.hmmm.misc.EnumArgumentType;
import dev.progames723.hmmm.obj_attach.AttachmentProvider;
import dev.progames723.hmmm_natives.NativeReflectUtils;
import io.github.classgraph.ClassGraph;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.burningwave.core.assembler.StaticComponentContainer;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Set;

@AutoRegisterEvents
public class HmmmLibrary {
	public static final String MOD_ID = "hmmm";
	public static final Logger LOGGER = LoggerFactory.getLogger("Hmmm Library");
	public static final Marker REFLECT = MarkerFactory.getMarker("Reflection");
	public static final Marker EVENT = MarkerFactory.getMarker("Event");
	
	public static void main(String[] args) {
		java.util.logging.Logger.getLogger("Hmmm Library").severe("This file cannot run this way");
		java.util.logging.Logger.getLogger("Hmmm Library").severe("Press enter to exit... ");
		try {
			System.in.read();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		System.exit(-1);
	}
	
	@ApiStatus.Internal
	@CallerSensitive
	public static void preInit() {
		CallerSensitive.Utils.throwExceptionIfNotAllowed();
		new NativeReflectUtils();//init libraries
		ClassGraph.CIRCUMVENT_ENCAPSULATION = ClassGraph.CircumventEncapsulationMethod.JVM_DRIVER;
		new StaticComponentContainer();//init 2
		LOGGER.info("Initializing Hmmm Library");
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
	
	private enum AttachmentsCommandEnum {
		GET,
		GET_FULL
	}
	
	@EventListener(priority = Event.EventPriority.HIGHEST)
	public static void onCommandRegister(CommandRegistrationEvent event) {
		CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
		dispatcher.register(
			Commands.literal("attachments")
				.executes(commandContext -> {
					commandContext.getSource().sendFailure(Component.literal("Command requires an argument!"));
					return -1;
				})
				.then(
					Commands.argument("enum_arg", new EnumArgumentType<>(AttachmentsCommandEnum.GET))
						.executes(commandContext -> {
							commandContext.getSource().sendFailure(Component.literal("Command requires an additional argument!"));
							return -1;
						})
						.then(
							Commands.argument("player", EntityArgument.player())
								.executes(commandContext -> {
									Player player = EntityArgument.getPlayer(commandContext, "player");
									AttachmentsCommandEnum commandEnum = commandContext.getArgument("enum_arg", AttachmentsCommandEnum.class);
									Set<AttachmentProvider> attachments = player.getAttachments();
									switch (commandEnum) {
										case GET -> {
											if (attachments.isEmpty()) {
												commandContext.getSource().sendSuccess(() -> Component.literal("No attachments found"), true);
											} else {
												commandContext.getSource().sendSystemMessage(Component.literal("Attachments: "));
												for (AttachmentProvider provider : attachments) {
													commandContext.getSource().sendSystemMessage(Component.literal(provider.toString()));
												}
											}
										}
										case GET_FULL -> {
											if (attachments.isEmpty()) {
												commandContext.getSource().sendSuccess(() -> Component.literal("No attachments found"), true);
											} else {
												commandContext.getSource().sendSystemMessage(Component.literal("Attachments: "));
												for (AttachmentProvider provider : attachments) {
													commandContext.getSource().sendSystemMessage(Component.literal("=========================="));
													commandContext.getSource().sendSystemMessage(Component.literal(provider.toString()));
													for (Field field : provider.getClass().getDeclaredFields()) {
														try {
															commandContext.getSource().sendSystemMessage(Component.literal(field.toString()));
															field.setAccessible(true);
															commandContext.getSource().sendSystemMessage(Component.literal(field.get(provider).toString()));
														} catch (Exception ignored) {
														} finally {
															field.setAccessible(false);
														}
													}
												}
											}
										}
										default -> commandContext.getSource().sendSystemMessage(Component.literal("Invalid enum usage!"));
									}
									return 0;
								})
						)
				)
		);
	}
	
	private static final DoubleTicker ticker = new DoubleTicker();
	
	@Environment(EnvType.CLIENT)
	@EventListener(priority = Event.EventPriority.HIGHEST)
	public static void onRender(RenderEvent event) {
		if (!event.isPreRender()) return;
		//render here
		int width = 40;
		int height = 40 + (int) (Math.round(Math.sin(ticker.tick(event.getPartialTick()) / 15) * 10));
		event.getGuiGraphics().fillGradient(20, 20, width, height, 100, 0x80FF00FF, 0xFFFF0000);
	}
}
