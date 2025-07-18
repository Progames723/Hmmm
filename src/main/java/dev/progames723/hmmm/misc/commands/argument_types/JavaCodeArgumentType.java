package dev.progames723.hmmm.misc.commands.argument_types;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.progames723.hmmm.HmmmLibrary;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.mozilla.javascript.*;

import java.util.Optional;

public class JavaCodeArgumentType implements ArgumentType<Optional<Object>> {
	public JavaCodeArgumentType() {}
	
	@Override
	public Optional<Object> parse(StringReader stringReader) throws CommandSyntaxException {
		if (!HmmmLibrary.getConfig().canEvalJavaCode()) throw new CommandSyntaxException(new SimpleCommandExceptionType(Component.literal("Cannot run unsafe code! (set \"eval_code\" to true in config to override)")),
			Component.literal("Cannot run unsafe code! (set \"eval_code\" to true in config to override)"));
		String s = stringReader.getRemaining();
		stringReader.setCursor(stringReader.getTotalLength());
		if (!s.endsWith(";"))
			return Optional.of("Please complete the code with a ;(semicolon) to evaluate it");
		Object result;
		try (Context context = Context.enter()) {
			ScriptableObject scope = context.initStandardObjects(null, false);
			result = context.evaluateString(scope, "hmmmPackage = Packages.dev.progames723.hmmm; minecraftPackage = Packages.net.minecraft; " + s, "evalConsole", 1, null);
		} catch (Throwable t) {
			throw new CommandSyntaxException(new SimpleCommandExceptionType(Component.literal("Caught exception while evaluating java code! Exception: " + t.getMessage())),
				Component.literal("Caught exception while evaluating java code! Exception: " + t.getMessage()));
		}
		if (result instanceof NativeJavaObject n) result = n.unwrap();
		if (result instanceof Undefined) result = null;
		HmmmLibrary.LOGGER.info("{} was evaluated to: {}", s, result);
		return Optional.ofNullable(result);
	}
	
	public static class Info implements ArgumentTypeInfo<JavaCodeArgumentType, Info.Template> {
		public Info() {}
		
		@Override
		public void serializeToNetwork(Template template, FriendlyByteBuf buffer) {}
		
		@NotNull
		@Override
		public Template deserializeFromNetwork(FriendlyByteBuf buffer) {return new Template();}
		
		@NotNull
		@Override
		public Template unpack(JavaCodeArgumentType argument) {return new Template();}
		
		@Override
		public void serializeToJson(Template template, JsonObject json) {}
		
		public static class Template implements ArgumentTypeInfo.Template<JavaCodeArgumentType> {
			public Template() {}
			
			@Override
			@NotNull
			public JavaCodeArgumentType instantiate(CommandBuildContext context) {return new JavaCodeArgumentType();}
			
			@Override
			@NotNull
			public Info type() {return new Info();}
		}
	}
	
	
}
