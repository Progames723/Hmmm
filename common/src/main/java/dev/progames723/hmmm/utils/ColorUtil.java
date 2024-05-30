package dev.progames723.hmmm.utils;

import org.jetbrains.annotations.Range;
import org.joml.Vector3d;
import org.joml.Vector4d;

@SuppressWarnings("unused")
public class ColorUtil {
	public static long RGBToDecimalColor(Vector3d colors) {
		return RGBToDecimalColor((long) colors.x, (long) colors.y, (long) colors.z);
	}
	
	public static Vector4d RGBToARGB(Vector3d colors) {
		return RGBToARGB(0, colors);
	}
	
	public static Vector4d RGBToARGB(@Range(from = 0, to = 255) long a, Vector3d colors) {
		return new Vector4d(a, (long) colors.x, (long) colors.y, (long) colors.z);
	}
	
	public static Vector4d RGBToARGB(@Range(from = 0, to = 255) long a, @Range(from = 0, to = 255) long r, @Range(from = 0, to = 255) long g, @Range(from = 0, to = 255) long b) {
		return new Vector4d(a, r, g, b);
	}
	
	public static long RGBToDecimalColor(@Range(from = 0, to = 255) long r, @Range(from = 0, to = 255) long g, @Range(from = 0, to = 255) long b) {
		return r*65536+g*256+b;//easy
	}
	
	public static long ARGBToDecimalColor(Vector4d colors) {
		return ARGBToDecimalColor((long) colors.x, (long) colors.y, (long) colors.z, (long) colors.w);
	}
	
	public static long ARGBToDecimalColor(@Range(from = 0, to = 255) long a, @Range(from = 0, to = 255) long r, @Range(from = 0, to = 255) long g, @Range(from = 0, to = 255) long b) {
		return a*16777216+r*65536+g*256+b;//easy
	}
	
	public static Vector3d decimalToRGBColor(long decimal) {//help me
		long red = (decimal >> 16) & 0xff;
		long green = (decimal >> 8) & 0xff;
		long blue = decimal & 0xff;
		return new Vector3d(red, green, blue);
	}
	
	public static Vector4d decimalToARGBColor(long decimal) {//help me
		long alpha = (decimal >> 24) & 0xff;
		long red = (decimal >> 16) & 0xff;
		long green = (decimal >> 8) & 0xff;
		long blue = decimal & 0xff;
		return new Vector4d(alpha, red, green, blue);
	}
	
	private ColorUtil() {throw new RuntimeException();}
}
