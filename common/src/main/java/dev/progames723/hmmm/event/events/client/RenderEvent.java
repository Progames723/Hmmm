package dev.progames723.hmmm.event.events.client;

import com.mojang.blaze3d.platform.Window;
import dev.progames723.hmmm.event.api.Event;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

@Environment(EnvType.CLIENT)
public sealed abstract class RenderEvent extends Event {
	private final GuiGraphics guiGraphics;
	private final float partialTick;
	
	protected RenderEvent(GuiGraphics guiGraphics, float partialTick) {
		super(false);
		this.guiGraphics = guiGraphics;
		this.partialTick = partialTick;
	}
	
	public abstract boolean isPreRender();
	
	public final GuiGraphics getGuiGraphics() {
		return guiGraphics;
	}
	
	public final float getPartialTick() {
		return partialTick;
	}
	
	public sealed abstract static class Screen extends RenderEvent {
		private final net.minecraft.client.gui.screens.Screen screen;
		private final int mouseX, mouseY;
		
		protected Screen(net.minecraft.client.gui.screens.Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
			super( guiGraphics, partialTick);
			this.screen = screen;
			this.mouseX = mouseX;
			this.mouseY = mouseY;
		}
		
		public final net.minecraft.client.gui.screens.Screen getScreen() {
			return screen;
		}
		
		public final int getMouseX() {
			return mouseX;
		}
		
		public final int getMouseY() {
			return mouseY;
		}
		
		public static final class Pre extends Screen {
			public Pre(net.minecraft.client.gui.screens.Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
				super(screen, guiGraphics, mouseX, mouseY, partialTick);
			}
			
			@Override
			public boolean isPreRender() {
				return true;
			}
		}
		
		public static final class Post extends Screen {
			public Post(net.minecraft.client.gui.screens.Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
				super(screen, guiGraphics, mouseX, mouseY, partialTick);
			}
			
			@Override
			public boolean isPreRender() {
				return false;
			}
		}
	}
	
	public sealed abstract static class Gui extends RenderEvent {
		private final Window window;
		private final Font font;
		
		protected Gui(Window window, Font font, GuiGraphics guiGraphics, float partialTick) {
			super(guiGraphics, partialTick);
			this.window = window;
			this.font = font;
		}
		
		public final Window getWindow() {
			return window;
		}
		
		public final Font getFont() {
			return font;
		}
		
		public static final class Pre extends Gui {
			public Pre(Window window, Font font, GuiGraphics guiGraphics, float partialTick) {
				super(window, font, guiGraphics, partialTick);
			}
			
			@Override
			public boolean isPreRender() {
				return true;
			}
		}
		
		public static final class Post extends Gui {
			public Post(Window window, Font font, GuiGraphics guiGraphics, float partialTick) {
				super(window, font, guiGraphics, partialTick);
			}
			
			@Override
			public boolean isPreRender() {
				return false;
			}
		}
	}
}
