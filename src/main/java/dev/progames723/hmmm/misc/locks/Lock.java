package dev.progames723.hmmm.misc.locks;

public interface Lock {
	void lock() throws InterruptedException;
	
	boolean isLocked();
	
	void unlock();
}
