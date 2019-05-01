package com.mrbysco.telepastries.blocks.cake;

import com.mrbysco.telepastries.Reference;
import com.mrbysco.telepastries.TelePastries;
import com.mrbysco.telepastries.blocks.BlockPastryBase;
import com.mrbysco.telepastries.config.TeleConfig;
import com.mrbysco.telepastries.util.CakeTeleporter;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class BlockCakeBase extends BlockPastryBase {
    public static final PropertyInteger BITES = PropertyInteger.create("bites", 0, 6);
    protected static final AxisAlignedBB[] CAKE_AABB = new AxisAlignedBB[] {new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.1875D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.3125D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.4375D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.5625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.6875D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.8125D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D)};

    public BlockCakeBase(String registry) {
        super(Material.CAKE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BITES, Integer.valueOf(0)));
        this.setTickRandomly(true);
        this.setHardness(0.5F);
        this.setSoundType(SoundType.CLOTH);
        this.setCreativeTab(TelePastries.teleTab);

        this.setRegistryName(registry);
        this.setTranslationKey(Reference.MOD_PREFIX + registry);
    }

    @Override
    @SuppressWarnings("deprecated")
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return CAKE_AABB[(state.getValue(BITES))];
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = playerIn.getHeldItem(hand);

        if (consumeCake() && stack != ItemStack.EMPTY && stack.getItem() == getRefillItem()) {
            if(playerIn.capabilities.isCreativeMode) {
                worldIn.setBlockState(pos, getStateFromMeta(0), 2); //Creative Refill
            } else {
                int biteAmount = getBites(state)-1; //Bite Amount - 1
                if (biteAmount >= 0) {
                    worldIn.setBlockState(pos, getStateFromMeta(biteAmount), 2);
                    stack.shrink(1);
                }
            }
            return true;
        } else if(!worldIn.isRemote && worldIn.provider.getDimension() != getCakeDimension()){
            if (playerIn.canEat(true) || playerIn.isCreative()) {
                if(TeleConfig.general.resetPastry && stack.getItem() == Item.getByNameOrId(TeleConfig.general.resetItem)) {
                    removeDimensionPosition((EntityPlayerMP)playerIn, getCakeDimension());

                    if(Item.getByNameOrId(TeleConfig.general.resetItem) == Items.MILK_BUCKET) {
                        if(!playerIn.capabilities.isCreativeMode) {
                            stack = new ItemStack(Items.BUCKET);
                        }
                    }
                } else {
                    //TelePastries.logger.debug("At onBlockActivated before eatCake");
                    eatCake(worldIn, pos, state, playerIn);
                    //TelePastries.logger.debug("At onBlockActivated after eatCake");
                }
                return true;
            } else {
                return false;
            }
        }

        return true;
    }

    private void eatCake(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
        int l = state.getValue(BITES);

        if (l < 6) {
            if(!playerIn.capabilities.isCreativeMode && consumeCake()) {
                playerIn.addStat(StatList.CAKE_SLICES_EATEN);
                playerIn.getFoodStats().addStats(2, 0.1F);
                worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(BITES, l + 1), 3);
            }
            if (!ForgeHooks.onTravelToDimension(playerIn, getCakeDimension()))
                return;

            //TelePastries.logger.debug("At eatCake before teleportToDimension");
            teleportToDimension(worldIn, pos, playerIn);
            //TelePastries.logger.debug("At eatCake after teleportToDimension");
        } else {
            if(!playerIn.capabilities.isCreativeMode && consumeCake()) {
                worldIn.setBlockToAir(pos);
            }
        }
    }

    public void teleportToDimension(World world, BlockPos pos, EntityPlayer player) {
        if (!player.isDead) {
            if (!world.isRemote && !player.isRiding() && !player.isBeingRidden() && player.isNonBoss()) {
                EntityPlayerMP playerMP = (EntityPlayerMP)player;
                //TelePastries.logger.debug("teleportToDimension oldDimension: " + world.provider.getDimension());
                //TelePastries.logger.debug("teleportToDimension target cakeDimension: " + getCakeDimension());
                CakeTeleporter teleporter = new CakeTeleporter(playerMP.getServer().getWorld(getCakeDimension()), playerMP.getPosition());
                teleporter.addDimensionPosition(playerMP, playerMP.dimension, playerMP.getPosition().add(0,1,0));
                teleporter.teleportToDimension(playerMP, getCakeDimension(), playerMP.getPosition());
            }
        }
    }

    public Item getRefillItem() {
        return null;
    }

    public int getCakeDimension() {
        return 0;
    }

    public boolean consumeCake() {
        return true;
    }

    @Override
    @SuppressWarnings("deprecated")
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(BITES, Integer.valueOf(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return (state.getValue(BITES));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {BITES});
    }

    @Override
    @SuppressWarnings("deprecated")
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    @Override
    @SuppressWarnings("deprecated")
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return (7 - (blockState.getValue(BITES))) * 2;
    }

    protected int getBites(IBlockState state)
    {
        return (state.getValue(BITES));
    }

    protected void removeDimensionPosition(EntityPlayerMP player, int dim) {
        NBTTagCompound playerData = player.getEntityData();
        NBTTagCompound data = getTag(playerData, EntityPlayer.PERSISTED_NBT_TAG);

        if(data.hasKey(Reference.MOD_PREFIX + dim)) {
            data.removeTag(Reference.MOD_PREFIX + dim);
            player.sendMessage(new TextComponentTranslation("telepastries.pastry.reset.complete", new Object[] {dim}));
        } else {
            player.sendMessage(new TextComponentTranslation("telepastries.pastry.reset.failed", new Object[] {dim}));
        }

        playerData.setTag(EntityPlayer.PERSISTED_NBT_TAG, data);
    }

    protected NBTTagCompound getTag(NBTTagCompound tag, String key) {
        if(tag == null || !tag.hasKey(key)) {
            return new NBTTagCompound();
        }
        return tag.getCompoundTag(key);
    }
}
