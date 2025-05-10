package dev.progames723.hmmm.misc.commands.argument_types;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import dev.progames723.hmmm.HmmmError;
import dev.progames723.hmmm.HmmmException;
import dev.progames723.hmmm.HmmmLibrary;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.mozilla.javascript.*;

public class JavaCodeArgumentType implements ArgumentType<Object> {
	public JavaCodeArgumentType() {}
	
	@Override
	public Object parse(StringReader stringReader) {
		if (!HmmmLibrary.CONFIG.canEvalJavaCode()) throw new HmmmError("Cannot run unsafe code! (set \"eval_code\" to true in config to override)");
		String s = stringReader.readUnquotedString();
		Object result;
		try (Context context = Context.enter()) {
			result = context.evaluateString(new ImporterTopLevel(), s, "evalConsole", 1, null);
		} catch (Throwable t) {
			throw new HmmmException(null, "Caught exception while evaluating java code!", t);
		}
		if (result instanceof NativeJavaObject n) result = n.unwrap();
		if (result instanceof Undefined) result = null;
		return result;
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
