package dev.progames723.hmmm.damage_types;

import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.HmmmRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

/**
 * Damage types here are used in a lot of mods
 */
public class HmmmDamageTypes {
	public static ResourceKey<DamageType> PIERCING = HmmmRegistries.registerDamageType(HmmmLibrary.MOD_ID,"piercing", HmmmLibrary.LOGGER);
}
