package dev.progames723.hmmm.neoforge;

import net.neoforged.fml.common.Mod;

import dev.progames723.hmmm.HmmmLibrary;

@Mod(HmmmLibrary.MOD_ID)
public final class HmmmLibraryNeoForge {
    public HmmmLibraryNeoForge() {
        HmmmLibrary.init();
    }
}
