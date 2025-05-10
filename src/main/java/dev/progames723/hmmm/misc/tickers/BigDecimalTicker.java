package dev.progames723.hmmm.misc.tickers;

import java.math.BigDecimal;

public class BigDecimalTicker {
	private BigDecimal tick;
	
	public BigDecimalTicker(BigDecimal initialValue) {
		tick = initialValue;
	}
	
	public BigDecimalTicker() {
		this(BigDecimal.ZERO);
	}
	
	public synchronized BigDecimal tick(BigDecimal partialTick) {
		return tick = tick.add(partialTick);
	}
	
	public BigDecimal getValue() {
		return tick;
	}
	
	@Override
	public String toString() {
		return "BigDecimalTicker{" +
			"tick=" + tick +
			'}';
	}
}
