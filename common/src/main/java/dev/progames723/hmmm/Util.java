package dev.progames723.hmmm;

import java.util.Random;

public class Util {
	private Util() {}
	
	public static int percent(int number, int max) {
		return (number / max) * 100;
	}
	
	public static double percent(double number, double max) {
		return (number / max) * 100;
	}
	
	public static float percent(float number, float max) {
		return (number / max) * 100;
	}
	
	public static boolean chance(double percent) {
		Random rand = new Random();
		double i = rand.nextInt(101);
		return i <= percent;
	}
	
	public static int randomRange(int min, int max) {
		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}
	
	public static double randomRange(double min, double max) {
		Random rand = new Random();
		return rand.nextDouble((max - min) + 1) + min;
	}
}
