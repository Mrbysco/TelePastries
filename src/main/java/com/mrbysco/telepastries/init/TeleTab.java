package com.mrbysco.telepastries.init;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TeleTab {
    public static final CreativeModeTab TELE_TAB = new CreativeModeTab("tele_tab") {
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(TeleRegistry.OVERWORLD_CAKE.get());
        }
    };
}
