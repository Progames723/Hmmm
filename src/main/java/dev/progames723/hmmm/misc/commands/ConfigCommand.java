package dev.progames723.hmmm.misc.commands;

import com.mojang.brigadier.CommandDispatcher;
import dev.progames723.hmmm.config.v0.Config;
import dev.progames723.hmmm.misc.commands.argument_types.EnumArgumentType;
import dev.progames723.hmmm.misc.commands.argument_types.JavaClassArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigCommand {
	private enum Type {
		UNLOAD,
		LOAD,
		RELOAD,
		GET_VALUES
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Config> void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
			Commands.literal("config")
				.executes(context -> {
					context.getSource().sendFailure(Component.literal("Invalid argument(s)"));
					return -1;
				})
				.then(
					Commands.argument("enum", new EnumArgumentType<>(Type.RELOAD))
						.executes(context -> {
							context.getSource().sendFailure(Component.literal("Invalid argument(s)"));
							return -1;
						})
						.then(
							Commands.argument("class", new JavaClassArgumentType(Config.class))
								.executes(context -> {
									Type type = context.getArgument("enum", Type.class);
									Class<T> cls = null;
									try {
										 cls = (Class<T>) context.getArgument("class", Class.class);
										 List<Config> usedInstances = new ArrayList<>();
										 for (Config config : Config.getConfigInstances()) {
											 if (config.getClass() == cls) usedInstances.add(config);
										 }
										 for (Config config : usedInstances) {
											 switch (type) {
												 case LOAD -> config.loadConfig();
												 case UNLOAD -> config.unloadConfig();
												 case RELOAD -> config.reloadConfig();
												 case GET_VALUES -> {
													 StringBuilder stringBuilder = new StringBuilder();
													 stringBuilder.append(config.getClass().getSimpleName()).append(" config values:");
													 for (Map.Entry<String, String> s : config.getAllValues()) {
														 stringBuilder.append("\n").append(s.getKey()).append("=").append(s.getValue());
													 }
													 context.getSource().sendSystemMessage(Component.literal(stringBuilder.toString()));
												 }
											 }
										 }
									} catch (IOException e) {
										context.getSource().sendFailure(Component.literal(("Caught an IOException while trying to %s, %s config").formatted(type.name().toLowerCase(), cls)));
										throw new UncheckedIOException(e);
									} catch (Exception e) {
										context.getSource().sendFailure(Component.literal(("Caught an Exception while trying to %s, %s config").formatted(type.name().toLowerCase(), cls)));
										throw e;
									}
									return 0;
								})
						)
				)
		);
	}
}
