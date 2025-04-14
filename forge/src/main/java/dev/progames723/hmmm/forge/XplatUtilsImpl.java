package dev.progames723.hmmm.forge;

import dev.progames723.hmmm.utils.XplatUtils;
import net.minecraftforge.fml.loading.FMLLoader;

public class XplatUtilsImpl extends XplatUtils {
	public XplatUtilsImpl() {super();}
	
	@Override
	public Side getSide() {
		return FMLLoader.getDist().isClient() ? Side.CLIENT : Side.SERVER;
	}
}
