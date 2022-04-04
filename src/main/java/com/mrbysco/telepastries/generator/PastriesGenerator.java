package com.mrbysco.telepastries.generator;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mrbysco.telepastries.Reference;
import com.mrbysco.telepastries.blocks.cake.BlockCakeBase;
import com.mrbysco.telepastries.init.TeleRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.mrbysco.telepastries.init.TeleRegistry.CUSTOM_CAKE;
import static com.mrbysco.telepastries.init.TeleRegistry.CUSTOM_CAKE2;
import static com.mrbysco.telepastries.init.TeleRegistry.CUSTOM_CAKE3;
import static com.mrbysco.telepastries.init.TeleRegistry.END_CAKE;
import static com.mrbysco.telepastries.init.TeleRegistry.ITEMS;
import static com.mrbysco.telepastries.init.TeleRegistry.LOST_CITY_CAKE;
import static com.mrbysco.telepastries.init.TeleRegistry.NETHER_CAKE;
import static com.mrbysco.telepastries.init.TeleRegistry.OVERWORLD_CAKE;
import static com.mrbysco.telepastries.init.TeleRegistry.TWILIGHT_CAKE;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PastriesGenerator {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(new Loots(generator));
		}
		if (event.includeClient()) {
			generator.addProvider(new PastryBlockStates(generator, helper));
			generator.addProvider(new PastryItemModels(generator, helper));
		}
	}

	private static class Loots extends LootTableProvider {
		public Loots(DataGenerator gen) {
			super(gen);
		}

		@Override
		protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
			return ImmutableList.of(
					Pair.of(TeleBlocks::new, LootContextParamSets.BLOCK)
			);
		}

		@Override
		protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationresults) {
			map.forEach((name, table) -> LootTables.validate(validationresults, name, table));
		}

		private static class TeleBlocks extends BlockLoot {
			@Override
			protected void addTables() {
				for(RegistryObject<Block> blockObject : TeleRegistry.BLOCKS.getEntries()) {
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

		public PastryBlockStates(DataGenerator gen, ExistingFileHelper helper) {
			super(gen, Reference.MOD_ID, helper);
		}

		@Override
		protected void registerStatesAndModels() {
			makeCake(OVERWORLD_CAKE.get(), "overworld");
			makeCake(NETHER_CAKE.get(), "nether");
			makeCake(END_CAKE.get(), "end");
			makeCake(TWILIGHT_CAKE.get(), "twilight");
			makeCake(LOST_CITY_CAKE.get(), "cities");
			makeCake(CUSTOM_CAKE.get(), "custom");
			makeCake(CUSTOM_CAKE2.get(), "custom");
			makeCake(CUSTOM_CAKE3.get(), "custom");
		}

		private void makeCake(Block block, String dimension) {
			ModelFile model = models().getBuilder(block.getRegistryName().getPath())
					.parent(models().getExistingFile(mcLoc("block/cake")))
					.texture("particle", "block/" + dimension + "/cake_side")
					.texture("bottom", "block/" + dimension + "/cake_bottom")
					.texture("top", "block/" + dimension + "/cake_top")
					.texture("side", "block/" + dimension + "/cake_side");

			ModelFile modelSlice1 = models().getBuilder(block.getRegistryName().getPath() + "_slice1")
					.parent(models().getExistingFile(mcLoc("block/cake_slice1")))
					.texture("particle", "block/" + dimension + "/cake_side")
					.texture("bottom", "block/" + dimension + "/cake_bottom")
					.texture("top", "block/" + dimension + "/cake_top")
					.texture("side", "block/" + dimension + "/cake_side")
					.texture("inside", "block/" + dimension + "/cake_inner");
			ModelFile modelSlice2 = models().getBuilder(block.getRegistryName().getPath() + "_slice2")
					.parent(models().getExistingFile(mcLoc("block/cake_slice2")))
					.texture("particle", "block/" + dimension + "/cake_side")
					.texture("bottom", "block/" + dimension + "/cake_bottom")
					.texture("top", "block/" + dimension + "/cake_top")
					.texture("side", "block/" + dimension + "/cake_side")
					.texture("inside", "block/" + dimension + "/cake_inner");
			ModelFile modelSlice36 = models().getBuilder(block.getRegistryName().getPath() + "_slice3")
					.parent(models().getExistingFile(mcLoc("block/cake_slice3")))
					.texture("particle", "block/" + dimension + "/cake_side")
					.texture("bottom", "block/" + dimension + "/cake_bottom")
					.texture("top", "block/" + dimension + "/cake_top")
					.texture("side", "block/" + dimension + "/cake_side")
					.texture("inside", "block/" + dimension + "/cake_inner");
			ModelFile modelSlice4 = models().getBuilder(block.getRegistryName().getPath() + "_slice4")
					.parent(models().getExistingFile(mcLoc("block/cake_slice4")))
					.texture("particle", "block/" + dimension + "/cake_side")
					.texture("bottom", "block/" + dimension + "/cake_bottom")
					.texture("top", "block/" + dimension + "/cake_top")
					.texture("side", "block/" + dimension + "/cake_side")
					.texture("inside", "block/" + dimension + "/cake_inner");
			ModelFile modelSlice5 = models().getBuilder(block.getRegistryName().getPath() + "_slice5")
					.parent(models().getExistingFile(mcLoc("block/cake_slice5")))
					.texture("particle", "block/" + dimension + "/cake_side")
					.texture("bottom", "block/" + dimension + "/cake_bottom")
					.texture("top", "block/" + dimension + "/cake_top")
					.texture("side", "block/" + dimension + "/cake_side")
					.texture("inside", "block/" + dimension + "/cake_inner");
			ModelFile modelSlice6 = models().getBuilder(block.getRegistryName().getPath() + "_slice6")
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
								.modelFile(untouched ? model : models().getBuilder(block.getRegistryName().getPath() + "_slice" + bites)).build();
					});
		}
	}

	private static class PastryItemModels extends ItemModelProvider {
		public PastryItemModels(DataGenerator gen, ExistingFileHelper helper) {
			super(gen, Reference.MOD_ID, helper);
		}

		@Override
		protected void registerModels() {
			ITEMS.getEntries().stream()
					.map(RegistryObject::get)
					.forEach(item -> {
						String path = Objects.requireNonNull(item.getRegistryName()).getPath();
						singleTexture(path, mcLoc("item/generated"), "layer0", modLoc("item/" + path));
					});
		}
	}
}
