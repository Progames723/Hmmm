//? if neoforge {
/*package dev.progames723.hmmm.platforms.neoforge;

import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.HmmmLibraryClient;
import dev.progames723.hmmm.platforms.neoforge.event.HmmmLibraryLoadEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
//? if >= 1.21.1
/^import net.neoforged.fml.common.EventBusSubscriber;^/
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(HmmmLibrary.MOD_ID)
//? if >= 1.21.1
/^@EventBusSubscriber(modid = HmmmLibrary.MOD_ID)^/
//? if < 1.21.1
@Mod.EventBusSubscriber(modid = HmmmLibrary.MOD_ID)
public final class HmmmLibraryNeoForge {
	private static IEventBus bus;
	
	public HmmmLibraryNeoForge(IEventBus bus) {
		bus.addListener(EventPriority.HIGHEST, true, HmmmLibraryNeoForge::onClientInit);
		bus.addListener(EventPriority.HIGHEST, true, HmmmLibraryNeoForge::onInit);
		HmmmLibraryNeoForge.bus = bus;
	}
	
	@SubscribeEvent
	static void onInit(FMLCommonSetupEvent event) {
		HmmmLibrary.preInit();
		if (bus != null) bus.post(new HmmmLibraryLoadEvent());
		HmmmLibrary.init();
	}
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	static void onClientInit(FMLClientSetupEvent event) {
		HmmmLibraryClient.init();
	}
}
*///?}