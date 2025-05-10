//? if forge {
/*package dev.progames723.hmmm.platforms.forge;

import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.HmmmLibraryClient;
import dev.progames723.hmmm.platforms.forge.event.HmmmLibraryLoadEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(HmmmLibrary.MOD_ID)
@Mod.EventBusSubscriber(modid = HmmmLibrary.MOD_ID)
public class HmmmLibraryForge {
	public HmmmLibraryForge() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(EventPriority.HIGHEST, true, HmmmLibraryForge::onClientInit);
		bus.addListener(EventPriority.HIGHEST, true, HmmmLibraryForge::onInit);
	}
	
	@SubscribeEvent
	static void onInit(FMLCommonSetupEvent event) {
		HmmmLibrary.preInit();
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.post(new HmmmLibraryLoadEvent(), (iEventListener, e) -> {
			HmmmLibrary.LOGGER.debug("passing hmmm library load event to {}", iEventListener.listenerName());
			iEventListener.invoke(e);
		});
		HmmmLibraryClient.init();
		HmmmLibrary.init();
	}
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	static void onClientInit(FMLClientSetupEvent event) {
		HmmmLibraryClient.init();
	}
}
*///?}