package dev.progames723.hmmm.interface_injection;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.progames723.hmmm.HmmmException;
import dev.progames723.hmmm.annotations.Temporary;
import net.minecraft.client.renderer.RenderType;

//$ clientOnly
@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public interface GuiGraphicsEnhancement {
	default void fillHorizontalGradient(int x1, int y1, int x2, int y2, int colorFrom, int colorTo) {
		throw new HmmmException(null, "Not transformed!");
	}
	
	default void fillHorizontalGradient(int x1, int y1, int x2, int y2, int z, int colorFrom, int colorTo) {
		throw new HmmmException(null, "Not transformed!");
	}
	
	default void fillHorizontalGradient(RenderType renderType, int x1, int y1, int x2, int y2, int colorFrom, int colorTo, int z) {
		throw new HmmmException(null, "Not transformed!");
	}
	
	default void fillHorizontalGradient(VertexConsumer consumer, int x1, int y1, int x2, int y2, int z, int colorFrom, int colorTo) {
		throw new HmmmException(null, "Not transformed!");
	}
}
