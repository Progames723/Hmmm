package dev.progames723.hmmm.mixin.client;

import dev.progames723.hmmm.event.api.Events;
import dev.progames723.hmmm.event.events.client.RenderEvent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin extends AbstractContainerEventHandler implements Renderable {
	
	@Inject(
		method = "render",
		at = @At("HEAD")
	)
	private void preScreenRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
		Events.invokeEvent(new RenderEvent.Screen.Pre((Screen) (Object) this, guiGraphics, mouseX, mouseY, partialTick));
	}
	
	@Inject(
		method = "render",
		at = @At("RETURN")
	)
	private void postScreenRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
		Events.invokeEvent(new RenderEvent.Screen.Post((Screen) (Object) this, guiGraphics, mouseX, mouseY, partialTick));
	}
}
