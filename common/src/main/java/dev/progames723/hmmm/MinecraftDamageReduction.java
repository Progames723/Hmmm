package dev.progames723.hmmm;

import dev.progames723.hmmm.mixin.LivingEntityAccess;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.ApiStatus;

public class MinecraftDamageReduction {
	private MinecraftDamageReduction() {}
	
	private static float getDamageAfterArmorAbsorb(DamageSource damageSource, float f, LivingEntity entity) {
		if (!damageSource.is(DamageTypeTags.BYPASSES_ARMOR)) {
			HmmmLibrary.LOGGER.info("damage source doesnt bypass armor");
			float i = 2.0F + (float)entity.getAttributeValue(Attributes.ARMOR_TOUGHNESS) / 4.0F;
			float j = Mth.clamp((float)entity.getArmorValue() - f / i, (float)entity.getArmorValue() * 0.2F, 20.0F);
			f = f * (1.0F - j / 25.0F);
		}
		HmmmLibrary.LOGGER.info("getDamageAfterArmorAbsorb returned {}", f);
		return f;
	}
	
	private static float getDamageAfterMagicAbsorb(DamageSource damageSource, float f, LivingEntity entity) {
		if (damageSource.is(DamageTypeTags.BYPASSES_EFFECTS)) {
			HmmmLibrary.LOGGER.info("damage source doesnt bypasses magic, returned {}", f);
			return f;
		} else {
			if (entity.hasEffect(MobEffects.DAMAGE_RESISTANCE) && !damageSource.is(DamageTypeTags.BYPASSES_RESISTANCE)) {
				HmmmLibrary.LOGGER.info("call to entity.getEffect");
				int i = (entity.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() + 1) * 5;
				HmmmLibrary.LOGGER.info("resistance damage reduction");
				int j = 25 - i;
				HmmmLibrary.LOGGER.info("resistance damage reduction 2");
				float g = f * (float)j;
				HmmmLibrary.LOGGER.info("resistance damage reduction 3");
				f = Math.max(g / 25.0F, 0.0F);
				
			}
			if (f <= 0.0F) {
				f = 0.0f;
				HmmmLibrary.LOGGER.info("getDamageAfterMagicAbsorb returned {}", f);
				return 0.0F;
			} else if (damageSource.is(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
				HmmmLibrary.LOGGER.info("damage source doesnt bypasses enchantments, returned {}", f);
				return f;
			} else {
				HmmmLibrary.LOGGER.info("call to EnchantmentHelper.getDamageProtection");
				int i = EnchantmentHelper.getDamageProtection(entity.getArmorSlots(), damageSource);
				if (i > 0) {
					HmmmLibrary.LOGGER.info("replacement of CombatRules.getDamageAfterMagicAbsorb");
					float h = Mth.clamp((float)i, 0.0F, 20.0F);
					f = f * (1.0F - h / 25.0F);
				}
				HmmmLibrary.LOGGER.info("getDamageAfterMagicAbsorb returned {}", f);
				return f;
			}
		}
	}
	
	private static float actuallyHurtDamageReduction(float damage, DamageSource source, LivingEntity entity) {
		HmmmLibrary.LOGGER.info("getDamageAfterArmorAbsorb Call");
		damage = getDamageAfterArmorAbsorb(source, damage, entity);
		HmmmLibrary.LOGGER.info("getDamageAfterMagicAbsorb Call");
		damage = getDamageAfterMagicAbsorb(source, damage, entity);
		HmmmLibrary.LOGGER.info("possibly reducing damage by absorption");
		damage = Math.max(damage - entity.getAbsorptionAmount(), 0.0F);
		HmmmLibrary.LOGGER.info("actuallyHurtDamageReduction returned {}", damage);
		return damage;
	}
	
	@ApiStatus.Experimental
	public static float damageReduction(float damage, DamageSource source, LivingEntity entity) {//good method name
		HmmmLibrary.LOGGER.info("damageReduction invoked");
		if (!entity.isInvulnerableTo(source) && !entity.isInvulnerable() && !entity.isDeadOrDying()) {
			if (source.is(DamageTypeTags.IS_FIRE) && entity.hasEffect(MobEffects.FIRE_RESISTANCE)) {
				damage = 0.0f;
				HmmmLibrary.LOGGER.info("damageReduction returned {}", damage);//logging 👍
				return damage;
			}
			if (source.is(DamageTypeTags.IS_FREEZING) && entity.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
				HmmmLibrary.LOGGER.info("damage * 5");
				damage *= 5.0f;
			}
			if (source.is(DamageTypeTags.DAMAGES_HELMET) && entity.hasItemInSlot(EquipmentSlot.HEAD)) {
				HmmmLibrary.LOGGER.info("damage * 0.75");
				damage *= 0.75f;
			}
			if (entity.invulnerableTime > 10.0f && !source.is(DamageTypeTags.BYPASSES_COOLDOWN)) {
				if (damage <= ((LivingEntityAccess) entity).getLastHurt()) {
					damage = 0.0f;
					HmmmLibrary.LOGGER.info("damageReduction returned {}", damage);
					return damage;
				}
				HmmmLibrary.LOGGER.info("actuallyHurtDamageReduction Call");
				damage = actuallyHurtDamageReduction(damage - ((LivingEntityAccess) entity).getLastHurt(), source, entity);
			} else {
				HmmmLibrary.LOGGER.info("actuallyHurtDamageReduction Call");
				damage = actuallyHurtDamageReduction(damage, source, entity);
			}
		} else {
			damage = 0.0f;
			HmmmLibrary.LOGGER.info("damageReduction returned {}", damage);
			return damage;
		}
		HmmmLibrary.LOGGER.info("damageReduction returned {}", damage);
		return damage;
	}
}
