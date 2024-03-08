package dev.progames723.hmmm;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class MinecraftDamageReduction {
	private MinecraftDamageReduction() {}
	/**
	 * Calculates magic damage reduction<p>
	 * Ripped straight from minecraft's code!
	 * @param arg {@link DamageSource}
	 * @param entity a {@link LivingEntity}
	 * @param g {@link Float} damage amount
	 * @return reduced damage(if reduction can be applied)
	 */
	public static float getDamageAfterMagicAbsorb(DamageSource arg, LivingEntity entity, float g) {
		if (arg.is(DamageTypeTags.BYPASSES_EFFECTS)) {
			return g;
		} else {
			int k;
			if (entity.hasEffect(MobEffects.DAMAGE_RESISTANCE) && !arg.is(DamageTypeTags.BYPASSES_RESISTANCE)) {
				k = (entity.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() + 1) * 5;
				int j = 25 - k;
				float f = g * (float)j;
				g = Math.max(f / 25.0F, 0.0F);
			}

			if (g <= 0.0F) {
				return 0.0F;
			} else if (arg.is(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
				return g;
			} else {
				k = EnchantmentHelper.getDamageProtection(entity.getArmorSlots(), arg);
				if (k > 0) {
					g = CombatRules.getDamageAfterMagicAbsorb(g, (float)k);
				}
				return g;
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
	public static float getDamageAfterArmorAbsorb(float damage, LivingEntity entity, DamageSource source) {
		if (source.is(DamageTypeTags.BYPASSES_ARMOR)) {
			return damage;
		}
		damage = CombatRules.getDamageAfterAbsorb(damage, (float)Math.floor(entity.getAttributeValue(Attributes.ARMOR)), (float)entity.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
		if (source.is(DamageTypeTags.DAMAGES_HELMET)){
			damage *= 0.75f;
		}
		return damage;
	}
}
