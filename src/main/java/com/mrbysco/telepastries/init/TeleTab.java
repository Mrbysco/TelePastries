package com.mrbysco.telepastries.init;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TeleTab {
    public static final ItemGroup TELE_TAB = new ItemGroup("tele_tab") {
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(TeleRegistry.OVERWORLD_CAKE.get());
        }
    };
}
