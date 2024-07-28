package dev.progames723.hmmm.utils;

import dev.progames723.hmmm.mixin.LivingEntityAccess;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

@SuppressWarnings("unused")
public class MinecraftUtil {
	private MinecraftUtil() {throw new RuntimeException();}
	
	//everything else later
	
	public static class DamageReduciton {
		private DamageReduciton() {throw new RuntimeException();}
		
		public static float getDamageAfterArmorAbsorb(DamageSource damageSource, float f, LivingEntity entity) {
			if (!damageSource.is(DamageTypeTags.BYPASSES_ARMOR)) {
				float i = 2.0F + (float)entity.getAttributeValue(Attributes.ARMOR_TOUGHNESS) / 4.0F;
				float j = Mth.clamp((float)entity.getArmorValue() - f / i, (float)entity.getArmorValue() * 0.2F, 20.0F);
				f = f * (1.0F - j / 25.0F);
			}
			return f;
		}
		
		public static float getDamageAfterMagicAbsorb(DamageSource damageSource, float f, LivingEntity entity) {
			if (damageSource.is(DamageTypeTags.BYPASSES_EFFECTS)) {
				return f;
			} else {
				if (entity.hasEffect(MobEffects.DAMAGE_RESISTANCE) && !damageSource.is(DamageTypeTags.BYPASSES_RESISTANCE)) {
					MobEffectInstance instance = entity.getEffect(MobEffects.DAMAGE_RESISTANCE);
					assert instance != null;
					int i = (instance.getAmplifier() + 1) * 5;
					int j = 25 - i;
					float g = f * (float)j;
					f = Math.max(g / 25.0F, 0.0F);
					
				}
				if (f <= 0.0F) {
					return 0.0F;
				} else if (damageSource.is(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
					return f;
				} else {
					int i = EnchantmentHelper.getDamageProtection(entity.getArmorSlots(), damageSource);
					if (i > 0) {
						float h = Mth.clamp((float)i, 0.0F, 20.0F);
						f = f * (1.0F - h / 25.0F);
					}
					return f;
				}
			}
		}
		
		public static float actuallyHurtDamageReduction(float damage, DamageSource source, LivingEntity entity) {
			damage = getDamageAfterArmorAbsorb(source, damage, entity);
			damage = getDamageAfterMagicAbsorb(source, damage, entity);
			damage = Math.max(damage - entity.getAbsorptionAmount(), 0.0F);
			return damage;
		}
		
		public static float calculate(float damage, DamageSource source, LivingEntity entity) {//good method name
			if (!entity.isInvulnerableTo(source) && !entity.isInvulnerable() && !entity.isDeadOrDying()) {
				if (source.is(DamageTypeTags.IS_FIRE) && entity.hasEffect(MobEffects.FIRE_RESISTANCE)) return 0.0f;
				if (source.is(DamageTypeTags.IS_FREEZING) && entity.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) damage *= 5.0f;
				if (source.is(DamageTypeTags.DAMAGES_HELMET) && entity.hasItemInSlot(EquipmentSlot.HEAD)) damage *= 0.75f;
				
				if (entity.invulnerableTime > 10.0f && !source.is(DamageTypeTags.BYPASSES_COOLDOWN)) {
					if (damage <= ((LivingEntityAccess) entity).getLastHurt()) return 0.0f;
					damage = actuallyHurtDamageReduction(damage - ((LivingEntityAccess) entity).getLastHurt(), source, entity);
				} else {
					damage = actuallyHurtDamageReduction(damage, source, entity);
				}
			} else return 0.0f;
			return damage;
		}
	}
}
