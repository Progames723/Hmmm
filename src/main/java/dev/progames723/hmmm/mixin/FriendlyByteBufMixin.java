package dev.progames723.hmmm.mixin;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FriendlyByteBuf.class)
public abstract class FriendlyByteBufMixin extends ByteBuf {
	@ModifyVariable(method = "readVarIntArray(I)[I", at = @At("HEAD"), argsOnly = true)
	private int modifyMaxSize(int value) {
		return Integer.MAX_VALUE - 8;//max array length
	}
}
