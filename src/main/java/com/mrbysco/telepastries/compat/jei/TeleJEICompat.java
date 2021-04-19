package com.mrbysco.telepastries.compat.jei;

import com.mrbysco.telepastries.Reference;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class TeleJEICompat implements IModPlugin {
	private static final ResourceLocation UID = new ResourceLocation(Reference.MOD_ID, "jei_plugin");

	@Override
	public ResourceLocation getPluginUid() {
		return UID;
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
//		registration.addIngredientInfo(new ItemStack(Items.APPLE), VanillaTypes.ITEM, I18n.format(""));
	}
}
