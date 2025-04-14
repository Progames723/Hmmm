package dev.progames723.hmmm.item;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public interface ItemBars {
	void render(GuiGraphics guiGraphics, ItemStack stack, int x, int y);
}
