package dev.progames723.hmmm;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.Nullable;

public class DamageSourceSomething {
	private DamageSourceSomething() {}
	/**
	 * Creates a new {@link DamageSource} from {@link DamageType}.
	 * @param level registry access for retrieving dynamic {@link DamageType} registry.
	 * @param damageType key for finding the {@link DamageType}.
	 * @param directEntity the entity directly responsible for causing damage.
	 * @param causingEntity the entity that is responsible for <code>directEntity</code> to cause damage to the target, or an {@link net.minecraft.world.entity.Entity} that shot the projectile.
	 * @return new {@link DamageSource} instance.
	 */
	public static DamageSource of(LevelReader level, ResourceKey<DamageType> damageType, @Nullable Entity directEntity, @Nullable Entity causingEntity) {
		return of(level.registryAccess(), damageType, directEntity, causingEntity);
	}
	/**
	 * Creates a new {@link DamageSource} from {@link DamageType}.
	 * @param registryAccess registry access for retrieving dynamic {@link DamageType} registry.
	 * @param damageType key for finding the {@link DamageType}.
	 * @param directEntity the entity directly responsible for causing damage.
	 * @param causingEntity the entity that is responsible for <code>directEntity</code> to cause damage to the target.
	 * @return new {@link DamageSource} instance.
	 */
	public static DamageSource of(RegistryAccess registryAccess, ResourceKey<DamageType> damageType, @Nullable Entity directEntity, @Nullable Entity causingEntity) {
		return new DamageSource(registryAccess.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageType), directEntity, causingEntity);
	}
}
