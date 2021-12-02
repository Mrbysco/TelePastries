package com.mrbysco.telepastries.compat.waila;

import com.mrbysco.telepastries.Reference;
import com.mrbysco.telepastries.blocks.cake.BlockCakeBase;
import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

@WailaPlugin
public class TeleWailaCompat implements IWailaPlugin {
	private static final ResourceLocation CONFIG_TELEPASTRY_BITES = new ResourceLocation(Reference.MOD_ID, "telepastries.bites.name");

	@Override
	public void register(IRegistrar registrar) {
		registrar.registerComponentProvider(PastryBodyHandler.INSTANCE, TooltipPosition.BODY, BlockCakeBase.class);

		registrar.addConfig(CONFIG_TELEPASTRY_BITES, true);
	}

	public static class PastryBodyHandler implements IComponentProvider {
		public static final PastryBodyHandler INSTANCE = new PastryBodyHandler();

		@Override
		public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig iPluginConfig) {
			tooltip.add(new TextComponent("Bites: " + (6 - accessor.getBlockState().getValue(BlockCakeBase.BITES)) + " / 6").withStyle(ChatFormatting.GRAY));
		}
	}
}
