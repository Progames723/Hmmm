package dev.progames723.hmmm.neoforge;

import dev.progames723.hmmm.utils.XplatUtils;
import net.neoforged.fml.loading.FMLLoader;

public class XplatUtilsImpl extends XplatUtils {
	public XplatUtilsImpl() {super();}
	
	@Override
	public Side getSide() {
		return FMLLoader.getDist().isClient() ? Side.CLIENT : Side.SERVER;
	}
}
