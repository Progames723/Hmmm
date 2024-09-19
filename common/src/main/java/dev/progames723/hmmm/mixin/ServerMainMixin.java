package dev.progames723.hmmm.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(Main.class)
@Environment(EnvType.SERVER)
public abstract class ServerMainMixin {
	@Unique
	private static boolean test_hmmm;
	
	@Unique
	private static boolean enableUnsafeReflect;
	
	@Inject(method = "main", at = @At("HEAD"), remap = false)
	private static void mainInject(String[] strings, CallbackInfo ci) {
		test_hmmm = Arrays.asList(strings).contains("test_hmmm");
		enableUnsafeReflect = Arrays.asList(strings).contains("enable_unsafe_reflection_hmmm");
	}
}
