package dev.progames723.hmmm;

import dev.progames723.hmmm.mixin.LivingEntityAccess;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.ApiStatus;

public class MinecraftDamageReduction {
	private MinecraftDamageReduction() {}
	
	private static float actuallyHurtDamageReduction(float damage, DamageSource source, LivingEntity entity) {
		if (!source.is(DamageTypeTags.BYPASSES_ARMOR)) {
			damage = CombatRules.getDamageAfterAbsorb(damage, (float)entity.getArmorValue(), (float)entity.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
		}
		if (source.is(DamageTypeTags.BYPASSES_EFFECTS)) {
			damage = Math.max(damage - entity.getAbsorptionAmount(), 0.0F);
			return damage;
		} else {
			int i;
			if (entity.hasEffect(MobEffects.DAMAGE_RESISTANCE) && !source.is(DamageTypeTags.BYPASSES_RESISTANCE)) {
				i = (entity.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() + 1) * 5;
				int j = 25 - i;
				float g = damage * (float)j;
				damage = Math.max(g / 25.0F, 0.0F);
			}
			if (damage <= 0.0F) {
				return 0.0F;
			} else {
				i = EnchantmentHelper.getDamageProtection(entity.getArmorSlots(), source);
				if (i > 0) {
					damage = CombatRules.getDamageAfterMagicAbsorb(damage, (float)i);
				}
			}
		}
		damage = Math.max(damage - entity.getAbsorptionAmount(), 0.0F);
		return damage;
	}
	
	@ApiStatus.Experimental
	public static float getFinalDamageReduction(float damage, DamageSource source, LivingEntity entity) {//good method name
		if (!entity.isInvulnerableTo(source) && !entity.isInvulnerable() && !entity.isDeadOrDying()) {
			if (source.is(DamageTypeTags.IS_FIRE) && entity.hasEffect(MobEffects.FIRE_RESISTANCE)) {
				return 0.0f;
			}
			if (source.is(DamageTypeTags.IS_FREEZING) && entity.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
				damage *= 5.0f;
			}
			if (source.is(DamageTypeTags.DAMAGES_HELMET) && !entity.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
				damage *= 0.75f;
			}
			if (entity.invulnerableTime > 10.0f && !source.is(DamageTypeTags.BYPASSES_COOLDOWN)) {
				if (damage <= ((LivingEntityAccess) entity).getLastHurt()) {
					return 0.0f;
				}
				damage = actuallyHurtDamageReduction(damage - ((LivingEntityAccess) entity).getLastHurt(), source, entity);
			} else {
				damage = actuallyHurtDamageReduction(damage, source, entity);
			}
		} else {
			return 0.0f;
		}
		return damage;
	}
}
