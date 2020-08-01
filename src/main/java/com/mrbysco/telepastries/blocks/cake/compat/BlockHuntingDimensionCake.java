package com.mrbysco.telepastries.blocks.cake.compat;

import com.mrbysco.telepastries.blocks.cake.BlockCakeBase;
import net.minecraft.block.AbstractBlock;

public class BlockHuntingDimensionCake extends BlockCakeBase {
    public BlockHuntingDimensionCake(AbstractBlock.Properties properties) {
        super(properties);
    }
//
//    @Override
//    public void teleportToDimension(World world, BlockPos pos, PlayerEntity player) {
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