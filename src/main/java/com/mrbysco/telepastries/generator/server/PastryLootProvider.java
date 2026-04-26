package com.mrbysco.telepastries.generator.server;

import com.mrbysco.telepastries.init.TeleRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class PastryLootProvider extends LootTableProvider {
	public PastryLootProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, Set.of(), List.of(
				new SubProviderEntry(TeleBlocks::new, LootContextParamSets.BLOCK)
		), lookupProvider);
	}

	private static class TeleBlocks extends BlockLootSubProvider {

		protected TeleBlocks(HolderLookup.Provider provider) {
			super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
		}

		@Override
		protected void generate() {
			for (DeferredHolder<Block, ? extends Block> blockObject : TeleRegistry.BLOCKS.getEntries()) {
				this.add(blockObject.get(), noDrop());
			}
		}

		@Override
		protected Iterable<Block> getKnownBlocks() {
			return TeleRegistry.BLOCKS.getEntries().stream().map(holder -> (Block) holder.get())::iterator;
		}
	}
}
