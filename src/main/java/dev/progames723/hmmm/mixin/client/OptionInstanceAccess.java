package dev.progames723.hmmm.mixin.client;

import dev.progames723.hmmm.annotations.Temporary;
import net.minecraft.client.OptionInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//$ clientOnly
@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
@Mixin(OptionInstance.class)
public interface OptionInstanceAccess<T> {//idk why but yes
	@Accessor
	T getInitialValue();
	
	@Accessor
	void setValue(T value);
}
