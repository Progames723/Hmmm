package dev.progames723.hmmm.misc.commands;

import com.mojang.brigadier.CommandDispatcher;
import dev.progames723.hmmm.HmmmError;
import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.misc.commands.argument_types.JavaCodeArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.Optional;

public class JavaEvalCommand {
	@SuppressWarnings("unchecked")
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		if (!HmmmLibrary.getConfig().canEvalJavaCode()) throw new HmmmError("Cannot run unsafe code! (set \"eval_code\" to true in config to override)");
		dispatcher.register(
			Commands.literal("eval_java")
				.requires(commandSourceStack ->
					(commandSourceStack.isPlayer() && HmmmLibrary.getConfig().getCanEval().contains(commandSourceStack.getTextName()))
					|| (commandSourceStack.getEntity() == null && commandSourceStack.hasPermission(4))
				)
				.executes(context -> {
					context.getSource().sendFailure(Component.literal("No code provided!"));
					return -1;
				})
				.then(
					Commands.argument("code", new JavaCodeArgumentType())
						.executes(context -> {
							Optional<Object> object = (Optional<Object>) context.getArgument("code", Optional.class);
							if (object.isEmpty()) {
								context.getSource().sendSystemMessage(Component.literal("null"));
								return 0;
							}
							Object o = object.get();
							if (o instanceof CharSequence charSequence) {
								o = charSequence.toString();
							}
							context.getSource().sendSystemMessage(Component.literal(o.toString()));
							return 0;
						})
				)
		);
	}
}
