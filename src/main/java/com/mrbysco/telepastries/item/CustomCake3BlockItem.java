package com.mrbysco.telepastries.item;

import com.mrbysco.telepastries.config.TeleConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class CustomCake3BlockItem extends CakeBlockItem {
	public CustomCake3BlockItem(Block blockIn, Properties builder) {
		super(blockIn, builder);
	}

	@Override
	public Component getName(ItemStack stack) {
		return new TranslatableComponent(this.getDescriptionId(stack), TeleConfig.COMMON.customCake3Name.get());
	}
}
