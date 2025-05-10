//? if fabric {
package dev.progames723.hmmm.platforms.fabric;

import dev.progames723.hmmm.HmmmLibrary;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import java.lang.reflect.Field;

public class HmmmLibraryFabric implements PreLaunchEntrypoint {
	@Override
	public void onPreLaunch() {
		HmmmLibrary.preInit();
		FabricLoader.getInstance().invokeEntrypoints("hmmm_library", HmmmLibraryEntrypoint.class, hmmmLibraryEntrypoint -> {
			HmmmLibrary.LOGGER.debug("Invoking {}", hmmmLibraryEntrypoint.getClass());
			hmmmLibraryEntrypoint.onLoad();
		});
		HmmmLibrary.init();
	}
}
//?}