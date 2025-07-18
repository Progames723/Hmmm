package dev.progames723.hmmm.misc.locks;

public final class SpinLock implements Lock {
	private volatile Thread locker = null;
	
	public SpinLock() {}
	
	public void lock() throws InterruptedException {
		if (isLockerActive()) unlock();
		else waitForUnlock();
		locker = Thread.currentThread();
	}
	
	@Override
	public boolean isLocked() {
		return isLockerActive();
	}
	
	public void unlock() {
		locker = null;
	}
	
	private boolean isLockerActive() {
		return locker != null && locker.isAlive();
	}
	
	private void waitForUnlock() throws InterruptedException {
		while (isLockerActive()) {
			Thread.onSpinWait();
		}
		throw new InterruptedException();
	}
}
