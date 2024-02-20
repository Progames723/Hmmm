package dev.progames723.hmmm.fabric;

import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.fabric.event.LivingEvents;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.player.Player;

public class HmmmLibraryFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        HmmmLibrary.init();
        HmmmLibrary.initializeEvents();
        LivingEvents.LIVING_BEFORE_EFFECT_APPLIED.register((level, entity, mobEffectInstance, mobEffect) -> {
            if (entity instanceof Player){
                if (mobEffect.getCategory() == MobEffectCategory.HARMFUL){
                    return LivingEvents.EventLogic.NO;
                } else if (mobEffect.getCategory() == MobEffectCategory.BENEFICIAL) {
                    return LivingEvents.EventLogic.YES;
                }
            }
            return LivingEvents.EventLogic.DEFAULT;
        });
    }
}