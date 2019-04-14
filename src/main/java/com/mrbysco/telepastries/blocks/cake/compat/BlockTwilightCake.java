package com.mrbysco.telepastries.blocks.cake.compat;

import com.mrbysco.telepastries.blocks.cake.BlockCakeBase;
import com.mrbysco.telepastries.config.TeleConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTwilightCake extends BlockCakeBase {
    public BlockTwilightCake(String registry) {
        super(registry);
    }

    @Override
    public void teleportToDimension(World world, BlockPos pos, EntityPlayer player) {
        super.teleportToDimension(world, pos, player);
    }

    @Override
    public Item getRefillItem() {
        return Item.REGISTRY.getObject(new ResourceLocation(TeleConfig.pastriesCompat.twilightForest.twilightForestCakeRefillItem));
    }

    @Override
    public int getCakeDimension() {
        return twilightforest.TFConfig.dimension.dimensionID;
    }

    @Override
    public boolean consumeCake() {
        return TeleConfig.pastriesCompat.twilightForest.consumeTwilightForestCake;
    }
}
