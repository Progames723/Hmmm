package dev.progames723.hmmm;

public interface Mappings<I, O> {
	O map(I input);
	
	I unmap(O output);
}
