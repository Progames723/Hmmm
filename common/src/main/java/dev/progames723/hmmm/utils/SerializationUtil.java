package dev.progames723.hmmm.utils;

import com.esotericsoftware.kryo.kryo5.Kryo;
import com.esotericsoftware.kryo.kryo5.io.Input;
import com.esotericsoftware.kryo.kryo5.io.Output;
import com.esotericsoftware.kryo.kryo5.objenesis.strategy.StdInstantiatorStrategy;
import com.esotericsoftware.kryo.kryo5.util.DefaultInstantiatorStrategy;
import com.esotericsoftware.kryo.kryo5.util.MapReferenceResolver;

/**
 * do not use unless you have no other choice
 */
public final class SerializationUtil {
	private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
		Kryo kryo = new Kryo(new MapReferenceResolver());
		kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
		kryo.setRegistrationRequired(false);
		kryo.setWarnUnregisteredClasses(false);
		return kryo;
	});
	
	private SerializationUtil() {MiscUtil.instantiationOfUtilClass();}
	
	/**
	 * serializes the object
	 * @param o object to serialize
	 * @return a serialized object(aka byte array)
	 */
	public static byte[] serializeObject(Object o) {
		try (Output output = new Output(0, -1)) {
			kryoThreadLocal.get().writeObject(output, o);
			return output.getBuffer();
		}
	}
	
	/**
	 * <b>YOU MUST PROVIDE THE EXACT SAME CLASS THAT WAS USED FOR THE SERIALIZED OBJECT</b>
	 * @param bytes serialized object from {@link SerializationUtil#serializeObject}
	 * @param type type to cast to
	 * @return deserialized object
	 * @throws ClassCastException if type cannot be cast to the provided class
	 * @param <T> type
	 */
	public static <T> T deserializeObject(byte[] bytes, Class<T> type) throws ClassCastException {
		try (Input input = new Input(bytes)) {
			return kryoThreadLocal.get().readObject(input, type);
		}
	}
}
