package dev.progames723.hmmm;

import java.security.SecureRandom;
import java.util.Random;

public abstract class ActualSecureRandom {
	public static SecureRandom what(){
		Random random = new Random();
		byte[] seed = new byte[]{(byte)random.nextInt(128),
				(byte)random.nextInt(128),
				(byte)random.nextInt(128),
				(byte)random.nextInt(128),
				(byte)random.nextInt(128),
				(byte)random.nextInt(128),
				(byte)random.nextInt(128),
				(byte)random.nextInt(128),
				(byte)random.nextInt(128),
				(byte)random.nextInt(128),
				(byte)random.nextInt(128),
				(byte)random.nextInt(128),
				(byte)random.nextInt(128),
				(byte)random.nextInt(128),
				(byte)random.nextInt(128),
				(byte)random.nextInt(128),
				(byte)random.nextInt(128),
				(byte)random.nextInt(128),
				(byte)random.nextInt(128),
				(byte)random.nextInt(128),
				(byte)random.nextInt(128)};
		SecureRandom sRandom = new SecureRandom(seed);
		byte[] whatTheFuck = sRandom.generateSeed(Integer.MAX_VALUE);
		SecureRandom sRandom1 = new SecureRandom(whatTheFuck);
		byte[] damn1 = sRandom1.generateSeed(Integer.MIN_VALUE);
		SecureRandom secureRandom = new SecureRandom(damn1);
		byte[] damn2 = new byte[0];
		int letsgo = secureRandom.nextInt(3);
		switch (letsgo){
			case 1:
				damn2 = sRandom.generateSeed(Integer.MIN_VALUE);
			case 2:
				damn2 = sRandom.generateSeed(Integer.MAX_VALUE);
		}
		return new SecureRandom(damn2);
	}
}
