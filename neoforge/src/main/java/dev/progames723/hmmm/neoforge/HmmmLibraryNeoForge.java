package dev.progames723.hmmm.neoforge;

import dev.progames723.hmmm.HmmmLibrary;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(HmmmLibrary.MOD_ID)
public final class HmmmLibraryNeoForge {
    public HmmmLibraryNeoForge(IEventBus bus) {
        HmmmLibrary.init();
    }
}
