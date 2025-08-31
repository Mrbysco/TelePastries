package com.mrbysco.telepastries.generator.client;

import com.mrbysco.telepastries.Reference;
import com.mrbysco.telepastries.init.TeleRegistry;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.jetbrains.annotations.Nullable;

public class PastryLanguageProvider extends LanguageProvider {
	public PastryLanguageProvider(PackOutput output) {
		super(output, Reference.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {
		add("itemGroup.tele_tab", "TelePastries");

		addBlock(TeleRegistry.NETHER_CAKE, "Nether Cake");
		addBlock(TeleRegistry.END_CAKE, "End Cake");
		addBlock(TeleRegistry.OVERWORLD_CAKE, "Overworld Cake");

		addBlock(TeleRegistry.TWILIGHT_CAKE, "Twilight Forest Cake");
		addBlock(TeleRegistry.LOST_CITY_CAKE, "Lost City Cake");
		addBlock(TeleRegistry.CUSTOM_CAKE, "%s Cake");
		addBlock(TeleRegistry.CUSTOM_CAKE2, "%s Cake");
		addBlock(TeleRegistry.CUSTOM_CAKE3, "%s Cake");

		add("telepastries.pastry.reset.complete", "Dimension %s 's saved position has been reset.");
		add("telepastries.pastry.reset.failed", "Dimension %s does not have a saved position.");
		add("telepastries.pastry.support.disabled", "Cake is disabled because %s isn't installed");
		add("telepastries.pastry.custom.unbound", "Custom Cake has no dimension bound in config ");
		add("telepastries.pastry.custom.invalid", "Destination of cake invalid %s");
		add("telepastries.same_dimension", "Can't teleport to the same dimension");
		add("telepastries.teleport_restricted", "Can't teleport to a non-overworld dimension");

		add("config.jade.plugin_telepastries.bites", "Cake bites");

		addConfig("General", "General", "General Settings");
		addConfig("explosionImmune", "Explosion Immune", "Defines if the pastries should be immune to explosions");
		addConfig("ignoreHunger", "Ignore Hunger", "Defines if the pastry usage requires hunger, when set to true it ignores hunger");
		addConfig("disableHopping", "Disable Hopping", "Disable placement of non-overworld cakes in dimensions that aren't the overworld (Meaning you can't go from the nether straight to the end)");
		addConfig("resetPastry", "Reset Pastry", "Defines if the pastry teleportation point can be reset with a milk bucket");
		addConfig("resetItems", "Reset Items", "Defines the item needed to reset the pastry teleportation point");

		addConfig("Nether", "Nether", "Nether Cake Settings");
		addConfig("consumeNetherCake", "Consume Nether Cake", "Defines if the Nether Cake gets partly consumed when eaten");
		addConfig("netherCakeRefillItems", "Nether Cake Refill Items", "Set the refill items used by Nether Cake (Only change if you know what you're doing) [modid:itemname]");

		addConfig("End", "End", "End Cake Settings");
		addConfig("consumeEndCake", "Consume End Cake", "Defines if the End Cake gets partly consumed when eaten");
		addConfig("endCakeRefillItems", "End Cake Refill Items", "Set the refill items used by End Cake (Only change if you know what you're doing) [modid:itemname]");

		addConfig("Overworld", "Overworld", "Overworld Cake Settings");
		addConfig("consumeOverworldCake", "Consume Overworld Cake", "Defines if the Overworld Cake gets partly consumed when eaten");
		addConfig("overworldCakeRefillItems", "Overworld Cake Refill Items", "Set the refill items used by Overworld Cake (Only change if you know what you're doing) [modid:itemname]");

		addConfig("Compat", "Compat", "Compatibility Cake Settings");
		addConfig("consumeTwilightCake", "Consume Twilight Cake", "Defines if the Twilight Forest Cake gets partly consumed when eaten");
		addConfig("TwilightCakeRefillItems", "Twilight Cake Refill Items", "Set the refill items used by the Twilight Forest Cake (Only change if you know what you're doing) [modid:itemname]");
		addConfig("consumeLostCitiesCake", "Consume Lost Cities Cake", "Defines if the Lost Cities Cake gets partly consumed when eaten");
		addConfig("lostCitiesCakeRefillItem", "Lost Cities Cake Refill Items", "Set the refill items used by the Lost Cities Cake (Only change if you know what you're doing) [modid:itemname]");

		addConfig("CustomCake", "Custom Cake", "Custom Cake Settings");
		addConfig("consumeCustomCake", "Consume Custom Cake", "Defines if the Custom Cake gets partly consumed when eaten");
		addConfig("customCakeName", "Custom Cake Name", "Defines the name of the cake");
		addConfig("customCakeDimension", "Custom Cake Dimension", "Defines the dimension bound to the custom cake [modid:dimensionname]");
		addConfig("customCakeRefillItem", "Custom Cake Refill Items", "Set the refill items used by the Custom Cake (Only change if you know what you're doing) [modid:itemname]");
		addConfig("customCakeMinY", "Custom Cake Min Y", "Set the minimum Y location that the cake can spawn you at");
		addConfig("customCakeMaxY", "Custom Cake Max Y", "Set the maximum Y location that the cake can spawn you at");

		addConfig("CustomCake2", "Custom Cake 2", "Custom Cake Settings");
		addConfig("consumeCustomCake2", "Consume Custom Cake 2", "Defines if the Custom Cake gets partly consumed when eaten");
		addConfig("customCake2Name", "Custom Cake Name 2", "Defines the name of the cake");
		addConfig("customCake2Dimension", "Custom Cake 2 Dimension", "Defines the dimension bound to the custom cake [modid:dimensionname]");
		addConfig("customCake2RefillItem", "Custom Cake 2 Refill Items", "Set the refill items used by the Custom Cake (Only change if you know what you're doing) [modid:itemname]");
		addConfig("customCake2MinY", "Custom Cake 2 Min Y", "Set the minimum Y location that the cake can spawn you at");
		addConfig("customCake2MaxY", "Custom Cake 2 Max Y", "Set the maximum Y location that the cake can spawn you at");

		addConfig("CustomCake3", "Custom Cake 3", "Custom Cake Settings");
		addConfig("consumeCustomCake3", "Consume Custom Cake 3", "Defines if the Custom Cake gets partly consumed when eaten");
		addConfig("customCake3Name", "Custom Cake Name 3", "Defines the name of the cake");
		addConfig("customCake3Dimension", "Custom Cake 3 Dimension", "Defines the dimension bound to the custom cake [modid:dimensionname]");
		addConfig("customCake3RefillItem", "Custom Cake 3 Refill Items", "Set the refill items used by the Custom Cake (Only change if you know what you're doing) [modid:itemname]");
		addConfig("customCake3MinY", "Custom Cake 3 Min Y", "Set the minimum Y location that the cake can spawn you at");
		addConfig("customCake3MaxY", "Custom Cake 3 Max Y", "Set the maximum Y location that the cake can spawn you at");
	}

	/**
	 * Add the translation for a config entry
	 *
	 * @param path        The path of the config entry
	 * @param name        The name of the config entry
	 * @param description The description of the config entry (optional in case of targeting "title" or similar entries that have no tooltip)
	 */
	private void addConfig(String path, String name, @Nullable String description) {
		this.add(Reference.MOD_ID + ".configuration." + path, name);
		if (description != null && !description.isEmpty())
			this.add(Reference.MOD_ID + ".configuration." + path + ".tooltip", description);
	}
}
