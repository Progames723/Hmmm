package dev.progames723.hmmm.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityAccess {
	@Accessor
	float getLastHurt();
	
	@Invoker("getDamageAfterArmorAbsorb")
	float getDamageAfterArmorAbsorb(DamageSource damageSource, float f);
	
	@Invoker("getDamageAfterMagicAbsorb")
	float getDamageAfterMagicAbsorb(DamageSource damageSource, float f);
}
