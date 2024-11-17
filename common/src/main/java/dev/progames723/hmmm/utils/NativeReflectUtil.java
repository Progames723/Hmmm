package dev.progames723.hmmm.utils;

import dev.architectury.platform.Platform;
import dev.progames723.hmmm.HmmmException;
import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.internal.CallerSensitive;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class NativeReflectUtil {
	private static native void registerNatives();
	
	static {
		try {
			registerNatives();
		} catch (UnsatisfiedLinkError e) {
			NativeUtil.works = false;
			HmmmLibrary.LOGGER.error(HmmmLibrary.NATIVE, "NativeReflectUtil probably has a faulty library!", e);
		}
	}
	
	private static native void setFieldValue0(String fieldName, String signature, Class<?> clazz, Object object, Object value);
	
	private static native Object getFieldValue0(String fieldName, String signature, Class<?> clazz, Object object);
	
	private static native void setAccessible0(AccessibleObject o, boolean flag);
	
	private static native Object invokeMethod0(String methodName, String signature, Class<?> clazz, Object object, Object... args);
	
	private static native <T> T invokeConstructor0(String signature, Class<?> clazz, Object... args);
	
	/**
	 * sets the field value forcefully, including final fields. <p>
	 * "i have become death, mutator of final fields" - some developer
	 * @param fieldName field name duh
	 * @param signature field signature
	 * @param clazz the {@link Class} of the field
	 * @param object object instance with the field
	 * @param value value to set to
	 */
	public static void setFieldValue(String fieldName, String signature, Class<?> clazz, Object object, Object value) {
		if (!NativeUtil.works) {
			warnOnFaultyLibs();
			Field field;
			try {
				field = object.getClass().getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), e);
			}
			try {
				field.set(object, value);
			} catch (IllegalAccessException e) {
				throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unable to set a field's value! Field: " + field, e);
			}
		}
		if (!HmmmLibrary.UNSAFE_REFLECT && !Platform.isDevelopmentEnvironment()) throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unsafe reflection not enabled!");
		setFieldValue0(fieldName, signature, clazz, object, value);
	}
	
	public static Object getFieldValue(String fieldName, String signature, Class<?> clazz, Object object) {
		if (!NativeUtil.works) {
			warnOnFaultyLibs();
			Field field;
			try {
				field = object.getClass().getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), e);
			}
			try {
				return field.get(object);
			} catch (IllegalAccessException e) {
				throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unable to get a field's value! Field: " + field, e);
			}
		}
		if (!HmmmLibrary.UNSAFE_REFLECT && !Platform.isDevelopmentEnvironment()) throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unsafe reflection not enabled!");
		return getFieldValue0(fieldName, signature, clazz, object);
	}
	
	public static void setAccessible(AccessibleObject o, boolean flag) {
		if (!NativeUtil.works) {
			warnOnFaultyLibs();
			try {
				o.setAccessible(flag);
			} catch (Exception e) {
				throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unable to make " + o + " accessible!", e);
			}
		}
		if (!HmmmLibrary.UNSAFE_REFLECT && !Platform.isDevelopmentEnvironment()) throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unsafe reflection not enabled!");
		setAccessible0(o, flag);
	}
	
	public static Object invokeMethod(String methodName, String signature, Class<?> clazz, Object object, Object... args) {
		if (!NativeUtil.works) {
			warnOnFaultyLibs();
			Method method;
			try {
				method = object.getClass().getDeclaredMethod(methodName);
			} catch (NoSuchMethodException e) {
				throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), e);
			}
			try {
				return method.invoke(object, args);
			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unable to invoke method: " + method + "!", e);
			}
		}
		if (!HmmmLibrary.UNSAFE_REFLECT && !Platform.isDevelopmentEnvironment()) throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unsafe reflection not enabled!");
		return invokeMethod0(methodName, signature, clazz, object, args);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T invokeConstructor(String signature, Class<?> clazz, Object... args) {
		if (!NativeUtil.works) {
			warnOnFaultyLibs();
			List<Class<?>> parameterTypes = new ArrayList<>();
			for (Object o : args) {
				parameterTypes.add(o.getClass().componentType());
			}
			Constructor<T> constructor;
			try {
				constructor = (Constructor<T>) clazz.getDeclaredConstructor(parameterTypes.toArray(new Class[0]));
			} catch (NoSuchMethodException e) {
				throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), e);
			}
			try {
				return constructor.newInstance(args);
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
				throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unable to invoke constructor: " + constructor + "!", e);
			}
		}
		if (!HmmmLibrary.UNSAFE_REFLECT && !Platform.isDevelopmentEnvironment()) throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unsafe reflection not enabled!");
		return invokeConstructor0(signature, clazz, args);
	}
	
	private static void warnOnFaultyLibs() {
		HmmmLibrary.LOGGER.error(HmmmLibrary.NATIVE, "Native libraries dont work properly, delegating to java impl!");
	}
	
	public static class Primitives {
		private static native void registerNatives();
		
		static {
			try {
				registerNatives();
			} catch (UnsatisfiedLinkError e) {
				NativeUtil.works = false;
				HmmmLibrary.LOGGER.error(HmmmLibrary.NATIVE, "NativeReflectUtil$Primitives probably has a faulty library!", e);
			}
		}
		
		private static native int getIntField0(String fieldName, Class<?> clazz, Object object);
		private static native void setIntField0(String fieldName, Class<?> clazz, Object object, int value);
		
		private static native long getLongField0(String fieldName, Class<?> clazz, Object object);
		private static native void setLongField0(String fieldName, Class<?> clazz, Object object, long value);
		
		private static native float getFloatField0(String fieldName, Class<?> clazz, Object object);
		private static native void setFloatField0(String fieldName, Class<?> clazz, Object object, float value);
		
		private static native double getDoubleField0(String fieldName, Class<?> clazz, Object object);
		private static native void setDoubleField0(String fieldName, Class<?> clazz, Object object, double value);
		
		private static native short getShortField0(String fieldName, Class<?> clazz, Object object);
		private static native void setShortField0(String fieldName, Class<?> clazz, Object object, short value);
		
		private static native byte getByteField0(String fieldName, Class<?> clazz, Object object);
		private static native void setByteField0(String fieldName, Class<?> clazz, Object object, byte value);
		
		private static native char getCharField0(String fieldName, Class<?> clazz, Object object);
		private static native void setCharField0(String fieldName, Class<?> clazz, Object object, char value);
		
		private static native boolean getBooleanField0(String fieldName, Class<?> clazz, Object object);
		private static native void setBooleanField0(String fieldName, Class<?> clazz, Object object, boolean value);
		
		@CallerSensitive
		private static int getIntField(String fieldName, Class<?> clazz, Object object) {
			if (!NativeUtil.works) {
				warnOnFaultyLibs();
				Field field;
				try {
					field = object.getClass().getDeclaredField(fieldName);
				} catch (NoSuchFieldException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), e);
				}
				try {
					return field.getInt(object);
				} catch (IllegalAccessException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unable to get a field's value! Field: " + field, e);
				}
			}
			if (!HmmmLibrary.UNSAFE_REFLECT && !Platform.isDevelopmentEnvironment()) throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unsafe reflection not enabled!");
			return getIntField0(fieldName, clazz, object);
		}
		
		@CallerSensitive
		private static void setIntField(String fieldName, Class<?> clazz, Object object, int value) {
			if (!NativeUtil.works) {
				warnOnFaultyLibs();
				Field field;
				try {
					field = object.getClass().getDeclaredField(fieldName);
				} catch (NoSuchFieldException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), e);
				}
				try {
					field.setInt(object, value);
				} catch (IllegalAccessException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unable to set a field's value! Field: " + field, e);
				}
			}
			if (!HmmmLibrary.UNSAFE_REFLECT && !Platform.isDevelopmentEnvironment()) throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unsafe reflection not enabled!");
			setIntField0(fieldName, clazz, object, value);
		}
		
		@CallerSensitive
		private static long getLongField(String fieldName, Class<?> clazz, Object object) {
			if (!NativeUtil.works) {
				warnOnFaultyLibs();
				Field field;
				try {
					field = object.getClass().getDeclaredField(fieldName);
				} catch (NoSuchFieldException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), e);
				}
				try {
					return field.getLong(object);
				} catch (IllegalAccessException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unable to get a field's value! Field: " + field, e);
				}
			}
			if (!HmmmLibrary.UNSAFE_REFLECT && !Platform.isDevelopmentEnvironment()) throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unsafe reflection not enabled!");
			return getLongField0(fieldName, clazz, object);
		}
		
		@CallerSensitive
		private static void setLongField(String fieldName, Class<?> clazz, Object object, long value) {
			if (!NativeUtil.works) {
				warnOnFaultyLibs();
				Field field;
				try {
					field = object.getClass().getDeclaredField(fieldName);
				} catch (NoSuchFieldException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), e);
				}
				try {
					field.setLong(object, value);
				} catch (IllegalAccessException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unable to set a field's value! Field: " + field, e);
				}
			}
			if (!HmmmLibrary.UNSAFE_REFLECT && !Platform.isDevelopmentEnvironment()) throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unsafe reflection not enabled!");
			setLongField0(fieldName, clazz, object, value);
		}
		
		@CallerSensitive
		private static float getFloatField(String fieldName, Class<?> clazz, Object object) {
			if (!NativeUtil.works) {
				warnOnFaultyLibs();
				Field field;
				try {
					field = object.getClass().getDeclaredField(fieldName);
				} catch (NoSuchFieldException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), e);
				}
				try {
					return field.getFloat(object);
				} catch (IllegalAccessException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unable to get a field's value! Field: " + field, e);
				}
			}
			if (!HmmmLibrary.UNSAFE_REFLECT && !Platform.isDevelopmentEnvironment()) throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unsafe reflection not enabled!");
			return getFloatField0(fieldName, clazz, object);
		}
		
		@CallerSensitive
		private static void setFloatField(String fieldName, Class<?> clazz, Object object, float value) {
			if (!NativeUtil.works) {
				warnOnFaultyLibs();
				Field field;
				try {
					field = object.getClass().getDeclaredField(fieldName);
				} catch (NoSuchFieldException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), e);
				}
				try {
					field.setFloat(object, value);
				} catch (IllegalAccessException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unable to set a field's value! Field: " + field, e);
				}
			}
			if (!HmmmLibrary.UNSAFE_REFLECT && !Platform.isDevelopmentEnvironment()) throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unsafe reflection not enabled!");
			setFloatField0(fieldName, clazz, object, value);
		}
		
		@CallerSensitive
		private static double getDoubleField(String fieldName, Class<?> clazz, Object object) {
			if (!NativeUtil.works) {
				warnOnFaultyLibs();
				Field field;
				try {
					field = object.getClass().getDeclaredField(fieldName);
				} catch (NoSuchFieldException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), e);
				}
				try {
					return field.getDouble(object);
				} catch (IllegalAccessException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unable to get a field's value! Field: " + field, e);
				}
			}
			if (!HmmmLibrary.UNSAFE_REFLECT && !Platform.isDevelopmentEnvironment()) throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unsafe reflection not enabled!");
			return getDoubleField0(fieldName, clazz, object);
		}
		
		@CallerSensitive
		private static void setDoubleField(String fieldName, Class<?> clazz, Object object, double value) {
			if (!NativeUtil.works) {
				warnOnFaultyLibs();
				Field field;
				try {
					field = object.getClass().getDeclaredField(fieldName);
				} catch (NoSuchFieldException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), e);
				}
				try {
					field.setDouble(object, value);
				} catch (IllegalAccessException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unable to set a field's value! Field: " + field, e);
				}
			}
			if (!HmmmLibrary.UNSAFE_REFLECT && !Platform.isDevelopmentEnvironment()) throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unsafe reflection not enabled!");
			setDoubleField0(fieldName, clazz, object, value);
		}
		
		@CallerSensitive
		private static short getShortField(String fieldName, Class<?> clazz, Object object) {
			if (!NativeUtil.works) {
				warnOnFaultyLibs();
				Field field;
				try {
					field = object.getClass().getDeclaredField(fieldName);
				} catch (NoSuchFieldException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), e);
				}
				try {
					return field.getShort(object);
				} catch (IllegalAccessException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unable to get a field's value! Field: " + field, e);
				}
			}
			if (!HmmmLibrary.UNSAFE_REFLECT && !Platform.isDevelopmentEnvironment()) throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unsafe reflection not enabled!");
			return getShortField0(fieldName, clazz, object);
		}
		
		@CallerSensitive
		private static void setShortField(String fieldName, Class<?> clazz, Object object, short value) {
			if (!NativeUtil.works) {
				warnOnFaultyLibs();
				Field field;
				try {
					field = object.getClass().getDeclaredField(fieldName);
				} catch (NoSuchFieldException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), e);
				}
				try {
					field.setShort(object, value);
				} catch (IllegalAccessException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unable to set a field's value! Field: " + field, e);
				}
			}
			if (!HmmmLibrary.UNSAFE_REFLECT && !Platform.isDevelopmentEnvironment()) throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unsafe reflection not enabled!");
			setShortField0(fieldName, clazz, object, value);
		}
		
		@CallerSensitive
		private static byte getByteField(String fieldName, Class<?> clazz, Object object) {
			if (!NativeUtil.works) {
				warnOnFaultyLibs();
				Field field;
				try {
					field = object.getClass().getDeclaredField(fieldName);
				} catch (NoSuchFieldException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), e);
				}
				try {
					return field.getByte(object);
				} catch (IllegalAccessException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unable to get a field's value! Field: " + field, e);
				}
			}
			if (!HmmmLibrary.UNSAFE_REFLECT && !Platform.isDevelopmentEnvironment()) throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unsafe reflection not enabled!");
			return getByteField0(fieldName, clazz, object);
		}
		
		@CallerSensitive
		private static void setByteField(String fieldName, Class<?> clazz, Object object, byte value) {
			if (!NativeUtil.works) {
				warnOnFaultyLibs();
				Field field;
				try {
					field = object.getClass().getDeclaredField(fieldName);
				} catch (NoSuchFieldException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), e);
				}
				try {
					field.setByte(object, value);
				} catch (IllegalAccessException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unable to set a field's value! Field: " + field, e);
				}
			}
			if (!HmmmLibrary.UNSAFE_REFLECT && !Platform.isDevelopmentEnvironment()) throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unsafe reflection not enabled!");
			setByteField0(fieldName, clazz, object, value);
		}
		
		@CallerSensitive
		private static char getCharField(String fieldName, Class<?> clazz, Object object) {
			if (!NativeUtil.works) {
				warnOnFaultyLibs();
				Field field;
				try {
					field = object.getClass().getDeclaredField(fieldName);
				} catch (NoSuchFieldException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), e);
				}
				try {
					return field.getChar(object);
				} catch (IllegalAccessException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unable to get a field's value! Field: " + field, e);
				}
			}
			if (!HmmmLibrary.UNSAFE_REFLECT && !Platform.isDevelopmentEnvironment()) throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unsafe reflection not enabled!");
			return getCharField0(fieldName, clazz, object);
		}
		
		@CallerSensitive
		private static void setCharField(String fieldName, Class<?> clazz, Object object, char value) {
			if (!NativeUtil.works) {
				warnOnFaultyLibs();
				Field field;
				try {
					field = object.getClass().getDeclaredField(fieldName);
				} catch (NoSuchFieldException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), e);
				}
				try {
					field.setChar(object, value);
				} catch (IllegalAccessException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unable to set a field's value! Field: " + field, e);
				}
			}
			if (!HmmmLibrary.UNSAFE_REFLECT && !Platform.isDevelopmentEnvironment()) throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unsafe reflection not enabled!");
			setCharField0(fieldName, clazz,  object, value);
		}
		
		@CallerSensitive
		private static boolean getBooleanField(String fieldName, Class<?> clazz, Object object) {
			if (!NativeUtil.works) {
				warnOnFaultyLibs();
				Field field;
				try {
					field = object.getClass().getDeclaredField(fieldName);
				} catch (NoSuchFieldException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), e);
				}
				try {
					return field.getBoolean(object);
				} catch (IllegalAccessException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unable to get a field's value! Field: " + field, e);
				}
			}
			if (!HmmmLibrary.UNSAFE_REFLECT && !Platform.isDevelopmentEnvironment()) throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unsafe reflection not enabled!");
			return getBooleanField0(fieldName, clazz, object);
		}
		
		@CallerSensitive
		private static void setBooleanField(String fieldName, Class<?> clazz, Object object, boolean value) {
			if (!NativeUtil.works) {
				warnOnFaultyLibs();
				Field field;
				try {
					field = object.getClass().getDeclaredField(fieldName);
				} catch (NoSuchFieldException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), e);
				}
				try {
					field.setBoolean(object, value);
				} catch (IllegalAccessException e) {
					throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unable to set a field's value! Field: " + field, e);
				}
			}
			if (!HmmmLibrary.UNSAFE_REFLECT && !Platform.isDevelopmentEnvironment()) throw new HmmmException(ReflectUtil.STACK_WALKER.getCallerClass(), "Unsafe reflection not enabled!");
			setBooleanField0(fieldName, clazz, object, value);
		}
	}
}
