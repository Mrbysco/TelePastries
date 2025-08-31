package com.mrbysco.telepastries.init;

import com.mrbysco.telepastries.Reference;
import com.mrbysco.telepastries.blocks.cake.BlockEndCake;
import com.mrbysco.telepastries.blocks.cake.BlockNetherCake;
import com.mrbysco.telepastries.blocks.cake.BlockOverworldCake;
import com.mrbysco.telepastries.blocks.cake.compat.BlockCustomCake;
import com.mrbysco.telepastries.blocks.cake.compat.BlockCustomCake2;
import com.mrbysco.telepastries.blocks.cake.compat.BlockCustomCake3;
import com.mrbysco.telepastries.blocks.cake.compat.BlockLostCityCake;
import com.mrbysco.telepastries.blocks.cake.compat.BlockTwilightCake;
import com.mrbysco.telepastries.item.CakeBlockItem;
import com.mrbysco.telepastries.item.CustomCake2BlockItem;
import com.mrbysco.telepastries.item.CustomCake3BlockItem;
import com.mrbysco.telepastries.item.CustomCakeBlockItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class TeleRegistry {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Reference.MOD_ID);
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MOD_ID);
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Reference.MOD_ID);

	public static final DeferredBlock<BlockNetherCake> NETHER_CAKE = BLOCKS.registerBlock("nether_cake", BlockNetherCake::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE).strength(0.5F).sound(SoundType.WOOL));
	public static final DeferredBlock<BlockEndCake> END_CAKE = BLOCKS.registerBlock("end_cake", BlockEndCake::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE).strength(0.5F).sound(SoundType.WOOL));
	public static final DeferredBlock<BlockOverworldCake> OVERWORLD_CAKE = BLOCKS.registerBlock("overworld_cake", BlockOverworldCake::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE).strength(0.5F).sound(SoundType.WOOL));

	public static final DeferredBlock<BlockTwilightCake> TWILIGHT_CAKE = BLOCKS.registerBlock("twilight_cake", BlockTwilightCake::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE).strength(0.5F).sound(SoundType.WOOL));
	public static final DeferredBlock<BlockLostCityCake> LOST_CITY_CAKE = BLOCKS.registerBlock("lost_city_cake", BlockLostCityCake::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE).strength(0.5F).sound(SoundType.WOOL));
	public static final DeferredBlock<BlockCustomCake> CUSTOM_CAKE = BLOCKS.registerBlock("custom_cake", BlockCustomCake::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE).strength(0.5F).sound(SoundType.WOOL));
	public static final DeferredBlock<BlockCustomCake2> CUSTOM_CAKE2 = BLOCKS.registerBlock("custom_cake2", BlockCustomCake2::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE).strength(0.5F).sound(SoundType.WOOL));
	public static final DeferredBlock<BlockCustomCake3> CUSTOM_CAKE3 = BLOCKS.registerBlock("custom_cake3", BlockCustomCake3::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE).strength(0.5F).sound(SoundType.WOOL));

	public static final DeferredItem<CakeBlockItem> NETHER_CAKE_ITEM = ITEMS.registerItem("nether_cake", (properties) -> new CakeBlockItem(NETHER_CAKE.get(), properties));
	public static final DeferredItem<CakeBlockItem> END_CAKE_ITEM = ITEMS.registerItem("end_cake", (properties) -> new CakeBlockItem(END_CAKE.get(), properties));
	public static final DeferredItem<CakeBlockItem> OVERWORLD_CAKE_ITEM = ITEMS.registerItem("overworld_cake", (properties) -> new CakeBlockItem(OVERWORLD_CAKE.get(), properties));

	public static final DeferredItem<CakeBlockItem> TWILIGHT_CAKE_ITEM = ITEMS.registerItem("twilight_cake", (properties) -> new CakeBlockItem(TWILIGHT_CAKE.get(), properties));
	public static final DeferredItem<CakeBlockItem> LOST_CITY_CAKE_ITEM = ITEMS.registerItem("lost_city_cake", (properties) -> new CakeBlockItem(LOST_CITY_CAKE.get(), properties));
	public static final DeferredItem<CakeBlockItem> CUSTOM_CAKE_ITEM = ITEMS.registerItem("custom_cake", (properties) -> new CustomCakeBlockItem(CUSTOM_CAKE.get(), properties));
	public static final DeferredItem<CakeBlockItem> CUSTOM_CAKE2_ITEM = ITEMS.registerItem("custom_cake2", (properties) -> new CustomCake2BlockItem(CUSTOM_CAKE2.get(), properties));
	public static final DeferredItem<CakeBlockItem> CUSTOM_CAKE3_ITEM = ITEMS.registerItem("custom_cake3", (properties) -> new CustomCake3BlockItem(CUSTOM_CAKE3.get(), properties));

	public static final Supplier<CreativeModeTab> PASTRIES_TAB = CREATIVE_MODE_TABS.register("tab", (properties) -> CreativeModeTab.builder()
			.icon(() -> new ItemStack(TeleRegistry.OVERWORLD_CAKE.get()))
			.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
			.title(Component.translatable("itemGroup.tele_tab"))
			.displayItems((displayParameters, output) -> {
				List<ItemStack> stacks = TeleRegistry.ITEMS.getEntries().stream().map(reg -> new ItemStack(reg.get())).toList();
				output.acceptAll(stacks);
			}).build());
}
