package com.mrbysco.telepastries.generator;

import com.mrbysco.telepastries.Reference;
import com.mrbysco.telepastries.blocks.cake.BlockCakeBase;
import com.mrbysco.telepastries.init.TeleRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PastriesGenerator {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(event.includeServer(), new Loots(packOutput));
		}
		if (event.includeClient()) {
			generator.addProvider(event.includeClient(), new PastryBlockStates(packOutput, helper));
			generator.addProvider(event.includeClient(), new PastryItemModels(packOutput, helper));
		}
	}

	private static class Loots extends LootTableProvider {
		public Loots(PackOutput packOutput) {
			super(packOutput, Set.of(), List.of(
					new SubProviderEntry(TeleBlocks::new, LootContextParamSets.BLOCK)));
		}

		@Override
		protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationresults) {
			map.forEach((name, table) -> LootTables.validate(validationresults, name, table));
		}

		private static class TeleBlocks extends BlockLootSubProvider {

			protected TeleBlocks() {
				super(Set.of(), FeatureFlags.REGISTRY.allFlags());
			}

			@Override
			protected void generate() {
				for (RegistryObject<Block> blockObject : TeleRegistry.BLOCKS.getEntries()) {
					this.add(blockObject.get(), noDrop());
				}
			}

			@Override
			protected Iterable<Block> getKnownBlocks() {
				return TeleRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
			}
		}
	}

	private static class PastryBlockStates extends BlockStateProvider {

		public PastryBlockStates(PackOutput packOutput, ExistingFileHelper helper) {
			super(packOutput, Reference.MOD_ID, helper);
		}

		@Override
		protected void registerStatesAndModels() {
			makeCake(TeleRegistry.OVERWORLD_CAKE.get(), "overworld");
			makeCake(TeleRegistry.NETHER_CAKE.get(), "nether");
			makeCake(TeleRegistry.END_CAKE.get(), "end");
			makeCake(TeleRegistry.TWILIGHT_CAKE.get(), "twilight");
			makeCake(TeleRegistry.LOST_CITY_CAKE.get(), "cities");
			makeCake(TeleRegistry.CUSTOM_CAKE.get(), "custom");
			makeCake(TeleRegistry.CUSTOM_CAKE2.get(), "custom");
			makeCake(TeleRegistry.CUSTOM_CAKE3.get(), "custom");
		}

		private void makeCake(Block block, String dimension) {
			ModelFile model = models().getBuilder(ForgeRegistries.BLOCKS.getKey(block).getPath()).renderType("cutout")
					.parent(models().getExistingFile(mcLoc("block/cake")))
					.texture("particle", "block/" + dimension + "/cake_side")
					.texture("bottom", "block/" + dimension + "/cake_bottom")
					.texture("top", "block/" + dimension + "/cake_top")
					.texture("side", "block/" + dimension + "/cake_side");

			ModelFile modelSlice1 = models().getBuilder(ForgeRegistries.BLOCKS.getKey(block).getPath() + "_slice1").renderType("cutout")
					.parent(models().getExistingFile(mcLoc("block/cake_slice1")))
					.texture("particle", "block/" + dimension + "/cake_side")
					.texture("bottom", "block/" + dimension + "/cake_bottom")
					.texture("top", "block/" + dimension + "/cake_top")
					.texture("side", "block/" + dimension + "/cake_side")
					.texture("inside", "block/" + dimension + "/cake_inner");
			ModelFile modelSlice2 = models().getBuilder(ForgeRegistries.BLOCKS.getKey(block).getPath() + "_slice2").renderType("cutout")
					.parent(models().getExistingFile(mcLoc("block/cake_slice2")))
					.texture("particle", "block/" + dimension + "/cake_side")
					.texture("bottom", "block/" + dimension + "/cake_bottom")
					.texture("top", "block/" + dimension + "/cake_top")
					.texture("side", "block/" + dimension + "/cake_side")
					.texture("inside", "block/" + dimension + "/cake_inner");
			ModelFile modelSlice36 = models().getBuilder(ForgeRegistries.BLOCKS.getKey(block).getPath() + "_slice3").renderType("cutout")
					.parent(models().getExistingFile(mcLoc("block/cake_slice3")))
					.texture("particle", "block/" + dimension + "/cake_side")
					.texture("bottom", "block/" + dimension + "/cake_bottom")
					.texture("top", "block/" + dimension + "/cake_top")
					.texture("side", "block/" + dimension + "/cake_side")
					.texture("inside", "block/" + dimension + "/cake_inner");
			ModelFile modelSlice4 = models().getBuilder(ForgeRegistries.BLOCKS.getKey(block).getPath() + "_slice4").renderType("cutout")
					.parent(models().getExistingFile(mcLoc("block/cake_slice4")))
					.texture("particle", "block/" + dimension + "/cake_side")
					.texture("bottom", "block/" + dimension + "/cake_bottom")
					.texture("top", "block/" + dimension + "/cake_top")
					.texture("side", "block/" + dimension + "/cake_side")
					.texture("inside", "block/" + dimension + "/cake_inner");
			ModelFile modelSlice5 = models().getBuilder(ForgeRegistries.BLOCKS.getKey(block).getPath() + "_slice5").renderType("cutout")
					.parent(models().getExistingFile(mcLoc("block/cake_slice5")))
					.texture("particle", "block/" + dimension + "/cake_side")
					.texture("bottom", "block/" + dimension + "/cake_bottom")
					.texture("top", "block/" + dimension + "/cake_top")
					.texture("side", "block/" + dimension + "/cake_side")
					.texture("inside", "block/" + dimension + "/cake_inner");
			ModelFile modelSlice6 = models().getBuilder(ForgeRegistries.BLOCKS.getKey(block).getPath() + "_slice6").renderType("cutout")
					.parent(models().getExistingFile(mcLoc("block/cake_slice6")))
					.texture("particle", "block/" + dimension + "/cake_side")
					.texture("bottom", "block/" + dimension + "/cake_bottom")
					.texture("top", "block/" + dimension + "/cake_top")
					.texture("side", "block/" + dimension + "/cake_side")
					.texture("inside", "block/" + dimension + "/cake_inner");

			getVariantBuilder(block)
					.forAllStates(state -> {
						int bites = state.getValue(BlockCakeBase.BITES);
						boolean untouched = bites == 0;
						return ConfiguredModel.builder()
								.modelFile(untouched ? model : models()
										.getBuilder(ForgeRegistries.BLOCKS.getKey(block).getPath() + "_slice" + bites)).build();
					});
		}
	}

	private static class PastryItemModels extends ItemModelProvider {
		public PastryItemModels(PackOutput packOutput, ExistingFileHelper helper) {
			super(packOutput, Reference.MOD_ID, helper);
		}

		@Override
		protected void registerModels() {
			TeleRegistry.ITEMS.getEntries().stream()
					.map(RegistryObject::get)
					.forEach(item -> {
						String path = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).getPath();
						singleTexture(path, mcLoc("item/generated"), "layer0", modLoc("item/" + path));
					});
		}
	}
}
