package dev.progames723.hmmm.misc.tickers;

public class DoubleTicker {
	private double tick;
	
	public DoubleTicker(double initialValue) {
		tick = initialValue;
	}
	
	public DoubleTicker() {
		this(0.0);
	}
	
	public synchronized double tick(double partialTick) {
		double tempTick = tick + partialTick;
		if (tempTick == tick) tempTick = 0.0;
		return tick = tempTick;
	}
	
	public double getValue() {
		return tick;
	}
	
	@Override
	public String toString() {
		return "DoubleTicker{" +
			"tick=" + tick +
			'}';
	}
}
