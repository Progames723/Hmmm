package dev.progames723.hmmm.event.events;

import com.mojang.brigadier.CommandDispatcher;
import dev.progames723.hmmm.event.api.Event;
import dev.progames723.hmmm.misc.TriConsumer;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class CommandRegistrationEvent extends Event {
	private final CommandDispatcher<CommandSourceStack> dispatcher;
	private final Commands.CommandSelection selection;
	private final CommandBuildContext context;
	
	public CommandRegistrationEvent(CommandDispatcher<CommandSourceStack> dispatcher, Commands.CommandSelection selection, CommandBuildContext context) {
		super(false);
		this.dispatcher = dispatcher;
		this.selection = selection;
		this.context = context;
	}
	
	/**
	 * @param consumer the registration
	 */
	public void registerCommand(TriConsumer<CommandDispatcher<CommandSourceStack>, Commands.CommandSelection, CommandBuildContext> consumer) {
		consumer.accept(dispatcher, selection, context);
	}
	
	//use either the registerCommand method or get the dispatcher and everything else if needed
	
	public CommandDispatcher<CommandSourceStack> getDispatcher() {
		return dispatcher;
	}
	
	public Commands.CommandSelection getSelection() {
		return selection;
	}
	
	public CommandBuildContext getContext() {
		return context;
	}
}
