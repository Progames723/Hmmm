package dev.progames723.hmmm.mixin.client;

import dev.progames723.hmmm.item.ItemBars;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {
	@Inject(
		method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/Minecraft;player:Lnet/minecraft/client/player/LocalPlayer;"
		)
	)
	private void injectionThing(Font font, ItemStack stack, int x, int y, String text, CallbackInfo ci) {
		if (stack.getItem() instanceof ItemBars bars) bars.render((GuiGraphics) (Object) this, stack, x, y);
	}
}
