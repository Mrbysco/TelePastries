package com.mrbysco.telepastries.blocks.cake.compat;

import com.mrbysco.telepastries.blocks.cake.BlockCakeBase;
import com.mrbysco.telepastries.config.TeleConfig;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;

import java.util.List;

public class BlockLostCityCake extends BlockCakeBase {
    public BlockLostCityCake(AbstractBlock.Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(ModList.get().isLoaded("lostcities")) {
            return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
        } else {
            if(player.getActiveHand() == handIn && !worldIn.isRemote) {
                player.sendMessage(new TranslationTextComponent("telepastries.pastry.support.disabled", "lostcities").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
            }
            return ActionResultType.SUCCESS;
        }
    }

    @Override
    public boolean isRefillItem(ItemStack stack) {
        List<? extends String> items = TeleConfig.SERVER.lostCitiesCakeRefillItem.get();
        if (items == null || items.isEmpty()) return false;
        ResourceLocation registryLocation = stack.getItem().getRegistryName();
        return registryLocation != null && items.contains(registryLocation.toString());
    }

    @Override
    public RegistryKey<World> getCakeWorld() {
        return RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("lostcities", "lostcity"));
    }

    @Override
    public boolean consumeCake() {
        return TeleConfig.SERVER.consumeLostCitiesCake.get();
    }
}
