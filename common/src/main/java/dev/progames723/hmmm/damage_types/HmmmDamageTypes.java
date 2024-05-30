package dev.progames723.hmmm.damage_types;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;

import static dev.progames723.hmmm.HmmmLibrary.MOD_ID;

/**
 * Damage types here are used in a lot of mods
 */
@SuppressWarnings("unused")
public class HmmmDamageTypes {
	private final RegistryAccess access;
	
	public HmmmDamageTypes(LevelReader reader) {this.access = reader.registryAccess();}
	
	public HmmmDamageTypes(RegistryAccess access) {this.access = access;}
	
	public DamageSource piercing() {
		return DamageSourceFactory.create(access, PIERCING);
	}
	
	public DamageSource piercing(Entity directEntity) {
		return DamageSourceFactory.create(access, PIERCING, directEntity);
	}
	
	public DamageSource piercing(Entity directEntity, Entity causingEntity) {
		return DamageSourceFactory.create(access, PIERCING, directEntity, causingEntity);
	}
	
	public static ResourceKey<DamageType> PIERCING = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(MOD_ID, "piercing"));
}
