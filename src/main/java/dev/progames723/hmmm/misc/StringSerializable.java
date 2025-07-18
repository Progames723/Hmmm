package dev.progames723.hmmm.misc;

public interface StringSerializable {
	/**
	 * yes
	 * @return a serialized string
	 */
	String toSerializedString();
	
	/**
	 * does nothing by default, modifies the current class
	 * @param string serialized string
	 * @implSpec you must override this if your class is mutable
	 */
	default void fromSerializedString(String string) {}
}
