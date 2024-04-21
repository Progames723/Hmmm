package dev.progames723.hmmm;

import org.jetbrains.annotations.Range;

import java.util.Random;

public class Util {
	private Util() {}//no instances allowed

	public static long percent(long number, long max) {return (number / max) * 100;}
	
	public static int percent(int number, int max) {return (int) percent((long) number, max);}
	
	public static double percent(double number, double max) {return (number / max) * 100;}
	
	public static float percent(float number, float max) {return (float)percent((double) number, max);}
	
	public static boolean chance(double percent) {
		Random rand = new Random();
		double i = rand.nextDouble();
		if (percent > 100) percent = 100;
		if (percent <= 0) return false;
		percent /= 100; //clamping it
		return i <= percent;
	}

	public static boolean chance(float percent) {return chance((double) percent);}
	
	public static boolean chance(long percent) {
		Random rand = new Random();
		int i = rand.nextInt(101);
		return i <= percent;
	}
	
	public static boolean chance(int percent) {return chance((long) percent);}
	
	public static long randomRange(long min, long max) {
		Random rand = new Random();
		return rand.nextLong((max - min) + 1) + min;
	}
	
	public static int randomRange(int min, int max) {return (int) randomRange((long) min, max);}
	
	public static double randomRange(double min, double max) {
		Random rand = new Random();
		return rand.nextDouble((max - min) + 1) + min;
	}
	
	public static float randomRange(float min, float max) {return (float) randomRange((double) min, max);}
	
	public static double unExponentialFormula(double value, double divisor, double maxValue) {//great name
		if (value < 0) throw new IllegalArgumentException("value must be higher than 0");
		else if (value == 0) return 0.0;
		double output = (1-(1/(1+value/divisor))) * maxValue;
		output = roundTo(output, 3);//rounding to the 0.001!
		return output;
	}
	
	public static double roundTo(double value, @Range(from = 0, to = Long.MAX_VALUE) long powerOf10) {//no way this actually works
		return Math.round(value * Math.pow(10.0, powerOf10)) / Math.pow(10.0, powerOf10);
	}
	
	public static float unExponentialFormula(float value, float divisor, float maxValue) {
		return (float) unExponentialFormula((double) value, divisor, maxValue);
	}
	
	public static float roundTo(float value, @Range(from = 0, to = Long.MAX_VALUE) long powerOf10) {
		return (float) roundTo((double) value, powerOf10);
	}
}
