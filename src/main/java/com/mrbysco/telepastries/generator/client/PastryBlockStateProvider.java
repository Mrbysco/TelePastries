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
import net.minecraftforge.registries.RegistryObject;

public class PastryBlockStateProvider extends BlockStateProvider {

	public PastryBlockStateProvider(PackOutput packOutput, ExistingFileHelper helper) {
		super(packOutput, Reference.MOD_ID, helper);
	}

	@Override
	protected void registerStatesAndModels() {
		makeCake(TeleRegistry.OVERWORLD_CAKE, "overworld");
		makeCake(TeleRegistry.NETHER_CAKE, "nether");
		makeCake(TeleRegistry.END_CAKE, "end");
		makeCake(TeleRegistry.TWILIGHT_CAKE, "twilight");
		makeCake(TeleRegistry.LOST_CITY_CAKE, "cities");
		makeCake(TeleRegistry.CUSTOM_CAKE, "custom");
		makeCake(TeleRegistry.CUSTOM_CAKE2, "custom");
		makeCake(TeleRegistry.CUSTOM_CAKE3, "custom");
	}

	private void makeCake(RegistryObject<Block> holder, String dimension) {
		String path = holder.getId().getPath();
		ModelFile model = models().getBuilder(path).renderType("cutout")
				.parent(models().getExistingFile(mcLoc("block/cake")))
				.texture("particle", "block/" + dimension + "/cake_side")
				.texture("bottom", "block/" + dimension + "/cake_bottom")
				.texture("top", "block/" + dimension + "/cake_top")
				.texture("side", "block/" + dimension + "/cake_side");

		//Generate slice models
		models().getBuilder(path + "_slice1").renderType("cutout")
				.parent(models().getExistingFile(mcLoc("block/cake_slice1")))
				.texture("particle", "block/" + dimension + "/cake_side")
				.texture("bottom", "block/" + dimension + "/cake_bottom")
				.texture("top", "block/" + dimension + "/cake_top")
				.texture("side", "block/" + dimension + "/cake_side")
				.texture("inside", "block/" + dimension + "/cake_inner");
		models().getBuilder(path + "_slice2").renderType("cutout")
				.parent(models().getExistingFile(mcLoc("block/cake_slice2")))
				.texture("particle", "block/" + dimension + "/cake_side")
				.texture("bottom", "block/" + dimension + "/cake_bottom")
				.texture("top", "block/" + dimension + "/cake_top")
				.texture("side", "block/" + dimension + "/cake_side")
				.texture("inside", "block/" + dimension + "/cake_inner");
		models().getBuilder(path + "_slice3").renderType("cutout")
				.parent(models().getExistingFile(mcLoc("block/cake_slice3")))
				.texture("particle", "block/" + dimension + "/cake_side")
				.texture("bottom", "block/" + dimension + "/cake_bottom")
				.texture("top", "block/" + dimension + "/cake_top")
				.texture("side", "block/" + dimension + "/cake_side")
				.texture("inside", "block/" + dimension + "/cake_inner");
		models().getBuilder(path + "_slice4").renderType("cutout")
				.parent(models().getExistingFile(mcLoc("block/cake_slice4")))
				.texture("particle", "block/" + dimension + "/cake_side")
				.texture("bottom", "block/" + dimension + "/cake_bottom")
				.texture("top", "block/" + dimension + "/cake_top")
				.texture("side", "block/" + dimension + "/cake_side")
				.texture("inside", "block/" + dimension + "/cake_inner");
		models().getBuilder(path + "_slice5").renderType("cutout")
				.parent(models().getExistingFile(mcLoc("block/cake_slice5")))
				.texture("particle", "block/" + dimension + "/cake_side")
				.texture("bottom", "block/" + dimension + "/cake_bottom")
				.texture("top", "block/" + dimension + "/cake_top")
				.texture("side", "block/" + dimension + "/cake_side")
				.texture("inside", "block/" + dimension + "/cake_inner");
		models().getBuilder(path + "_slice6").renderType("cutout")
				.parent(models().getExistingFile(mcLoc("block/cake_slice6")))
				.texture("particle", "block/" + dimension + "/cake_side")
				.texture("bottom", "block/" + dimension + "/cake_bottom")
				.texture("top", "block/" + dimension + "/cake_top")
				.texture("side", "block/" + dimension + "/cake_side")
				.texture("inside", "block/" + dimension + "/cake_inner");

		getVariantBuilder(holder.get())
				.forAllStates(state -> {
					int bites = state.getValue(BlockCakeBase.BITES);
					boolean untouched = bites == 0;
					return ConfiguredModel.builder()
							.modelFile(untouched ? model : models().getBuilder(path + "_slice" + bites))
							.build();
				});
	}
}
