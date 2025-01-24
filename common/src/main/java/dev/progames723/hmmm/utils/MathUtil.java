package dev.progames723.hmmm.utils;

import dev.progames723.hmmm.ActualSecureRandom;
import dev.progames723.hmmm.HmmmError;
import dev.progames723.hmmm.internal.CallerSensitive;

import java.security.SecureRandom;
import java.util.Random;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class MathUtil {
	private static final Supplier<SecureRandom> actualSecureRandom = ActualSecureRandom::createSecureRandom;
	
	@CallerSensitive
	private MathUtil() {throw new HmmmError(ReflectUtil.CALLER_CLASS.getCallerClass(), "This mf really made an instance of a util class, what a shame");}//no instances allowed
	
	public static long percent(long number, long max) {return (number / max) * 100;}
	
	public static double percent(double number, double max) {return number / max;}
	
	public static boolean chance(double percent) {
		double i = new Random().nextDouble();
		if (percent > 1) percent = 1;
		if (percent <= 0) return false;
		return actualSecureRandom.get().nextBoolean() ? i <= percent : i >= percent;
	}
	
	public static boolean chance(long percent) {
		int i = new Random().nextInt(101);
		return actualSecureRandom.get().nextBoolean() ? i <= percent : i >= percent;
	}
	
	public static long randomRange(long min, long max) {
		return actualSecureRandom.get().nextLong((max - min) + 1) + min;
	}
	
	public static double randomRange(double min, double max) {
		return actualSecureRandom.get().nextDouble((max - min) + 1) + min;
	}
	
	public static double unExponentialFormula(double value, double divisor, double maxValue) {//great name
		if (value < 0) throw new IllegalArgumentException("value must be higher than 0");
		else if (value == 0) return 0.0;
		double output = (1-(1/(1+value/divisor))) * maxValue;
		return roundTo(output, 3);//rounding to the 0.001!
	}
	
	public static double roundTo(double value, long tenthPower) {//no way this actually works
		return Math.round(value * Math.pow(10.0, tenthPower)) / Math.pow(10.0, tenthPower);
	}
	
	public static double clamp(double value, double min, double max) {
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
