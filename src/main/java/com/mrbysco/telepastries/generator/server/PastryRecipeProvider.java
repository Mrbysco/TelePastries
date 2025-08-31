package com.mrbysco.telepastries.generator.server;

import com.mrbysco.telepastries.init.TeleRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

import java.util.concurrent.CompletableFuture;

public class PastryRecipeProvider extends RecipeProvider {
	public PastryRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
		super(provider, recipeOutput);
	}

	@Override
	protected void buildRecipes() {
		shaped(RecipeCategory.TRANSPORTATION, TeleRegistry.END_CAKE.get())
				.pattern("EEE")
				.pattern("ECE")
				.pattern("EEE")
				.define('C', Items.CAKE)
				.define('E', Items.ENDER_EYE)
				.unlockedBy("has_cake", has(Items.CAKE))
				.unlockedBy("has_ender_eye", has(Items.ENDER_EYE))
				.save(output);

		shaped(RecipeCategory.TRANSPORTATION, TeleRegistry.NETHER_CAKE.get())
				.pattern("OOO")
				.pattern("OCO")
				.pattern("OOO")
				.define('C', Items.CAKE)
				.define('O', Tags.Items.OBSIDIANS)
				.unlockedBy("has_cake", has(Items.CAKE))
				.unlockedBy("has_obsidian", has(Tags.Items.OBSIDIANS))
				.save(output);

		shaped(RecipeCategory.TRANSPORTATION, TeleRegistry.OVERWORLD_CAKE.get())
				.pattern("SSS")
				.pattern("SCS")
				.pattern("SSS")
				.define('C', Items.CAKE)
				.define('S', ItemTags.SAPLINGS)
				.unlockedBy("has_cake", has(Items.CAKE))
				.unlockedBy("has_sapling", has(ItemTags.SAPLINGS))
				.save(output);

		//Twilight Forest cake recipe
		RecipeOutput twilightLoaded = output.withConditions(new ModLoadedCondition("twilightforest"));
		shaped(RecipeCategory.TRANSPORTATION, TeleRegistry.TWILIGHT_CAKE.get())
				.pattern("YRY")
				.pattern("RCR")
				.pattern("YRY")
				.define('C', Items.CAKE)
				.define('R', Items.POPPY)
				.define('Y', Items.DANDELION)
				.unlockedBy("has_cake", has(Items.CAKE))
				.unlockedBy("has_poppy", has(Items.POPPY))
				.unlockedBy("has_dandelion", has(Items.DANDELION))
				.save(twilightLoaded);
	}

	public static class Runner extends RecipeProvider.Runner {
		public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
			super(output, completableFuture);
		}

		@Override
		protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
			return new PastryRecipeProvider(provider, recipeOutput);
		}

		@Override
		public String getName() {
			return "TelePastries Recipes";
		}
	}
}
