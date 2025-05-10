package dev.progames723.hmmm.networking;

import dev.progames723.hmmm.HmmmException;
import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.internal.CallerSensitive;
import dev.progames723.hmmm.utils.MiscUtil;
import dev.progames723.hmmm.utils.XplatUtils;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
//? if > 1.20.1 {
/*import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
*///?} else {
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
//?}
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public final class HmmmNetworking {
	private HmmmNetworking() {MiscUtil.instantiationOfUtilClass();}
	
	private static final Set<MessageHandler> S2CHandlers = new HashSet<>();
	private static final Set<MessageHandler> C2SHandlers = new HashSet<>();
	private static final Set<MessageHandler> handlers = new HashSet<>();
	
	public static Packet<?> constructPacket(Message message) {
		//? if > 1.20.1 {
		/*if (!message.isS2C())
			return new ServerboundCustomPayloadPacket(new MessagePacketPayload(message));
		return new ClientboundCustomPayloadPacket(new MessagePacketPayload(message));
		*///?} else {
		return new MessagePacket(message);
		//?}
	}
	
	public static void send(Message message) {
		try {
			if (XplatUtils.getSide().isServer()) S2CSend((S2CMessage) message);
			else C2SSend((C2SMessage) message);
		} catch (ClassCastException e) {
			HmmmLibrary.LOGGER.warn("Discarding invalid message: {}, on side {}", message, XplatUtils.getSide());
		}
	}
	
	public static void C2SSend(C2SMessage message) {
		C2SSend(constructPacket(message));
	}
	
	public static void C2SSend(Packet<?> packet) {
		ClientPacketListener connection = Minecraft.getInstance().getConnection();
		if (connection != null) {
			connection.send(packet);
		} else {
			HmmmLibrary.LOGGER.warn("Discarding packet {} because connection is null!", packet);
		}
	}
	
	public static void S2CSend(S2CMessage message) {
		S2CSend(message.receiver, constructPacket(message));
	}
	
	public static void S2CSend(ServerPlayer player, Packet<?> packet) {
		player.connection.send(packet);
	}
	
	public static void registerS2CMessageHandler(MessageHandler handler) {
		S2CHandlers.add(handler);
	}
	
	public static void registerC2SMessageHandler(MessageHandler handler) {
		C2SHandlers.add(handler);
	}
	
	public static void registerMessageHandler(MessageHandler handler, boolean sided) {
		if (!sided) {
			handlers.add(handler);
			return;
		}
		if (XplatUtils.getSide().isServer())
			registerS2CMessageHandler(handler);
		else registerC2SMessageHandler(handler);
	}
	
	//? if > 1.20.1 {
	/*/^*
	 * DO NOT USE
	 * @param payload payload
	 ^/
	@ApiStatus.Internal
	@CallerSensitive(allowedClasses = {ClientPacketListener.class, ServerCommonPacketListenerImpl.class})
	public static void iterateThroughHandlers(MessagePacketPayload payload) {
		CallerSensitive.Utils.throwExceptionIfNotAllowed();
		handlers.forEach(handler -> handler.handle(payload));
		if (payload.message.isS2C())
			S2CHandlers.forEach(handler -> handler.S2CHandle(payload));
		else
			C2SHandlers.forEach(handler -> handler.C2SHandle(payload));
	}
	*///?} else {
	private static void iterateThroughHandlers(MessagePacket payload) {
		handlers.forEach(handler -> handler.handle(payload));
		if (payload.message.isS2C())
			S2CHandlers.forEach(handler -> handler.S2CHandle(payload));
		else
			C2SHandlers.forEach(handler -> handler.C2SHandle(payload));
	}
	//?}
	
	//? if > 1.20.1 {
	/*@FunctionalInterface
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
	*///?} else {
	@FunctionalInterface
	public interface MessageHandler {
		//yes it only handles my packets but why do i need other packets?
		void handle(MessagePacket payload);
		
		default void S2CHandle(MessagePacket payload) {
			if (!payload.message.isS2C()) return;
			handle(payload);
		}
		
		default void C2SHandle(MessagePacket payload) {
			if (payload.message.isS2C()) return;
			handle(payload);
		}
	}
	//?}
	
	//? if > 1.20.1 {
	/*public record MessagePacketPayload(Message message) implements CustomPacketPayload {
		//? if <= 1.20.4 {
		@Override
		public void write(FriendlyByteBuf buffer) {
			message.writeToBuf(buffer);
		}
		
		@Override
		@NotNull
		public ResourceLocation id() {
			return message.id;
		}
		//?}
		
		@Nullable
		public ServerPlayer receiver() {
			if (message instanceof S2CMessage m) return m.receiver;
			return null;
		}
		
		@Nullable
		public UUID sender() {
			if (message instanceof C2SMessage m) return m.sender;
			return null;
		}
		
		//? if > 1.20.4 {
		/^@Override
		@NotNull
		public Type<? extends CustomPacketPayload> type() {
			return new Type<>(message.id);
		}
		^///?}
	}
	*///?} else {
	public record MessagePacket(Message message) implements Packet<MessagePacket.MessagePacketListener> {
		@Override
		public void write(FriendlyByteBuf buffer) {
			message.writeToBuf(buffer);
		}
		
		@Override
		public void handle(MessagePacketListener handler) {
			iterateThroughHandlers(this);
		}
		
		public static final class MessagePacketListener implements PacketListener {
			@Override
			public void onDisconnect(Component reason) {
				if (XplatUtils.getSide().isClient()) {
					ClientPacketListener connection = Minecraft.getInstance().getConnection();
					if (connection == null) return;
					connection.onDisconnect(reason);
				}
			}
			
			@Override
			public boolean isAcceptingMessages() {
				if (XplatUtils.getSide().isServer()) return true;
				return Minecraft.getInstance().getConnection() != null;
			}
		}
	}
	//?}
	
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
		
		@Override
		public abstract String toString();
	}
	
	public static final class S2CMessage extends Message {
		private final ServerPlayer receiver;
		
		public S2CMessage(ServerPlayer receiver, ResourceLocation id, Consumer<FriendlyByteBuf> bufConsumer) {
			super(id, bufConsumer);
			if (receiver == null) throw new HmmmException(new IllegalArgumentException());
			this.receiver = receiver;
		}
		
		public ServerPlayer getReceiver() {
			return receiver;
		}
		
		public boolean isS2C() {
			return true;
		}
		
		@Override
		public String toString() {
			return "S2CMessage{" +
				"receiver=" + receiver +
				", id=" + getID() +
				'}';
		}
	}
	
	public static final class C2SMessage extends Message {
		private final UUID sender;
		
		public C2SMessage(UUID sender, ResourceLocation id, Consumer<FriendlyByteBuf> bufConsumer) {
			super(id, bufConsumer);
			if (sender == null) throw new HmmmException(new IllegalArgumentException());
			this.sender = sender;
		}
		
		public UUID getSender() {
			return sender;
		}
		
		@Override
		public String toString() {
			return "S2CMessage{" +
				"sender=" + sender +
				", id=" + getID() +
				'}';
		}
		
		public boolean isS2C() {
			return false;
		}
	}
}
