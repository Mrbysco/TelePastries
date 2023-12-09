package com.mrbysco.telepastries.generator.client;

import com.mrbysco.telepastries.Reference;
import com.mrbysco.telepastries.init.TeleRegistry;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

public class PastryItemModelProvider extends ItemModelProvider {
	public PastryItemModelProvider(PackOutput packOutput, ExistingFileHelper helper) {
		super(packOutput, Reference.MOD_ID, helper);
	}

	@Override
	protected void registerModels() {
		TeleRegistry.ITEMS.getEntries().stream()
				.map(DeferredHolder::getId)
				.forEach(itemLocation -> {
					String path = itemLocation.getPath();
					singleTexture(path, mcLoc("item/generated"), "layer0", modLoc("item/" + path));
				});
	}
}
