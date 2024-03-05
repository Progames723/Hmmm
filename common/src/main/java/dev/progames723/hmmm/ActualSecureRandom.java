package dev.progames723.hmmm;

import java.security.SecureRandom;
import java.util.Random;

/**
 * NOT ACTUAL IMPLEMENTATION OF SECURE RANDOM
 */
public abstract class ActualSecureRandom {
	public static SecureRandom createSecureRandom(){
		SecureRandom sRandom = new SecureRandom(generateSeed());
		SecureRandom sRandom1 = new SecureRandom(sRandom.generateSeed(Integer.MAX_VALUE));
		SecureRandom secureRandom = new SecureRandom(sRandom1.generateSeed(Integer.MAX_VALUE));
		return new SecureRandom(generateSeed((byte) (secureRandom.nextInt(128)+1), secureRandom.nextInt(320)));
	}
	private static byte[] generateSeed() {
		Random random = new Random();
		SecureRandom secureRandom = new SecureRandom(new byte[]{
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
				(byte)random.nextInt(128),
				(byte)random.nextInt(128),
				(byte)random.nextInt(128),
				(byte)random.nextInt(128)
		});
		byte[] lol = secureRandom.generateSeed(Integer.MAX_VALUE);
		byte[] lol2 = secureRandom.generateSeed(Integer.MAX_VALUE);
		byte[] lol3 = secureRandom.generateSeed(Integer.MAX_VALUE);
		byte[] lol4 = secureRandom.generateSeed(Integer.MAX_VALUE);
		byte[] lol5 = secureRandom.generateSeed(Integer.MAX_VALUE);
		byte[] lol6 = secureRandom.generateSeed(Integer.MAX_VALUE);
		byte[] lol7 = secureRandom.generateSeed(Integer.MAX_VALUE);
		byte[] lol8 = secureRandom.generateSeed(Integer.MAX_VALUE);
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
			default -> new byte[]{(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128),
					(byte) random.nextInt(128)};
		};
	}
	private static byte[] generateSeed(byte maxBound, int funny) {
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
		byte[] lol = secureRandom.generateSeed(Integer.MAX_VALUE-funny);
		byte[] lol2 = secureRandom.generateSeed(Integer.MAX_VALUE-funny);
		byte[] lol3 = secureRandom.generateSeed(Integer.MAX_VALUE-funny);
		byte[] lol4 = secureRandom.generateSeed(Integer.MAX_VALUE-funny);
		byte[] lol5 = secureRandom.generateSeed(Integer.MAX_VALUE-funny);
		byte[] lol6 = secureRandom.generateSeed(Integer.MAX_VALUE-funny);
		byte[] lol7 = secureRandom.generateSeed(Integer.MAX_VALUE-funny);
		byte[] lol8 = secureRandom.generateSeed(Integer.MAX_VALUE-funny);
		
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