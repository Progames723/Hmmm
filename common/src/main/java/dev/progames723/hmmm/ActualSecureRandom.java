package dev.progames723.hmmm;

import java.security.SecureRandom;
import java.util.Random;

/**
 * NOT ACTUAL IMPLEMENTATION OF SECURE RANDOM
 */
public class ActualSecureRandom {
	private ActualSecureRandom() {}
	
	public static SecureRandom createSecureRandom() {
		SecureRandom sRandom = new SecureRandom(generateSeed());
		SecureRandom sRandom1 = new SecureRandom(sRandom.generateSeed(1000));
		SecureRandom secureRandom = new SecureRandom(sRandom1.generateSeed(1000));
		SecureRandom random = new SecureRandom(generateSeed((byte) secureRandom.nextInt(128), secureRandom.nextInt(320)));
		return new SecureRandom(generateSeed((byte) random.nextInt(128), random.nextInt(320)));
	}
	
	private static byte[] generateSeed() {
		return generateSeed(Byte.MAX_VALUE, 1000);
	}
	
	private static byte[] generateSeed(byte maxBound, int funny) {//please dont ask about this
		Random random = new Random();
		if (funny < 0) funny *= -1;
		SecureRandom secureRandom = new SecureRandom(new byte[]{
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound),
				(byte)random.nextInt(maxBound)
		});
		byte[] lol = secureRandom.generateSeed(funny);
		byte[] lol2 = secureRandom.generateSeed(funny);
		byte[] lol3 = secureRandom.generateSeed(funny);
		byte[] lol4 = secureRandom.generateSeed(funny);
		byte[] lol5 = secureRandom.generateSeed(funny);
		byte[] lol6 = secureRandom.generateSeed(funny);
		byte[] lol7 = secureRandom.generateSeed(funny);
		byte[] lol8 = secureRandom.generateSeed(funny);
		
		int yes = secureRandom.nextInt(9);
		return switch (yes) {
			case 1 -> lol;
			case 2 -> lol2;
			case 3 -> lol3;
			case 4 -> lol4;
			case 5 -> lol5;
			case 6 -> lol6;
			case 7 -> lol7;
			case 8 -> lol8;
			default -> new byte[]{
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound),
					(byte)random.nextInt(maxBound)
			};
		};
	}
}