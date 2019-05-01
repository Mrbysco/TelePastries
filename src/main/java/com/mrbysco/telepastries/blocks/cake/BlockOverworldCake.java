package com.mrbysco.telepastries.blocks.cake;

import com.mrbysco.telepastries.config.TeleConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class BlockOverworldCake extends BlockCakeBase {
    public BlockOverworldCake(String registry) {
        super(registry);
    }

    @Override
    public void teleportToDimension(World world, BlockPos pos, EntityPlayer player) {
        super.teleportToDimension(world, pos, player);
    }

    @Override
    public Item getRefillItem() {
        return Item.REGISTRY.getObject(new ResourceLocation(TeleConfig.pastries.overworld.overworldCakeRefillItem));
    }

    @Override
    public int getCakeDimension(World worldIn) {
        int cakeDim = getCakeDimension();
        if(worldIn.getWorldType().getName().equals("compactsky")) {
            int[] ids = DimensionManager.getDimensions(DimensionType.byName("CompactMachines"));
            cakeDim = ids[0];
        }

        return cakeDim;
    }

    @Override
    public int getCakeDimension() {
        return 0;
    }

    @Override
    public boolean consumeCake() {
        return TeleConfig.pastries.overworld.consumeOverworldCake;
    }
}
