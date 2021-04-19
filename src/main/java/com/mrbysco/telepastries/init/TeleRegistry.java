package com.mrbysco.telepastries.init;

import com.mrbysco.telepastries.Reference;
import com.mrbysco.telepastries.blocks.cake.BlockEndCake;
import com.mrbysco.telepastries.blocks.cake.BlockNetherCake;
import com.mrbysco.telepastries.blocks.cake.BlockOverworldCake;
import com.mrbysco.telepastries.blocks.cake.compat.BlockCustomCake;
import com.mrbysco.telepastries.blocks.cake.compat.BlockLostCityCake;
import com.mrbysco.telepastries.blocks.cake.compat.BlockTwilightCake;
import com.mrbysco.telepastries.item.CustomCakeBlockItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TeleRegistry {
   public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);

   public static final RegistryObject<Block> NETHER_CAKE = BLOCKS.register("nether_cake", () -> new BlockNetherCake(AbstractBlock.Properties.create(Material.CAKE).hardnessAndResistance(0.5F).sound(SoundType.CLOTH)));
   public static final RegistryObject<Block> END_CAKE = BLOCKS.register("end_cake", () -> new BlockEndCake(AbstractBlock.Properties.create(Material.CAKE).hardnessAndResistance(0.5F).sound(SoundType.CLOTH)));
   public static final RegistryObject<Block> OVERWORLD_CAKE = BLOCKS.register("overworld_cake", () -> new BlockOverworldCake(AbstractBlock.Properties.create(Material.CAKE).hardnessAndResistance(0.5F).sound(SoundType.CLOTH)));

   public static final RegistryObject<Block> TWILIGHT_CAKE = BLOCKS.register("twilight_cake", () -> new BlockTwilightCake(AbstractBlock.Properties.create(Material.CAKE).hardnessAndResistance(0.5F).sound(SoundType.CLOTH)));
   public static final RegistryObject<Block> LOST_CITY_CAKE = BLOCKS.register("lost_city_cake", () -> new BlockLostCityCake(AbstractBlock.Properties.create(Material.CAKE).hardnessAndResistance(0.5F).sound(SoundType.CLOTH)));
   public static final RegistryObject<Block> CUSTOM_CAKE = BLOCKS.register("custom_cake", () -> new BlockCustomCake(AbstractBlock.Properties.create(Material.CAKE).hardnessAndResistance(0.5F).sound(SoundType.CLOTH)));
//   public static final RegistryObject<Block> HUNTING_DIMENSION_CAKE = BLOCKS.register("hunting_dimension_cake", () -> new BlockHuntingDimensionCake("hunting_dimension_cake"));

   public static final RegistryObject<Item> NETHER_CAKE_ITEM = ITEMS.register("nether_cake", () -> new BlockItem(NETHER_CAKE.get(), new Item.Properties().group(TeleTab.TELE_TAB)));
   public static final RegistryObject<Item> END_CAKE_ITEM = ITEMS.register("end_cake", () -> new BlockItem(END_CAKE.get(), new Item.Properties().group(TeleTab.TELE_TAB)));
   public static final RegistryObject<Item> OVERWORLD_CAKE_ITEM = ITEMS.register("overworld_cake", () -> new BlockItem(OVERWORLD_CAKE.get(), new Item.Properties().group(TeleTab.TELE_TAB)));

   public static final RegistryObject<Item> TWILIGHT_CAKE_ITEM = ITEMS.register("twilight_cake", () -> new BlockItem(TWILIGHT_CAKE.get(), new Item.Properties().group(TeleTab.TELE_TAB)));
   public static final RegistryObject<Item> LOST_CITY_CAKE_ITEM = ITEMS.register("lost_city_cake", () -> new BlockItem(LOST_CITY_CAKE.get(), new Item.Properties().group(TeleTab.TELE_TAB)));
   public static final RegistryObject<Item> CUSTOM_CAKE_ITEM = ITEMS.register("custom_cake", () -> new CustomCakeBlockItem(CUSTOM_CAKE.get(), new Item.Properties().group(TeleTab.TELE_TAB)));
}
