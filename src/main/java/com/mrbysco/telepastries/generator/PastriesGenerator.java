package com.mrbysco.telepastries.generator;

import com.mrbysco.telepastries.generator.client.PastryBlockStateProvider;
import com.mrbysco.telepastries.generator.client.PastryItemModelProvider;
import com.mrbysco.telepastries.generator.server.PastryLootProvider;
import com.mrbysco.telepastries.generator.server.PastryRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PastriesGenerator {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(event.includeServer(), new PastryLootProvider(packOutput));
			generator.addProvider(event.includeServer(), new PastryRecipeProvider(packOutput, event.getLookupProvider()));
		}
		if (event.includeClient()) {
			generator.addProvider(event.includeClient(), new PastryBlockStateProvider(packOutput, helper));
			generator.addProvider(event.includeClient(), new PastryItemModelProvider(packOutput, helper));
		}
	}
}
