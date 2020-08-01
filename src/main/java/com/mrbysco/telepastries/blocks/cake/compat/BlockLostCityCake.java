package com.mrbysco.telepastries.blocks.cake.compat;

import com.mrbysco.telepastries.blocks.cake.BlockCakeBase;
import net.minecraft.block.AbstractBlock;

public class BlockLostCityCake extends BlockCakeBase {
    public BlockLostCityCake(AbstractBlock.Properties properties) {
        super(properties);
    }
//
//    @Override
//    public void teleportToDimension(World world, BlockPos pos, PlayerEntity player) {
//        super.teleportToDimension(world, pos, player);
//    }
//    @Override
//    public Item getRefillItem() {
//        return Item.REGISTRY.getObject(new ResourceLocation(TeleConfig.pastriesCompat.lostCities.lostCitiesCakeRefillItem));
//    }
//
//    @Override
//    public RegistryKey<DimensionType> getCakeDimension() {
//        return mcjty.lostcities.config.LostCityConfiguration.DIMENSION_ID;
//    }
//
//    @Override
//    public boolean consumeCake() {
//        return TeleConfig.pastriesCompat.lostCities.consumeLostCitiesCake;
//    }
}
