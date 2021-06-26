package com.mrbysco.telepastries.util;

import com.mrbysco.telepastries.Reference;
import com.mrbysco.telepastries.TelePastries;
import com.mrbysco.telepastries.blocks.cake.BlockCakeBase;
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
        pos = customCompat(destWorld, new BlockPos(pos.pos), entity);
        pos = moveToSafeCoords(destWorld, entity, pos != null ? new BlockPos(pos.pos) : dimensionPosition(entity, destWorld));

        return pos;
    }

    @Nullable
    private static PortalInfo placeNearExistingCake(ServerWorld destWorld, Entity entity, BlockPos pos, boolean isPlayer) {
        int i = 200;
        BlockPos blockpos = pos;
        boolean isToEnd = destWorld.dimension() == World.END;

        if(isToEnd) {
            ServerWorld.makeObsidianPlatform(destWorld);
            blockpos = ServerWorld.END_SPAWN_POINT;

            return new PortalInfo(new Vector3d((double)blockpos.getX() + 0.5D, (double)blockpos.getY(), (double)blockpos.getZ() + 0.5D), entity.getDeltaMovement(), entity.yRot, entity.xRot);
        } else {
            blockpos = getDimensionPosition(entity, destWorld.dimension(), entity.blockPosition());
        }

        if (blockpos.equals(BlockPos.ZERO)) {
            return null;
        } else {
            return makePortalInfo(entity, blockpos.getX(), blockpos.getY(), blockpos.getZ());
        }
    }

    private BlockPos dimensionPosition(Entity entity, World destWorld) {
        boolean flag2 = destWorld.dimension() == World.NETHER;
        if (entity.level.dimension() != World.NETHER && !flag2) {
            return entity.blockPosition();
        } else {
            WorldBorder worldborder = destWorld.getWorldBorder();
            double d0 = Math.max(-2.9999872E7D, worldborder.getMinX() + 16.0D);
            double d1 = Math.max(-2.9999872E7D, worldborder.getMinZ() + 16.0D);
            double d2 = Math.min(2.9999872E7D, worldborder.getMaxX() - 16.0D);
            double d3 = Math.min(2.9999872E7D, worldborder.getMaxZ() - 16.0D);
            double d4 = DimensionType.getTeleportationScale(entity.level.dimensionType(), destWorld.dimensionType());
            BlockPos blockpos1 = new BlockPos(MathHelper.clamp(entity.getX() * d4, d0, d2), entity.getY(), MathHelper.clamp(entity.getZ() * d4, d1, d3));

            return blockpos1;
        }
    }

    public CakeTeleporter(ServerWorld worldIn) {
    }

    @Override
    public Entity placeEntity(Entity newEntity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        newEntity.fallDistance = 0;
        if (newEntity instanceof LivingEntity) { //Give resistance
            ((LivingEntity)newEntity).addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 200, 200, false, false));
        }
        return repositionEntity.apply(false); //Must be false or we fall on vanilla
    }

    public static void addDimensionPosition(Entity entityIn, RegistryKey<World> dim, BlockPos position) {
        CompoundNBT entityData = entityIn.getPersistentData();
        CompoundNBT data = getTag(entityData);
        ResourceLocation dimLocation = dim.location();

        if(dim == World.END) {
            BlockPos spawnPlatform = ServerWorld.END_SPAWN_POINT;
            TelePastries.LOGGER.debug("Setting {}'s position of {} to: {}", entityIn.getDisplayName().getContents(), dimLocation, spawnPlatform);
            data.putLong(Reference.MOD_PREFIX + dimLocation, spawnPlatform.asLong());
        } else {
            TelePastries.LOGGER.debug("Setting {}'s position of {} to: {}", entityIn.getDisplayName().getContents(), dimLocation, position);
            data.putLong(Reference.MOD_PREFIX + dimLocation, position.asLong());
        }
        entityData.put(PlayerEntity.PERSISTED_NBT_TAG, data);
    }

    public static BlockPos getDimensionPosition(Entity entityIn, RegistryKey<World> dim, BlockPos position) {
        CompoundNBT entityData = entityIn.getPersistentData();
        CompoundNBT data = getTag(entityData);
        ResourceLocation dimLocation = dim.location();

        BlockPos dimPos = position;
        if(data.contains(Reference.MOD_PREFIX + dimLocation)) {
            dimPos = BlockPos.of(data.getLong(Reference.MOD_PREFIX + dimLocation));
            TelePastries.LOGGER.debug("Found {}'s position of {} to: {}", entityIn.getDisplayName().getContents(), dimLocation, dimPos);
            return dimPos;
        }

        TelePastries.LOGGER.debug("Could not find {}'s previous location. Using current location", entityIn.getDisplayName().getContents());
        return dimPos;
    }

    public static boolean hasDimensionPosition(Entity entityIn, RegistryKey<World> dim) {
        CompoundNBT entityData = entityIn.getPersistentData();
        CompoundNBT data = getTag(entityData);

        TelePastries.LOGGER.debug("Checking if entity has position stored for : " + dim.location());
        return data.contains(Reference.MOD_PREFIX + dim.location());
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
            RegistryKey<World> twilightKey = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("twilightforest", "twilightforest"));
            if (destWorld.dimension() == twilightKey) {
                if (entity instanceof ServerPlayerEntity) {
                    ServerPlayerEntity playerMP = (ServerPlayerEntity) entity;
                    playerMP.setRespawnPosition(twilightKey, pos, playerMP.yRot, true, false);
                }
            }
        }

        if (ModList.get().isLoaded("lostcities")) {
            RegistryKey<World> lostCityKey = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("lostcities", "lostcity"));
            if (destWorld.dimension() == lostCityKey) {
                if (entity instanceof ServerPlayerEntity) {
                    ServerPlayerEntity playerMP = (ServerPlayerEntity) entity;
                    playerMP.setRespawnPosition(lostCityKey, pos, playerMP.yRot, true, false);
                }
            }
        }

        if (ModList.get().isLoaded("topography")) {
            //Make sure to stay between 220 and 250 Y.
            RegistryKey<World> infiniteDarkKey = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("topography", "infinite_dark"));
            if (destWorld.dimension() == infiniteDarkKey) {
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
        if (world.isEmptyBlock(pos.below())) {
            int distance;
            for(distance = 1; world.getBlockState(pos.below(distance)).getBlock().isPossibleToRespawnInThis(); ++distance) {
            }

            if (distance > 4) {
                makePlatform(world, pos);
            }
        } else {
            if(world.getBlockState(pos.above()).getBlock().isPossibleToRespawnInThis() && world.getBlockState(pos.above(1)).getBlock().isPossibleToRespawnInThis()) {
                BlockPos abovePos = pos.above(1);
                return makePortalInfo(entity, abovePos.getX() + 0.5D, abovePos.getY(), abovePos.getZ() + 0.5D);
            }
            if(!world.isEmptyBlock(pos.below()) || !world.isEmptyBlock(pos)) {
                makePlatform(world, pos);
            }
        }

        return makePortalInfo(entity, pos.getX(), pos.getY(), pos.getZ());
    }

    private static void makePlatform(ServerWorld world, BlockPos pos) {
        int i = pos.getX();
        int j = pos.getY() - 2;
        int k = pos.getZ();
        BlockPos.betweenClosed(i - 2, j + 1, k - 2, i + 2, j + 4, k + 2).forEach((blockPos) -> {
            if(!(world.getBlockState(blockPos).getBlock() instanceof BlockCakeBase)) {
                if(!world.getFluidState(blockPos).isEmpty() || world.getBlockState(blockPos).getDestroySpeed(world, blockPos) >= 0) {
                    world.setBlockAndUpdate(blockPos, Blocks.COBBLESTONE.defaultBlockState());
                }
            }
        });
        BlockPos.betweenClosed(i - 1, j + 1, k - 1, i + 1, j + 3, k + 1).forEach((blockPos) -> {
            if(!(world.getBlockState(blockPos).getBlock() instanceof BlockCakeBase)) {
                if(world.getBlockState(blockPos).getDestroySpeed(world, blockPos) >= 0) {
                    world.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                }
            }
        });
        BlockPos.betweenClosed(i - 1, j, k - 1, i + 1, j, k + 1).forEach((blockPos) -> {
            if(!(world.getBlockState(blockPos).getBlock() instanceof BlockCakeBase)) {
                if(world.getBlockState(blockPos).getDestroySpeed(world, blockPos) >= 0) {
                    world.setBlockAndUpdate(blockPos, Blocks.OBSIDIAN.defaultBlockState());
                }
            }
        });
    }

    private static PortalInfo makePortalInfo(Entity entity, double x, double y, double z) {
        return makePortalInfo(entity, new Vector3d(x, y, z));
    }

    private static PortalInfo makePortalInfo(Entity entity, Vector3d pos) {
        return new PortalInfo(pos, Vector3d.ZERO, entity.yRot, entity.xRot);
    }
}
