package com.mrbysco.telepastries.init;

import com.mrbysco.telepastries.Reference;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class TeleTab {
	private static CreativeModeTab TELE_TAB;

	@SubscribeEvent
	public void registerCreativeTabs(final CreativeModeTabEvent.Register event) {
		TELE_TAB = event.registerCreativeModeTab(new ResourceLocation(Reference.MOD_ID, "tab"), builder ->
				builder.icon(() -> new ItemStack(TeleRegistry.OVERWORLD_CAKE.get()))
						.title(Component.translatable("itemGroup.tele_tab"))
						.displayItems((displayParameters, output) -> {
							List<ItemStack> stacks = TeleRegistry.ITEMS.getEntries().stream().map(reg -> new ItemStack(reg.get())).toList();
							output.acceptAll(stacks);
						}));
	}
}
