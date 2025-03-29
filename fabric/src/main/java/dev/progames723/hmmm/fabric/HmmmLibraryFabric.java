package dev.progames723.hmmm.fabric;

import dev.progames723.hmmm.HmmmLibrary;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class HmmmLibraryFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        HmmmLibrary.preInit();
        FabricLoader.getInstance().invokeEntrypoints("hmmm_library", HmmmLibraryEntrypoint.class, hmmmLibraryEntrypoint -> {
            HmmmLibrary.LOGGER.debug("Invoking {}", hmmmLibraryEntrypoint.getClass());
            hmmmLibraryEntrypoint.onLoad();
        });
        HmmmLibrary.init();
    }
}