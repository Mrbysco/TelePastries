package com.mrbysco.telepastries.compat.waila;

import com.mrbysco.telepastries.Reference;
import com.mrbysco.telepastries.blocks.BlockPastryBase;
import com.mrbysco.telepastries.blocks.cake.BlockCakeBase;
import com.mrbysco.telepastries.init.TeleBlocks;
import mcp.mobius.waila.api.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCake;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

@WailaPlugin
public class TeleWailaCompat implements IWailaDataProvider, IWailaPlugin {
    private static final String CONFIG_TELEPASTRY_BITES = "telepastries.bites.name";

    @Override
    public void register(IWailaRegistrar registrar) {
        TeleWailaCompat instance = new TeleWailaCompat();
        for(Block block : TeleBlocks.BLOCKS)
        {
            registrar.registerBodyProvider(instance, block.getClass());
        }

        registrar.addConfig(Reference.MOD_NAME, CONFIG_TELEPASTRY_BITES, true);
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor,
                                     IWailaConfigHandler config) {
        return tooltip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor,
                                     IWailaConfigHandler config) {
        Block block = accessor.getBlock();
        if (block instanceof BlockCakeBase) {
            tooltip.add(TextFormatting.GRAY + "Bites: " + (6 - accessor.getBlockState().getValue(BlockCakeBase.BITES)) + " / 6");
        }
        return tooltip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor,
                                     IWailaConfigHandler config) {
        return tooltip;
    }
}
