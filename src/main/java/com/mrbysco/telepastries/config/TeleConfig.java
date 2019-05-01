package com.mrbysco.telepastries.config;

import com.mrbysco.telepastries.Reference;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Reference.MOD_ID, name = "TelePastries", category = "")
@Config.LangKey("telepastries.config.title")
public class TeleConfig {
    @Config.Comment({"General TelePastry settings"})
    public static General general = new General();

    @Config.Comment({"General pastry settings"})
    public static Pastries pastries = new Pastries();

    @Config.Comment({"Mod Compat pastry settings"})
    public static CompatPastries pastriesCompat = new CompatPastries();

    public static class General {
        @Config.Comment("Defines if the pastry teleportation point can be reset with a milk bucket [default: false]")
        public boolean resetPastry = false;

        @Config.Comment("Defines the item needed to reset the pastry teleportation point [default: minecraft:milk_bucket]")
        public String resetItem = "minecraft:milk_bucket";
    }

    public static class Pastries {
        @Config.Comment({"Nether Pastry settings"})
        public final Nether nether = new Nether();

        @Config.Comment({"End Pastry settings"})
        public final End end = new End();

        @Config.Comment({"Overworld Pastry settings"})
        public final Overworld overworld = new Overworld();

        public static class Nether{
            @Config.Comment("Defines if the Nether Cake gets partly consumed when eaten [default: true]")
            public boolean consumeNetherCake = true;

            @Config.Comment("Set the refill item used by Nether Cake (Only change if you know what you're doing) [modid:itemname].")
            public String netherCakeRefillItem = "minecraft:obsidian";

            @Config.Comment("Defines if the Nether Cake should teleport the player 1x1 (Use this if you're replacing the Nether dimension with one that is 1x1) [default: false].")
            public boolean netherCake1x1Logic = false;
        }

        public static class End{
            @Config.Comment("Defines if the End Cake gets partly consumed when eaten [default: true]")
            public boolean consumeEndCake = true;

            @Config.Comment("Set the refill item used by End Cake (Only change if you know what you're doing) [modid:itemname].")
            public String endCakeRefillItem = "minecraft:ender_eye";
        }

        public static class Overworld{
            @Config.Comment("Defines if the Overworld Cake gets partly consumed when eaten [default: true]")
            public boolean consumeOverworldCake = true;

            @Config.Comment("Set the refill item used by Overworld Cake (Only change if you know what you're doing) [modid:itemname].")
            public String overworldCakeRefillItem = "minecraft:sapling";
        }
    }


    public static class CompatPastries {
        @Config.Comment({"Twilight Forest Pastry settings"})
        public final TwilightForest twilightForest = new TwilightForest();

        @Config.Comment({"Lost Cities Pastry settings"})
        public final LostCities lostCities = new LostCities();

        @Config.Comment({"Hunting Dimension Pastry settings"})
        public final HuntingDimension huntingDimension = new HuntingDimension();

        public static class TwilightForest{
            @Config.Comment("Defines if the Twilight Forest Cake gets partly consumed when eaten [default: true]")
            public boolean consumeTwilightForestCake = true;

            @Config.Comment("Set the refill item used by Twilight Forest Cake (Only change if you know what you're doing) [modid:itemname].")
            public String twilightForestCakeRefillItem = "minecraft:diamond";
        }

        public static class LostCities{
            @Config.Comment("Defines if the Lost Cities Cake gets partly consumed when eaten [default: true]")
            public boolean consumeLostCitiesCake = true;

            @Config.Comment("Set the refill item used by Lost Cities Cake (Only change if you know what you're doing) [modid:itemname].")
            public String lostCitiesCakeRefillItem = "minecraft:bed";
        }

        public static class HuntingDimension{
            @Config.Comment("Defines if the Hunting Dimension Cake gets partly consumed when eaten [default: true]")
            public boolean consumeHuntingDimensionCake = true;

            @Config.Comment("Set the refill item used by Hunting Dimension Cake (Only change if you know what you're doing) [modid:itemname].")
            public String huntingDimensionCakeRefillItem = "minecraft:arrow";
        }
    }

    @Mod.EventBusSubscriber(modid = Reference.MOD_ID)
    private static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(Reference.MOD_ID)) {
                ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}
