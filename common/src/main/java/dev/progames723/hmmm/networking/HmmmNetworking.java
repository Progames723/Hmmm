package dev.progames723.hmmm.networking;

import com.google.common.collect.ImmutableSet;
import dev.progames723.hmmm.HmmmException;
import dev.progames723.hmmm.internal.CallerSensitive;
import dev.progames723.hmmm.utils.MiscUtil;
import dev.progames723.hmmm.utils.XplatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public final class HmmmNetworking {
	private HmmmNetworking() {MiscUtil.instantiationOfUtilClass();}
	
	private static final Set<MessageHandler> S2CHandlers;
	private static final Set<MessageHandler> C2SHandlers;
	private static final Set<MessageHandler> handlers;
	
	static {
		if (XplatUtils.getInstance().getSide().isServer()) {
			S2CHandlers = new HashSet<>();
			C2SHandlers = new ImmutableSet.Builder<MessageHandler>().build();
		} else {
			S2CHandlers = new ImmutableSet.Builder<MessageHandler>().build();
			C2SHandlers = new HashSet<>();
		}
		handlers = new HashSet<>();
	}
	
	public static Packet<?> constructPacket(Message message) {
		if (XplatUtils.getInstance().getSide().isServer())
			return new ServerboundCustomPayloadPacket(new MessagePacketPayload(message));
		return new ClientboundCustomPayloadPacket(new MessagePacketPayload(message));
	}
	
	public static void send(Message message) {
		if (XplatUtils.getInstance().getSide().isServer()) S2CSend(message);
		else C2SSend(message);
	}
	
	public static void send(Packet<?> packet) {
		if (XplatUtils.getInstance().getSide().isServer()) S2CSend(packet);
		else C2SSend(packet);
	}
	
	public static void C2SSend(Message message) {
		C2SSend(constructPacket(message));
	}
	
	public static void C2SSend(Packet<?> packet) {
		ClientPacketListener connection = Minecraft.getInstance().getConnection();
		if (connection != null) {
			connection.send(packet);
		} else {
			throw new HmmmException(new IllegalStateException("You must be in-game to send packets"));
		}
	}
	
	public static void S2CSend(Message message) {
		C2SSend(constructPacket(message));
	}
	
	public static void S2CSend(Packet<?> packet) {
		ClientPacketListener connection = Minecraft.getInstance().getConnection();
		if (connection != null) {
			connection.send(packet);
		} else {
			throw new HmmmException(new IllegalStateException("You must be in-game to send packets"));
		}
	}
	
	public static void registerS2CMessageHandler(MessageHandler handler) {
		if (!XplatUtils.getInstance().getSide().isServer()) throw new HmmmException("Why are you adding S2C handlers on a client");
		S2CHandlers.add(handler);
	}
	
	public static void registerC2SMessageHandler(MessageHandler handler) {
		if (!XplatUtils.getInstance().getSide().isClient()) throw new HmmmException("Why are you adding C2S handlers on a server");
		C2SHandlers.add(handler);
	}
	
	public static void registerMessageHandler(MessageHandler handler, boolean sided) {
		if (!sided) {
			handlers.add(handler);
			return;
		}
		if (XplatUtils.getInstance().getSide().isServer())
			registerS2CMessageHandler(handler);
		else registerC2SMessageHandler(handler);
	}
	
	@ApiStatus.Internal
	@CallerSensitive
	public static void iterateThroughHandlers(MessagePacketPayload payload) {
		CallerSensitive.Utils.throwExceptionIfNotAllowed();
		handlers.forEach(handler -> handler.handle(payload));
		if (payload.message().isS2C())
			S2CHandlers.forEach(handler -> handler.S2CHandle(payload));
		else
			C2SHandlers.forEach(handler -> handler.C2SHandle(payload));
	}
	
	@FunctionalInterface
	public interface MessageHandler {
		//yes it only handles my packets but why do i need other packets?
		void handle(MessagePacketPayload payload);
		
		default void S2CHandle(MessagePacketPayload payload) {
			if (!payload.message.isS2C()) return;
			handle(payload);
		}
		
		default void C2SHandle(MessagePacketPayload payload) {
			if (payload.message.isS2C()) return;
			handle(payload);
		}
	}
	
	public record MessagePacketPayload(Message message) implements CustomPacketPayload {
		@Override
		public void write(FriendlyByteBuf buffer) {
			message.writeToBuf(buffer);
		}
		
		@Override
		@NotNull
		public ResourceLocation id() {
			return message.id;
		}
		
		@Nullable
		public Player reciever() {
			if (message instanceof S2CMessage m) return m.receiver;
			return null;
		}
	}
	
	public static abstract sealed class Message permits S2CMessage, C2SMessage {
		private final ResourceLocation id;
		private final Consumer<FriendlyByteBuf> bufConsumer;
		
		public Message(ResourceLocation id, Consumer<FriendlyByteBuf> bufConsumer) {
			if (id == null || bufConsumer == null) throw new HmmmException(new IllegalArgumentException());
			this.id = id;
			this.bufConsumer = bufConsumer;
		}
		
		public final void writeToBuf(FriendlyByteBuf buf) {
			if (buf == null) throw new HmmmException(new NullPointerException());
			bufConsumer.accept(buf);
		}
		
		public final ResourceLocation getID() {
			return id;
		}
		
		public abstract boolean isS2C();
	}
	
	public static final class S2CMessage extends Message {
		private final Player receiver;
		
		public S2CMessage(Player receiver, ResourceLocation id, Consumer<FriendlyByteBuf> bufConsumer) {
			super(id, bufConsumer);
			if (receiver == null) throw new HmmmException(new IllegalArgumentException());
			this.receiver = receiver;
		}
		
		public Player getReceiver() {
			return receiver;
		}
		
		public boolean isS2C() {
			return true;
		}
	}
	
	public static final class C2SMessage extends Message {
		public C2SMessage(ResourceLocation id, Consumer<FriendlyByteBuf> bufConsumer) {
			super(id, bufConsumer);
		}
		
		public boolean isS2C() {
			return false;
		}
	}
}
