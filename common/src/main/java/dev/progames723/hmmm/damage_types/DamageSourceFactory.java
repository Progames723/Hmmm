package dev.progames723.hmmm.damage_types;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public final class DamageSourceFactory {
	/**
	 * Creates a new {@link net.minecraft.world.damagesource.DamageSource} from {@code ResourceKey<DamageType>}.
	 * @param registryAccess registry access for retrieving dynamic {@link DamageType} registry.
	 * @param damageType key for finding the {@link DamageType}.
	 * @param directEntity the entity directly responsible for causing damage.
	 * @param causingEntity the entity that is responsible for <code>directEntity</code> to cause damage to the target, or an {@link net.minecraft.world.entity.Entity} that shot the projectile.
	 * @return new {@link net.minecraft.world.damagesource.DamageSource} instance.
	 */
	public static DamageSource create(RegistryAccess registryAccess, ResourceKey<DamageType> damageType, @Nullable Entity directEntity, @Nullable Entity causingEntity) {
		return new DamageSource(registryAccess.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageType), directEntity, causingEntity);
	}
	
	/**
	 * Creates a new {@link net.minecraft.world.damagesource.DamageSource} from {@code ResourceKey<DamageType>}.
	 * @param registryAccess registry access for retrieving dynamic {@link DamageType} registry.
	 * @param damageType key for finding the {@link DamageType}.
	 * @param directEntity the entity directly responsible for causing damage.
	 * @return new {@link net.minecraft.world.damagesource.DamageSource} instance.
	 */
	public static DamageSource create(RegistryAccess registryAccess, ResourceKey<DamageType> damageType, @Nullable Entity directEntity) {
		return create(registryAccess, damageType, directEntity, null);
	}
	
	/**
	 * Creates a new {@link net.minecraft.world.damagesource.DamageSource} from {@code ResourceKey<DamageType>}.
	 * @param registryAccess registry access for retrieving dynamic {@link DamageType} registry.
	 * @param damageType key for finding the {@link DamageType}.
	 * @return new {@link net.minecraft.world.damagesource.DamageSource} instance.
	 */
	public static DamageSource create(RegistryAccess registryAccess, ResourceKey<DamageType> damageType) {
		return create(registryAccess, damageType, null, null);
	}
	
	
	/**
	 * Creates a new {@link net.minecraft.world.damagesource.DamageSource} from {@code ResourceKey<DamageType>}.
	 * @param level registry access for retrieving dynamic {@link DamageType} registry.
	 * @param damageType key for finding the {@link DamageType}.
	 * @param directEntity the entity directly responsible for causing damage.
	 * @param causingEntity the entity that is responsible for <code>directEntity</code> to cause damage to the target, or an {@link net.minecraft.world.entity.Entity} that shot the projectile.
	 * @return new {@link net.minecraft.world.damagesource.DamageSource} instance.
	 */
	public static DamageSource create(LevelReader level, ResourceKey<DamageType> damageType, @Nullable Entity directEntity, @Nullable Entity causingEntity) {
		return create(level.registryAccess(), damageType, directEntity, causingEntity);
	}
	
	/**
	 * Creates a new {@link net.minecraft.world.damagesource.DamageSource} from {@code ResourceKey<DamageType>}.
	 * @param level registry access for retrieving dynamic {@link DamageType} registry.
	 * @param damageType key for finding the {@link DamageType}.
	 * @param directEntity the entity directly responsible for causing damage.
	 * @return new {@link net.minecraft.world.damagesource.DamageSource} instance.
	 */
	public static DamageSource create(LevelReader level, ResourceKey<DamageType> damageType, @Nullable Entity directEntity) {
		return create(level.registryAccess(), damageType, directEntity, null);
	}
	
	/**
	 * Creates a new {@link net.minecraft.world.damagesource.DamageSource} from {@code ResourceKey<DamageType>}.
	 * @param level registry access for retrieving dynamic {@link DamageType} registry.
	 * @param damageType key for finding the {@link DamageType}.
	 * @return new {@link net.minecraft.world.damagesource.DamageSource} instance.
	 */
	public static DamageSource create(LevelReader level, ResourceKey<DamageType> damageType) {
		return create(level.registryAccess(), damageType, null, null);
	}
	
	private DamageSourceFactory() {throw new RuntimeException();}
}
