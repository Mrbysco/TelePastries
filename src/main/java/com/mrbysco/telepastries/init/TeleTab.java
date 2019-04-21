package com.mrbysco.telepastries.init;

import com.mrbysco.telepastries.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TeleTab extends CreativeTabs {

    public TeleTab() {
        super(Reference.MOD_ID);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(TeleBlocks.overworld_cake);
    }
}
