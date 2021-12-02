package com.mrbysco.telepastries.client;

import com.mrbysco.telepastries.init.TeleRegistry;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientHandler {
    public static void doClientStuff(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(TeleRegistry.NETHER_CAKE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(TeleRegistry.OVERWORLD_CAKE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(TeleRegistry.END_CAKE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(TeleRegistry.LOST_CITY_CAKE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(TeleRegistry.CUSTOM_CAKE.get(), RenderType.cutout());
    }
}
