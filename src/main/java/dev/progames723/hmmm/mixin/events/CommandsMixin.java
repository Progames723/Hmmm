package dev.progames723.hmmm.mixin.events;

import com.mojang.brigadier.CommandDispatcher;
import dev.progames723.hmmm.event.api.Events;
import dev.progames723.hmmm.event.events.server.CommandRegistrationEvent;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Commands.class)
public abstract class CommandsMixin {
	@Shadow @Final private CommandDispatcher<CommandSourceStack> dispatcher;
	
	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/CommandDispatcher;setConsumer(Lcom/mojang/brigadier/ResultConsumer;)V"))
	private void initInject(Commands.CommandSelection selection, CommandBuildContext context, CallbackInfo ci) {
		Events.invokeEvent(new CommandRegistrationEvent(dispatcher, selection, context));
	}
}
