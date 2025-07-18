package dev.progames723.hmmm.misc.commands.argument_types;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.progames723.hmmm.utils.InternalUtils;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class JavaClassArgumentType implements ArgumentType<Class<?>> {
	private final Class<?> defaultClass;
	private final Class<?> cls;
	
	@SafeVarargs
	public <T> JavaClassArgumentType(Class<T> defaultClass, T... typeGetter) {
		this.defaultClass = defaultClass;
		cls = defaultClass != null ? defaultClass : typeGetter.getClass().componentType();
	}
	
	@Override
	public Class<?> parse(StringReader stringReader) throws CommandSyntaxException {
		String s = stringReader.getRemaining();
		stringReader.setCursor(stringReader.getTotalLength());
		Class<?> parsedClass = null;
		try {
			parsedClass = Class.forName(s);
		} catch (Exception ignored) {}
		if (parsedClass == null) {
			throw new CommandSyntaxException(new SimpleCommandExceptionType(Component.literal("No classes found!")), Component.literal("No classes found!"));
		}
		return parsedClass;
	}
	
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		String trueArg = context.getInput().substring(context.getInput().lastIndexOf(' ') + 1);
		List<Class<?>> classes = InternalUtils.getClassesForString(trueArg, cls,
			cls.isInterface() ? InternalUtils.ScanType.INTERFACE_IMPL : InternalUtils.ScanType.SUB_CLASSES, true);
		if (classes.isEmpty()) builder.suggest("NOT FOUND");
		for (Class<?> cls : classes) {
			builder.suggest(cls.getName());
		}
		return builder.buildFuture();
	}
	
	public static class Info implements ArgumentTypeInfo<JavaClassArgumentType, Info.Template> {
		public Info() {}
		
		@Override
		public void serializeToNetwork(Template template, FriendlyByteBuf buffer) {
			buffer.writeVarInt(template.defaultClass.getName().length());
			buffer.writeCharSequence(template.defaultClass.getName(), StandardCharsets.UTF_8);
		}
		
		@NotNull
		@Override
		public Template deserializeFromNetwork(FriendlyByteBuf buffer) {
			Class<?> cls = null;
			try {
				cls = Class.forName(buffer.readCharSequence(buffer.readVarInt(), StandardCharsets.UTF_8).toString());
			} catch (ClassNotFoundException ignored) {}
			return new Template(cls);
		}
		
		@NotNull
		@Override
		public Template unpack(JavaClassArgumentType argument) {
			return new Template(argument.defaultClass);
		}
		
		@Override
		public void serializeToJson(Template template, JsonObject json) {
			json.add("cls", new Gson().toJsonTree(template.defaultClass.getName()));
		}
		
		public static class Template implements ArgumentTypeInfo.Template<JavaClassArgumentType> {
			private final Class<?> defaultClass;
			
			public Template(Class<?> defaultClass) {
				this.defaultClass = defaultClass;
			}
			
			@NotNull
			@Override
			public JavaClassArgumentType instantiate(CommandBuildContext context) {
				return new JavaClassArgumentType(defaultClass);
			}
			
			@NotNull
			@Override
			public JavaClassArgumentType.Info type() {
				return new Info();
			}
		}
	}
}