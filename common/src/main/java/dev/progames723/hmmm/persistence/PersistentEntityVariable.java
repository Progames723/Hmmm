package dev.progames723.hmmm.persistence;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;

import java.util.Random;

//TODO add the ability to store anything
//TODO rewrite this shi
public class PersistentEntityVariable<T extends Tag> {
	private final T baseVariable;
	private final Entity entity;
	private final String name;
	private static final String HmmmPersistent = "HmmmPersistent";
	
	private PersistentEntityVariable(Entity entity, T baseVariable) {
		this.entity = entity;
		this.baseVariable = baseVariable;
		this.name = "PersistentValue_%s".formatted(new Random().nextLong(100000000L));
		attachToEntity();
	}
	
	private PersistentEntityVariable(String name, Entity entity, T baseVariable) {
		this.entity = entity;
		this.baseVariable = baseVariable;
		this.name = name;
		attachToEntity();
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public CompoundTag getAsCompound() {
		CompoundTag tag = new CompoundTag();
		tag.put(name, baseVariable);
		return tag;
	}
	
	private void attachToEntity() {
		CompoundTag tag = entity.saveWithoutId(new CompoundTag());
		tag.putString("id", entity.getEncodeId());
		tag.put(HmmmPersistent, getAsCompound());
	}
	
	@SuppressWarnings("unchecked")
	private T getVariableInEntity() {
		return (T) entity.saveWithoutId(new CompoundTag()).getCompound(HmmmPersistent).get(name);
	}
	
	private void setVariableInEntity(T variable) {
		entity.saveWithoutId(new CompoundTag()).getCompound(HmmmPersistent).put(name, variable);
	}
	
	public static <T extends Tag> PersistentEntityVariable<T> create(Entity entity, T variable) {
		return new PersistentEntityVariable<>(entity, variable);
	}
	
	public static <T extends Tag> PersistentEntityVariable<T> create(String name, Entity entity, T variable) {
		return new PersistentEntityVariable<>(name, entity, variable);
	}
}
