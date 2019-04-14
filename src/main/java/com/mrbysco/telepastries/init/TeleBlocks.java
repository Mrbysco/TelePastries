package com.mrbysco.telepastries.init;

import com.mrbysco.telepastries.Reference;
import com.mrbysco.telepastries.blocks.cake.BlockCakeBase;
import com.mrbysco.telepastries.blocks.cake.BlockEndCake;
import com.mrbysco.telepastries.blocks.cake.BlockNetherCake;
import com.mrbysco.telepastries.blocks.cake.BlockOverworldCake;
import com.mrbysco.telepastries.blocks.cake.compat.BlockHuntingDimensionCake;
import com.mrbysco.telepastries.blocks.cake.compat.BlockLostCityCake;
import com.mrbysco.telepastries.blocks.cake.compat.BlockTwilightCake;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class TeleBlocks {
    public static BlockCakeBase nether_cake;
    public static BlockCakeBase end_cake;
    public static BlockCakeBase overworld_cake;

    public static BlockCakeBase twilight_cake;
    public static BlockCakeBase lost_city_cake;
    public static BlockCakeBase hunting_dimension_cake;

    public static ArrayList<Block> BLOCKS = new ArrayList<>();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        IForgeRegistry<Block> registry = event.getRegistry();

        nether_cake = registerBlock(new BlockNetherCake("nether_cake"));
        end_cake = registerBlock(new BlockEndCake("end_cake"));
        overworld_cake = registerBlock(new BlockOverworldCake("overworld_cake"));

        //Mod Compat
        if(Loader.isModLoaded("twilightforest")) {
            twilight_cake = registerBlock(new BlockTwilightCake("twilight_cake"));
        }
        if(Loader.isModLoaded("lostcities")) {
            lost_city_cake = registerBlock(new BlockLostCityCake("lost_city_cake"));
        }
        if(Loader.isModLoaded("huntingdim")) {
            hunting_dimension_cake = registerBlock(new BlockHuntingDimensionCake("hunting_dimension_cake"));
        }

        registry.registerAll(BLOCKS.toArray(new Block[0]));
    }

    public static <T extends Block> T registerBlock(T block)
    {
        return registerBlock(block, new ItemBlock(block));
    }

    public static <T extends Block> T registerBlock(T block, ItemBlock item)
    {
        item.setRegistryName(block.getRegistryName());
        TeleItems.ITEMS.add(item);
        BLOCKS.add(block);
        return block;
    }
}
