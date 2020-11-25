package com.mrbysco.telepastries.blocks.cake;

import com.mrbysco.telepastries.Reference;
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
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;

import java.util.List;

public class BlockCakeBase extends BlockPastryBase {
    public static final IntegerProperty BITES = BlockStateProperties.BITES_0_6;
    protected static final VoxelShape[] SHAPES = new VoxelShape[]{Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D), Block.makeCuboidShape(3.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D), Block.makeCuboidShape(5.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D), Block.makeCuboidShape(7.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D), Block.makeCuboidShape(9.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D), Block.makeCuboidShape(11.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D), Block.makeCuboidShape(13.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D)};

    public BlockCakeBase(AbstractBlock.Properties properties) {
        super(properties.hardnessAndResistance(0.5F).sound(SoundType.CLOTH).tickRandomly());
        this.setDefaultState(this.stateContainer.getBaseState().with(BITES, Integer.valueOf(0)));
    }

    @Override
    @SuppressWarnings("deprecated")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPES[state.get(BITES)];
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote) {
            ItemStack stack = player.getHeldItem(handIn);
            if (consumeCake()) {
                if(!stack.isEmpty() && isRefillItem(stack)) {
                    int i = state.get(BITES);
                    if(i > 0) {
                        worldIn.setBlockState(pos, state.with(BITES, Integer.valueOf(i - 1)), 3);
                    }
                    if(!player.abilities.isCreativeMode) {
                        stack.shrink(1);
                    }
                    return ActionResultType.SUCCESS;
                } else {
                    if(worldIn.getDimensionKey() != getCakeWorld()) {
                        if(TeleConfig.SERVER.resetPastry.get() && isResetItem(stack)) {
                            removeDimensionPosition((ServerPlayerEntity)player, getCakeWorld());

                            if(stack.getItem() == Items.MILK_BUCKET) {
                                if(!player.abilities.isCreativeMode) {
                                    stack.shrink(1);
                                    player.setHeldItem(handIn, new ItemStack(Items.BUCKET));
                                }
                            }
                            return ActionResultType.SUCCESS;
                        } else {
                            //TelePastries.logger.debug("At onBlockActivated before eatCake");
                            if (this.eatSlice(worldIn, pos, state, player).isSuccessOrConsume()) {
                                return ActionResultType.SUCCESS;
                            }
                            //TelePastries.logger.debug("At onBlockActivated after eatCake");
                            return ActionResultType.FAIL;
                        }
                    } else {
                        return ActionResultType.FAIL;
                    }
                }
            }

            if (stack.isEmpty()) {
                return ActionResultType.CONSUME;
            }
        }

        return ActionResultType.SUCCESS;
    }

    private ActionResultType eatSlice(IWorld world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!player.canEat(TeleConfig.SERVER.ignoreHunger.get())) {
            return ActionResultType.PASS;
        } else {
            player.addStat(Stats.EAT_CAKE_SLICE);
            player.getFoodStats().addStats(2, 0.1F);
            if(!player.abilities.isCreativeMode) {
                int i = state.get(BITES);
                if (i < 6) {
                    world.setBlockState(pos, state.with(BITES, Integer.valueOf(i + 1)), 3);
                } else {
                    world.removeBlock(pos, false);
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
        if (player.isAlive() && !worldIn.isRemote()) {
            World world = ((IServerWorld)worldIn).getWorld();
            if (!world.isRemote && !player.isPassenger() && !player.isBeingRidden() && player.isNonBoss()) {
                ServerPlayerEntity playerMP = (ServerPlayerEntity) player;
                MinecraftServer server = player.getServer();
                ServerWorld destinationWorld = server != null ? server.getWorld(getCakeWorld()) : null;
                if(destinationWorld == null)
                    return;

                CakeTeleporter teleporter = new CakeTeleporter(destinationWorld);
                teleporter.addDimensionPosition(playerMP, playerMP.getServerWorld().getDimensionKey(), playerMP.getPosition().add(0,1,0));
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

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BITES);
    }

    @Override
    @SuppressWarnings("deprecated")
    public boolean hasComparatorInputOverride(BlockState state)
    {
        return true;
    }

    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecated")
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        return (7 - blockState.get(BITES)) * 2;
    }

    protected void removeDimensionPosition(ServerPlayerEntity player, RegistryKey<World> dim) {
        CompoundNBT playerData = player.getPersistentData();
        CompoundNBT data = getTag(playerData);

        if(data.contains(Reference.MOD_PREFIX + dim.getLocation())) {
            data.remove(Reference.MOD_PREFIX + dim.getLocation());
            player.sendMessage(new TranslationTextComponent("telepastries.pastry.reset.complete", dim.getLocation()), Util.DUMMY_UUID);
        } else {
            player.sendMessage(new TranslationTextComponent("telepastries.pastry.reset.failed", dim.getLocation()), Util.DUMMY_UUID);
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
