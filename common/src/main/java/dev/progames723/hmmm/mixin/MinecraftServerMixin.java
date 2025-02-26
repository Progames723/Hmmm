package dev.progames723.hmmm.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin extends ReentrantBlockableEventLoop<TickTask>  {
	@Unique
	private static MinecraftServer hmmm$serverInstance = null;
	
	public MinecraftServerMixin(String name) {
		super(name);
	}
	
	@Inject(method = "spin", at = @At("RETURN"))
	private static <S extends MinecraftServer> void spinInject(Function<Thread, S> threadFunction, CallbackInfoReturnable<S> cir) {
		hmmm$serverInstance = cir.getReturnValue();
	}
}
