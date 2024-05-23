package dev.progames723.hmmm;

import dev.architectury.platform.Platform;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class HmmmRegistries {
	public static MobEffect registerEffect(String modid, String name, MobEffect obj, @Nullable Logger logger) {
		if (!Platform.isForgeLike()){
			if (logger != null) {
				logger.info("Registered effect: {}", name);
			}
			return Registry.register(BuiltInRegistries.MOB_EFFECT, new ResourceLocation(modid, name), obj);
		}
		throw new RuntimeException("Fabric and Quilt only!");
	}

	public static Item registerItem(String modid, String name, Item obj, @Nullable Logger logger) {
		if (!Platform.isForgeLike()){
			if (logger != null) {
				logger.info("Registered item: {}", name);
			}
			return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(modid, name), obj);
		}
		throw new RuntimeException("Fabric and Quilt only!");
	}

	public static Enchantment registerEnchantment(String modid, String name, Enchantment obj, @Nullable Logger logger) {
		if (!Platform.isForgeLike()){
			if (logger != null) {
				logger.info("Registered enchantment: {}", name);
			}
			return Registry.register(BuiltInRegistries.ENCHANTMENT, new ResourceLocation(modid, name), obj);
		}
		throw new RuntimeException("Fabric and Quilt only!");
	}

	public static Attribute registerAttribute(String modid, String name, Attribute obj, @Nullable Logger logger) {
		if (!Platform.isForgeLike()){
			if (logger != null) {
				logger.info("Registered attribute: {}", name);
			}
			return Registry.register(BuiltInRegistries.ATTRIBUTE, new ResourceLocation(modid, name), obj);
		}
		throw new RuntimeException("Fabric and Quilt only!");
	}
	public static Block registerBlock(String modid, String name, Block obj, @Nullable Logger logger) {
		if (!Platform.isForgeLike()){
			if (logger != null) {
				logger.info("Registered block: {}", name);
			}
			return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(modid, name), obj);
		}
		throw new RuntimeException("Fabric and Quilt only!");
	}

	public static ResourceKey<DamageType> registerDamageType(String modid, String name, @Nullable Logger logger) {
		if (logger != null){
			logger.info("Registered damage type: {}", name);
		}
		return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(modid, name));
	}
}
