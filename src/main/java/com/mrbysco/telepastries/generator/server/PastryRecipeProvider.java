package com.mrbysco.telepastries.generator.server;

import com.mrbysco.telepastries.init.TeleRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

import java.util.concurrent.CompletableFuture;

public class PastryRecipeProvider extends RecipeProvider {
	public PastryRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	@Override
	protected void buildRecipes(RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, TeleRegistry.END_CAKE.get())
				.pattern("EEE")
				.pattern("ECE")
				.pattern("EEE")
				.define('C', Items.CAKE)
				.define('E', Items.ENDER_EYE)
				.unlockedBy("has_cake", has(Items.CAKE))
				.unlockedBy("has_ender_eye", has(Items.ENDER_EYE))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, TeleRegistry.NETHER_CAKE.get())
				.pattern("OOO")
				.pattern("OCO")
				.pattern("OOO")
				.define('C', Items.CAKE)
				.define('O', Tags.Items.OBSIDIAN)
				.unlockedBy("has_cake", has(Items.CAKE))
				.unlockedBy("has_obsidian", has(Tags.Items.OBSIDIAN))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, TeleRegistry.OVERWORLD_CAKE.get())
				.pattern("SSS")
				.pattern("SCS")
				.pattern("SSS")
				.define('C', Items.CAKE)
				.define('S', ItemTags.SAPLINGS)
				.unlockedBy("has_cake", has(Items.CAKE))
				.unlockedBy("has_sapling", has(ItemTags.SAPLINGS))
				.save(recipeOutput);

		//Twilight Forest cake recipe
		RecipeOutput twilightLoaded = recipeOutput.withConditions(new ModLoadedCondition("twilightforest"));
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
				.save(twilightLoaded, TeleRegistry.TWILIGHT_CAKE.getId());
	}
}
