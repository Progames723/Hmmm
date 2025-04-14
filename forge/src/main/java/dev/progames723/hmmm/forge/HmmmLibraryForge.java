package dev.progames723.hmmm.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.forge.event.HmmmLibraryLoadEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(HmmmLibrary.MOD_ID)
@Mod.EventBusSubscriber(modid = HmmmLibrary.MOD_ID)
public class HmmmLibraryForge {
    public HmmmLibraryForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(HmmmLibrary.MOD_ID, bus);
        HmmmLibrary.preInit();
        bus.post(new HmmmLibraryLoadEvent(), (iEventListener, e) -> {
            HmmmLibrary.LOGGER.debug("passing hmmm library load event to {}", iEventListener.listenerName());
            iEventListener.invoke(e);
        });
        HmmmLibrary.init();
    }
 }