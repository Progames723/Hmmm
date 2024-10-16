package dev.progames723.hmmm;

import java.lang.annotation.Native;
import java.math.BigDecimal;

/**
 * NOT RECOMMENDED TO USE<p>
 * please use {@link GMPWrapper} instead
 */
public class GMP extends Number {
	static void test() {
		new GMP().clear();
	}
	
	@Native
	@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
	private boolean isCleared = false;
	
	@Override
	public native int intValue();
	
	@Override
	public native long longValue();
	
	@Override
	public native float floatValue();
	
	@Override
	public native double doubleValue();
	
	public native String getAsString();
	
	public BigDecimal getAsBigDecimal() {
		if (getAsString().matches("[^\\-0-9.]")) return BigDecimal.ZERO;//default impl
		return new BigDecimal(getAsString());
	}
	
	public GMP(String string) {
		if (!string.matches("[^\\-0-9.]")) create(string);
		else create(0);
	}
	
	public GMP() {
		this(0);
	}
	
	public GMP(int i) {
		this((long) i);
	}
	
	public GMP(long l) {
		this((double) l);
	}
	
	public GMP(double d) {
		create(d);
	}
	
	public GMP(float f) {
		this((double) f);
	}
	
	private native void create(double d);
	
	private native void create(String s);
	
	//math operations
	public native GMP multiply(GMP gmp);
	public native GMP multiply(String string);
	public native GMP divide(GMP gmp);
	public native GMP divide(String string);
	public native GMP pow(GMP gmp);
	public native GMP pow(String string);
	public native GMP sqrt();
	public GMP nthRoot(GMP gmp) {
		GMP temp = new GMP(1);
		try {return pow(temp.divide(gmp));} finally {temp.clear();}
	}
	public GMP nthRoot(String string) {
		GMP temp = new GMP(1);
		try {return pow(temp.divide(string));} finally {temp.clear();}
	}
	public native GMP add(GMP gmp);
	public native GMP add(String string);
	public native GMP subtract(GMP gmp);
	public native GMP subtract(String string);
	public native GMP ceil();
	public native GMP floor();
	public native GMP abs();
	public native GMP truncate();
	//end of math operations
	
	//helper methods
	public native void clear();
	public static void clear(GMP... gmps) {
		for (GMP gmp : gmps) gmp.clear();
	}
	public native GMP set(GMP gmp);
	public native GMP set(String str);
	//end of helper methods
	
	
	@Override
	public native boolean equals(Object obj);
	
	@Override
	public int hashCode() {
		double precision = (super.hashCode() - getAsBigDecimal().hashCode()) * doubleValue();
		return (int) precision;
	}
	
	public boolean isCleared() {
		return isCleared;
	}
}
