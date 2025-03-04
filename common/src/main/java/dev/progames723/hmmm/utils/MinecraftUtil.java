package dev.progames723.hmmm.utils;

import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.mixin.LivingEntityAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@SuppressWarnings("unused")
public class MinecraftUtil {
	private MinecraftUtil() {MiscUtil.instantiationOfUtilClass(ReflectUtil.CALLER_CLASS.getCallerClass());}
	
	//everything else later
	
	public static class Server {
		private Server() {MiscUtil.instantiationOfUtilClass(ReflectUtil.CALLER_CLASS.getCallerClass());}
		
		/**
		 * really neat on the server-side, huh?
		 * @return {@code null} if the server is not running and if not running locally
		 */
		@SuppressWarnings("JavaReflectionMemberAccess")
		public static MinecraftServer getServerInstance() {
			Field field;
			MinecraftServer instance;
			try {
				field = MinecraftServer.class.getDeclaredField("hmmm$serverInstance");
				field.setAccessible(true);
				instance = (MinecraftServer) field.get(null);
				field.setAccessible(false);
			} catch (Exception e) {
				HmmmLibrary.LOGGER.debug("Exception while getting server instance!", e);
				return null;
			}
			return instance;
		}
	}
	
	public static class DamageReduction {
		private DamageReduction() {MiscUtil.instantiationOfUtilClass(ReflectUtil.CALLER_CLASS.getCallerClass());}
		
		public static float getDamageAfterArmorAbsorb(DamageSource damageSource, float f, LivingEntity entity) {
			//no explanations needed
			return !damageSource.is(DamageTypeTags.BYPASSES_ARMOR) ?
				f * (1.0F - Mth.clamp(
					/*clamped value*/(float) entity.getArmorValue() - f / (2.0F + (float) entity.getAttributeValue(Attributes.ARMOR_TOUGHNESS) / 4.0F),
					/*    min value*/(float) entity.getArmorValue() * 0.2F,
					/*    max value*/20.0F) / 25.0F) :
				f;//maximum damage reduction is 80% btw
		}
		
		public static float getDamageAfterMagicAbsorb(DamageSource damageSource, float f, LivingEntity entity) {
			if (f <= 0.0F) return 0.0F;
			boolean bypassesEffects = damageSource.is(DamageTypeTags.BYPASSES_EFFECTS);
			boolean bypassesResistance = damageSource.is(DamageTypeTags.BYPASSES_RESISTANCE);
			boolean bypassesEnchantments = damageSource.is(DamageTypeTags.BYPASSES_ENCHANTMENTS);
			if (bypassesEffects) return f;
			
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
				return damage > ((LivingEntityAccess) entity).getLastHurt() ?//do i really need a comment for this?
					actuallyHurtDamageReduction(damage - ((LivingEntityAccess) entity).getLastHurt(), source, entity) :
					0.0f;
			return actuallyHurtDamageReduction(damage, source, entity);
		}
		
//		public static float test(float damage, DamageSource source, LivingEntity entity) {
//			if (entity.saveWithoutId(new CompoundTag()).contains("isClone")) return -1;
//			LivingEntity cloned = (LivingEntity) Entities.cloneEntity(entity);
//			float totalHealth = cloned.getHealth() + cloned.getAbsorptionAmount();
//			cloned.hurt(source, damage);
//			float afterHurt = cloned.getHealth() + cloned.getAbsorptionAmount();
//			cloned.discard();
//			return totalHealth - afterHurt;
//		}
	}
	
	public static class Entities {
		public Entities() {throw new RuntimeException();}
		
		public static Entity cloneEntity(Entity entity) {
			if (entity.level().isClientSide) return null;
			ServerLevel level = (ServerLevel) entity.level();//should be true
			CompoundTag nbt = new CompoundTag();
			nbt = entity.saveWithoutId(nbt);
			nbt.putString("id", EntityType.getKey(entity.getType()).toString());
			if (nbt.contains("isClone")) return entity;
			nbt.putBoolean("isClone", true);
			if (nbt.hasUUID("UUID")) nbt.putUUID("UUID", UUID.randomUUID());
			Optional<Entity> optional = EntityType.create(nbt, level);
			if (optional.isEmpty()) throw new RuntimeException("Optional empty!");
			Entity clone = optional.get();
			if (clone instanceof Player p) p.setCustomName(Component.literal("TEMPORARY_PLAYER_" + new Random().nextInt(101)));
			if (clone instanceof LivingEntity e) {
				e.setInvisible(true);
				e.teleportRelative(0, 10000, 0);
			}
			clone.getType().create(entity.level());
			return clone;
		}
	}
}
