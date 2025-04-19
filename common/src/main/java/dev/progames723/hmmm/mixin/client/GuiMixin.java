package dev.progames723.hmmm.mixin.client;

import dev.progames723.hmmm.event.api.Events;
import dev.progames723.hmmm.event.events.client.RenderEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {
	@Shadow public abstract Font getFont();
	
	@Shadow @Final private Minecraft minecraft;
	
	@Inject(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/Gui;getFont()Lnet/minecraft/client/gui/Font;"
		)
	)
	private void earlyRenderInject(GuiGraphics guiGraphics, float partialTick, CallbackInfo ci) {
		Events.invokeEvent(new RenderEvent.Gui.Pre(minecraft.getWindow(), getFont(), guiGraphics, partialTick));
	}
	
	@Inject(
		method = "render",
		at = @At(
			value = "RETURN"
		)
	)
	private void lateRenderInject(GuiGraphics guiGraphics, float partialTick, CallbackInfo ci) {
		Events.invokeEvent(new RenderEvent.Gui.Post(minecraft.getWindow(), getFont(), guiGraphics, partialTick));
	}
}
