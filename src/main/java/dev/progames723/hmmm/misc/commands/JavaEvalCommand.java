package dev.progames723.hmmm.misc.commands;

import com.mojang.brigadier.CommandDispatcher;
import dev.progames723.hmmm.HmmmError;
import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.misc.commands.argument_types.JavaCodeArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class JavaEvalCommand {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		if (!HmmmLibrary.CONFIG.canEvalJavaCode()) throw new HmmmError("Cannot run unsafe code! (set \"eval_code\" to true in config to override)");
		dispatcher.register(
			Commands.literal("eval_java")
				.executes(context -> {
					context.getSource().sendFailure(Component.literal("No code provided!"));
					return -1;
				})
				.then(
					Commands.argument("code", new JavaCodeArgumentType())
						.executes(context -> {
							Object object = context.getArgument("code", Object.class);
							if (object instanceof CharSequence charSequence) {
								object = charSequence.toString();
							}
							context.getSource().sendSystemMessage(Component.literal(object != null ? object.toString() : "null"));
							return 0;
						})
				)
		);
	}
}
