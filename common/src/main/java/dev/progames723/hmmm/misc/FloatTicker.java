package dev.progames723.hmmm.misc;

public class FloatTicker {
	private float tick;
	
	public FloatTicker(float initialValue) {
		tick = initialValue;
	}
	
	public FloatTicker() {
		this(0.0f);
	}
	
	public float tick(float partialTick) {
		float tempTick = tick + partialTick;
		if (tempTick == tick) tempTick = 0;
		return tick = tempTick;
	}
	
	public float getValue() {
		return tick;
	}
	
	@Override
	public String toString() {
		return "FloatTicker{" +
			"tick=" + tick +
			'}';
	}
}
