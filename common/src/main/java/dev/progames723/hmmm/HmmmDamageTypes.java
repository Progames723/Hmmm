package dev.progames723.hmmm;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class HmmmDamageTypes {
	private static ResourceKey<DamageType> register(String name) {
		return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(HmmmLibrary.MOD_ID, name));
	}
	public static ResourceKey<DamageType> PIERCING = register("piercing");
}
