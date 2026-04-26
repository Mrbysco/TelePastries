package com.mrbysco.telepastries.generator.client;

import com.mrbysco.telepastries.Reference;
import com.mrbysco.telepastries.blocks.cake.BlockCakeBase;
import com.mrbysco.telepastries.init.TeleRegistry;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.registries.DeferredBlock;

public class PastryModelProvider extends ModelProvider {
	public static final ModelTemplate CAKE = ModelTemplates.create("cake", TextureSlot.PARTICLE, TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE);
	public static final ModelTemplate CAKE_SLICE1 = ModelTemplates.create("cake_slice1", TextureSlot.PARTICLE, TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.INSIDE);
	public static final ModelTemplate CAKE_SLICE2 = ModelTemplates.create("cake_slice2", TextureSlot.PARTICLE, TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.INSIDE);
	public static final ModelTemplate CAKE_SLICE3 = ModelTemplates.create("cake_slice3", TextureSlot.PARTICLE, TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.INSIDE);
	public static final ModelTemplate CAKE_SLICE4 = ModelTemplates.create("cake_slice4", TextureSlot.PARTICLE, TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.INSIDE);
	public static final ModelTemplate CAKE_SLICE5 = ModelTemplates.create("cake_slice5", TextureSlot.PARTICLE, TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.INSIDE);
	public static final ModelTemplate CAKE_SLICE6 = ModelTemplates.create("cake_slice6", TextureSlot.PARTICLE, TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.INSIDE);

	public PastryModelProvider(PackOutput packOutput) {
		super(packOutput, Reference.MOD_ID);
	}

	@Override
	protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
		makeCake(blockModels, TeleRegistry.OVERWORLD_CAKE, "overworld");
		makeCake(blockModels, TeleRegistry.NETHER_CAKE, "nether");
		makeCake(blockModels, TeleRegistry.END_CAKE, "end");
		makeCake(blockModels, TeleRegistry.TWILIGHT_CAKE, "twilight");
		makeCake(blockModels, TeleRegistry.LOST_CITY_CAKE, "cities");
		makeCake(blockModels, TeleRegistry.CUSTOM_CAKE, "custom");
		makeCake(blockModels, TeleRegistry.CUSTOM_CAKE2, "custom");
		makeCake(blockModels, TeleRegistry.CUSTOM_CAKE3, "custom");
	}

	private void makeCake(BlockModelGenerators blockModels, DeferredBlock<? extends BlockCakeBase> holder, String dimension) {
		Identifier side = modLocation("block/" + dimension + "/cake_side");
		Identifier top = modLocation("block/" + dimension + "/cake_top");
		Identifier bottom = modLocation("block/" + dimension + "/cake_bottom");
		Identifier inside = modLocation("block/" + dimension + "/cake_inner");

		TextureMapping cakeMapping = TextureMapping
				.singleSlot(TextureSlot.PARTICLE, new Material(side))
				.put(TextureSlot.SIDE, new Material(side))
				.put(TextureSlot.TOP, new Material(top))
				.put(TextureSlot.BOTTOM, new Material(bottom));
		TextureMapping cakeSliceMapping = cakeMapping.copy()
				.put(TextureSlot.INSIDE, new Material(inside));

		Identifier model = CAKE.create(holder.get(), cakeMapping, blockModels.modelOutput);
		Identifier slice1 = CAKE_SLICE1.createWithSuffix(holder.get(), "_slice1", cakeSliceMapping, blockModels.modelOutput);
		Identifier slice2 = CAKE_SLICE2.createWithSuffix(holder.get(), "_slice2", cakeSliceMapping, blockModels.modelOutput);
		Identifier slice3 = CAKE_SLICE3.createWithSuffix(holder.get(), "_slice3", cakeSliceMapping, blockModels.modelOutput);
		Identifier slice4 = CAKE_SLICE4.createWithSuffix(holder.get(), "_slice4", cakeSliceMapping, blockModels.modelOutput);
		Identifier slice5 = CAKE_SLICE5.createWithSuffix(holder.get(), "_slice5", cakeSliceMapping, blockModels.modelOutput);
		Identifier slice6 = CAKE_SLICE6.createWithSuffix(holder.get(), "_slice6", cakeSliceMapping, blockModels.modelOutput);

		blockModels.registerSimpleFlatItemModel(holder.asItem());
		blockModels.blockStateOutput
				.accept(
						MultiVariantGenerator.dispatch(holder.get())
								.with(
										PropertyDispatch.initial(BlockStateProperties.BITES)
												.select(0, BlockModelGenerators.plainVariant(model))
												.select(1, BlockModelGenerators.plainVariant(slice1))
												.select(2, BlockModelGenerators.plainVariant(slice2))
												.select(3, BlockModelGenerators.plainVariant(slice3))
												.select(4, BlockModelGenerators.plainVariant(slice4))
												.select(5, BlockModelGenerators.plainVariant(slice5))
												.select(6, BlockModelGenerators.plainVariant(slice6))
								)
				);
	}
}
