package com.mrbysco.telepastries.client;

import com.mrbysco.telepastries.init.TeleRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientHandler {
	public static void doClientStuff(final FMLClientSetupEvent event) {
		RenderTypeLookup.setRenderLayer(TeleRegistry.NETHER_CAKE.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(TeleRegistry.OVERWORLD_CAKE.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(TeleRegistry.END_CAKE.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(TeleRegistry.LOST_CITY_CAKE.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(TeleRegistry.CUSTOM_CAKE.get(), RenderType.cutout());
	}
}
