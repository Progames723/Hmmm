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

@SuppressWarnings("unchecked")
public class JavaClassArgumentType<T> implements ArgumentType<Class<T>> {
	private final Class<T> defaultClass;
	private final Class<T> cls;
	
	@SafeVarargs
	public JavaClassArgumentType(Class<T> defaultClass, T... typeGetter) {
		this.defaultClass = defaultClass;
		cls = (Class<T>) typeGetter.getClass().componentType();
	}
	
	@Override
	public Class<T> parse(StringReader stringReader) throws CommandSyntaxException {
		String s = stringReader.readUnquotedString();
		Class<T> parsedClass = null;
		try {
			parsedClass = (Class<T>) Class.forName(s);
		} catch (Exception ignored) {}
		if (parsedClass == null && defaultClass == null) {
			throw new CommandSyntaxException(new SimpleCommandExceptionType(Component.literal("No classes found!")), Component.literal("No classes found!"));
		}
		return parsedClass != null ? parsedClass : defaultClass;
	}
	
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		String trueArg = context.getInput().substring(context.getInput().lastIndexOf(' ') + 1);
		List<Class<T>> classes = InternalUtils.getClassesForString(trueArg, cls,
			cls.isInterface() ? InternalUtils.ScanType.INTERFACE_IMPL : dev.progames723.hmmm.utils.InternalUtils.ScanType.SUB_CLASSES, true);
		if (classes.isEmpty() && defaultClass == null) builder.suggest("NOT FOUND");
		if (defaultClass != null && !classes.contains(defaultClass)) builder.suggest(defaultClass.getName());
		for (Class<T> cls : classes) {
			builder.suggest(cls.getName());
		}
		return builder.buildFuture();
	}
	
	public static class Info<T> implements ArgumentTypeInfo<JavaClassArgumentType<T>, Info.Template<T>> {
		public Info() {}
		
		@Override
		public void serializeToNetwork(Template template, FriendlyByteBuf buffer) {
			buffer.writeVarInt(template.defaultClass.getName().length());
			buffer.writeCharSequence(template.defaultClass.getName(), StandardCharsets.UTF_8);
		}
		
		@NotNull
		@Override
		public Template<T> deserializeFromNetwork(FriendlyByteBuf buffer) {
			Class<T> cls = null;
			try {
				cls = (Class<T>) Class.forName(buffer.readCharSequence(buffer.readVarInt(), StandardCharsets.UTF_8).toString());
			} catch (ClassNotFoundException ignored) {}
			return new Template<>(cls);
		}
		
		@NotNull
		@Override
		public Template<T> unpack(JavaClassArgumentType<T> argument) {
			return new Template<>(argument.defaultClass);
		}
		
		@Override
		public void serializeToJson(Template template, JsonObject json) {
			json.add("cls", new Gson().toJsonTree(template.defaultClass.getName()));
		}
		
		public static class Template<T> implements ArgumentTypeInfo.Template<JavaClassArgumentType<T>> {
			private final Class<T> defaultClass;
			
			public Template(Class<T> defaultClass) {
				this.defaultClass = defaultClass;
			}
			
			@NotNull
			@Override
			public JavaClassArgumentType<T> instantiate(CommandBuildContext context) {
				return new JavaClassArgumentType<>(defaultClass);
			}
			
			@NotNull
			@Override
			public JavaClassArgumentType.Info<T> type() {
				return new Info<>();
			}
		}
	}
}