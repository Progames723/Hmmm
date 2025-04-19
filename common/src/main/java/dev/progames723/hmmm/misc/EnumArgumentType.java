package dev.progames723.hmmm.misc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unchecked")
public class EnumArgumentType<E extends Enum<E>> implements ArgumentType<E> {
	private final E e;
	
	public EnumArgumentType(E e) {
		this.e = e;
	}
	
	@Override
	public E parse(StringReader stringReader) throws CommandSyntaxException {
		String s = stringReader.readUnquotedString();
		E e1 = null;
		for (Field field : e.getDeclaringClass().getDeclaredFields()) {
			try {
				field.setAccessible(true);
				if (!field.getName().equals(s)) continue;
				e1 = (E) field.get(null);
			} catch (Exception ignored) {
			} finally {
				field.setAccessible(false);
			}
		}
		if (e1 == null)
			throw new CommandSyntaxException(new SimpleCommandExceptionType(Component.literal("Invalid enum argument!")), Component.literal("Invalid enum argument!"));;
		return e1;
	}
	
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		EnumSet<E> e1 = EnumSet.allOf(e.getDeclaringClass());
		String trueArg = context.getInput().substring(context.getInput().lastIndexOf(' ') + 1);
		if (!trueArg.isEmpty()) e1.removeIf(e2 -> !e2.name().contains(trueArg));
		if (e1.isEmpty()) {
			builder.suggest("NOT FOUND");
		} else {
			for (E e : e1) builder.suggest(e.name());
		}
		return builder.buildFuture();
	}
	
	public static class Info implements ArgumentTypeInfo<EnumArgumentType<?>, Info.Template> {
		public Info() {}
		
		@Override
		public void serializeToNetwork(Template template, FriendlyByteBuf buffer) {
			buffer.writeEnum(template.e);
		}
		
		@NotNull
		@Override
		public Template deserializeFromNetwork(FriendlyByteBuf buffer) {
			return new Template(buffer.readEnum(Enum.class));
		}
		
		@NotNull
		@Override
		public Template unpack(EnumArgumentType<?> argument) {
			return new Template(argument.e);
		}
		
		@Override
		public void serializeToJson(Template template, JsonObject json) {
			json.add("enum", new Gson().toJsonTree(template.e.getDeclaringClass().getName()));
		}
		
		public static class Template implements ArgumentTypeInfo.Template<EnumArgumentType<?>> {
			private final Enum<?> e;
			
			public Template(Enum<?> e) {
				this.e = e;
			}
			
			@NotNull
			@Override
			public EnumArgumentType<?> instantiate(CommandBuildContext context) {
				return instantiateGenerics(context);
			}
			
			public <E extends Enum<E>> EnumArgumentType<?> instantiateGenerics(CommandBuildContext context) {
				return new EnumArgumentType<>((E) e);
			}
			
			@NotNull
			@Override
			public EnumArgumentType.Info type() {
				return new Info();
			}
		}
	}
}
