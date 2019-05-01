package com.mrbysco.telepastries.util;

import com.mrbysco.telepastries.Reference;
import com.mrbysco.telepastries.config.TeleConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

public class CakeTeleporter extends Teleporter {
    private BlockPos position;

    public CakeTeleporter(WorldServer world, BlockPos position) {
        super(world);
        this.position = position;
    }

    public void teleportToDimension(EntityPlayer player, int dimension, BlockPos pos) {
        BlockPos dimPos = getDimensionPosition((EntityPlayerMP)player, dimension, pos);
        teleportToDimension(player, dimension, (double)dimPos.getX() + 0.5D, (double)dimPos.getY(), (double)dimPos.getZ() + 0.5D);
    }

    public void teleportToDimension(EntityPlayer player, int dimension, double x, double y, double z) {
        int oldDimension = player.getEntityWorld().provider.getDimension();
        EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player;
        this.world.playSound(null, x + 0.5D, y + 0.5D, z + 0.5D, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.MASTER, 0.25F, this.world.rand.nextFloat() * 0.4F + 0.8F);
        if (!player.capabilities.isCreativeMode) {
            player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 200, 200, false, false));
        }

        if (this.world != null && this.world.getMinecraftServer() != null) {
            PlayerList playerList = this.world.getMinecraftServer().getPlayerList();
            playerList.transferPlayerToDimension(entityPlayerMP, dimension, this);

            player.setPositionAndUpdate(x, y, z);

            if(dimension == -1)
            {
                relocateInNether(entityPlayerMP, x, y, z);
            }

            if(dimension == 1) {
                endPlacement(entityPlayerMP);
            }

            if (oldDimension == 1) {
                player.setPositionAndUpdate(x, y, z);
                this.world.spawnEntity(player);
                this.world.updateEntityWithOptionalForce(player, false);
            }


            if(dimension != 0) {
                customCompat(entityPlayerMP, dimension, x, y, z);
            }
        } else {
            throw new IllegalArgumentException("Dimension: " + dimension + " doesn't exist!");
        }
    }

    private void customCompat(EntityPlayerMP playerMP, int dimension, double x, double y, double z) {

        if(Loader.isModLoaded("twilightforest")) {
            if(dimension == twilightforest.TFConfig.dimension.dimensionID){
                twilightPlacement(playerMP, x, y, z);
            }
        }

        if(Loader.isModLoaded("lostcities")) {
            if(dimension == mcjty.lostcities.config.LostCityConfiguration.DIMENSION_ID){
                lostCitiesPlacement(playerMP, x, y, z);
            }
        }
        if(Loader.isModLoaded("huntingdim")) {
            if(dimension == net.darkhax.huntingdim.handler.ConfigurationHandler.dimensionId) {
                huntingDimensionPlacement(playerMP, x, y, z);
            }
        }
    }

    public void addDimensionPosition(EntityPlayerMP player, int oldDim, BlockPos position) {
        NBTTagCompound playerData = player.getEntityData();
        NBTTagCompound data = getTag(playerData, EntityPlayer.PERSISTED_NBT_TAG);

        if(oldDim == 1) {
            BlockPos spawnPlatform = player.getServer().getWorld(1).getSpawnCoordinate();
            data.setLong(Reference.MOD_PREFIX + oldDim, spawnPlatform.toLong());
        } else {
            data.setLong(Reference.MOD_PREFIX + oldDim, position.toLong());
        }

        playerData.setTag(EntityPlayer.PERSISTED_NBT_TAG, data);
    }

    public BlockPos getDimensionPosition(EntityPlayerMP player, int dim, BlockPos position) {
        NBTTagCompound playerData = player.getEntityData();
        NBTTagCompound data = getTag(playerData, EntityPlayer.PERSISTED_NBT_TAG);

        BlockPos dimPos = position;
        if(data.hasKey(Reference.MOD_PREFIX + dim)) {
            dimPos = BlockPos.fromLong(data.getLong(Reference.MOD_PREFIX + dim));
        }

        return dimPos;
    }

    public boolean hasDimensionPosition(EntityPlayerMP player, int dim) {
        NBTTagCompound playerData = player.getEntityData();
        NBTTagCompound data = getTag(playerData, EntityPlayer.PERSISTED_NBT_TAG);

        return data.hasKey(Reference.MOD_PREFIX + dim);
    }

    private NBTTagCompound getTag(NBTTagCompound tag, String key) {
        if(tag == null || !tag.hasKey(key)) {
            return new NBTTagCompound();
        }
        return tag.getCompoundTag(key);
    }

    @Override
    public void placeInPortal(Entity entityIn, float rotationYaw) {
        entityIn.setPosition(position.getX(), position.getY(), position.getZ());
        entityIn.motionX = 0.0D;
        entityIn.motionY = 0.0D;
        entityIn.motionZ = 0.0D;
    }

    private void endPlacement(EntityPlayerMP player) {
        //TelePastries.logger.debug("before endPlacement");

        int i = MathHelper.floor(player.posX);
        int j = MathHelper.floor(player.posY) - 1;
        int k = MathHelper.floor(player.posZ);

        for (int j1 = -2; j1 <= 2; ++j1)
        {
            for (int k1 = -2; k1 <= 2; ++k1)
            {
                for (int l1 = -1; l1 < 3; ++l1)
                {
                    int i2 = i + k1 * 1 + j1 * 0;
                    int j2 = j + l1;
                    int k2 = k + k1 * 0 - j1 * 1;
                    boolean flag = l1 < 0;
                    this.world.setBlockState(new BlockPos(i2, j2, k2), flag ? Blocks.OBSIDIAN.getDefaultState() : Blocks.AIR.getDefaultState());
                }
            }
        }

        //TelePastries.logger.debug("after endPlacement");
    }

    private void relocateInNether(EntityPlayerMP playerMP, double x, double y, double z){
        if(TeleConfig.pastries.nether.netherCake1x1Logic) {
            protectPlayer(playerMP, new BlockPos(x, y, z));
        } else {
            if  (hasDimensionPosition(playerMP, -1)) {
                //TelePastries.logger.debug("at relocateInNether before protectPlayer");
                protectPlayer(playerMP, new BlockPos(x, y, z));
                //TelePastries.logger.debug("at relocateInNether after protectPlayer");
            } else {
                double moveFactor = 0.125D;
                double d0 = MathHelper.clamp(x * moveFactor, this.world.getWorldBorder().minX() + 16.0D, this.world.getWorldBorder().maxX() - 16.0D);
                double d1 = MathHelper.clamp(z * moveFactor, this.world.getWorldBorder().minZ() + 16.0D, this.world.getWorldBorder().maxZ() - 16.0D);
                double d2 = 8.0D;

                d0 = (double)MathHelper.clamp((int)d0, -29999872, 29999872);
                d1 = (double)MathHelper.clamp((int)d1, -29999872, 29999872);

                double newY = y;
                if(y > (world.getActualHeight() - 10)) {
                    newY = MathHelper.clamp(y, 70, this.world.getActualHeight() - 10);
                }
                protectPlayer(playerMP, new BlockPos(d0, newY, d1));
            }
        }
    }

    private void protectPlayer(EntityPlayerMP playerIn, BlockPos position)
    {
        boolean foundSuitablePlatform = false;
        if (this.world.provider.getDimension() != 0) {
            for(int j1 = 0; j1 < 5; j1++)
            {
                BlockPos checkingPos = position.add(0, -(j1), 0);
                /** Check to see if the block is solid. */
                if(this.world.getBlockState(checkingPos).isFullBlock())
                {
                    /** If there are solid blocks within a 3 block radius under you set foundSuitablePlatform to true */
                    foundSuitablePlatform = true;
                    break;
                }
            }

            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    if((x == -2 || x == 2) && (z == -2 || z == 2)) {
                        BlockPos testPos = new BlockPos(position.add(x, 3, z));
                        if (!this.world.getBlockState(testPos).isFullBlock() && this.world.getBlockState(testPos).getMaterial().isLiquid()) {
                            this.world.setBlockState(testPos, Blocks.OBSIDIAN.getDefaultState());
                        }
                    } else {
                        if(this.world.getBlockState(position.add(x, 3, z)).getMaterial().isLiquid()) {
                            this.world.setBlockState(position.add(x, 3, z), Blocks.OBSIDIAN.getDefaultState());
                        }
                        if(!foundSuitablePlatform) {
                            BlockPos testPos = new BlockPos(position.add(x, -2, z));
                            if (!this.world.getBlockState(testPos).isFullBlock() || this.world.getBlockState(testPos).getMaterial().isLiquid()) {
                                this.world.setBlockState(testPos, Blocks.OBSIDIAN.getDefaultState());
                            }
                        }
                    }
                }
            }
        }

        BlockPos platformPos = new BlockPos(position.add(1, 2, 1));
        for (int y = 1; y <= 3; y++) {
            if (this.world.getBlockState(position.add(0, y, 0)).isFullBlock() || this.world.getBlockState(position.add(0, y, 0)).getMaterial().isLiquid()) {
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        BlockPos testPos = position.add(x, y, z);
                        if (this.world.getBlockState(testPos).isFullBlock() || this.world.getBlockState(testPos).getMaterial().isLiquid()) {
                            this.world.setBlockToAir(testPos);
                        }
                    }
                }
            }
        }

        playerIn.setLocationAndAngles((double)position.getX() + 0.5D, (double)platformPos.getY(), (double)position.getZ() + 0.5D, 90.0F, 0.0F);
        playerIn.setPositionAndUpdate((double)position.getX() + 0.5D, (double)platformPos.getY(), (double)position.getZ() + 0.5D);
    }

    @Optional.Method(modid = "twilightforest")
    private void twilightPlacement(EntityPlayerMP playerMP, double x, double y, double z){
        //TelePastries.logger.debug("at twilightPlacement before protectPlayer");
        protectPlayer(playerMP, new BlockPos(x, y, z));
        playerMP.setSpawnChunk(new BlockPos(playerMP), true, this.world.provider.getDimension());
        //TelePastries.logger.debug("at twilightPlacement after protectPlayer");
    }

    @Optional.Method(modid = "lostcities")
    private void lostCitiesPlacement(EntityPlayerMP playerMP, double x, double y, double z){
        //TelePastries.logger.debug("at lostCitiesPlacement before protectPlayer");
        protectPlayer(playerMP, new BlockPos(x, y, z));
        playerMP.setSpawnChunk(new BlockPos(playerMP), true, this.world.provider.getDimension());
        //TelePastries.logger.debug("at lostCitiesPlacement after protectPlayer");
    }

    @Optional.Method(modid = "huntingdim")
    private void huntingDimensionPlacement(EntityPlayerMP playerMP, double x, double y, double z){
        //TelePastries.logger.debug("at huntingDimensionPlacement before protectPlayer");
        protectPlayer(playerMP, new BlockPos(x, y, z));
        //TelePastries.logger.debug("at huntingDimensionPlacement after protectPlayer");
    }

    public boolean aboveMax(BlockPos pos) {
        boolean flag1 = (this.world.provider.getDimension() == -1 && !TeleConfig.pastries.nether.netherCake1x1Logic && (pos.getY() >= 122 || pos.add(0,1,0).getY() >= 122));
        boolean flag2 = this.world.isOutsideBuildHeight(pos);
        return flag1 || flag2;
    }
}
