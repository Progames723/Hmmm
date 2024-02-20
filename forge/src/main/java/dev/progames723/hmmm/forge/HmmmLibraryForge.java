package dev.progames723.hmmm.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.progames723.hmmm.HmmmLibrary;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(HmmmLibrary.MOD_ID)
public class HmmmLibraryForge {
    public HmmmLibraryForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(HmmmLibrary.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        HmmmLibrary.init();
    }
}