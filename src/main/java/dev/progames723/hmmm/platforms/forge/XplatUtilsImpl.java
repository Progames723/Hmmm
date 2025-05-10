//? if forge {
/*package dev.progames723.hmmm.platforms.forge;

import dev.progames723.hmmm.utils.XplatUtils;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class XplatUtilsImpl extends XplatUtils {
	public XplatUtilsImpl() {super();}
	
	@Override
	protected Side getSide(Void unused) {
		return FMLLoader.getDist().isClient() ? Side.CLIENT : Side.SERVER;
	}
	
	@Override
	protected boolean isDevEnv(Void unused) {
		return !FMLLoader.isProduction();
	}
	
	@Override
	protected Path getConfigFolder(Void unused) {
		return FMLPaths.CONFIGDIR.get();
	}
}
*///?}