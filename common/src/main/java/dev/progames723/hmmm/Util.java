package dev.progames723.hmmm;

import java.util.Random;

public class Util {
	private Util() {}//no instances allowed
	
	public static int iPercent(int number, int max) {return (int)lPercent(number, max);}

	public static long lPercent(long number, long max) {return (number / max) * 100;}
	
	public static double dPercent(double number, double max) {return (number / max) * 100;}
	
	public static float fPercent(float number, float max) {return (float)dPercent(number, max);}
	
	public static boolean dChance(double percent) {
		Random rand = new Random();
		double i = rand.nextDouble();
		if (percent > 100) percent = 100;
		if (percent <= 0) return false;
		percent /= 100; //clamping it
		return i <= percent;
	}

	public static boolean fChance(float percent) {return dChance(percent);}
	
	public static boolean lChance(long percent) {
		Random rand = new Random();
		int i = rand.nextInt(101);
		return i <= percent;
	}
	
	public static boolean iChance(int percent) {return lChance(percent);}
	
	public static long lRandomRange(long min, long max) {
		Random rand = new Random();
		return rand.nextLong((max - min) + 1) + min;
	}
	
	public static int iRandomRange(int min, int max) {return (int)lRandomRange(min, max);}
	
	public static double dRandomRange(double min, double max) {
		Random rand = new Random();
		return rand.nextDouble((max - min) + 1) + min;
	}
	
	public static float fRandomRange(float min, float max) {return (float)dRandomRange(min, max);}
}
