package dev.progames723.hmmm.utils;

import dev.progames723.hmmm.xplat.XplatProvider;

public abstract class XplatUtils implements XplatProvider {
	private static XplatUtils instance;
	
	public static XplatUtils getInstance() {
		if (instance == null) instance = XplatProvider.tryFindInstance(XplatUtils.class, false);
		return instance;
	}
	
	public abstract Side getSide();
	
	public enum Side {
		CLIENT,
		SERVER;
		
		public boolean isClient() {
			return this.equals(CLIENT);
		}
		
		public boolean isServer() {
			return this.equals(SERVER);
		}
	}
}
