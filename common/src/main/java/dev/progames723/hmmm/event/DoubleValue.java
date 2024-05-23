package dev.progames723.hmmm.event;

public class DoubleValue<A, B> {
	private A a;
	private B b;
	
	public DoubleValue(A a, B b) {
		this.a = a;
		this.b = b;
	}
	
	public B getB() {
		return b;
	}
	
	public void setB(B b) {
		this.b = b;
	}
	
	public A getA() {
		return a;
	}
	
	public void setA(A a) {
		this.a = a;
	}
}
