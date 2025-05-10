package dev.progames723.hmmm.mixin;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.progames723.hmmm.misc.commands.argument_types.EnumArgumentType;
import dev.progames723.hmmm.misc.commands.argument_types.JavaClassArgumentType;
import dev.progames723.hmmm.misc.commands.argument_types.JavaCodeArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArgumentTypeInfos.class)
public abstract class ArgumentTypeInfosMixin {
	@Shadow
	private static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> ArgumentTypeInfo<A, T> register(Registry<ArgumentTypeInfo<?, ?>> registry, String id, Class<? extends A> argumentClass, ArgumentTypeInfo<A, T> info) {
		return null;
	}
	
	@SuppressWarnings({"unchecked", "RedundantCast", "ResultOfMethodCallIgnored"})//not very "redundant" because casting is a requirement
	@Inject(method = "bootstrap", at = @At("HEAD"))
	private static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> void registerInfoInject(Registry<ArgumentTypeInfo<?, ?>> registry, CallbackInfoReturnable<ArgumentTypeInfo<?, ?>> cir) {
		register(
			registry,
			"hmmm:extendable_enum_argument_type",
			(Class<? extends EnumArgumentType<?>>) new EnumArgumentType.Info.Template(null).instantiateGenerics(null).getClass(),
			new EnumArgumentType.Info()
		);
		register(
			registry,
			"hmmm:java_code_argument_type",
			JavaCodeArgumentType.class,
			new JavaCodeArgumentType.Info()
		);
		register(
			registry,
			"hmmm:java_class_argument_type",
			(Class<? extends JavaClassArgumentType<?>>) new JavaClassArgumentType.Info.Template<>(null).instantiate(null).getClass(),
			new JavaClassArgumentType.Info<>()
		);
	}
}
