package com.mrbysco.telepastries.generator;

import com.mrbysco.telepastries.generator.client.PastryBlockStateProvider;
import com.mrbysco.telepastries.generator.client.PastryItemModelProvider;
import com.mrbysco.telepastries.generator.client.PastryLanguageProvider;
import com.mrbysco.telepastries.generator.server.PastryLootProvider;
import com.mrbysco.telepastries.generator.server.PastryRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class PastriesGenerator {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper helper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		if (event.includeServer()) {
			generator.addProvider(true, new PastryLootProvider(packOutput, lookupProvider));
			generator.addProvider(true, new PastryRecipeProvider(packOutput, lookupProvider));
		}
		if (event.includeClient()) {
			generator.addProvider(true, new PastryBlockStateProvider(packOutput, helper));
			generator.addProvider(true, new PastryItemModelProvider(packOutput, helper));
			generator.addProvider(true, new PastryLanguageProvider(packOutput));
		}
	}
}
