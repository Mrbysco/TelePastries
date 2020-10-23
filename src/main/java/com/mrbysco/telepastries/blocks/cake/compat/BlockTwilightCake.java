package com.mrbysco.telepastries.blocks.cake.compat;

import com.mrbysco.telepastries.blocks.cake.BlockCakeBase;
import com.mrbysco.telepastries.config.TeleConfig;
import net.minecraft.block.AbstractBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;

import java.util.List;

public class BlockTwilightCake extends BlockCakeBase {
    public BlockTwilightCake(AbstractBlock.Properties properties) {
        super(properties);
    }

    @Override
    public void teleportToDimension(IWorld world, BlockPos pos, PlayerEntity player) {
        if(ModList.get().isLoaded("twilightforest")) {
            super.teleportToDimension(world, pos, player);
        }
        player.sendMessage(new TranslationTextComponent("telepastries.pastry.support.disabled", "twilightforest").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
    }

    @Override
    public boolean isRefillItem(ItemStack stack) {
        List<? extends String> items = TeleConfig.SERVER.TwilightCakeRefillItems.get();
        if (items == null || items.isEmpty()) return false;
        ResourceLocation registryLocation = stack.getItem().getRegistryName();
        return registryLocation != null && items.contains(registryLocation.toString());
    }

    @Override
    public RegistryKey<World> getCakeWorld() {
        return RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("twilightforest", "twilightforest"));
    }

    @Override
    public boolean consumeCake() {
        return TeleConfig.SERVER.consumeTwilightCake.get();
    }
}
