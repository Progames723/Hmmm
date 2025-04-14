package dev.progames723.hmmm.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;

public class ItemBarRenderUtil {
	private ItemBarRenderUtil() {MiscUtil.instantiationOfUtilClass();}
	
	public static void renderBar(GuiGraphics guiGraphics, float fillRatio, int xOffset, int yOffset, int z, int color) {
		RenderSystem.disableBlend();
		
		int i = Math.round(13.0f - fillRatio * 13.0f);
		int x = xOffset + 2;
		int y = yOffset + 12;
		
		guiGraphics.fill(RenderType.guiOverlay(), x, y, x + i, y + 1, z + 190, color);
		
		RenderSystem.enableBlend();
	}
	
	public static void renderGradientBar(GuiGraphics guiGraphics, float fillRatio, int xOffset, int yOffset, int z, int colorFrom, int colorTo) {
		RenderSystem.disableBlend();
		
		int i = Math.round(13.0f - fillRatio * 13.0f);
		int x = xOffset + 2;
		int y = yOffset + 12;
		
		guiGraphics.fillGradient(RenderType.guiOverlay(), x, y, x + i, y + 1, z + 190, colorFrom, colorTo);
		
		RenderSystem.enableBlend();
	}
}
