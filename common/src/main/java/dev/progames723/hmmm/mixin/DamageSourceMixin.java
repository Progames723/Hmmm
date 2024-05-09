package dev.progames723.hmmm.mixin;

import dev.progames723.hmmm.damage_types.ExtDamageSource;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(DamageSource.class)
public class DamageSourceMixin implements ExtDamageSource {
	
	@Unique
	private static DamageSource of(LevelReader level, ResourceKey<DamageType> damageType, @Nullable Entity directEntity, @Nullable Entity causingEntity) {
		return of(level.registryAccess(), damageType, directEntity, causingEntity);
	}
	
	@Unique
	private static DamageSource of(LevelReader level, ResourceKey<DamageType> damageType, @Nullable Entity directEntity) {
		return of(level, damageType, directEntity, null);
	}
	
	@Unique
	private static DamageSource of(LevelReader level, ResourceKey<DamageType> damageType) {
		return of(level, damageType, null, null);
	}
	
	@Unique
	private static DamageSource of(RegistryAccess registryAccess, ResourceKey<DamageType> damageType, @Nullable Entity directEntity, @Nullable Entity causingEntity) {
		return new net.minecraft.world.damagesource.DamageSource(registryAccess.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageType), directEntity, causingEntity);
	}
}
