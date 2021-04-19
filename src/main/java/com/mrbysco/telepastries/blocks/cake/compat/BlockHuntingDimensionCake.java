package com.mrbysco.telepastries.blocks.cake.compat;

import com.mrbysco.telepastries.blocks.cake.BlockCakeBase;
import net.minecraft.block.AbstractBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class BlockHuntingDimensionCake extends BlockCakeBase {
    public BlockHuntingDimensionCake(AbstractBlock.Properties properties) {
        super(properties);
    }
//
//    @Override
//    public void teleportToDimension(IWorld world, BlockPos pos, PlayerEntity player) {
//        super.teleportToDimension(world, pos, player);
//    }
//
//    @Override
//    public Item getRefillItem() {
//        return Item.REGISTRY.getObject(new ResourceLocation(TeleConfig.pastriesCompat.huntingDimension.huntingDimensionCakeRefillItem));
//    }
//
//    @Override
//    public RegistryKey<DimensionType> getCakeDimension() {
//        return net.darkhax.huntingdim.handler.ConfigurationHandler.dimensionId;
//    }
//
//    @Override
//    public boolean consumeCake() {
//        return TeleConfig.pastriesCompat.huntingDimension.consumeHuntingDimensionCake;
//    }
}