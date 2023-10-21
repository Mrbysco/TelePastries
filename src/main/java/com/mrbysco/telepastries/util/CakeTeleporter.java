package com.mrbysco.telepastries.util;

import com.mojang.datafixers.util.Pair;
import com.mrbysco.telepastries.Reference;
import com.mrbysco.telepastries.TelePastries;
import com.mrbysco.telepastries.blocks.cake.BlockCakeBase;
import com.mrbysco.telepastries.config.TeleConfig;
import it.unimi.dsi.fastutil.longs.Long2BooleanArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class CakeTeleporter implements ITeleporter {

	private static final Object2ObjectMap<ResourceKey<Level>, LevelTeleportFinder> LEVEL_TELEPORTERS = Util.make(new Object2ObjectOpenHashMap<>(), map ->
			map.put(ServerLevel.END, (entity, destWorld, minMaxBounds, cacheMap) -> toEnd(entity, destWorld))
	);

	@Nullable
	@Override
	public PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
		// First, check if there is already an existing position to pull from
		// If the position doesn't exist, use the current entity's current position clamped between the build heights
		BlockPos spawnPos = getDimensionPosition(entity, destWorld.dimension());

		// Cache map so that we don't need to run checking the specific block position each time
		Long2BooleanArrayMap safeLocation = new Long2BooleanArrayMap();

		// Add compatibility bounds for checking the y-positions the entity can spawn
		var minMaxBounds = customCompatBounds(destWorld).mapFirst(min -> Math.max(min, destWorld.getMinBuildHeight())).mapSecond(max -> Math.min(max, destWorld.getMaxBuildHeight()));

		// If spawn position exists, verify position is safe
		if (spawnPos != null && destWorld.getBlockState(spawnPos.relative(Direction.DOWN)).isSolid() && isPositionSafe(entity, destWorld, spawnPos, safeLocation, minMaxBounds)) {
			return postProcessAndMake(destWorld, spawnPos, entity);
		}

		// Check level teleporter to determine portal info
		@Nullable
		PortalInfo levelInfo = LEVEL_TELEPORTERS.getOrDefault(destWorld.dimension(), CakeTeleporter::searchAroundAndDown).determineTeleportLocation(entity, destWorld, minMaxBounds, safeLocation);
		if (levelInfo != null) {
			return levelInfo;
		}


		// If none of these positions work, use the entity's current position and spawn and safety ring around them
		// If the entity's position isn't within the world bounds, use default coordinates instead (0, 70, 0)
		BlockPos teleportPos = destWorld.getWorldBorder().isWithinBounds(entity.blockPosition())
				&& minMaxBounds.getFirst() < entity.blockPosition().getY() - 1
				&& minMaxBounds.getSecond() > entity.blockPosition().getY() + entity.getBbHeight() + 1
				? entity.blockPosition() : new BlockPos(0, Math.max(minMaxBounds.getFirst(), 70), 0);
		var halfWidth = entity.getBbWidth() / 2;
		int minX = Mth.floor(teleportPos.getX() - halfWidth - 1),
				minY = teleportPos.getY() - 1,
				minZ = Mth.floor(teleportPos.getZ() - halfWidth - 1),
				maxX = Mth.ceil(teleportPos.getX() + halfWidth + 1),
				maxY = Mth.ceil(teleportPos.getY() + entity.getBbHeight() + 1),
				maxZ = Mth.ceil(teleportPos.getZ() + halfWidth + 1);
		for (var pedestalPos : BlockPos.betweenClosed(minX, minY, minZ, maxX, maxY, maxZ)) {
			// Don't do anything if the position is outside the world border
			if (!destWorld.getWorldBorder().isWithinBounds(pedestalPos)) continue;

			// Get the block position to check
			BlockState pedestalState = destWorld.getBlockState(pedestalPos);

			// Don't do anything if the block is a cake
			if (pedestalState.getBlock() instanceof BlockCakeBase) continue;

			// If the position is beneath the entity and isn't solid, set to obsidian
			if(pedestalPos.getY() == minY && !pedestalState.isSolid()) destWorld.setBlockAndUpdate(pedestalPos, Blocks.OBSIDIAN.defaultBlockState());
			// Otherwise, if the position is surrounding the entity and isn't solid, set to cobblestone
			else if ((pedestalPos.getY() == maxY || pedestalPos.getX() == minX || pedestalPos.getX() == maxX || pedestalPos.getZ() == minZ || pedestalPos.getZ() == maxZ) && !pedestalState.isSolid())
				destWorld.setBlockAndUpdate(pedestalPos, Blocks.COBBLESTONE.defaultBlockState());
			// Otherwise, just set to air if the entity can't spawn in it
			else if (!pedestalState.getBlock().isPossibleToRespawnInThis(pedestalState)) destWorld.setBlockAndUpdate(pedestalPos, Blocks.AIR.defaultBlockState());
		}

		// Create info
		return postProcessAndMake(destWorld, teleportPos, entity);
	}

	/**
	 * Search eight blocks around the current block and check down to determine where to teleport.
	 *
	 * @param entity the entity attempting to spawn at the location
	 * @param destWorld the level the entity is teleporting to
	 * @param cacheMap a cache to prevent additional lookups to the position
	 * @param minMaxBounds the bounds of the y position the entity can spawn within
	 * @return the portal information to teleport to, or {@code null} if there is none
	 */
	@Nullable
	private static PortalInfo searchAroundAndDown(Entity entity, ServerLevel destWorld, Pair<Integer, Integer> minMaxBounds, Long2BooleanArrayMap cacheMap) {
		// Set y position to max possible
		double dimensionScale = DimensionType.getTeleportationScale(entity.level().dimensionType(), destWorld.dimensionType());
		BlockPos spawnPos = destWorld.getWorldBorder().clampToBounds(entity.blockPosition().getX() * dimensionScale, entity.blockPosition().getY(), entity.blockPosition().getZ() * dimensionScale)
				.atY(Math.min(minMaxBounds.getSecond(), destWorld.getMinBuildHeight() + destWorld.getLogicalHeight()) - 1);

		// No spawn position or isn't valid, so loop around location
		for (var checkPos: BlockPos.spiralAround(spawnPos, 16, Direction.EAST, Direction.SOUTH)) {
			// Load chunk to actually check the location
			destWorld.getChunk(checkPos);

			// Cycle through positions from top to bottom
			for (int heightY = Math.min(spawnPos.getY(), destWorld.getHeight(Heightmap.Types.MOTION_BLOCKING, checkPos.getX(), checkPos.getZ())); heightY > minMaxBounds.getFirst(); --heightY) {
				checkPos.setY(heightY);

				// Since we are checking going down, we want to verify the player is on the floor
				// Check the player position afterward
				if (!destWorld.getBlockState(checkPos.immutable().relative(Direction.DOWN)).isSolid()
						|| !isPositionSafe(entity, destWorld, checkPos, cacheMap, minMaxBounds)
				) continue;

				// All positions the entity is in is safe, so spawn in that location
				return postProcessAndMake(destWorld, checkPos, entity);
			}
		}

		// If it fails, return null
		return null;
	}

	/**
	 * Set the portal information to the end's spawn point.
	 *
	 * @param entity the entity attempting to spawn at the location
	 * @param destWorld the level the entity is teleporting to
	 * @return the portal information to teleport to, or {@code null} if there is none
	 *
	 * @deprecated this should be removed in favor of a datagen solution
	 */
	@Deprecated
	private static PortalInfo toEnd(Entity entity, ServerLevel destWorld) {
		// Get teleport position
		BlockPos teleportPos = ServerLevel.END_SPAWN_POINT;

		// Get space around entity and below
		var halfWidth = entity.getBbWidth() / 2;
		int minY = teleportPos.getY() - 1;

		// Spawn platform
		for (var pedestalPos : BlockPos.betweenClosed(
				Mth.floor(teleportPos.getX() - halfWidth),
				minY,
				Mth.floor(teleportPos.getZ() - halfWidth),
				Mth.ceil(teleportPos.getX() + halfWidth),
				Mth.ceil(teleportPos.getY() + entity.getBbHeight() + 1),
				Mth.ceil(teleportPos.getZ() + halfWidth)
		)) {
			// Get the block position to check
			BlockState pedestalState = destWorld.getBlockState(pedestalPos);

			// Don't do anything if the block is a cake
			if (pedestalState.getBlock() instanceof BlockCakeBase) continue;

			// If the position is beneath the entity and isn't solid, set to obsidian
			if(pedestalPos.getY() == minY && !pedestalState.isSolid()) destWorld.setBlockAndUpdate(pedestalPos, Blocks.OBSIDIAN.defaultBlockState());
			// Otherwise, just set to air if the entity can't spawn in it
			else if (!pedestalState.getBlock().isPossibleToRespawnInThis(pedestalState)) destWorld.setBlockAndUpdate(pedestalPos, Blocks.AIR.defaultBlockState());
		}

		return postProcessAndMake(destWorld, teleportPos, entity);
	}

	/**
	 * Checks if all blocks within the entity's bounding box is safe to spawn in.
	 *
	 * @param entity the entity attempting to spawn at the location
	 * @param destWorld the level the entity is teleporting to
	 * @param checkPos the position the entity is trying to be spawned at
	 * @param cacheMap a cache to prevent additional lookups to the position
	 * @param minMaxBounds the bounds of the y position the entity can spawn within
	 * @return {@code true} if it is safe for the entity to spawn here, {@code false} otherwise
	 */
	private static boolean isPositionSafe(Entity entity, ServerLevel destWorld, BlockPos checkPos, Long2BooleanArrayMap cacheMap, Pair<Integer, Integer> minMaxBounds) {
		var halfWidth = entity.getBbWidth() / 2;
		// We construct the position based on the entity radius
		// We could use the AABB method; however we want to account fo edge cases where the entity is touching a corner with
		// the box, causing the safety check to fail and change the spawn position.
		// This is also why we round to the higher or lower value
		for (var entityBoxPos: BlockPos.betweenClosed(
				Math.round(checkPos.getX() - halfWidth),
				checkPos.getY(),
				Math.round(checkPos.getZ() - halfWidth),
				Math.round(checkPos.getX() + halfWidth),
				Math.round(checkPos.getY() + entity.getBbHeight()),
				Math.round(checkPos.getZ() + halfWidth)
		)) {
			// If a safe position isn't found in the entity is in, move to next spot to check
			if (!cacheMap.computeIfAbsent(
					entityBoxPos.asLong(),
					c -> {
						// Check if position is within bounds or that the position's min build height is higher than the spawning position
						if (!destWorld.getWorldBorder().isWithinBounds(entityBoxPos) || minMaxBounds.getFirst() >= entityBoxPos.getY()) return false;
						// Get block state and check if it is possible to respawn
						BlockState entityBoxState = destWorld.getBlockState(entityBoxPos);
						return entityBoxState.getBlock().isPossibleToRespawnInThis(entityBoxState);
					}
			)) {
				return false;
			}
		}

		// If nothing fails, it is a safe location
		return true;
	}

	@Override
	public Entity placeEntity(Entity newEntity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
		newEntity.fallDistance = 0;
		if (newEntity instanceof LivingEntity) { //Give resistance
			((LivingEntity) newEntity).addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 200, false, false));
		}
		return repositionEntity.apply(false); //Must be false or we fall on vanilla
	}

	public static void addDimensionPosition(Entity entityIn, ResourceKey<Level> dim, BlockPos position) {
		CompoundTag entityData = entityIn.getPersistentData();
		CompoundTag data = getTag(entityData);
		ResourceLocation dimLocation = dim.location();

		if (dim == Level.END) {
			BlockPos spawnPlatform = ServerLevel.END_SPAWN_POINT;
			TelePastries.LOGGER.debug("Setting {}'s position of {} to: {}", entityIn.getDisplayName().getContents(), dimLocation, spawnPlatform);
			data.putLong(Reference.MOD_PREFIX + dimLocation, spawnPlatform.asLong());
		} else {
			TelePastries.LOGGER.debug("Setting {}'s position of {} to: {}", entityIn.getDisplayName().getContents(), dimLocation, position);
			data.putLong(Reference.MOD_PREFIX + dimLocation, position.asLong());
		}
		entityData.put(Player.PERSISTED_NBT_TAG, data);
	}

	@Nullable
	public static BlockPos getDimensionPosition(Entity entityIn, ResourceKey<Level> dim) {
		CompoundTag entityData = entityIn.getPersistentData();
		CompoundTag data = getTag(entityData);
		ResourceLocation dimLocation = dim.location();

		if (data.contains(Reference.MOD_PREFIX + dimLocation)) {
			BlockPos dimPos = BlockPos.of(data.getLong(Reference.MOD_PREFIX + dimLocation));
			TelePastries.LOGGER.debug("Found {}'s position of {} to: {}", entityIn.getDisplayName().getContents(), dimLocation, dimPos);
			return dimPos;
		}

		TelePastries.LOGGER.debug("Could not find {}'s previous location. Using current location", entityIn.getDisplayName().getContents());
		return null;
	}
	private static CompoundTag getTag(CompoundTag tag) {
		if (tag == null || !tag.contains(Player.PERSISTED_NBT_TAG)) {
			return new CompoundTag();
		}
		return tag.getCompound(Player.PERSISTED_NBT_TAG);
	}

	/**
	 * Returns the minimum and maximum y values the entity can spawn between.
	 *
	 * @param destWorld the level the entity is teleporting to
	 * @return The bounded y values the entity must spawn in-between
	 */
	private static Pair<Integer, Integer> customCompatBounds(ServerLevel destWorld) {
		ResourceLocation customLocation = ResourceLocation.tryParse(TeleConfig.COMMON.customCakeDimension.get());
		if (customLocation != null) {
			ResourceKey<Level> customWorldKey = ResourceKey.create(Registries.DIMENSION, customLocation);
			if (destWorld.dimension() == customWorldKey) {
				int minY = TeleConfig.COMMON.customCakeMinY.get();
				int maxY = TeleConfig.COMMON.customCakeMaxY.get();
				return Pair.of(minY, maxY);
			}
		}

		ResourceLocation customLocation2 = ResourceLocation.tryParse(TeleConfig.COMMON.customCake2Dimension.get());
		if (customLocation2 != null) {
			ResourceKey<Level> customWorldKey = ResourceKey.create(Registries.DIMENSION, customLocation2);
			if (destWorld.dimension() == customWorldKey) {
				int minY = TeleConfig.COMMON.customCake2MinY.get();
				int maxY = TeleConfig.COMMON.customCake2MaxY.get();
				return Pair.of(minY, maxY);
			}
		}

		ResourceLocation customLocation3 = ResourceLocation.tryParse(TeleConfig.COMMON.customCake3Dimension.get());
		if (customLocation3 != null) {
			ResourceKey<Level> customWorldKey = ResourceKey.create(Registries.DIMENSION, customLocation3);
			if (destWorld.dimension() == customWorldKey) {
				int minY = TeleConfig.COMMON.customCake3MinY.get();
				int maxY = TeleConfig.COMMON.customCake3MaxY.get();
				return Pair.of(minY, maxY);
			}
		}

		return Pair.of(Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	/**
	 * Sets the spawn location of the entity in compatible levels.
	 *
	 * @param destWorld the level the entity is teleporting to
	 * @param pos the position the entity is trying to be spawned at
	 * @param entity the entity attempting to spawn at the location
	 */
	private static PortalInfo postProcessAndMake(ServerLevel destWorld, BlockPos pos, Entity entity) {
		// Set overworld back to respawn position when using cake.
		if (destWorld.dimension() == Level.OVERWORLD) {
			if (entity instanceof ServerPlayer serverPlayer) {
				serverPlayer.setRespawnPosition(Level.OVERWORLD, pos, serverPlayer.getYRot(), true, false);
			}
		}

		if (ModList.get().isLoaded("twilightforest")) {
			ResourceKey<Level> twilightKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation("twilightforest", "twilight_forest"));
			if (destWorld.dimension() == twilightKey) {
				if (entity instanceof ServerPlayer serverPlayer) {
					serverPlayer.setRespawnPosition(twilightKey, pos, serverPlayer.getYRot(), true, false);
				}
			}
		}

		if (ModList.get().isLoaded("lostcities")) {
			ResourceKey<Level> lostCityKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation("lostcities", "lostcity"));
			if (destWorld.dimension() == lostCityKey) {
				if (entity instanceof ServerPlayer serverPlayer) {
					serverPlayer.setRespawnPosition(lostCityKey, pos, serverPlayer.getYRot(), true, false);
				}
			}
		}

		return makePortalInfo(entity, pos);
	}

	/**
	 * Creates the portal info based on the given block position.
	 *
	 * @param entity the entity attempting to spawn at the location
	 * @param pos the position the entity is trying to be spawned at
	 * @return the information necessary to teleport the entity
	 */
	private static PortalInfo makePortalInfo(Entity entity, BlockPos pos) {
		return new PortalInfo(new Vec3(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5), Vec3.ZERO, entity.getYRot(), entity.getXRot());
	}

	/**
	 * A location finder for determining where to teleport the entity in a given level.
	 */
	@FunctionalInterface
	interface LevelTeleportFinder {

		/**
		 * Determine where to teleport the entity for the given level.
		 *
		 * @param entity the entity attempting to spawn at the location
		 * @param destWorld the level the entity is teleporting to
		 * @param cacheMap a cache to prevent additional lookups to the position
		 * @param minMaxBounds the bounds of the y position the entity can spawn within
		 * @return the portal information to teleport to, or {@code null} if there is none
		 */
		@Nullable
		PortalInfo determineTeleportLocation(Entity entity, ServerLevel destWorld, Pair<Integer, Integer> minMaxBounds, Long2BooleanArrayMap cacheMap);
	}
}
