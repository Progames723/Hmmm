package dev.progames723.hmmm.fabric;

import dev.progames723.hmmm.HmmmLibrary;
import net.fabricmc.api.ModInitializer;

public class HmmmLibraryFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        HmmmLibrary.init();
    }
}