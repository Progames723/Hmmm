package dev.progames723.hmmm.mixin.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {
	@Shadow protected int imageWidth;
	
	@Shadow protected int imageHeight;
	
	@Inject(method = "render", at = @At("HEAD"))
	private void preRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
		Field field;
		try {
			//yes
			field = GuiGraphics.class.getDeclaredField("hmmm$isContainerScreenRender");
			field.setAccessible(true);
			field.setBoolean(guiGraphics, true);
			field.setAccessible(false);
			//width
			field = GuiGraphics.class.getDeclaredField("hmmm$imageWidth");
			field.setAccessible(true);
			field.setInt(guiGraphics, imageWidth);
			field.setAccessible(false);
			//height
			field = GuiGraphics.class.getDeclaredField("hmmm$imageHeight");
			field.setAccessible(true);
			field.setInt(guiGraphics, imageHeight);
			field.setAccessible(false);
		} catch (Exception e) {
			//impossible
		}
	}
}
