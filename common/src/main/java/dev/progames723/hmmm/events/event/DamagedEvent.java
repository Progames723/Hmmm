package dev.progames723.hmmm.events.event;

import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.utils.ReflectUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class DamagedEvent extends Event {
	private final LivingEntity target;
	
	private final Entity entity;
	
	private final Entity directEntity;
	
	private float damage;
	
	public LivingEntity getTarget() {return target;}
	
	public Entity getDirectEntity() {return directEntity;}
	
	public Entity getEntity() {return entity;}
	
	public float getDamage() {
		if (Float.isNaN(damage) || Float.isInfinite(damage)) damage = -1;
		return damage;
	}
	
	public void setDamage(float damage) {
		if (Float.isNaN(damage) || Float.isInfinite(damage)) {
			HmmmLibrary.LOGGER.warn("#setDamage() invoked with NaN or infinite float values! Damage will not be set. Caller Class: {}", ReflectUtil.getCallerClass());
			return;
		}
		this.damage = damage;
	}
	
	DamagedEvent(LivingEntity target, Entity entity, Entity directEntity, float damage) {
		this.target = target;
		this.entity = entity;
		this.directEntity = directEntity;
		this.damage = damage;
	}
	
	@Override
	public boolean isCancellable() {
		return true;
	}
}
