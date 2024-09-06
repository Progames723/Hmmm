package dev.progames723.hmmm.events.test;

import dev.progames723.hmmm.events.annotations.EventListener;
import dev.progames723.hmmm.events.annotations.EventListenerClass;
import dev.progames723.hmmm.events.event.DamagedEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

@EventListenerClass
public class ExampleEvent {
	@EventListener()
	public void livingDamaged(DamagedEvent e) {
		if (e.getTarget() instanceof Player player)
			player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 30, 1, false, false, false));
		else
			e.getTarget().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 1, false, false, false));
		e.setDamage(e.getDamage() / 2);
	}
}
