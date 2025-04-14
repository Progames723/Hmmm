package dev.progames723.hmmm.fabric;

import dev.progames723.hmmm.utils.XplatUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

public class XplatUtilsImpl extends XplatUtils {
	public XplatUtilsImpl() {super();}
	
	@Override
	public Side getSide() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? Side.CLIENT : Side.SERVER;
	}
}
