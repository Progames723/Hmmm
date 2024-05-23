package dev.progames723.hmmm.damage_types;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

import static dev.progames723.hmmm.HmmmLibrary.MOD_ID;

/**
 * Damage types here are used in a lot create mods
 */
public class HmmmDamageTypes {
	public static ResourceKey<DamageType> PIERCING = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(MOD_ID, "piercing"));;
}
