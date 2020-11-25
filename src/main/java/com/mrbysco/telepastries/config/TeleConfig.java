package com.mrbysco.telepastries.config;

import com.mrbysco.telepastries.TelePastries;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;

public class TeleConfig {

    public static class Server {
        public final BooleanValue ignoreHunger;

        public final BooleanValue resetPastry;
        public final ConfigValue<List<? extends String>> resetItems;

        public final BooleanValue consumeNetherCake;
        public final ConfigValue<List<? extends String>> netherCakeRefillItems;
        public final BooleanValue netherCake1x1Logic;

        public final BooleanValue consumeEndCake;
        public final ConfigValue<List<? extends String>> endCakeRefillItems;

        public final BooleanValue consumeOverworldCake;
        public final ConfigValue<List<? extends String>> overworldCakeRefillItems;

        public final BooleanValue consumeTwilightCake;
        public final ConfigValue<List<? extends String>> TwilightCakeRefillItems;

        Server(ForgeConfigSpec.Builder builder) {
            builder.comment("General settings")
                    .push("General");

            ignoreHunger = builder
                    .comment("Defines if the pastry usage requires hunger, when set to true it ignores hunger [default: false]")
                    .define("ignoreHunger", false);

            resetPastry = builder
                    .comment("Defines if the pastry teleportation point can be reset with a milk bucket [default: false]")
                    .define("resetPastry", false);

            String[] resetItem = new String[]
                    {
                            "minecraft:milk_bucket"
                    };

            resetItems = builder
                    .comment("Defines the item needed to reset the pastry teleportation point [default: minecraft:milk_bucket]")
                    .defineList("resetItems", Arrays.asList(resetItem), o -> (o instanceof String));

            builder.pop();
            builder.comment("Nether settings")
                    .push("Nether");

            consumeNetherCake = builder
                    .comment("Defines if the Nether Cake gets partly consumed when eaten [default: true]")
                    .define("consumeNetherCake", true);

            String[] netherItems = new String[]
                    {
                            "minecraft:obsidian"
                    };

            netherCakeRefillItems = builder
                    .comment("Set the refill items used by Nether Cake (Only change if you know what you're doing) [modid:itemname].")
                    .defineList("netherCakeRefillItems", Arrays.asList(netherItems), o -> (o instanceof String));

            netherCake1x1Logic = builder
                    .comment("Defines if the Nether Cake should teleport the player 1x1 (Use this if you're replacing the Nether dimension with one that is 1x1) [default: false].")
                    .define("netherCake1x1Logic", false);

            builder.pop();
            builder.comment("End settings")
                    .push("End");

            consumeEndCake = builder
                    .comment("Defines if the End Cake gets partly consumed when eaten [default: true]")
                    .define("consumeEndCake", true);

            String[] endItems = new String[]
                    {
                            "minecraft:ender_eye"
                    };

            endCakeRefillItems = builder
                    .comment("Set the refill items used by End Cake (Only change if you know what you're doing) [modid:itemname].")
                    .defineList("endCakeRefillItems", Arrays.asList(endItems), o -> (o instanceof String));

            builder.pop();
            builder.comment("Overworld settings")
                    .push("Overworld");

            consumeOverworldCake = builder
                    .comment("Defines if the Overworld Cake gets partly consumed when eaten [default: true]")
                    .define("consumeOverworldCake", true);

            String[] overworldItems = new String[]
                    {
                            "minecraft:oak_sapling",
                            "minecraft:spruce_sapling",
                            "minecraft:birch_sapling",
                            "minecraft:jungle_sapling",
                            "minecraft:acacia_sapling",
                            "minecraft:dark_oak_sapling"
                    };

            overworldCakeRefillItems = builder
                    .comment("Set the refill items used by Overworld Cake (Only change if you know what you're doing) [modid:itemname].")
                    .defineList("overworldCakeRefillItems", Arrays.asList(overworldItems), o -> (o instanceof String));

            builder.pop();
            builder.comment("Compat settings")
                    .push("Compat");

            consumeTwilightCake = builder
                    .comment("Defines if the Twilight Forest Cake gets partly consumed when eaten [default: true]")
                    .define("consumeTwilightCake", true);

            String[] twilightItems = new String[]
                    {
                            "minecraft:diamond"
                    };

            TwilightCakeRefillItems = builder
                    .comment("Set the refill items used by Twilight Forest Cake (Only change if you know what you're doing) [modid:itemname]")
                    .defineList("TwilightCakeRefillItems", Arrays.asList(twilightItems), o -> (o instanceof String));

            builder.pop();
        }
    }

    public static final ForgeConfigSpec serverSpec;
    public static final TeleConfig.Server SERVER;

    static {
        final Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(TeleConfig.Server::new);
        serverSpec = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
        TelePastries.LOGGER.debug("Loaded TelePastries' config file {}", configEvent.getConfig().getFileName());
    }

    @SubscribeEvent
    public static void onFileChange(final ModConfig.Reloading configEvent) {
        TelePastries.LOGGER.debug("TelePastries' config just got changed on the file system!");
    }
}