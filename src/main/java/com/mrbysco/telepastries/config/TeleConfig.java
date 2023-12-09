package com.mrbysco.telepastries.config;

import com.mrbysco.telepastries.TelePastries;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;

public class TeleConfig {

	public static class Common {
		public final BooleanValue explosionImmune;
		public final BooleanValue ignoreHunger;
		public final BooleanValue disableHopping;

		public final BooleanValue resetPastry;
		public final ConfigValue<List<? extends String>> resetItems;

		public final BooleanValue consumeNetherCake;
		public final ConfigValue<List<? extends String>> netherCakeRefillItems;

		public final BooleanValue consumeEndCake;
		public final ConfigValue<List<? extends String>> endCakeRefillItems;

		public final BooleanValue consumeOverworldCake;
		public final ConfigValue<List<? extends String>> overworldCakeRefillItems;

		public final BooleanValue consumeTwilightCake;
		public final ConfigValue<List<? extends String>> twilightCakeRefillItems;

		public final BooleanValue consumeLostCitiesCake;
		public final ConfigValue<List<? extends String>> lostCitiesCakeRefillItem;

		public final BooleanValue consumeCustomCake;
		public final ConfigValue<? extends String> customCakeName;
		public final ConfigValue<? extends String> customCakeDimension;
		public final ConfigValue<List<? extends String>> customCakeRefillItem;
		public final IntValue customCakeMinY;
		public final IntValue customCakeMaxY;

		public final BooleanValue consumeCustomCake2;
		public final ConfigValue<? extends String> customCake2Name;
		public final ConfigValue<? extends String> customCake2Dimension;
		public final ConfigValue<List<? extends String>> customCake2RefillItem;
		public final IntValue customCake2MinY;
		public final IntValue customCake2MaxY;

		public final BooleanValue consumeCustomCake3;
		public final ConfigValue<? extends String> customCake3Name;
		public final ConfigValue<? extends String> customCake3Dimension;
		public final ConfigValue<List<? extends String>> customCake3RefillItem;
		public final IntValue customCake3MinY;
		public final IntValue customCake3MaxY;

		Common(ForgeConfigSpec.Builder builder) {
			builder.comment("General settings")
					.push("General");

			explosionImmune = builder
					.comment("Defines if the pastries should be immune to explosions [default: false]")
					.define("explosionImmune", false);

			ignoreHunger = builder
					.comment("Defines if the pastry usage requires hunger, when set to true it ignores hunger [default: false]")
					.define("ignoreHunger", false);

			disableHopping = builder
					.comment("Disable placement of non-overworld cakes in dimensions that aren't the overworld (Meaning you can't go from the nether straight to the end) [default: true]")
					.define("disableHopping", true);

			resetPastry = builder
					.comment("Defines if the pastry teleportation point can be reset with a milk bucket [default: false]")
					.define("resetPastry", false);

			String[] resetItemsList = new String[]
					{
							"minecraft:milk_bucket"
					};

			resetItems = builder
					.comment("Defines the item needed to reset the pastry teleportation point [default: minecraft:milk_bucket]")
					.defineList("resetItems", () -> Arrays.asList(resetItemsList), o -> (o instanceof String));

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
					.defineList("netherCakeRefillItems", () -> Arrays.asList(netherItems), o -> (o instanceof String));

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
					.defineList("endCakeRefillItems", () -> Arrays.asList(endItems), o -> (o instanceof String));

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
					.defineList("overworldCakeRefillItems", () -> Arrays.asList(overworldItems), o -> (o instanceof String));

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

			twilightCakeRefillItems = builder
					.comment("Set the refill items used by the Twilight Forest Cake (Only change if you know what you're doing) [modid:itemname]")
					.defineList("TwilightCakeRefillItems", () -> Arrays.asList(twilightItems), o -> (o instanceof String));

			consumeLostCitiesCake = builder
					.comment("Defines if the Lost Cities Cake gets partly consumed when eaten [default: true]")
					.define("consumeLostCitiesCake", true);

			String[] lostcityItems = new String[]
					{
							"minecraft:bed"
					};

			lostCitiesCakeRefillItem = builder
					.comment("Set the refill items used by the Lost Cities Cake (Only change if you know what you're doing) [modid:itemname]")
					.defineList("lostCitiesCakeRefillItem", () -> Arrays.asList(lostcityItems), o -> (o instanceof String));

			builder.pop();
			builder.comment("Custom Cake settings");
			builder.push("CustomCake");

			consumeCustomCake = builder
					.comment("Defines if the Custom Cake gets partly consumed when eaten [default: true]")
					.define("consumeCustomCake", true);

			customCakeName = builder
					.comment("Defines the name of the cake [default: \"Custom\"]")
					.define("customCakeName", "Custom", o -> (o instanceof String));

			customCakeDimension = builder
					.comment("Defines the dimension bound to the custom cake [default: \"minecraft:overworld\"]")
					.define("customCakeDimension", "minecraft:overworld", o -> (o instanceof String));

			String[] customItems = new String[]
					{
							"minecraft:cobblestone"
					};

			customCakeRefillItem = builder
					.comment("Set the refill items used by the Custom Cake (Only change if you know what you're doing) [modid:itemname]")
					.defineList("customCakeRefillItem", () -> Arrays.asList(customItems), o -> (o instanceof String));

			customCakeMinY = builder
					.comment("Set the minimum Y location that the cake can spawn you at [Default: 2]")
					.defineInRange("customCakeMinY", 2, 1, 256);

			customCakeMaxY = builder
					.comment("Set the maximum Y location that the cake can spawn you at [Default: 2]")
					.defineInRange("customCakeMaxY", 254, 1, 256);

			builder.pop();
			builder.push("CustomCake2");

			consumeCustomCake2 = builder
					.comment("Defines if the Custom Cake gets partly consumed when eaten [default: true]")
					.define("consumeCustomCake2", true);

			customCake2Name = builder
					.comment("Defines the name of the cake [default: \"Another Custom\"]")
					.define("customCake2Name", "Another Custom", o -> (o instanceof String));

			customCake2Dimension = builder
					.comment("Defines the dimension bound to the custom cake [default: \"minecraft:overworld\"]")
					.define("customCake2Dimension", "minecraft:overworld", o -> (o instanceof String));

			String[] customItems2 = new String[]
					{
							"minecraft:cobblestone"
					};

			customCake2RefillItem = builder
					.comment("Set the refill items used by the Custom Cake (Only change if you know what you're doing) [modid:itemname]")
					.defineList("customCake2RefillItem", () -> Arrays.asList(customItems2), o -> (o instanceof String));

			customCake2MinY = builder
					.comment("Set the minimum Y location that the cake can spawn you at [Default: 2]")
					.defineInRange("customCake2MinY", 2, 1, 256);

			customCake2MaxY = builder
					.comment("Set the maximum Y location that the cake can spawn you at [Default: 2]")
					.defineInRange("customCake2MaxY", 254, 1, 256);

			builder.pop();
			builder.push("CustomCake3");

			consumeCustomCake3 = builder
					.comment("Defines if the Custom Cake gets partly consumed when eaten [default: true]")
					.define("consumeCustomCake3", true);

			customCake3Name = builder
					.comment("Defines the name of the cake [default: \"Yet Another Custom\"]")
					.define("customCake3Name", "Yet Another Custom", o -> (o instanceof String));

			customCake3Dimension = builder
					.comment("Defines the dimension bound to the custom cake [default: \"minecraft:overworld\"]")
					.define("customCake3Dimension", "minecraft:overworld", o -> (o instanceof String));

			String[] customItems3 = new String[]
					{
							"minecraft:cobblestone"
					};

			customCake3RefillItem = builder
					.comment("Set the refill items used by the Custom Cake (Only change if you know what you're doing) [modid:itemname]")
					.defineList("customCake3RefillItem", () -> Arrays.asList(customItems3), o -> (o instanceof String));

			customCake3MinY = builder
					.comment("Set the minimum Y location that the cake can spawn you at [Default: 2]")
					.defineInRange("customCake3MinY", 2, 1, 256);

			customCake3MaxY = builder
					.comment("Set the maximum Y location that the cake can spawn you at [Default: 2]")
					.defineInRange("customCake3MaxY", 254, 1, 256);

			builder.pop();
		}
	}

	public static final ForgeConfigSpec commonSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfigEvent.Loading configEvent) {
		TelePastries.LOGGER.debug("Loaded TelePastries' config file {}", configEvent.getConfig().getFileName());
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfigEvent.Reloading configEvent) {
		TelePastries.LOGGER.debug("TelePastries' config just got changed on the file system!");
	}
}