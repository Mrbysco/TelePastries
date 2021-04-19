package com.mrbysco.telepastries.item;

import com.mrbysco.telepastries.config.TeleConfig;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class CustomCakeBlockItem extends BlockItem {
	public CustomCakeBlockItem(Block blockIn, Properties builder) {
		super(blockIn, builder);
	}

	@Override
	public ITextComponent getDisplayName(ItemStack stack) {
		return new TranslationTextComponent(this.getTranslationKey(stack), TeleConfig.SERVER.customCakeName.get());
	}
}
