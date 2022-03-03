package com.mrbysco.telepastries.init;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class TeleTab {
	public static final CreativeModeTab TELE_TAB = new CreativeModeTab("tele_tab") {
		public ItemStack makeIcon() {
			return new ItemStack(TeleRegistry.OVERWORLD_CAKE.get());
		}
	};
}
