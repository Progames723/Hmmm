package dev.progames723.hmmm.neoforge;

import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.neoforge.event.HmmmLibraryLoadEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.ApiStatus;

@Mod(HmmmLibrary.MOD_ID)
public final class HmmmLibraryNeoForge {
    public HmmmLibraryNeoForge(IEventBus bus) {
        bus.addListener(this::setup);
    }
    
    @SubscribeEvent
    @ApiStatus.Internal
    public void setup(FMLCommonSetupEvent event) {
        HmmmLibrary.preInit();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.post(new HmmmLibraryLoadEvent());
        HmmmLibrary.init();
    }
}
