package com.mrbysco.telepastries.compat.top;

import com.mrbysco.telepastries.Reference;
import com.mrbysco.telepastries.blocks.cake.BlockCakeBase;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.InterModComms;

import java.util.function.Function;

public class TeleTOPCompat {
	public static void register() {
		InterModComms.sendTo("theoneprobe", "getTheOneProbe", GetTheOneProbe::new);
	}

    public static final class GetTheOneProbe implements Function<ITheOneProbe, Void> {

        @Override
        public Void apply(ITheOneProbe input) {
            input.registerProvider(new PastryInfo());
            return null;
        }
    }

    public static final class PastryInfo implements IProbeInfoProvider {

        @Override
        public String getID() {
            return Reference.MOD_ID;
        }

		@Override
		public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data) {
			final Block block = world.getBlockState(data.getPos()).getBlock();
			if (block instanceof BlockCakeBase) {
				probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
						.item(new ItemStack(block))
						.text(new StringTextComponent("Bites: ").mergeStyle(TextFormatting.GREEN))
						.progress(6 - blockState.get(BlockCakeBase.BITES), 6);
			}
		}
    }
}
