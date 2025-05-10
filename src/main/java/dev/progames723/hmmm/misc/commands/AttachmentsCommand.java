package dev.progames723.hmmm.misc.commands;

import com.mojang.brigadier.CommandDispatcher;
import dev.progames723.hmmm.interface_injection.PlayerAttachments;
import dev.progames723.hmmm.misc.commands.argument_types.EnumArgumentType;
import dev.progames723.hmmm.obj_attach.AttachmentProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.lang.reflect.Field;
import java.util.Set;

public class AttachmentsCommand {
	private enum AttachmentsCommandEnum {
		GET,
		GET_FULL
	}
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
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
									Set<? extends AttachmentProvider> attachments = ((PlayerAttachments) player).getPlayerAttachments();
									switch (commandEnum) {
										case GET -> {
											if (attachments.isEmpty()) {
												commandContext.getSource().sendSuccess(() -> Component.literal("No attachments found"), true);
											} else {
												commandContext.getSource().sendSystemMessage(Component.literal("Attachments: "));
												for (AttachmentProvider provider : attachments) {
													commandContext.getSource().sendSystemMessage(Component.literal("=========================="));
													commandContext.getSource().sendSystemMessage(Component.literal(provider.toString()));
												}
												if (!attachments.isEmpty()) commandContext.getSource().sendSystemMessage(Component.literal("=========================="));
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
															commandContext.getSource().sendSystemMessage(Component.literal("Field: " + field));
															field.setAccessible(true);
															commandContext.getSource().sendSystemMessage(Component.literal("Field value: " + field.get(provider)));
														} catch (Exception ignored) {
														} finally {
															field.setAccessible(false);
														}
													}
												}
												if (!attachments.isEmpty()) commandContext.getSource().sendSystemMessage(Component.literal("=========================="));
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
}
