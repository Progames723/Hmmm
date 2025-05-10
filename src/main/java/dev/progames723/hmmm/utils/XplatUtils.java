package dev.progames723.hmmm.utils;

import dev.progames723.hmmm.xplat.XplatProvider;

import java.nio.file.Path;

public abstract class XplatUtils implements XplatProvider {
	private static XplatUtils instance;
	private static final Side side = getInstance().getSide(null);
	private static final boolean isDevEnv = getInstance().isDevEnv(null);
	private static final Path configFolder = getInstance().getConfigFolder(null);
	
	private static XplatUtils getInstance() {
		if (instance == null) instance = XplatProvider.tryFindInstance(XplatUtils.class, false);
		return instance;
	}
	
	public static Side getSide() {
		return side;
	}
	
	public static boolean isDevEnv() {
		return isDevEnv;
	}
	
	public static Path getConfigFolder() {
		return configFolder;
	}
	
	protected abstract Side getSide(Void unused);
	
	protected abstract boolean isDevEnv(Void unused);
	
	protected abstract Path getConfigFolder(Void unused);
	
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
