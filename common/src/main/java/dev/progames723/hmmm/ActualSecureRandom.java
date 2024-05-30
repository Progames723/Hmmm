package dev.progames723.hmmm;

import java.security.SecureRandom;
import java.util.Random;

/**
 * NOT ACTUAL IMPLEMENTATION OF SECURE RANDOM
 */
public class ActualSecureRandom {
	
	private ActualSecureRandom() {}
	
	public static SecureRandom createSecureRandom(){
		SecureRandom sRandom = new SecureRandom(generateSeed());
		SecureRandom sRandom1 = new SecureRandom(sRandom.generateSeed(Integer.MAX_VALUE));
		SecureRandom secureRandom = new SecureRandom(sRandom1.generateSeed(Integer.MAX_VALUE));
		SecureRandom random = new SecureRandom(generateSeed((byte) (secureRandom.nextInt(128)+1), secureRandom.nextInt(320)));
		return new SecureRandom(generateSeed((byte) (random.nextInt(128)+1), random.nextInt(320)));
	}
	
	private static byte[] generateSeed() {
		return generateSeed(Byte.MAX_VALUE, 0);
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