package dev.progames723.hmmm.utils;

import dev.progames723.hmmm.HmmmLibrary;
import net.minecraft.server.MinecraftServer;

import java.lang.reflect.Field;

@SuppressWarnings("unused")
public class MinecraftUtil {
	private MinecraftUtil() {MiscUtil.instantiationOfUtilClass();}
	
	//everything else later
	
	public static class Server {
		private Server() {MiscUtil.instantiationOfUtilClass();}
		
		/**
		 * really neat on the server-side, huh?
		 * @return {@code null} if server is not running(should never happen on server side) or if not running locally
		 */
		@SuppressWarnings("JavaReflectionMemberAccess")
		public static MinecraftServer getServerInstance() {
			Field field;
			MinecraftServer instance;
			try {
				field = MinecraftServer.class.getDeclaredField("hmmm$serverInstance");
				field.setAccessible(true);
				instance = (MinecraftServer) field.get(null);
				field.setAccessible(false);
			} catch (Exception e) {
				HmmmLibrary.LOGGER.debug("Exception while getting server instance!", e);
				return null;
			}
			return instance;
		}
	}
}
