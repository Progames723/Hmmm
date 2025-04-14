package dev.progames723.hmmm.utils;

import dev.progames723.hmmm.internal.CallerSensitive;

import java.security.SecureRandom;
import java.util.Random;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class MathUtil {
	private static final Supplier<SecureRandom> secureRandom = SecureRandom::new;
	
	@CallerSensitive
	private MathUtil() {MiscUtil.instantiationOfUtilClass();}
	
	public static long percent(long number, long max) {
		return (long) (number / (double) max) * 100;
	}
	
	public static double percent(double number, double max) {
		return number / max;
	}
	
	public static boolean chance(double percent) {
		double i = new Random().nextDouble();
		if (percent > 1) percent = 1;
		if (percent <= 0) return false;
		return secureRandom.get().nextBoolean() ? i <= percent : i >= percent;
	}
	
	public static boolean chance(long percent) {
		int i = new Random().nextInt(101);
		return secureRandom.get().nextBoolean() ? i <= percent : i >= percent;
	}
	
	public static long randomRange(long min, long max) {
		return secureRandom.get().nextLong((max - min) + 1) + min;
	}
	
	public static double randomRange(double min, double max) {
		return secureRandom.get().nextDouble((max - min) + 1) + min;
	}
	
	public static double saturationFunction(double value, double divisor, double maxValue) {//now i know the real name
		if (value <= 0.0d) return 0.0d;
		double output = (1 - (1 / (1 + value / divisor))) * maxValue;
		return roundTo(output, 3);//rounding to the 0.001!
	}
	
	public static double invertedSaturationFunction(double value, double divisor, double maxValue) {
		if (value <= 0.0d) return maxValue;
		double output = -((1 - (1 / (1 + value / divisor))) * maxValue) + maxValue;
		return roundTo(output, 3);
	}
	
	public static double roundTo(double value, long digits) {//no way this actually works
		return Math.round(value * Math.pow(10.0, digits)) / Math.pow(10.0, digits);
	}
	
	public static double clamp(double value, double min, double max) {
		if (Double.isNaN(value)) value = 0;
		if (value == Double.POSITIVE_INFINITY) return max;
		if (value == Double.NEGATIVE_INFINITY) return min;
		return Math.max(Math.min(value, max), min);
	}
	
	public static long clamp(long value, long min, long max) {
		return Math.max(Math.min(value, max), min);
	}
	
	public static double javaFastInvSqrt(double x) {
		double x2 = x * 0.5;
		
		long i = Double.doubleToRawLongBits(x);
		i = 0x5fe6eb50c7b537a9L - (i >> 1);
		x = Double.longBitsToDouble(i);
		
		x = x * (1.5 - (x2 * x * x));
//		x = x * (1.5 - (x2 * x * x));
		
		return x;
	}
	
	public static float javaFastInvSqrt(float x) {
		float x2 = x * 0.5F;
		
		int i = Float.floatToRawIntBits(x);
		i = 0x5f3759df - (i >> 1);
		x = Float.intBitsToFloat(i);
		
		x = x * (1.5f - (x2 * x * x));
//		x = x * (1.5f - (x2 * x * x));
		
		return x;
	}
}
