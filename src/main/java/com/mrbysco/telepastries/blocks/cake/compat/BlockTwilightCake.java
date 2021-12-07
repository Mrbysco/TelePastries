package com.mrbysco.telepastries.blocks.cake.compat;

import com.mrbysco.telepastries.blocks.cake.BlockCakeBase;
import com.mrbysco.telepastries.config.TeleConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fml.ModList;

import java.util.List;

public class BlockTwilightCake extends BlockCakeBase {
    public BlockTwilightCake(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if(ModList.get().isLoaded("twilightforest")) {
            return super.use(state, worldIn, pos, player, handIn, hit);
        } else {
            if(player.getUsedItemHand() == handIn && !worldIn.isClientSide) {
                player.sendMessage(new TranslatableComponent("telepastries.pastry.support.disabled", "twilightforest").withStyle(ChatFormatting.RED), Util.NIL_UUID);
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public boolean isRefillItem(ItemStack stack) {
        List<? extends String> items = TeleConfig.SERVER.twilightCakeRefillItems.get();
        if (items == null || items.isEmpty()) return false;
        ResourceLocation registryLocation = stack.getItem().getRegistryName();
        return registryLocation != null && items.contains(registryLocation.toString());
    }

    @Override
    public ResourceKey<Level> getCakeWorld() {
        return ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("twilightforest", "twilight_forest"));
    }

    @Override
    public boolean consumeCake() {
        return TeleConfig.SERVER.consumeTwilightCake.get();
    }
}
