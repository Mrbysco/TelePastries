package com.mrbysco.telepastries.util;

import com.mrbysco.telepastries.Reference;
import com.mrbysco.telepastries.TelePastries;
import com.mrbysco.telepastries.blocks.cake.BlockCakeBase;
import com.mrbysco.telepastries.config.TeleConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;

public class CakeTeleporter implements ITeleporter {

    @Nullable
    @Override
    public PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        PortalInfo pos;

        pos = placeNearExistingCake(destWorld, entity, dimensionPosition(entity, destWorld), entity instanceof Player);
        pos = customCompat(destWorld, new BlockPos(pos.pos), entity);
        pos = moveToSafeCoords(destWorld, entity, pos != null ? new BlockPos(pos.pos) : dimensionPosition(entity, destWorld));

        return pos;
    }

    @Nullable
    private static PortalInfo placeNearExistingCake(ServerLevel destWorld, Entity entity, BlockPos pos, boolean isPlayer) {
        int i = 200;
        BlockPos blockpos = pos;
        boolean isToEnd = destWorld.dimension() == Level.END;
        boolean isToOverworld = destWorld.dimension() == Level.OVERWORLD;
        boolean isFromEnd = entity.level.dimension() == Level.END && isToOverworld;

        if(isToEnd) {
            ServerLevel.makeObsidianPlatform(destWorld);
            blockpos = ServerLevel.END_SPAWN_POINT;

            return new PortalInfo(new Vec3((double)blockpos.getX() + 0.5D, (double)blockpos.getY(), (double)blockpos.getZ() + 0.5D), entity.getDeltaMovement(), entity.getYRot(), entity.getXRot());
        } else {
            blockpos = getDimensionPosition(entity, destWorld.dimension(), entity.blockPosition());
            if(blockpos == null) {
                if(isFromEnd && isToOverworld) {
                    TelePastries.LOGGER.info("Couldn't locate a cake location, using spawn point instead");
                    blockpos = destWorld.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, destWorld.getSharedSpawnPos());
                    float angle = entity.getXRot();
                    if(isPlayer && entity instanceof ServerPlayer serverPlayer) {
                        BlockPos respawnPos = serverPlayer.getRespawnPosition();
                        float respawnAngle = serverPlayer.getRespawnAngle();
                        Optional<Vec3> optional;
                        if (serverPlayer != null && respawnPos != null) {
                            optional = Player.findRespawnPositionAndUseSpawnBlock(destWorld, respawnPos, respawnAngle, false, false);
                        } else {
                            optional = Optional.empty();
                        }

                        if (optional.isPresent()) {
                            BlockState blockstate = destWorld.getBlockState(respawnPos);
                            boolean blockIsRespawnAnchor = blockstate.is(Blocks.RESPAWN_ANCHOR);
                            Vec3 vector3d = optional.get();
                            float f1;
                            if (!blockstate.is(BlockTags.BEDS) && !blockIsRespawnAnchor) {
                                f1 = respawnAngle;
                            } else {
                                Vec3 vector3d1 = Vec3.atBottomCenterOf(respawnPos).subtract(vector3d).normalize();
                                f1 = (float) Mth.wrapDegrees(Mth.atan2(vector3d1.z, vector3d1.x) * (double) (180F / (float) Math.PI) - 90.0D);
                            }
                            angle = f1;
                            blockpos = new BlockPos(vector3d.x, vector3d.y, vector3d.z);
                            return new PortalInfo(new Vec3((double)blockpos.getX() + 0.5D, (double)blockpos.getY(), (double)blockpos.getZ() + 0.5D), entity.getDeltaMovement(), angle, entity.getXRot());
                        }
                    }
                } else {
                    blockpos = entity.blockPosition();
                }
            }
        }

        if (blockpos.equals(BlockPos.ZERO)) {
            return null;
        } else {
            return makePortalInfo(entity, blockpos.getX(), blockpos.getY(), blockpos.getZ());
        }
    }

    private BlockPos dimensionPosition(Entity entity, Level destWorld) {
        boolean flag2 = destWorld.dimension() == Level.NETHER;
        if (entity.level.dimension() != Level.NETHER && !flag2) {
            return entity.blockPosition();
        } else {
            WorldBorder worldborder = destWorld.getWorldBorder();
            double d0 = Math.max(-2.9999872E7D, worldborder.getMinX() + 16.0D);
            double d1 = Math.max(-2.9999872E7D, worldborder.getMinZ() + 16.0D);
            double d2 = Math.min(2.9999872E7D, worldborder.getMaxX() - 16.0D);
            double d3 = Math.min(2.9999872E7D, worldborder.getMaxZ() - 16.0D);
            double d4 = DimensionType.getTeleportationScale(entity.level.dimensionType(), destWorld.dimensionType());
            BlockPos blockpos1 = new BlockPos(Mth.clamp(entity.getX() * d4, d0, d2), entity.getY(), Mth.clamp(entity.getZ() * d4, d1, d3));

            return blockpos1;
        }
    }

    public CakeTeleporter(ServerLevel worldIn) {
    }

    @Override
    public Entity placeEntity(Entity newEntity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        newEntity.fallDistance = 0;
        if (newEntity instanceof LivingEntity) { //Give resistance
            ((LivingEntity)newEntity).addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 200, false, false));
        }
        return repositionEntity.apply(false); //Must be false or we fall on vanilla
    }

    public static void addDimensionPosition(Entity entityIn, ResourceKey<Level> dim, BlockPos position) {
        CompoundTag entityData = entityIn.getPersistentData();
        CompoundTag data = getTag(entityData);
        ResourceLocation dimLocation = dim.location();

        if(dim == Level.END) {
            BlockPos spawnPlatform = ServerLevel.END_SPAWN_POINT;
            TelePastries.LOGGER.debug("Setting {}'s position of {} to: {}", entityIn.getDisplayName().getContents(), dimLocation, spawnPlatform);
            data.putLong(Reference.MOD_PREFIX + dimLocation, spawnPlatform.asLong());
        } else {
            TelePastries.LOGGER.debug("Setting {}'s position of {} to: {}", entityIn.getDisplayName().getContents(), dimLocation, position);
            data.putLong(Reference.MOD_PREFIX + dimLocation, position.asLong());
        }
        entityData.put(Player.PERSISTED_NBT_TAG, data);
    }

    public static BlockPos getDimensionPosition(Entity entityIn, ResourceKey<Level> dim, BlockPos position) {
        CompoundTag entityData = entityIn.getPersistentData();
        CompoundTag data = getTag(entityData);
        ResourceLocation dimLocation = dim.location();

        BlockPos dimPos = null;
        if(data.contains(Reference.MOD_PREFIX + dimLocation)) {
            dimPos = BlockPos.of(data.getLong(Reference.MOD_PREFIX + dimLocation));
            TelePastries.LOGGER.debug("Found {}'s position of {} to: {}", entityIn.getDisplayName().getContents(), dimLocation, dimPos);
            return dimPos;
        }

        TelePastries.LOGGER.debug("Could not find {}'s previous location. Using current location", entityIn.getDisplayName().getContents());
        return dimPos;
    }

    public static boolean hasDimensionPosition(Entity entityIn, ResourceKey<Level> dim) {
        CompoundTag entityData = entityIn.getPersistentData();
        CompoundTag data = getTag(entityData);

        TelePastries.LOGGER.debug("Checking if entity has position stored for : " + dim.location());
        return data.contains(Reference.MOD_PREFIX + dim.location());
    }

    private static CompoundTag getTag(CompoundTag tag) {
        if(tag == null || !tag.contains(Player.PERSISTED_NBT_TAG)) {
            return new CompoundTag();
        }
        return tag.getCompound(Player.PERSISTED_NBT_TAG);
    }

    private static PortalInfo customCompat(ServerLevel destWorld, BlockPos pos, Entity entity) {
        BlockPos blockpos = pos;
        if (ModList.get().isLoaded("twilightforest")) {
            ResourceKey<Level> twilightKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("twilightforest", "twilight_forest"));
            if (destWorld.dimension() == twilightKey) {
                if (entity instanceof ServerPlayer playerMP) {
                    playerMP.setRespawnPosition(twilightKey, pos, playerMP.getYRot(), true, false);
                }
            }
        }

        if (ModList.get().isLoaded("lostcities")) {
            ResourceKey<Level> lostCityKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("lostcities", "lostcity"));
            if (destWorld.dimension() == lostCityKey) {
                if (entity instanceof ServerPlayer playerMP) {
                    playerMP.setRespawnPosition(lostCityKey, pos, playerMP.getYRot(), true, false);
                }
            }
        }

        ResourceLocation customLocation = ResourceLocation.tryParse(TeleConfig.SERVER.customCakeDimension.get());
        if (customLocation != null) {
            ResourceKey<Level> customWorldKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, customLocation);
            if (destWorld.dimension() == customWorldKey) {
                int minY = TeleConfig.SERVER.customCakeMinY.get();
                if(blockpos.getY() < minY) {
                    blockpos = new BlockPos(blockpos.getX(), minY, blockpos.getZ());
                }
                int maxY = TeleConfig.SERVER.customCakeMaxY.get();
                if(blockpos.getY() > maxY) {
                    blockpos = new BlockPos(blockpos.getX(), maxY, blockpos.getZ());
                }
                return makePortalInfo(entity, blockpos.getX(), blockpos.getY(), blockpos.getZ());
            }
        }

        return makePortalInfo(entity, blockpos.getX(), blockpos.getY(), blockpos.getZ());
    }

    //Safety stuff
    private static PortalInfo moveToSafeCoords(ServerLevel world, Entity entity, BlockPos pos) {
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

    private static void makePlatform(ServerLevel world, BlockPos pos) {
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
        return makePortalInfo(entity, new Vec3(x, y, z));
    }

    private static PortalInfo makePortalInfo(Entity entity, Vec3 pos) {
        return new PortalInfo(pos, Vec3.ZERO, entity.getYRot(), entity.getXRot());
    }
}
