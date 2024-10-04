module hmmm_library_common {
	requires architectury;
	requires architectury.injectables;
	requires static minecraft.merged.ca68899251;//change this and dont include it in PR's (yes its the loom quirks)
	requires static net.fabricmc.loader;
	requires org.apache.commons.io;
	requires transitive org.burningwave.core;
	requires transitive org.jetbrains.annotations;
	requires transitive org.joml;
	requires transitive org.slf4j;
	requires transitive org.spongepowered.mixin;
	requires java.logging;
	exports dev.progames723.hmmm;
	exports dev.progames723.hmmm.utils;
	exports dev.progames723.hmmm.event;
	exports dev.progames723.hmmm.event.utils;
	exports dev.progames723.hmmm.event.events;
	exports dev.progames723.hmmm.event.events.client;
	exports dev.progames723.hmmm.event.api;
	exports dev.progames723.hmmm.mixin;
	exports dev.progames723.hmmm.damage_types;
	exports dev.progames723.hmmm.internal to hmmm_library_fabric, hmmm_library_forge, hmmm_library_neoforge;
}