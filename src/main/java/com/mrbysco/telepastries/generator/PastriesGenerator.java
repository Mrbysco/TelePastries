package com.mrbysco.telepastries.generator;

import com.mrbysco.telepastries.generator.client.PastryLanguageProvider;
import com.mrbysco.telepastries.generator.client.PastryModelProvider;
import com.mrbysco.telepastries.generator.server.PastryLootProvider;
import com.mrbysco.telepastries.generator.server.PastryRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber
public class PastriesGenerator {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		generator.addProvider(true, new PastryLootProvider(packOutput, lookupProvider));
		generator.addProvider(true, new PastryRecipeProvider.Runner(packOutput, lookupProvider));

		generator.addProvider(true, new PastryModelProvider(packOutput));
		generator.addProvider(true, new PastryLanguageProvider(packOutput));

	}
}
