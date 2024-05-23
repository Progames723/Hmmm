package dev.progames723.hmmm.event;

public class TripleValue<A, B, C> extends DoubleValue<A, B> {
	private C c;
	
	public TripleValue(A a, B b, C c) {
		super(a, b);
		this.c = c;
	}
	
	public C getC() {
		return c;
	}
	
	public void setC(C c) {
		this.c = c;
	}
}
