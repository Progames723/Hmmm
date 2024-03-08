package dev.progames723.hmmm;

import java.util.Random;

public class Util {
	private Util() {}
	
	public static int Percent(int current, int max) {
		return (current / max) * 100;
	}
	public static double Percent(double current, double max) {
		return (current / max) * 100;
	}
	
	public static float Percent(float current, float max) {
		return (current / max) * 100;
	}
	
	public static boolean Chance(double percent) {
		Random rand = new Random();
		double i = rand.nextInt(101);
		return i <= percent;
	}
	
	public static int RandomRange(int min, int max) {
		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}
	
	public static double RandomRange(double min, double max) {
		Random rand = new Random();
		return rand.nextDouble((max - min) + 1) + min;
	}
}
