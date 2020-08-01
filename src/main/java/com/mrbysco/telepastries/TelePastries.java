package com.mrbysco.telepastries;

import com.mrbysco.telepastries.client.ClientHandler;
import com.mrbysco.telepastries.config.TeleConfig;
import com.mrbysco.telepastries.init.TeleRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MOD_ID)
public class TelePastries {
    public static final Logger LOGGER = LogManager.getLogger();

    public TelePastries() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(Type.SERVER, TeleConfig.serverSpec);
        FMLJavaModLoadingContext.get().getModEventBus().register(TeleConfig.class);

        TeleRegistry.BLOCKS.register(eventBus);
        TeleRegistry.ITEMS.register(eventBus);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientHandler::doClientStuff);
        });
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        //TODO: Wait for TOP to come back and register compat
        //InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }
}
