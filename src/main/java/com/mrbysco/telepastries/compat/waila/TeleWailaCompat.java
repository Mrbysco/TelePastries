package com.mrbysco.telepastries.compat.waila;

import com.mrbysco.telepastries.Reference;
import com.mrbysco.telepastries.blocks.cake.BlockCakeBase;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

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
		public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
			tooltip.add(new StringTextComponent("Bites: " + (6 - accessor.getBlockState().getValue(BlockCakeBase.BITES)) + " / 6").withStyle(TextFormatting.GRAY));
		}
	}
}
