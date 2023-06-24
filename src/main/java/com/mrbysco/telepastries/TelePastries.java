package com.mrbysco.telepastries;

import com.mojang.logging.LogUtils;
import com.mrbysco.telepastries.compat.top.TeleTOPCompat;
import com.mrbysco.telepastries.config.TeleConfig;
import com.mrbysco.telepastries.handler.ExplosionHandler;
import com.mrbysco.telepastries.init.TeleRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Reference.MOD_ID)
public class TelePastries {
	public static final Logger LOGGER = LogUtils.getLogger();

	public TelePastries() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(Type.COMMON, TeleConfig.commonSpec);
		FMLJavaModLoadingContext.get().getModEventBus().register(TeleConfig.class);

		eventBus.addListener(this::sendImc);

		TeleRegistry.BLOCKS.register(eventBus);
		TeleRegistry.ITEMS.register(eventBus);
		TeleRegistry.CREATIVE_MODE_TABS.register(eventBus);

		MinecraftForge.EVENT_BUS.addListener(ExplosionHandler::onExplosion);
	}

	public void sendImc(InterModEnqueueEvent event) {
		if (ModList.get().isLoaded("theoneprobe")) {
			TeleTOPCompat.register();
		}
	}
}
