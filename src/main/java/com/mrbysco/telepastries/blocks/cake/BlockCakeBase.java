package com.mrbysco.telepastries.blocks.cake;

import com.mrbysco.telepastries.Reference;
import com.mrbysco.telepastries.TelePastries;
import com.mrbysco.telepastries.blocks.BlockPastryBase;
import com.mrbysco.telepastries.config.TeleConfig;
import com.mrbysco.telepastries.util.CakeTeleporter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.util.FakePlayer;

import java.util.List;

public class BlockCakeBase extends BlockPastryBase {
	public static final IntegerProperty BITES = BlockStateProperties.BITES;
	protected static final VoxelShape[] SHAPES = new VoxelShape[]{
			Block.box(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D),
			Block.box(3.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D),
			Block.box(5.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D),
			Block.box(7.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D),
			Block.box(9.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D),
			Block.box(11.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D),
			Block.box(13.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D)};

	protected static final CakeTeleporter TELEPORTER = new CakeTeleporter();

	public BlockCakeBase(BlockBehaviour.Properties properties) {
		super(properties.strength(0.5F).sound(SoundType.WOOL).randomTicks());
		this.registerDefaultState(this.stateDefinition.any().setValue(BITES, Integer.valueOf(0)));
	}

	@Override
	@SuppressWarnings("deprecated")
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPES[state.getValue(BITES)];
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(handIn);
		if (consumeCake() && isRefillItem(stack)) {
			int i = state.getValue(BITES);
			if (i > 0) {
				level.setBlock(pos, state.setValue(BITES, Integer.valueOf(i - 1)), 3);
			}
			if (!player.getAbilities().instabuild) {
				stack.shrink(1);
			}
		} else {
			if (canTeleportTo(level.dimension().location(), getCakeWorld().location())) {
				if (TeleConfig.COMMON.resetPastry.get() && isResetItem(stack)) {
					if (level.isClientSide) {
						removeDimensionPosition((ServerPlayer) player, getCakeWorld());
					}

					if (stack.getItem() == Items.MILK_BUCKET) {
						if (!player.getAbilities().instabuild) {
							stack.shrink(1);
							player.setItemInHand(handIn, new ItemStack(Items.BUCKET));
						}
					}
				} else {
					//TelePastries.logger.debug("At onBlockActivated before eatCake");
					this.eatSlice(level, pos, state, player);
					//TelePastries.logger.debug("At onBlockActivated after eatCake");
				}
			} else {
				if (level.dimension().location().equals(getCakeWorld().location())) {
					player.displayClientMessage(Component.translatable("telepastries.same_dimension"), true);
				} else {
					player.displayClientMessage(Component.translatable("telepastries.teleport_restricted"), true);
				}
				return InteractionResult.PASS;
			}
		}

		return InteractionResult.FAIL;
	}

	public boolean canTeleportTo(ResourceLocation location, ResourceLocation toLocation) {
		if (TeleConfig.COMMON.disableHopping.get()) {
			ResourceLocation overworldLocation = Level.OVERWORLD.location();
			if (location.equals(overworldLocation)) {
				return !location.equals(toLocation);
			} else {
				return toLocation.equals(overworldLocation) && !location.equals(overworldLocation);
			}
		} else {
			return !location.equals(toLocation);
		}
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
		if (TeleConfig.COMMON.disableHopping.get()) {
			ResourceLocation overworldLocation = Level.OVERWORLD.location();
			ResourceLocation worldLocation = ((Level) worldIn).dimension().location();
			if (worldLocation.equals(overworldLocation)) {
				return !getCakeWorld().location().equals(overworldLocation);
			} else {
				return getCakeWorld().location().equals(overworldLocation);
			}
		}
		return super.canSurvive(state, worldIn, pos);
	}

	private InteractionResult eatSlice(LevelAccessor levelAccessor, BlockPos pos, BlockState state, Player player) {
		if (!player.canEat(TeleConfig.COMMON.ignoreHunger.get())) {
			return InteractionResult.PASS;
		} else {
			player.awardStat(Stats.EAT_CAKE_SLICE);
			player.getFoodData().eat(2, 0.1F);
			if (consumeCake()) {
				if (!player.getAbilities().instabuild) {
					int i = state.getValue(BITES);
					if (i < 6) {
						levelAccessor.setBlock(pos, state.setValue(BITES, Integer.valueOf(i + 1)), 3);
					} else {
						levelAccessor.removeBlock(pos, false);
					}
				}
			}

			if (!CommonHooks.onTravelToDimension(player, getCakeWorld()))
				return InteractionResult.FAIL;

			//TelePastries.logger.debug("At eatCake before teleportToDimension");
			teleportToDimension(levelAccessor, pos, player);
			//TelePastries.logger.debug("At eatCake after teleportToDimension");

			return InteractionResult.SUCCESS;
		}
	}

	private boolean isResetItem(ItemStack stack) {
		List<? extends String> items = TeleConfig.COMMON.resetItems.get();
		if (items == null || items.isEmpty()) return false;
		ResourceLocation registryLocation = BuiltInRegistries.ITEM.getKey(stack.getItem());
		return registryLocation != null && items.contains(registryLocation.toString());
	}

	public void teleportToDimension(LevelAccessor worldIn, BlockPos pos, Player player) {
		if (player != null && !(player instanceof FakePlayer) && player.isAlive() && !worldIn.isClientSide()) {
			Level world = ((ServerLevelAccessor) worldIn).getLevel();
			if (!world.isClientSide && !player.isPassenger() && !player.isVehicle() && player.canChangeDimensions()) {
				ServerPlayer serverPlayer = (ServerPlayer) player;
				MinecraftServer server = player.getServer();
				ServerLevel destinationWorld = server != null ? server.getLevel(getCakeWorld()) : null;
				if (destinationWorld == null) {
					player.sendSystemMessage(Component.translatable("telepastries.pastry.custom.invalid", getCakeWorld().location()).withStyle(ChatFormatting.RED));
					TelePastries.LOGGER.error("Destination of cake invalid {} isn't known", getCakeWorld().location());
					return;
				}

				CakeTeleporter.addDimensionPosition(serverPlayer, serverPlayer.level().dimension(), serverPlayer.blockPosition());
				serverPlayer.changeDimension(destinationWorld, TELEPORTER);
			}
		}
	}

	public boolean isRefillItem(ItemStack stack) {
		return false;
	}

	public ResourceKey<Level> getCakeWorld() {
		return Level.OVERWORLD;
	}

	public boolean consumeCake() {
		return true;
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BITES);
	}

	@Override
	@SuppressWarnings("deprecated")
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecated")
	public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
		return (7 - blockState.getValue(BITES)) * 2;
	}

	protected void removeDimensionPosition(ServerPlayer player, ResourceKey<Level> dim) {
		CompoundTag playerData = player.getPersistentData();
		CompoundTag data = getTag(playerData);

		if (data.contains(Reference.MOD_PREFIX + dim.location())) {
			data.remove(Reference.MOD_PREFIX + dim.location());
			player.sendSystemMessage(Component.translatable("telepastries.pastry.reset.complete", dim.location()));
		} else {
			player.sendSystemMessage(Component.translatable("telepastries.pastry.reset.failed", dim.location()));
		}

		playerData.put(Player.PERSISTED_NBT_TAG, data);
	}

	protected CompoundTag getTag(CompoundTag tag) {
		if (tag == null || !tag.contains(Player.PERSISTED_NBT_TAG)) {
			return new CompoundTag();
		}
		return tag.getCompound(Player.PERSISTED_NBT_TAG);
	}
}
