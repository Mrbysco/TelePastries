package com.mrbysco.telepastries;

import com.mrbysco.telepastries.compat.top.TeleTOPCompat;
import com.mrbysco.telepastries.config.TeleConfig;
import com.mrbysco.telepastries.init.TeleTab;
import com.mrbysco.telepastries.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Reference.MOD_ID,
    name = Reference.MOD_NAME,
    version = Reference.VERSION,
    acceptedMinecraftVersions = Reference.ACCEPTED_VERSIONS,
    dependencies = Reference.DEPENDENCIES)

public class TelePastries {
    @Instance(Reference.MOD_ID)
    public static TelePastries instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    public static final Logger logger = LogManager.getLogger(Reference.MOD_ID);

    public static TeleTab teleTab = new TeleTab();

    @EventHandler
    public void PreInit(FMLPreInitializationEvent event)
    {
        logger.info("Registering config");
        MinecraftForge.EVENT_BUS.register(new TeleConfig());

        proxy.Preinit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        if (Loader.isModLoaded("theoneprobe")) {
            TeleTOPCompat.register();
        }

        proxy.Init();
    }

    @EventHandler
    public void postInit(boolean event)
    {
        proxy.PostInit();
    }
}
