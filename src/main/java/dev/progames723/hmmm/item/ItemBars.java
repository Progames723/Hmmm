package dev.progames723.hmmm.item;

import net.minecraft.world.item.ItemStack;

import java.util.concurrent.atomic.AtomicInteger;

public interface ItemBars {
	/**
	 * @implSpec you must set atomic integer colors
	 * @param stack stack being rendered
	 * @param color color to be used, defaults to 0(black, transparent)
	 * @return amount of bar to render on a 0 to 1 scale
	 */
	default float renderSecondBar(ItemStack stack, AtomicInteger color) {
		return 0.0f;
	}
	
	/**
	 * @implSpec you must set atomic integer colors
	 * @param stack stack being rendered
	 * @param gradientColorFrom color to be used, defaults to 0(black, transparent)
	 * @param gradientColorTo color to be used, defaults to 0(black, transparent)
	 * @return amount of bar to render on a 0 to 1 scale
	 */
	default float renderSecondGradientBar(ItemStack stack, AtomicInteger gradientColorFrom, AtomicInteger gradientColorTo) {
		return 0.0f;
	}
	
	/**
	 * uses {@link ItemBars#renderSecondBar(ItemStack, AtomicInteger)}
	 * @param stack item stack being rendered
	 * @return if should render a bar
	 */
	default boolean shouldRenderSecondBar(ItemStack stack) {
		return false;
	}
	
	/**
	 * takes priority over {@link ItemBars#shouldRenderSecondBar(ItemStack)},<p>
	 * uses {@link ItemBars#renderSecondGradientBar(ItemStack, AtomicInteger, AtomicInteger)}
	 * @param stack item stack being rendered
	 * @return if should render a gradient bar
	 */
	default boolean shouldRenderSecondGradientBar(ItemStack stack) {
		return false;
	}
	
	/**
	 * @implSpec you must set atomic integer colors
	 * @param stack stack being rendered
	 * @param color color to be used, defaults to 0(black, transparent)
	 * @return amount of bar to render on a 0 to 1 scale
	 */
	default float renderThirdBar(ItemStack stack, AtomicInteger color) {
		return 0.0f;
	}
	
	/**
	 * @implSpec you must set atomic integer colors
	 * @param stack stack being rendered
	 * @param gradientColorFrom color to be used, defaults to 0(black, transparent)
	 * @param gradientColorTo color to be used, defaults to 0(black, transparent)
	 * @return amount of bar to render on a 0 to 1 scale
	 */
	default float renderThirdGradientBar(ItemStack stack, AtomicInteger gradientColorFrom, AtomicInteger gradientColorTo) {
		return 0.0f;
	}
	
	/**
	 * uses {@link ItemBars#renderThirdBar(ItemStack, AtomicInteger)}
	 * @param stack item stack being rendered
	 * @return if should render a bar
	 */
	default boolean shouldRenderThirdBar(ItemStack stack) {
		return false;
	}
	
	/**
	 * takes priority over {@link ItemBars#shouldRenderThirdBar(ItemStack)},<p>
	 * uses {@link ItemBars#renderThirdGradientBar(ItemStack, AtomicInteger, AtomicInteger)}
	 * @param stack item stack being rendered
	 * @return if should render a gradient bar
	 */
	default boolean shouldRenderThirdGradientBar(ItemStack stack) {
		return false;
	}
}
