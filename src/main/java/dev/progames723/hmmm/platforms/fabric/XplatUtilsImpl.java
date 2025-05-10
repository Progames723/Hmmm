//? if fabric {
package dev.progames723.hmmm.platforms.fabric;

import dev.progames723.hmmm.utils.XplatUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class XplatUtilsImpl extends XplatUtils {
	public XplatUtilsImpl() {super();}
	
	@Override
	protected Side getSide(Void unused) {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? Side.CLIENT : Side.SERVER;
	}
	
	@Override
	protected boolean isDevEnv(Void unused) {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}
	
	@Override
	protected Path getConfigFolder(Void unused) {
		return FabricLoader.getInstance().getConfigDir();
	}
}
//?}