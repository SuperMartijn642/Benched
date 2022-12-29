package com.supermartijn642.benched.blocks;

import com.supermartijn642.benched.seat.SeatBlock;
import com.supermartijn642.core.block.BlockProperties;
import com.supermartijn642.core.block.BlockShape;
import com.supermartijn642.core.block.EntityHoldingBlock;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 7/10/2020 by SuperMartijn642
 */
public class BenchBlock extends SeatBlock implements EntityHoldingBlock {

    private static final BlockShape SHAPE1 =
        BlockShape.or(BlockShape.create(0, 0, 3 / 32d, 1, 17 / 32d, 1),
            BlockShape.create(0, 0, 7 / 16d, 1, 28.5 / 32d, 1)),
        SHAPE2 =
            BlockShape.or(BlockShape.create(0, 0, 0, 29 / 32d, 17 / 32d, 1),
                BlockShape.create(0, 0, 0, 9 / 16d, 28.5 / 32d, 1)),
        SHAPE3 =
            BlockShape.or(BlockShape.create(0, 0, 0, 1, 17 / 32d, 29 / 32d),
                BlockShape.create(0, 0, 0, 1, 28.5 / 32d, 9 / 16d)),
        SHAPE4 =
            BlockShape.or(BlockShape.create(3 / 32d, 0, 0, 1, 17 / 32d, 1),
                BlockShape.create(7 / 16d, 0, 0, 1, 28.5 / 32d, 1));
    private static final BlockShape[] SHAPES = new BlockShape[]{SHAPE1, SHAPE2, SHAPE3, SHAPE4};

    public static final PropertyBool VISIBLE = PropertyBool.create("visible");
    public static final PropertyEnum<EnumFacing> ROTATION = PropertyEnum.create("rotation", EnumFacing.class, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST);

    public BenchBlock(){
        super(false, BlockProperties.create(Material.WOOD, MapColor.BROWN).destroyTime(1.5f).explosionResistance(6));
        this.setDefaultState(this.getDefaultState().withProperty(VISIBLE, true).withProperty(ROTATION, EnumFacing.NORTH));
    }

    @Override
    protected InteractionFeedback interact(IBlockState state, World level, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing hitSide, Vec3d hitLocation){
        if(!level.isRemote){
            ItemStack stack = player.getHeldItem(hand);
            TileEntity entity = level.getTileEntity(pos);
            if(stack.isEmpty()){
                if(player.isSneaking() && entity instanceof BenchBlockEntity){
                    stack = ((BenchBlockEntity)entity).removeItem();
                    if(!stack.isEmpty()){
                        player.setHeldItem(hand, stack);
                        return InteractionFeedback.CONSUME;
                    }
                }
            }else{
                if(entity instanceof BenchBlockEntity)
                    ((BenchBlockEntity)entity).addItem(stack);
                return InteractionFeedback.CONSUME;
            }
        }
        return super.interact(state, level, pos, player, hand, hitSide, hitLocation);
    }

    @Override
    protected double getSeatHeight(){
        return 0.7;
    }

    @Override
    public IBlockState getStateForPlacement(World level, BlockPos pos, EnumFacing hitSide, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand){
        EnumFacing facing = placer.getHorizontalFacing();
        if(!canBeReplaced(level, pos.offset(facing)) || !canBeReplaced(level, pos.offset(facing.rotateY())) || !canBeReplaced(level, pos.offset(facing).offset(facing.rotateY())))
            return null;

        return this.getDefaultState().withProperty(ROTATION, facing);
    }

    private static boolean canBeReplaced(World level, BlockPos pos){
        return level.isAirBlock(pos) || level.getBlockState(pos).getBlock().isReplaceable(level, pos);
    }

    @Override
    public void onBlockPlacedBy(World level, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack){
        EnumFacing facing = placer.getHorizontalFacing();
        List<BlockPos> others = new ArrayList<>(4);
        List<BenchBlockEntity> entities = new ArrayList<>(4);
        others.add(pos);
        TileEntity entity = level.getTileEntity(pos);
        if(entity instanceof BenchBlockEntity){
            entities.add((BenchBlockEntity)entity);
            ((BenchBlockEntity)entity).shape = facing.rotateY().getHorizontalIndex();
        }
        BlockPos pos1 = pos.offset(facing);
        others.add(pos1);
        level.setBlockState(pos1, state.withProperty(BenchBlock.VISIBLE, false));
        entity = level.getTileEntity(pos1);
        if(entity instanceof BenchBlockEntity){
            entities.add((BenchBlockEntity)entity);
            ((BenchBlockEntity)entity).shape = facing.rotateY().getHorizontalIndex();
        }
        pos1 = pos.offset(facing.rotateY());
        others.add(pos1);
        level.setBlockState(pos1, state.withProperty(BenchBlock.VISIBLE, false));
        entity = level.getTileEntity(pos1);
        if(entity instanceof BenchBlockEntity){
            entities.add((BenchBlockEntity)entity);
            ((BenchBlockEntity)entity).shape = facing.rotateYCCW().getHorizontalIndex();
        }
        pos1 = pos.offset(facing).offset(facing.rotateY());
        others.add(pos1);
        level.setBlockState(pos1, state.withProperty(BenchBlock.VISIBLE, false));
        entity = level.getTileEntity(pos1);
        if(entity instanceof BenchBlockEntity){
            entities.add((BenchBlockEntity)entity);
            ((BenchBlockEntity)entity).shape = facing.rotateYCCW().getHorizontalIndex();
        }

        for(BenchBlockEntity bench : entities){
            bench.setOthers(others);
            bench.markDirty();
        }
    }

    @Override
    public void breakBlock(World level, BlockPos pos, IBlockState state){
        TileEntity entity = level.getTileEntity(pos);
        if(entity instanceof BenchBlockEntity){
            ((BenchBlockEntity)entity).dropItems();
            for(BlockPos other : ((BenchBlockEntity)entity).getOthers())
                if(level.getBlockState(other).getBlock() == this)
                    level.setBlockState(other, Blocks.AIR.getDefaultState());
        }
        super.breakBlock(level, pos, state);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess level, BlockPos pos){
        TileEntity entity = level.getTileEntity(pos);
        if(entity instanceof BenchBlockEntity)
            return SHAPES[((BenchBlockEntity)entity).shape].simplify();
        return new AxisAlignedBB(0, 0, 0, 0, 0, 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public float getAmbientOcclusionLightValue(IBlockState state){
        return 1F;
    }

    @Override
    public boolean causesSuffocation(IBlockState state){
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state){
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos){
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state){
        return state.getValue(VISIBLE) ? EnumBlockRenderType.MODEL : EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public TileEntity createNewBlockEntity(){
        return new BenchBlockEntity();
    }

    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, VISIBLE, ROTATION);
    }

    @Override
    public IBlockState getStateFromMeta(int meta){
        return this.getDefaultState().withProperty(VISIBLE, meta % 2 == 1).withProperty(ROTATION, EnumFacing.getHorizontal(meta >> 1));
    }

    @Override
    public int getMetaFromState(IBlockState state){
        return (state.getValue(VISIBLE) ? 1 : 0) + (state.getValue(ROTATION).getHorizontalIndex() << 1);
    }

    @Override
    public boolean isTopSolid(IBlockState state){
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face){
        return BlockFaceShape.UNDEFINED;
    }
}
