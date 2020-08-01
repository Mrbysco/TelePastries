package com.mrbysco.telepastries.util;

import com.mrbysco.telepastries.Reference;
import com.mrbysco.telepastries.config.TeleConfig;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.ModList;

import java.util.function.Function;

public class CakeTeleporter extends Teleporter {

    public CakeTeleporter(ServerWorld world) {
        super(world);
    }

    @Override
    public Entity placeEntity(Entity newEntity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        Entity entity = repositionEntity.apply(false); //Must be false or we fall on vanilla

        BlockPos dimPos = getDimensionPosition(entity, destWorld.func_234923_W_(), entity.getPosition());
        System.out.println("Position to teleport entity to: " + dimPos);
        doSafetyChecks(entity, currentWorld, destWorld, (double)dimPos.getX() + 0.5D, dimPos.getY(), (double)dimPos.getZ() + 0.5D);
        return entity;
    }

    public void doSafetyChecks(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, double x, double y, double z) {
        this.world.playSound(null, x + 0.5D, y + 0.5D, z + 0.5D, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.MASTER, 0.25F, this.world.rand.nextFloat() * 0.4F + 0.8F);
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).addPotionEffect(new EffectInstance(Effects.RESISTANCE, 200, 200, false, false));
        }

        if (entity instanceof ServerPlayerEntity) {
            ((ServerPlayerEntity) entity).connection.setPlayerLocation(x, y, z, entity.rotationYaw, entity.rotationPitch);
        } else {
            entity.setLocationAndAngles(x, y, z, entity.rotationYaw, 0.0F);
        }
        entity.setMotion(Vector3d.ZERO);

        if(destWorld.func_234923_W_() == World.field_234919_h_) {
            relocateInNether(entity, x, y, z);
        } else if(destWorld.func_234923_W_() == World.field_234920_i_) {
            endPlacement(entity);
        } else if(destWorld.func_234923_W_() != World.field_234918_g_) {
            customCompat(entity, destWorld);
        }
    }

    public void addDimensionPosition(Entity entityIn, RegistryKey<World> dim, BlockPos position) {
        CompoundNBT entityData = entityIn.getPersistentData();
        CompoundNBT data = getTag(entityData, PlayerEntity.PERSISTED_NBT_TAG);
        ResourceLocation dimLocation = dim.func_240901_a_();

        if(dim == World.field_234920_i_) {
            BlockPos spawnPlatform = entityIn.getServer().getWorld(dim).field_241108_a_;
            System.out.println("Setting position of " + dimLocation + " to: " + spawnPlatform);
            data.putLong(Reference.MOD_PREFIX + dimLocation, spawnPlatform.toLong());
        } else {
            System.out.println("Setting position of " + dimLocation + " to: " + position);
            data.putLong(Reference.MOD_PREFIX + dimLocation, position.toLong());
        }
        entityData.put(PlayerEntity.PERSISTED_NBT_TAG, data);
    }

    public BlockPos getDimensionPosition(Entity entityIn, RegistryKey<World> dim, BlockPos position) {
        CompoundNBT entityData = entityIn.getPersistentData();
        CompoundNBT data = getTag(entityData, PlayerEntity.PERSISTED_NBT_TAG);
        ResourceLocation dimLocation = dim.func_240901_a_();

        BlockPos dimPos = position;
        if(data.contains(Reference.MOD_PREFIX + dimLocation)) {
            dimPos = BlockPos.fromLong(data.getLong(Reference.MOD_PREFIX + dimLocation));
            System.out.println("Found position of " + dimLocation + " to: " + dimPos);
            return dimPos;
        }

        System.out.println("Could not find previous location. Using current location");
        return dimPos;
    }

    public boolean hasDimensionPosition(Entity entityIn, RegistryKey<World> dim) {
        CompoundNBT entityData = entityIn.getPersistentData();
        CompoundNBT data = getTag(entityData, PlayerEntity.PERSISTED_NBT_TAG);

        System.out.println("Checking if entity has position stored for : " + dim.func_240901_a_());
        System.out.println("Answer is: " + data.contains(Reference.MOD_PREFIX + dim.func_240901_a_()));
        return data.contains(Reference.MOD_PREFIX + dim.func_240901_a_());
    }

    private CompoundNBT getTag(CompoundNBT tag, String key) {
        if(tag == null || !tag.contains(key)) {
            return new CompoundNBT();
        }
        return tag.getCompound(key);
    }

    @Override
    public boolean placeInPortal(Entity p_222268_1_, float p_222268_2_) {
        return false;
    }

    @Override
    public boolean makePortal(Entity entityIn) {
        return false;
    }

    private void endPlacement(Entity entityIn) {
        //TelePastries.logger.debug("before endPlacement");

        int i = MathHelper.floor(entityIn.getPosX());
        int j = MathHelper.floor(entityIn.getPosY()) - 1;
        int k = MathHelper.floor(entityIn.getPosZ());

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

    private void relocateInNether(Entity entityIn, double x, double y, double z){
        if(TeleConfig.SERVER.netherCake1x1Logic.get()) {
            protectEntity(entityIn, new BlockPos(x, y, z));
        } else {
            if (hasDimensionPosition(entityIn, World.field_234919_h_)) {
                //TelePastries.logger.debug("at relocateInNether before protectPlayer");
                protectEntity(entityIn, new BlockPos(x, y, z));
                //TelePastries.logger.debug("at relocateInNether after protectPlayer");
            } else {
                double moveFactor = 0.125D;
                double d0 = MathHelper.clamp(x * moveFactor, this.world.getWorldBorder().minX() + 16.0D, this.world.getWorldBorder().maxX() - 16.0D);
                double d1 = MathHelper.clamp(z * moveFactor, this.world.getWorldBorder().minZ() + 16.0D, this.world.getWorldBorder().maxZ() - 16.0D);
                double d2 = 8.0D;

                d0 = MathHelper.clamp((int)d0, -29999872, 29999872);
                d1 = MathHelper.clamp((int)d1, -29999872, 29999872);

                double newY = MathHelper.clamp(y, 70, this.world.getHeight() - 10);
                protectEntity(entityIn, new BlockPos(d0, newY, d1));
            }
        }
    }

    private void protectEntity(Entity playerIn, BlockPos position) {
        boolean foundSuitablePlatform = false;
        if (this.world.func_234923_W_() != World.field_234918_g_) {
            for(int j1 = 0; j1 < 5; j1++) {
                BlockPos checkingPos = position.add(0, -(j1), 0);
                /* Check to see if the block is solid. */
                if(this.world.getBlockState(checkingPos).isSolid()) {
                    /* If there are solid blocks within a 3 block radius under you set foundSuitablePlatform to true */
                    foundSuitablePlatform = true;
                    break;
                }
            }

            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    if((x == -2 || x == 2) && (z == -2 || z == 2)) {
                        BlockPos testPos = new BlockPos(position.add(x, 3, z));
                        if (!this.world.getBlockState(testPos).isSolid() && this.world.getBlockState(testPos).getMaterial().isLiquid()) {
                            this.world.setBlockState(testPos, Blocks.OBSIDIAN.getDefaultState());
                        }
                    } else {
                        if(this.world.getBlockState(position.add(x, 3, z)).getMaterial().isLiquid()) {
                            this.world.setBlockState(position.add(x, 3, z), Blocks.OBSIDIAN.getDefaultState());
                        }
                        if(!foundSuitablePlatform) {
                            BlockPos testPos = new BlockPos(position.add(x, -2, z));
                            if (!this.world.getBlockState(testPos).isSolid() || this.world.getBlockState(testPos).getMaterial().isLiquid()) {
                                this.world.setBlockState(testPos, Blocks.OBSIDIAN.getDefaultState());
                            }
                        }
                    }
                }
            }
        }

        BlockPos platformPos = new BlockPos(position.add(1, 2, 1));
        for (int y = 1; y <= 3; y++) {
            if (this.world.getBlockState(position.add(0, y, 0)).isSolid() || this.world.getBlockState(position.add(0, y, 0)).getMaterial().isLiquid()) {
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        BlockPos testPos = position.add(x, y, z);
                        if (this.world.getBlockState(testPos).isSolid() || this.world.getBlockState(testPos).getMaterial().isLiquid()) {
                            this.world.removeBlock(testPos, false);
                        }
                    }
                }
            }
        }

        playerIn.setLocationAndAngles((double)position.getX() + 0.5D, platformPos.getY(), (double)position.getZ() + 0.5D, 90.0F, 0.0F);
        playerIn.setPositionAndUpdate((double)position.getX() + 0.5D, platformPos.getY(), (double)position.getZ() + 0.5D);
    }

    private void customCompat(Entity entity, ServerWorld destWorld) {
        if(ModList.get().isLoaded("twilightforest")) {
            RegistryKey<World> twilightKey = RegistryKey.func_240903_a_(Registry.WORLD_KEY, new ResourceLocation("twilightforest", "twilightforest"));
            if(destWorld.func_234923_W_() == twilightKey) {
                protectEntity(entity, entity.getPosition());
                if(entity instanceof ServerPlayerEntity) {
                    ServerPlayerEntity playerMP = (ServerPlayerEntity)entity;
                    playerMP.func_241153_a_(twilightKey, playerMP.getPosition(), true, false);
                }
            }
        }
    }
//
//    @Optional.Method(modid = "lostcities")
//    private void lostCitiesPlacement(ServerPlayerEntity playerMP, double x, double y, double z){
//        //TelePastries.logger.debug("at lostCitiesPlacement before protectPlayer");
//        protectPlayer(playerMP, new BlockPos(x, y, z));
//        playerMP.setSpawnPoint(new BlockPos(playerMP), true, this.world.getDimension().getType());
//        //TelePastries.logger.debug("at lostCitiesPlacement after protectPlayer");
//    }
//
//    @Optional.Method(modid = "huntingdim")
//    private void huntingDimensionPlacement(ServerPlayerEntity playerMP, double x, double y, double z){
//        //TelePastries.logger.debug("at huntingDimensionPlacement before protectPlayer");
//        protectPlayer(playerMP, new BlockPos(x, y, z));
//        //TelePastries.logger.debug("at huntingDimensionPlacement after protectPlayer");
//    }

    public boolean aboveMax(BlockPos pos) {
        boolean flag1 = (world.func_234923_W_() == World.field_234919_h_ && !TeleConfig.SERVER.netherCake1x1Logic.get() && (pos.getY() >= 122 || pos.add(0,1,0).getY() >= 122));
        boolean flag2 = world.isOutsideBuildHeight(pos);
        return flag1 || flag2;
    }
}
