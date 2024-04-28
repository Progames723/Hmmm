package dev.progames723.hmmm;

import dev.progames723.hmmm.mixin.LivingEntityAccess;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.ApiStatus;

import java.beans.Transient;

public class MinecraftDamageReduction {
	private MinecraftDamageReduction() {}
	/**
	 * Calculates magic damage reduction<p>
	 * Ripped straight from minecraft's code!
	 * @param source {@link DamageSource}
	 * @param entity a {@link LivingEntity}
	 * @param damage {@link Float} damage amount
	 * @return reduced damage(if reduction can be applied)
	 */
	@Deprecated(forRemoval = true)
	public static float getDamageAfterMagicAbsorb(DamageSource source, LivingEntity entity, float damage) {
		if (entity.isInvulnerableTo(source)) {
			return 0;
		}
		if (source.is(DamageTypeTags.BYPASSES_EFFECTS)) {
			return damage;
		} else {
			int k;
			if (entity.hasEffect(MobEffects.DAMAGE_RESISTANCE) && !source.is(DamageTypeTags.BYPASSES_RESISTANCE)) {
				k = (entity.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() + 1) * 5;
				int j = 25 - k;
				float f = damage * (float)j;
				damage = Math.max(f / 25.0F, 0.0F);
			}

			if (damage <= 0.0F) {
				return 0.0F;
			} else if (source.is(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
				return damage;
			} else {
				k = EnchantmentHelper.getDamageProtection(entity.getArmorSlots(), source);
				if (k > 0) {
					damage = CombatRules.getDamageAfterMagicAbsorb(damage, (float)k);
				}
				return damage;
			}
		}
	}

	/**
	 * Calculates armor damage reduction<p>
	 * Ripped straight from minecraft's code!
	 * @param damage {@link Float} damage amount
	 * @param entity a {@link LivingEntity}
	 * @param source {@link DamageSource}
	 * @return reduced damage(if reduction can be applied)
	 */
	@Deprecated(forRemoval = true)
	public static float getDamageAfterArmorAbsorb(float damage, LivingEntity entity, DamageSource source) {
		if (source.is(DamageTypeTags.BYPASSES_ARMOR)) {
			return damage;
		}
		damage = CombatRules.getDamageAfterAbsorb(damage, (float)Math.floor(entity.getAttributeValue(Attributes.ARMOR)), (float)entity.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
		if (source.is(DamageTypeTags.DAMAGES_HELMET)){
			damage *= 0.75f;
		}
		if (entity.isInvulnerableTo(source)) {
			return 0;
		}
		return damage;
	}
	@ApiStatus.Experimental
	public static float getFinalDamageReduction(float damage, DamageSource source, LivingEntity entity) {//good method name
		float unModifiedDamage = damage;//use later if needed
		if (!entity.isInvulnerableTo(source) && !entity.isInvulnerable()) {
			damage = ((LivingEntityAccess) entity).getDamageAfterArmorAbsorb(source, damage);
			damage = ((LivingEntityAccess) entity).getDamageAfterMagicAbsorb(source, damage);
			damage = Math.max(damage - entity.getAbsorptionAmount(), 0.0F);
		} else {
			return 0.0f;
		}
		return damage;//TODO finish this once and for all
	}
}
