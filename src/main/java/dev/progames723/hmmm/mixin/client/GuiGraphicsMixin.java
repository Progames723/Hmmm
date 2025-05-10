package dev.progames723.hmmm.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.progames723.hmmm.interface_injection.GuiGraphicsEnhancement;
import dev.progames723.hmmm.item.ItemBars;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
//? if < 1.21.4
import net.minecraft.util.FastColor;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicInteger;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin implements GuiGraphicsEnhancement {
	@Shadow @Final private PoseStack pose;
	
	//? if < 1.21.4
	@Shadow protected abstract void flushIfUnmanaged();
	
	@Shadow
	@Final
	private MultiBufferSource.BufferSource bufferSource;
	
	@Unique
	private boolean hmmm$isContainerScreenRender = false;
	
	@Unique private int hmmm$imageWidth = 0;
	
	@Unique private int hmmm$imageHeight = 0;
	
	@Shadow public abstract void enableScissor(int minX, int minY, int maxX, int maxY);
	
	@Shadow public abstract void disableScissor();
	
	@Shadow public abstract void fill(RenderType renderType, int minX, int minY, int maxX, int maxY, int color);
	
	@Inject(
		method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
		at = @At(
			value = "INVOKE",
			//? if < 1.21.4
			target = "Lnet/minecraft/world/item/ItemStack;isBarVisible()Z"
			//? if >= 1.21.4
			/*target = "Lnet/minecraft/client/gui/GuiGraphics;renderItemBar(Lnet/minecraft/world/item/ItemStack;II)V"*/
		)
	)
	private void injectionThing(Font font, ItemStack stack, int x, int y, String text, CallbackInfo ci) {
		if (!(stack.getItem() instanceof ItemBars bars)) return;
		int k = x + 2;
		int l = y + 13 - (stack.isBarVisible() ? 2 : 0);
		int xOffset;
		int yOffset;
		boolean renderedSecondBar = false;
		Screen currentScreen = Minecraft.getInstance().screen;
		if (hmmm$isContainerScreenRender && currentScreen != null) {
			hmmm$isContainerScreenRender = false;
			xOffset = (currentScreen.width - hmmm$imageWidth) / 2;
			yOffset = (currentScreen.height - hmmm$imageHeight) / 2;
		} else {
			xOffset = 0;
			yOffset = 0;
		}
		if (bars.shouldRenderSecondGradientBar(stack)) {
			AtomicInteger from = new AtomicInteger();
			AtomicInteger to = new AtomicInteger();
			float fill = bars.renderSecondGradientBar(stack, from, to);
			int i = Math.round(fill * 13);
			fill(RenderType.guiOverlay(), k, l, k + 13, l + 2, 0xFF000000);
			enableScissor(k + xOffset, l + yOffset, k + i + xOffset, l + 1 + yOffset);
			fillHorizontalGradient(RenderType.guiOverlay(), k, l, k + Math.max(i, 13), l + 1, from.get(), to.get(), 0);
			disableScissor();
			renderedSecondBar = true;
		} else if (bars.shouldRenderSecondBar(stack)) {
			AtomicInteger color = new AtomicInteger();
			float fill = bars.renderSecondBar(stack, color);
			int i = Math.round(fill * 13);
			fill(RenderType.guiOverlay(), k, l, k + 13, l + 2, 0xFF000000);
			fill(RenderType.guiOverlay(), k, l, k + i, l + 1, color.get());
			renderedSecondBar = true;
		}
		l -= renderedSecondBar ? 2 : 0;
		if (bars.shouldRenderThirdGradientBar(stack)) {
			AtomicInteger from = new AtomicInteger();
			AtomicInteger to = new AtomicInteger();
			float fill = bars.renderThirdGradientBar(stack, from, to);
			int i = Math.round(fill * 13);
			fill(RenderType.guiOverlay(), k, l, k + 13, l + 2, 0xFF000000);
			enableScissor(k + xOffset, l + yOffset, k + i + xOffset, l + 1 + yOffset);
			fillHorizontalGradient(RenderType.guiOverlay(), k, l, k + Math.max(i, 13), l + 1, from.get(), to.get(), 0);
			disableScissor();
		} else if (bars.shouldRenderThirdBar(stack)) {
			AtomicInteger color = new AtomicInteger();
			float fill = bars.renderThirdBar(stack, color);
			int i = Math.round(fill * 13);
			fill(RenderType.guiOverlay(), k, l, k + 13, l + 2, 0xFF000000);
			fill(RenderType.guiOverlay(), k, l, k + i, l + 1, color.get());
		}
	}
	
	public void fillHorizontalGradient(int x1, int y1, int x2, int y2, int colorFrom, int colorTo) {
		fillHorizontalGradient(x1, y1, x2, y2, 0, colorFrom, colorTo);
	}
	
	public void fillHorizontalGradient(int x1, int y1, int x2, int y2, int z, int colorFrom, int colorTo) {
		fillHorizontalGradient(RenderType.gui(), x1, y1, x2, y2, colorFrom, colorTo, z);
	}
	
	public void fillHorizontalGradient(RenderType renderType, int x1, int y1, int x2, int y2, int colorFrom, int colorTo, int z) {
		fillHorizontalGradient(bufferSource.getBuffer(renderType), x1, y1, x2, y2, z, colorFrom, colorTo);
	}
	
	public void fillHorizontalGradient(VertexConsumer consumer, int x1, int y1, int x2, int y2, int z, int colorFrom, int colorTo) {
		//? if < 1.21.4 {
		float f = (float) FastColor.ARGB32.alpha(colorFrom) / 255.0F;
		float g = (float) FastColor.ARGB32.red(colorFrom) / 255.0F;
		float h = (float) FastColor.ARGB32.green(colorFrom) / 255.0F;
		float i = (float) FastColor.ARGB32.blue(colorFrom) / 255.0F;
		float j = (float) FastColor.ARGB32.alpha(colorTo) / 255.0F;
		float k = (float) FastColor.ARGB32.red(colorTo) / 255.0F;
		float l = (float) FastColor.ARGB32.green(colorTo) / 255.0F;
		float m = (float) FastColor.ARGB32.blue(colorTo) / 255.0F;
		//?}
		Matrix4f matrix4f = pose.last().pose();
		//? if < 1.21.1 {
		consumer.vertex(matrix4f, (float)x1, (float)y1, (float)z).color(k, l, m, j).endVertex();
		consumer.vertex(matrix4f, (float)x1, (float)y2, (float)z).color(k, l, m, j).endVertex();
		consumer.vertex(matrix4f, (float)x2, (float)y2, (float)z).color(g, h, i, f).endVertex();
		consumer.vertex(matrix4f, (float)x2, (float)y1, (float)z).color(g, h, i, f).endVertex();
		//?} else if >= 1.21.4 {
		/*consumer.addVertex(matrix4f, (float)x1, (float)y1, (float)z).setColor(colorTo);
		consumer.addVertex(matrix4f, (float)x1, (float)y2, (float)z).setColor(colorTo);
		consumer.addVertex(matrix4f, (float)x2, (float)y2, (float)z).setColor(colorFrom);
		consumer.addVertex(matrix4f, (float)x2, (float)y1, (float)z).setColor(colorFrom);
		*///?} else {
		/*consumer.addVertex(matrix4f, (float)x1, (float)y1, (float)z).setColor(k, l, m, j);
		consumer.addVertex(matrix4f, (float)x1, (float)y2, (float)z).setColor(k, l, m, j);
		consumer.addVertex(matrix4f, (float)x2, (float)y2, (float)z).setColor(g, h, i, f);
		consumer.addVertex(matrix4f, (float)x2, (float)y1, (float)z).setColor(g, h, i, f);
		*///?}
		//? if < 1.21.4
		this.flushIfUnmanaged();
	}
}
