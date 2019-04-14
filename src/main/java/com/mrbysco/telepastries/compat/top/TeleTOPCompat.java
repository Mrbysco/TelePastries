package com.mrbysco.telepastries.compat.top;

import com.mrbysco.telepastries.Reference;
import com.mrbysco.telepastries.blocks.BlockPastryBase;
import com.mrbysco.telepastries.blocks.cake.BlockCakeBase;
import mcjty.theoneprobe.api.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInterModComms;

import java.util.function.Function;

public class TeleTOPCompat {
    private static boolean registered;

    public static void register() {
        if (registered)
            return;
        registered = true;
        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "com.mrbysco.telepastries.compat.top.TeleTOPCompat$GetTheOneProbe");
    }

    public static final class GetTheOneProbe implements Function<ITheOneProbe, Void> {

        @Override
        public Void apply(ITheOneProbe input) {
            input.registerProvider(new TeleTOPCompat.PastryInfo());
            return null;
        }
    }

    public static final class PastryInfo implements IProbeInfoProvider {

        @Override
        public String getID() {
            return Reference.MOD_ID;
        }

        @Override
        public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
            final Block block = world.getBlockState(data.getPos()).getBlock();
            if (block instanceof BlockCakeBase) {
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                        .item(new ItemStack(block))
                        .text(TextFormatting.GREEN + "Bites: ")
                        .progress(6 - blockState.getValue(BlockCakeBase.BITES), 6);
            }
        }
    }
}
