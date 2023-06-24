package com.mrbysco.telepastries.generator.server;

import com.mrbysco.telepastries.init.TeleRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class PastryLootProvider extends LootTableProvider {
	public PastryLootProvider(PackOutput packOutput) {
		super(packOutput, Set.of(), List.of(
				new SubProviderEntry(TeleBlocks::new, LootContextParamSets.BLOCK)));
	}

	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationresults) {
		map.forEach((name, table) -> table.validate(validationresults));
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
