package com.mrbysco.telepastries.util;

import com.mrbysco.telepastries.Reference;
import com.mrbysco.telepastries.TelePastries;
import com.mrbysco.telepastries.config.TeleConfig;
import net.minecraft.block.Blocks;
import net.minecraft.block.PortalInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;
import java.util.function.Function;

public class CakeTeleporter implements ITeleporter {

    @Nullable
    @Override
    public PortalInfo getPortalInfo(Entity entity, ServerWorld destWorld, Function<ServerWorld, PortalInfo> defaultPortalInfo) {
        PortalInfo pos;

        pos = placeNearExistingCake(destWorld, entity, dimensionPosition(entity, destWorld), entity instanceof PlayerEntity);
        pos = moveToSafeCoords(destWorld, entity, pos != null ? new BlockPos(pos.pos) : dimensionPosition(entity, destWorld));
        pos = customCompat(destWorld, new BlockPos(pos.pos), entity);

        return pos;
    }

    @Nullable
    private static PortalInfo placeNearExistingCake(ServerWorld world, Entity entity, BlockPos pos, boolean isPlayer) {
        int i = 200;
        BlockPos blockpos = pos;
        boolean isFromEnd = entity.world.getDimensionKey() == World.THE_END && world.getDimensionKey() == World.OVERWORLD;
        boolean isToEnd = world.getDimensionKey() == World.THE_END;

        if(isFromEnd) {
            blockpos = world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, world.getSpawnPoint());
            return new PortalInfo(new Vector3d((double)blockpos.getX() + 0.5D, (double)blockpos.getY(), (double)blockpos.getZ() + 0.5D), entity.getMotion(), entity.rotationYaw, entity.rotationPitch);
        } else if(isToEnd) {
            ServerWorld.setupEndSpawnPlatform(world);
            blockpos = ServerWorld.END_SPAWN_AREA;

            return new PortalInfo(new Vector3d((double)blockpos.getX() + 0.5D, (double)blockpos.getY(), (double)blockpos.getZ() + 0.5D), entity.getMotion(), entity.rotationYaw, entity.rotationPitch);
        } else {
            blockpos = getDimensionPosition(entity, world.getDimensionKey(), entity.getPosition());
        }

        if (blockpos.equals(BlockPos.ZERO)) {
            return null;
        } else {
            return makePortalInfo(entity, blockpos.getX(), blockpos.getY(), blockpos.getZ());
        }
    }

    private BlockPos dimensionPosition(Entity entity, World destWorld) {
        boolean flag2 = destWorld.getDimensionKey() == World.THE_NETHER;
        if (entity.world.getDimensionKey() != World.THE_NETHER && !flag2) {
            return entity.getPosition();
        } else {
            if(TeleConfig.SERVER.netherCake1x1Logic.get()) {
                return entity.getPosition();
            } else {
                WorldBorder worldborder = destWorld.getWorldBorder();
                double d0 = Math.max(-2.9999872E7D, worldborder.minX() + 16.0D);
                double d1 = Math.max(-2.9999872E7D, worldborder.minZ() + 16.0D);
                double d2 = Math.min(2.9999872E7D, worldborder.maxX() - 16.0D);
                double d3 = Math.min(2.9999872E7D, worldborder.maxZ() - 16.0D);
                double d4 = DimensionType.getCoordinateDifference(entity.world.getDimensionType(), destWorld.getDimensionType());
                BlockPos blockpos1 = new BlockPos(MathHelper.clamp(entity.getPosX() * d4, d0, d2), entity.getPosY(), MathHelper.clamp(entity.getPosZ() * d4, d1, d3));

                return blockpos1;
            }
        }
    }

    public CakeTeleporter(ServerWorld worldIn) {
    }

    @Override
    public Entity placeEntity(Entity newEntity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        newEntity.fallDistance = 0;
        if (newEntity instanceof LivingEntity) { //Give resistance
            ((LivingEntity)newEntity).addPotionEffect(new EffectInstance(Effects.RESISTANCE, 200, 200, false, false));
        }
        return repositionEntity.apply(false); //Must be false or we fall on vanilla
    }

    public static void addDimensionPosition(Entity entityIn, RegistryKey<World> dim, BlockPos position) {
        CompoundNBT entityData = entityIn.getPersistentData();
        CompoundNBT data = getTag(entityData);
        ResourceLocation dimLocation = dim.getLocation();

        if(dim == World.THE_END) {
            BlockPos spawnPlatform = ServerWorld.END_SPAWN_AREA;
            TelePastries.LOGGER.debug("Setting {}'s position of {} to: {}", entityIn.getDisplayName().getUnformattedComponentText(), dimLocation, spawnPlatform);
            data.putLong(Reference.MOD_PREFIX + dimLocation, spawnPlatform.toLong());
        } else {
            TelePastries.LOGGER.debug("Setting {}'s position of {} to: {}", entityIn.getDisplayName().getUnformattedComponentText(), dimLocation, position);
            data.putLong(Reference.MOD_PREFIX + dimLocation, position.toLong());
        }
        entityData.put(PlayerEntity.PERSISTED_NBT_TAG, data);
    }

    public static BlockPos getDimensionPosition(Entity entityIn, RegistryKey<World> dim, BlockPos position) {
        CompoundNBT entityData = entityIn.getPersistentData();
        CompoundNBT data = getTag(entityData);
        ResourceLocation dimLocation = dim.getLocation();

        BlockPos dimPos = position;
        if(data.contains(Reference.MOD_PREFIX + dimLocation)) {
            dimPos = BlockPos.fromLong(data.getLong(Reference.MOD_PREFIX + dimLocation));
            TelePastries.LOGGER.debug("Found {}'s position of {} to: {}", entityIn.getDisplayName().getUnformattedComponentText(), dimLocation, dimPos);
            return dimPos;
        }

        TelePastries.LOGGER.debug("Could not find {}'s previous location. Using current location", entityIn.getDisplayName().getUnformattedComponentText());
        return dimPos;
    }

    public static boolean hasDimensionPosition(Entity entityIn, RegistryKey<World> dim) {
        CompoundNBT entityData = entityIn.getPersistentData();
        CompoundNBT data = getTag(entityData);

        TelePastries.LOGGER.debug("Checking if entity has position stored for : " + dim.getLocation());
        return data.contains(Reference.MOD_PREFIX + dim.getLocation());
    }

    private static CompoundNBT getTag(CompoundNBT tag) {
        if(tag == null || !tag.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
            return new CompoundNBT();
        }
        return tag.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
    }

    private static PortalInfo customCompat(ServerWorld destWorld, BlockPos pos, Entity entity) {
        BlockPos blockpos = pos;
        if (ModList.get().isLoaded("twilightforest")) {
            RegistryKey<World> twilightKey = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("twilightforest", "twilightforest"));
            if (destWorld.getDimensionKey() == twilightKey) {
                if (entity instanceof ServerPlayerEntity) {
                    ServerPlayerEntity playerMP = (ServerPlayerEntity) entity;
                    playerMP.func_242111_a(twilightKey, pos, playerMP.rotationYaw, true, false);
                }
            }
        }

        if (ModList.get().isLoaded("lostcities")) {
            RegistryKey<World> lostCityKey = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("lostcities", "lostcity"));
            if (destWorld.getDimensionKey() == lostCityKey) {
                if (entity instanceof ServerPlayerEntity) {
                    ServerPlayerEntity playerMP = (ServerPlayerEntity) entity;
                    playerMP.func_242111_a(lostCityKey, pos, playerMP.rotationYaw, true, false);
                }
            }
        }

        if (ModList.get().isLoaded("topography")) {
            //Make sure to stay between 220 and 250 Y.
            RegistryKey<World> infiniteDarkKey = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("topography", "infinite_dark"));
            if (destWorld.getDimensionKey() == infiniteDarkKey) {
                if(blockpos.getY() < 220) {
                    blockpos = new BlockPos(blockpos.getX(), 220, blockpos.getZ());
                }
                if(blockpos.getY() > 250) {
                    blockpos = new BlockPos(blockpos.getX(), 250, blockpos.getZ());
                }
                return makePortalInfo(entity, blockpos.getX(), blockpos.getY(), blockpos.getZ());
            }
        }
        return makePortalInfo(entity, blockpos.getX(), blockpos.getY(), blockpos.getZ());
    }

    //Safety stuff
    private static PortalInfo moveToSafeCoords(ServerWorld world, Entity entity, BlockPos pos) {
        if (world.getDimensionKey() != World.OVERWORLD) {
            int distanceDown;
            for(distanceDown = 1; world.isAirBlock(pos.down(distanceDown)); ++distanceDown) {
            }
            boolean foundSuitablePlatform = distanceDown < 3;

            if(!foundSuitablePlatform) {
                makePlatform(world, pos);
            }
        }

        return makePortalInfo(entity, pos.getX(), pos.getY(), pos.getZ());
    }

    private static void makePlatform(ServerWorld world, BlockPos pos) {
        int i = pos.getX();
        int j = pos.getY() - 2;
        int k = pos.getZ();
        BlockPos.getAllInBoxMutable(i - 2, j + 1, k - 2, i + 2, j + 4, k + 2).forEach((blockPos) -> {
            if(!world.getFluidState(blockPos).isEmpty() || world.getBlockState(blockPos).getBlockHardness(world, blockPos) >= 0) {
                world.setBlockState(blockPos, Blocks.COBBLESTONE.getDefaultState());
            }
        });
        BlockPos.getAllInBoxMutable(i - 1, j + 1, k - 1, i + 1, j + 3, k + 1).forEach((blockPos) -> {
            if(world.getBlockState(blockPos).getBlockHardness(world, blockPos) >= 0) {
                world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
            }
        });
        BlockPos.getAllInBoxMutable(i - 1, j, k - 1, i + 1, j, k + 1).forEach((blockPos) -> {
            if(world.getBlockState(blockPos).getBlockHardness(world, blockPos) >= 0) {
                world.setBlockState(blockPos, Blocks.OBSIDIAN.getDefaultState());
            }
        });
    }

    private static PortalInfo makePortalInfo(Entity entity, double x, double y, double z) {
        return makePortalInfo(entity, new Vector3d(x, y, z));
    }

    private static PortalInfo makePortalInfo(Entity entity, Vector3d pos) {
        return new PortalInfo(pos, Vector3d.ZERO, entity.rotationYaw, entity.rotationPitch);
    }
}
