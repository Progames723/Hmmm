package dev.progames723.hmmm;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class GMPWrapper {
	/**
	 * DO NOT MODIFY DURING RUNTIME
	 * <p>
	 * IF MODIFIED FROM {@code false} TO {@code true} EXPECT AN IRRECOVERABLE ERROR
	 */
	private static boolean isSupported = true;//assume true until false
	private boolean isCleared = false;
	private static final MathContext mc = new MathContext(0, RoundingMode.HALF_EVEN);
	private static final com.ibm.icu.math.MathContext mc2 = new com.ibm.icu.math.MathContext(0, com.ibm.icu.math.MathContext.SCIENTIFIC, false, com.ibm.icu.math.MathContext.ROUND_HALF_UP);
	
	public static void testGMP() {
		if (!isSupported) return;
		try {
			GMP.test();
		} catch (UnsatisfiedLinkError e) {
			HmmmLibrary.LOGGER.error(HmmmLibrary.NATIVE, "the fuck", e);
			isSupported = false;
		}
	}
	
	private final GMP instanceGMP;
	private BigDecimal instanceBD;//cannot be final
	
	private GMPWrapper(double d) {
		instanceGMP = isSupported ? new GMP(d) : null;
		instanceBD = BigDecimal.valueOf(d);
	}
	
	private GMPWrapper(String string) {
		instanceGMP = isSupported ? new GMP(string) : null;
		instanceBD = string.matches("[^\\-0-9.]") ? new BigDecimal(0, mc) : new BigDecimal(string, mc);
	}
	
	public static void create(String string) {
		new GMPWrapper(string);
	}
	
	public static void create(double d) {
		new GMPWrapper(d);
	}
	
	public int intValue() {
		if (isSupported) return instanceGMP.intValue();
		return instanceBD.intValue();
	}
	
	public long longValue() {
		if (isSupported) return instanceGMP.longValue();
		return instanceBD.longValue();
	}
	
	public float floatValue() {
		if (isSupported) return instanceGMP.floatValue();
		return instanceBD.floatValue();
	}
	
	public double doubleValue() {
		if (isSupported) return instanceGMP.doubleValue();
		return instanceBD.doubleValue();
	}
	
	public GMPWrapper multiply(GMPWrapper wrapper) {
		if (isSupported) instanceGMP.multiply(wrapper.instanceGMP);
		instanceBD = instanceBD.multiply(wrapper.instanceBD, mc);
		return this;
	}
	public GMPWrapper multiply(String string) {
		if (isSupported) instanceGMP.multiply(string);
		instanceBD = instanceBD.multiply(new BigDecimal(string, mc), mc);
		return this;
	}
	public GMPWrapper divide(GMPWrapper wrapper) {
		if (isSupported) instanceGMP.divide(wrapper.instanceGMP);
		instanceBD = instanceBD.divide(wrapper.instanceBD, mc);
		return this;
	}
	public GMPWrapper divide(String string) {
		if (isSupported) instanceGMP.divide(string);
		instanceBD = instanceBD.divide(new BigDecimal(string, mc), mc);
		return this;
	}
	public GMPWrapper pow(GMPWrapper wrapper) {
		if (isSupported) instanceGMP.pow(wrapper.instanceGMP);
		com.ibm.icu.math.BigDecimal temp1 = new com.ibm.icu.math.BigDecimal(instanceBD);
		com.ibm.icu.math.BigDecimal temp2 = new com.ibm.icu.math.BigDecimal(wrapper.instanceBD);
		temp1 = temp1.pow(temp2, mc2);
		instanceBD = temp1.toBigDecimal();
		return this;
	}
	public GMPWrapper pow(String string) {
		if (isSupported) instanceGMP.pow(string);
		com.ibm.icu.math.BigDecimal temp1 = new com.ibm.icu.math.BigDecimal(instanceBD);
		com.ibm.icu.math.BigDecimal temp2 = new com.ibm.icu.math.BigDecimal(string);
		temp1 = temp1.pow(temp2, mc2);
		instanceBD = temp1.toBigDecimal();
		return this;
	}
	public GMPWrapper sqrt() {
		if (isSupported) instanceGMP.sqrt();
		instanceBD = instanceBD.sqrt(mc);
		return this;
	}
	public GMPWrapper nthRoot(GMPWrapper wrapper) {
		if (isSupported) instanceGMP.nthRoot(wrapper.instanceGMP);
		com.ibm.icu.math.BigDecimal temp1 = new com.ibm.icu.math.BigDecimal(instanceBD);
		com.ibm.icu.math.BigDecimal temp2 = new com.ibm.icu.math.BigDecimal(BigDecimal.ONE.divide(wrapper.instanceBD, mc));
		temp1 = temp1.pow(temp2, mc2);
		instanceBD = temp1.toBigDecimal();
		return this;
	}
	public GMPWrapper nthRoot(String string) {
		if (isSupported) instanceGMP.nthRoot(string);
		com.ibm.icu.math.BigDecimal temp1 = new com.ibm.icu.math.BigDecimal(instanceBD);
		com.ibm.icu.math.BigDecimal temp2 = new com.ibm.icu.math.BigDecimal(BigDecimal.ONE.divide(new BigDecimal(string, mc), mc));
		temp1 = temp1.pow(temp2, mc2);
		instanceBD = temp1.toBigDecimal();
		return this;
	}
	public GMPWrapper add(GMPWrapper wrapper) {
		if (isSupported) instanceGMP.add(wrapper.instanceGMP);
		instanceBD = instanceBD.add(wrapper.instanceBD, mc);
		return this;
	}
	public GMPWrapper add(String string) {
		if (isSupported) instanceGMP.add(string);
		instanceBD = instanceBD.add(new BigDecimal(string, mc), mc);
		return this;
	}
	public GMPWrapper subtract(GMPWrapper wrapper) {
		if (isSupported) instanceGMP.subtract(wrapper.instanceGMP);
		instanceBD = instanceBD.subtract(wrapper.instanceBD, mc);
		return this;
	}
	public GMPWrapper subtract(String string) {
		if (isSupported) instanceGMP.subtract(string);
		instanceBD = instanceBD.subtract(new BigDecimal(string), mc);
		return this;
	}
	public GMPWrapper ceil() {
		if (isSupported) instanceGMP.ceil();
		instanceBD.round(new MathContext(1, RoundingMode.CEILING));
		return this;
	}
	public GMPWrapper floor() {
		if (isSupported) instanceGMP.floor();
		instanceBD.round(new MathContext(1, RoundingMode.FLOOR));
		return this;
	}
	public GMPWrapper abs() {
		if (isSupported) instanceGMP.abs();
		instanceBD = instanceBD.abs();
		return this;
	}
	public GMPWrapper truncate() {
		if (isSupported) instanceGMP.truncate();
		instanceBD = instanceBD.round(new MathContext(1, RoundingMode.FLOOR));
		return this;
	}
	
	public void clear() {
		if (isCleared) return;
		if (isSupported) instanceGMP.clear();
		instanceBD = null;
		isCleared = true;
	}
	
	public static void clear(GMPWrapper... wrappers) {
		for (GMPWrapper wrapper : wrappers) {
			if (wrapper.isCleared) continue;
			wrapper.clear();
		}
	}
	
	public GMPWrapper set(GMPWrapper wrapper) {
		if (!wrapper.isCleared) {
			if (isSupported) instanceGMP.set(wrapper.instanceGMP);
			instanceBD = wrapper.instanceBD;
		}
		return this;
	}
	
	public GMPWrapper set(String str) {
		if (isSupported) instanceGMP.set(str);
		instanceBD = str.matches("[^\\-0-9.]") ? new BigDecimal(0, mc) : new BigDecimal(str, mc);
		return this;
	}
}
