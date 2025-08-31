package com.mrbysco.telepastries.generator.client;

import com.mrbysco.telepastries.Reference;
import com.mrbysco.telepastries.init.TeleRegistry;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

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

		addBlock(TeleRegistry.TWILIGHT_CAKE, "Twilight Cake");
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
	}
}
