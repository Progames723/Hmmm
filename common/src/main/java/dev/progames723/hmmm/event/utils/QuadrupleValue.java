package dev.progames723.hmmm.event.utils;

@SuppressWarnings("unused")
public class QuadrupleValue<A, B, C, D> extends TripleValue<A, B, C> {
	private D d;
	
	public QuadrupleValue(A a, B b, C c, D d) {
		super(a, b, c);
		this.d = d;
	}
	
	public D getD() {
		return d;
	}
	
	public void setD(D d) {
		this.d = d;
	}
}
