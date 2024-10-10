package dev.progames723.hmmm.event.events;

import dev.progames723.hmmm.event.api.ReturnableEvent;
import dev.progames723.hmmm.event.utils.DoubleValue;
import dev.progames723.hmmm.event.utils.QuadrupleValue;
import dev.progames723.hmmm.event.utils.TripleValue;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class LivingEntityEvent<T> extends ReturnableEvent<T> {
	@Nullable private final LivingEntity entity;
	
	public LivingEntityEvent(boolean isVoid, @Nullable T value, @Nullable LivingEntity entity) {
		super(isVoid, true, true);
		this.value = value;
		this.entity = entity;
	}
	
	public LivingEntityEvent(boolean isCancellable, boolean isVoid, @Nullable T value, @Nullable LivingEntity entity) {
		super(isVoid, isCancellable, true);
		this.value = value;
		this.entity = entity;
	}
	
	@Override
	public boolean returnsNull() {
		if (value == null) return true;
		if (value instanceof QuadrupleValue<?,?,?,?> q) return q.getA() == null || q.getB() == null || q.getC() == null || q.getD() == null;
		if (value instanceof TripleValue<?,?,?> t) return t.getA() == null || t.getB() == null || t.getC() == null;
		if (value instanceof DoubleValue<?,?> d) return d.getA() == null || d.getB() == null;
		return false;
	}
	
	@Nullable
	public LivingEntity getEntity() {
		return entity;
	}
}
