package com.mrbysco.telepastries.blocks.cake;

import com.mrbysco.telepastries.Reference;
import com.mrbysco.telepastries.TelePastries;
import com.mrbysco.telepastries.blocks.BlockPastryBase;
import com.mrbysco.telepastries.config.TeleConfig;
import com.mrbysco.telepastries.util.CakeTeleporter;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.PathType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;

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

    public BlockCakeBase(AbstractBlock.Properties properties) {
        super(properties.strength(0.5F).sound(SoundType.WOOL).randomTicks());
        this.registerDefaultState(this.stateDefinition.any().setValue(BITES, Integer.valueOf(0)));
    }

    @Override
    @SuppressWarnings("deprecated")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPES[state.getValue(BITES)];
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isClientSide) {
            ItemStack stack = player.getItemInHand(handIn);
            if(consumeCake() && isRefillItem(stack)) {
                int i = state.getValue(BITES);
                if(i > 0) {
                    worldIn.setBlock(pos, state.setValue(BITES, Integer.valueOf(i - 1)), 3);
                }
                if(!player.abilities.instabuild) {
                    stack.shrink(1);
                }
                return ActionResultType.SUCCESS;
            } else {
                if(canTeleportTo(worldIn.dimension().location(), getCakeWorld().location())) {
                    if(TeleConfig.SERVER.resetPastry.get() && isResetItem(stack)) {
                        removeDimensionPosition((ServerPlayerEntity)player, getCakeWorld());

                        if(stack.getItem() == Items.MILK_BUCKET) {
                            if(!player.abilities.instabuild) {
                                stack.shrink(1);
                                player.setItemInHand(handIn, new ItemStack(Items.BUCKET));
                            }
                        }
                        return ActionResultType.SUCCESS;
                    } else {
                        //TelePastries.logger.debug("At onBlockActivated before eatCake");
                        if (this.eatSlice(worldIn, pos, state, player).consumesAction()) {
                            return ActionResultType.SUCCESS;
                        }
                        //TelePastries.logger.debug("At onBlockActivated after eatCake");
                        return ActionResultType.FAIL;
                    }
                } else {
                    if(worldIn.dimension().location().equals(getCakeWorld().location())) {
                        player.displayClientMessage(new TranslationTextComponent("telepastries.same_dimension"), true);
                    } else {
                        player.displayClientMessage(new TranslationTextComponent("telepastries.teleport_restricted"), true);
                    }
                    return ActionResultType.FAIL;
                }
            }
        }

        return ActionResultType.SUCCESS;
    }

    public boolean canTeleportTo(ResourceLocation location, ResourceLocation toLocation) {
        if(TeleConfig.SERVER.disableHopping.get()) {
            ResourceLocation overworldLocation = World.OVERWORLD.location();
            if(location.equals(overworldLocation)) {
                return !location.equals(toLocation);
            } else {
                return toLocation.equals(overworldLocation) && !location.equals(overworldLocation);
            }
        } else {
            return !location.equals(toLocation);
        }
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
        if(TeleConfig.SERVER.disableHopping.get()) {
            ResourceLocation overworldLocation = World.OVERWORLD.location();
            ResourceLocation worldLocation = ((World)worldIn).dimension().location();
            if(worldLocation.equals(overworldLocation)) {
                return !getCakeWorld().location().equals(overworldLocation);
            } else {
                return getCakeWorld().location().equals(overworldLocation);
            }
        }
        return super.canSurvive(state, worldIn, pos);
    }

    private ActionResultType eatSlice(IWorld world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!player.canEat(TeleConfig.SERVER.ignoreHunger.get())) {
            return ActionResultType.PASS;
        } else {
            player.awardStat(Stats.EAT_CAKE_SLICE);
            player.getFoodData().eat(2, 0.1F);
            if(consumeCake()) {
                if(!player.abilities.instabuild) {
                    int i = state.getValue(BITES);
                    if (i < 6) {
                        world.setBlock(pos, state.setValue(BITES, Integer.valueOf(i + 1)), 3);
                    } else {
                        world.removeBlock(pos, false);
                    }
                }
            }

            if (!ForgeHooks.onTravelToDimension(player, getCakeWorld()))
                return ActionResultType.SUCCESS;

            //TelePastries.logger.debug("At eatCake before teleportToDimension");
            teleportToDimension(world, pos, player);
            //TelePastries.logger.debug("At eatCake after teleportToDimension");

            return ActionResultType.SUCCESS;
        }
    }

    private boolean isResetItem(ItemStack stack) {
        List<? extends String> items = TeleConfig.SERVER.resetItems.get();
        if (items == null || items.isEmpty()) return false;
        ResourceLocation registryLocation = stack.getItem().getRegistryName();
        return registryLocation != null && items.contains(registryLocation.toString());
    }

    public void teleportToDimension(IWorld worldIn, BlockPos pos, PlayerEntity player) {
        if (player.isAlive() && !worldIn.isClientSide()) {
            World world = ((IServerWorld)worldIn).getLevel();
            if (!world.isClientSide && !player.isPassenger() && !player.isVehicle() && player.canChangeDimensions()) {
                ServerPlayerEntity playerMP = (ServerPlayerEntity) player;
                MinecraftServer server = player.getServer();
                ServerWorld destinationWorld = server != null ? server.getLevel(getCakeWorld()) : null;
                if(destinationWorld == null) {
                    player.sendMessage(new TranslationTextComponent("telepastries.pastry.custom.invalid", getCakeWorld().location()).withStyle(TextFormatting.RED), Util.NIL_UUID);
                    TelePastries.LOGGER.error("Destination of cake invalid {} isn't known", getCakeWorld().location());
                    return;
                }

                CakeTeleporter teleporter = new CakeTeleporter(destinationWorld);
                CakeTeleporter.addDimensionPosition(playerMP, playerMP.getLevel().dimension(), playerMP.blockPosition().offset(0,1,0));
                playerMP.changeDimension(destinationWorld, teleporter);
            }
        }
    }

    public boolean isRefillItem(ItemStack stack) {
        return false;
    }

    public RegistryKey<World> getCakeWorld() {
        return World.OVERWORLD;
    }

    public boolean consumeCake() {
        return true;
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BITES);
    }

    @Override
    @SuppressWarnings("deprecated")
    public boolean hasAnalogOutputSignal(BlockState state)
    {
        return true;
    }

    public boolean isPathfindable(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecated")
    public int getAnalogOutputSignal(BlockState blockState, World worldIn, BlockPos pos) {
        return (7 - blockState.getValue(BITES)) * 2;
    }

    protected void removeDimensionPosition(ServerPlayerEntity player, RegistryKey<World> dim) {
        CompoundNBT playerData = player.getPersistentData();
        CompoundNBT data = getTag(playerData);

        if(data.contains(Reference.MOD_PREFIX + dim.location())) {
            data.remove(Reference.MOD_PREFIX + dim.location());
            player.sendMessage(new TranslationTextComponent("telepastries.pastry.reset.complete", dim.location()), Util.NIL_UUID);
        } else {
            player.sendMessage(new TranslationTextComponent("telepastries.pastry.reset.failed", dim.location()), Util.NIL_UUID);
        }

        playerData.put(PlayerEntity.PERSISTED_NBT_TAG, data);
    }

    protected CompoundNBT getTag(CompoundNBT tag) {
        if(tag == null || !tag.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
            return new CompoundNBT();
        }
        return tag.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
    }
}
