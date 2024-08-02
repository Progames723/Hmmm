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
    
    public static class DamageReduction {
        private DamageReduction() {throw new RuntimeException();}
        
        public static float getDamageAfterArmorAbsorb(DamageSource damageSource, float f, LivingEntity entity) {
            return damageSource.is(DamageTypeTags.BYPASSES_ARMOR)
            ? f //no explanations needed
            : f * (1.0F - Mth.clamp(
            /*clamped value*/(float) entity.getArmorValue() - f / (2.0F + (float) entity.getAttributeValue(Attributes.ARMOR_TOUGHNESS) / 4.0F),
            /*    min value*/(float) entity.getArmorValue() * 0.2F,
            /*    max value*/20.0F) / 25.0F);//maximum damage reduction is 80% btw
        }
        
        public static float getDamageAfterMagicAbsorb(DamageSource damageSource, float f, LivingEntity entity) {
            if (f <= 0.0F) return 0.0F;
            boolean bypassesEffects = damageSource.is(DamageTypeTags.BYPASSES_EFFECTS);
            boolean bypassesResistance = damageSource.is(DamageTypeTags.BYPASSES_RESISTANCE);
            boolean bypassesEnchantments = damageSource.is(DamageTypeTags.BYPASSES_ENCHANTMENTS);
            if (!bypassesEffects) {
                if (!bypassesResistance && entity.hasEffect(MobEffects.DAMAGE_RESISTANCE)) {
                    MobEffectInstance instance = entity.getEffect(MobEffects.DAMAGE_RESISTANCE);
                    assert instance != null;
                    int j = (5 - (instance.getAmplifier() + 1)) * 5;
                    float g = f * (float)j;
                    f = Math.max(g / 25.0F, 0.0F);
                }
                
                if (!bypassesEnchantments) {
                    int i = EnchantmentHelper.getDamageProtection(entity.getArmorSlots(), damageSource);
                    f *= (1.0F - Mth.clamp((float) i, 0.0F, 20.0F) / 25.0F);
                    
                }
            }
            return f;
        }
        
        public static float actuallyHurtDamageReduction(float damage, DamageSource source, LivingEntity entity) {
            damage = getDamageAfterArmorAbsorb(source, damage, entity);
            damage = getDamageAfterMagicAbsorb(source, damage, entity);
            damage = Math.max(damage - entity.getAbsorptionAmount(), 0.0F);
            return damage;
        }
        
        public static float calculate(float damage, DamageSource source, LivingEntity entity) {//good method name
            if (entity.isInvulnerableTo(source) || entity.isInvulnerable() || entity.isDeadOrDying()) return 0;
            
            boolean fireDamageAndHasFireResistance = source.is(DamageTypeTags.IS_FIRE) && entity.hasEffect(MobEffects.FIRE_RESISTANCE);
            boolean extraFreezingDamage = source.is(DamageTypeTags.IS_FREEZING) && entity.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES);
            boolean damagesHelmetAndHasHelmet = source.is(DamageTypeTags.DAMAGES_HELMET) && entity.hasItemInSlot(EquipmentSlot.HEAD);
            boolean hasIFrames = entity.invulnerableTime > 10.0f && !source.is(DamageTypeTags.BYPASSES_COOLDOWN);
            
            if (fireDamageAndHasFireResistance) return 0.0f;
            if (extraFreezingDamage) damage *= 5.0f;
            if (damagesHelmetAndHasHelmet) damage *= 0.75f;
            
            if (hasIFrames)
                return damage > ((LivingEntityAccess) entity).getLastHurt() ?
                        actuallyHurtDamageReduction(damage - ((LivingEntityAccess) entity).getLastHurt(), source, entity) :
                        0.0f;
            return actuallyHurtDamageReduction(damage, source, entity);
        }
    }
}
