package com.mrbysco.telepastries.generator.client;

import com.mrbysco.telepastries.Reference;
import com.mrbysco.telepastries.blocks.cake.BlockCakeBase;
import com.mrbysco.telepastries.init.TeleRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class PastryBlockStateProvider extends BlockStateProvider {

	public PastryBlockStateProvider(PackOutput packOutput, ExistingFileHelper helper) {
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

		getVariantBuilder(block)
				.forAllStates(state -> {
					int bites = state.getValue(BlockCakeBase.BITES);
					boolean untouched = bites == 0;
					return ConfiguredModel.builder()
							.modelFile(untouched ? model : models().getBuilder(ForgeRegistries.BLOCKS.getKey(block).getPath() + "_slice" + bites))
							.build();
				});
	}
}
