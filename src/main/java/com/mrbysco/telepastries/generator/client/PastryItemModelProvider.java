package com.mrbysco.telepastries.generator.client;

import com.mrbysco.telepastries.Reference;
import com.mrbysco.telepastries.init.TeleRegistry;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

public class PastryItemModelProvider extends ItemModelProvider {
	public PastryItemModelProvider(PackOutput packOutput, ExistingFileHelper helper) {
		super(packOutput, Reference.MOD_ID, helper);
	}

	@Override
	protected void registerModels() {
		TeleRegistry.ITEMS.getEntries().stream()
				.map(RegistryObject::get)
				.forEach(item -> {
					String path = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).getPath();
					singleTexture(path, mcLoc("item/generated"), "layer0", modLoc("item/" + path));
				});
	}
}
