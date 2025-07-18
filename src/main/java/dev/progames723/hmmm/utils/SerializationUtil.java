package dev.progames723.hmmm.utils;

import com.esotericsoftware.kryo.kryo5.Kryo;
import com.esotericsoftware.kryo.kryo5.io.Input;
import com.esotericsoftware.kryo.kryo5.io.Output;
import com.esotericsoftware.kryo.kryo5.objenesis.strategy.StdInstantiatorStrategy;
import com.esotericsoftware.kryo.kryo5.util.DefaultInstantiatorStrategy;
import com.esotericsoftware.kryo.kryo5.util.MapReferenceResolver;
import dev.progames723.hmmm.HmmmLibrary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.DeflaterInputStream;
import java.util.zip.InflaterInputStream;

/**
 * do not use unless you have no other choice
 */
public final class SerializationUtil {
	private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
		Kryo kryo = new Kryo(new MapReferenceResolver(4096));
		kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
		kryo.setRegistrationRequired(false);
		kryo.setWarnUnregisteredClasses(false);
		kryo.setAutoReset(true);
		return kryo;
	});
	
	private SerializationUtil() {MiscUtil.instantiationOfUtilClass();}
	
	/**
	 * serializes the object
	 * @param o object to serialize
	 * @return a serialized object(aka byte array)
	 */
	public synchronized static byte[] serializeObject(Object o) {
		Kryo kryo = kryoThreadLocal.get();
		try (Output output = new Output(0, -1)) {
			kryo.writeObject(output, o);
			return output.getBuffer();
		} finally {
			kryoThreadLocal.set(kryo);
		}
	}
	
	/**
	 * serializes the object and compresses the result
	 * @param o object to serialize
	 * @return a serialized object(aka byte array)
	 */
	public synchronized static byte[] serializeAndCompressObject(Object o) {
		byte[] data = serializeObject(o);
		try {
			DeflaterInputStream stream = new DeflaterInputStream(new ByteArrayInputStream(data));
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			stream.transferTo(outputStream);
			byte[] compressed = outputStream.toByteArray();
			if (compressed.length > 0)
				return compressed;
		} catch (Exception e) {
			HmmmLibrary.LOGGER.error("Failed to compress and (or) serialize data!", e);
		}
		return data;
	}
	
	/**
	 * <b>YOU MUST PROVIDE THE EXACT SAME CLASS THAT WAS USED FOR THE SERIALIZED OBJECT</b>
	 * @param bytes serialized object from {@link SerializationUtil#serializeObject}
	 * @param type type to cast to
	 * @return deserialized object
	 * @param <T> type
	 */
	public synchronized static <T> T deserializeObject(byte[] bytes, Class<T> type) {
		Kryo kryo = kryoThreadLocal.get();
		try (Input input = new Input(bytes)) {
			return kryo.readObject(input, type);
		} finally {
			kryoThreadLocal.set(kryo);
		}
	}
	
	/**
	 * <b>YOU MUST PROVIDE THE EXACT SAME CLASS THAT WAS USED FOR THE SERIALIZED OBJECT</b>
	 * @param bytes compressed serialized object from {@link SerializationUtil#serializeAndCompressObject}
	 * @param type type to cast to
	 * @return deserialized object
	 * @param <T> type
	 */
	public synchronized static <T> T decompressAndDeserializeObject(byte[] bytes, Class<T> type) {
		try {
			return deserializeObject(bytes, type);
		} catch (Exception ignored) {}//data is compressed
		try {
			InflaterInputStream inputStream = new InflaterInputStream(new ByteArrayInputStream(bytes));
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			inputStream.transferTo(outputStream);
			byte[] decompressed = outputStream.toByteArray();
			return deserializeObject(decompressed, type);
		} catch (Exception e) {
			HmmmLibrary.LOGGER.error("Failed to decompress and (or) deserialize data!", e);
			return null;
		}
	}
}
