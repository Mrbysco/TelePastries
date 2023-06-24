package com.mrbysco.telepastries.generator.server;

import com.mrbysco.telepastries.init.TeleRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;

import java.util.function.Consumer;

public class PastryRecipeProvider extends RecipeProvider {
	public PastryRecipeProvider(PackOutput packOutput) {
		super(packOutput);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, TeleRegistry.END_CAKE.get())
				.pattern("EEE")
				.pattern("ECE")
				.pattern("EEE")
				.define('C', Items.CAKE)
				.define('E', Items.ENDER_EYE)
				.unlockedBy("has_cake", has(Items.CAKE))
				.unlockedBy("has_ender_eye", has(Items.ENDER_EYE))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, TeleRegistry.NETHER_CAKE.get())
				.pattern("OOO")
				.pattern("OCO")
				.pattern("OOO")
				.define('C', Items.CAKE)
				.define('O', Tags.Items.OBSIDIAN)
				.unlockedBy("has_cake", has(Items.CAKE))
				.unlockedBy("has_obsidian", has(Tags.Items.OBSIDIAN))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, TeleRegistry.OVERWORLD_CAKE.get())
				.pattern("SSS")
				.pattern("SCS")
				.pattern("SSS")
				.define('C', Items.CAKE)
				.define('S', ItemTags.SAPLINGS)
				.unlockedBy("has_cake", has(Items.CAKE))
				.unlockedBy("has_sapling", has(ItemTags.SAPLINGS))
				.save(consumer);

		//Twilight Forest cake recipe
		new ConditionalRecipe.Builder()
				.addCondition(
						new ModLoadedCondition("twilightforest")
				)
				.addRecipe(
						ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, TeleRegistry.TWILIGHT_CAKE.get())
								.pattern("YRY")
								.pattern("RCR")
								.pattern("YRY")
								.define('C', Items.CAKE)
								.define('R', Items.POPPY)
								.define('Y', Items.DANDELION)
								.unlockedBy("has_cake", has(Items.CAKE))
								.unlockedBy("has_poppy", has(Items.POPPY))
								.unlockedBy("has_dandelion", has(Items.DANDELION))
								::save
				)
				.build(consumer, TeleRegistry.TWILIGHT_CAKE.getId());
	}
}
