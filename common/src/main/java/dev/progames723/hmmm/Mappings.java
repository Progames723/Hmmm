package dev.progames723.hmmm;

public interface Mappings<A, B> {
	B map(A from);
	
	A unmap(B from);
}
