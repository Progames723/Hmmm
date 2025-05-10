//? if fabric {
package dev.progames723.hmmm.platforms.fabric.client;

import dev.progames723.hmmm.HmmmLibraryClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class HmmmLibraryClientFabric implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HmmmLibraryClient.init();
	}
}
//?}