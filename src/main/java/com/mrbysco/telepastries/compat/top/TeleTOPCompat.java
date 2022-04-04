package com.mrbysco.telepastries.compat.top;

import com.mrbysco.telepastries.Reference;
import com.mrbysco.telepastries.blocks.cake.BlockCakeBase;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
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
		public ResourceLocation getID() {
			return new ResourceLocation(Reference.MOD_ID, "main");
		}

		@Override
		public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData data) {
			final Block block = world.getBlockState(data.getPos()).getBlock();
			if (block instanceof BlockCakeBase) {
				probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
						.item(new ItemStack(block))
						.text(new TextComponent("Bites: ").withStyle(ChatFormatting.GREEN))
						.progress(6 - blockState.getValue(BlockCakeBase.BITES), 6);
			}
		}
	}
}
