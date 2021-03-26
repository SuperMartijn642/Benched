package com.supermartijn642.benched.blocks;

import com.supermartijn642.benched.seat.SeatBlock;
import com.supermartijn642.core.ToolType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 7/10/2020 by SuperMartijn642
 */
public class BenchBlock extends SeatBlock {

    private static final AxisAlignedBB SHAPE3 =
        new AxisAlignedBB(0, 0, 0, 1, 17 / 32d, 29 / 32d).union(
            new AxisAlignedBB(0, 0, 0, 1, 28.5 / 32d, 9 / 16d)),
        SHAPE1 =
            new AxisAlignedBB(0, 0, 3 / 32d, 1, 17 / 32d, 1).union(
                new AxisAlignedBB(0, 0, 7 / 16d, 1, 28.5 / 32d, 1)),
        SHAPE2 =
            new AxisAlignedBB(0, 0, 0, 29 / 32d, 17 / 32d, 1).union(
                new AxisAlignedBB(0, 0, 0, 9 / 16d, 28.5 / 32d, 1)),
        SHAPE4 =
            new AxisAlignedBB(3 / 32d, 0, 0, 1, 17 / 32d, 1).union(
                new AxisAlignedBB(7 / 16d, 0, 0, 1, 28.5 / 32d, 1));
    private static final AxisAlignedBB[] SHAPES = new AxisAlignedBB[]{SHAPE1, SHAPE2, SHAPE3, SHAPE4};

    public static final PropertyBool VISIBLE = PropertyBool.create("visible");
    public static final PropertyEnum<EnumFacing> ROTATION = PropertyEnum.create("rotation", EnumFacing.class, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST);

    public BenchBlock(String registryName){
        super(Properties.create(Material.WOOD, MapColor.BROWN).hardnessAndResistance(1.5f, 6).harvestLevel(0).harvestTool(ToolType.AXE), registryName, false);
        this.setUnlocalizedName("benched." + registryName);
        this.setCreativeTab(CreativeTabs.DECORATIONS);

        this.setDefaultState(this.getDefaultState().withProperty(VISIBLE, true).withProperty(ROTATION, EnumFacing.NORTH));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand handIn, EnumFacing facing, float hitX, float hitY, float hitZ){
        if(!worldIn.isRemote){
            ItemStack stack = player.getHeldItem(handIn);
            TileEntity tile = worldIn.getTileEntity(pos);
            if(stack.isEmpty()){
                if(player.isSneaking() && tile instanceof BenchTile){
                    stack = ((BenchTile)tile).removeItem();
                    if(!stack.isEmpty()){
                        player.setHeldItem(handIn, stack);
                        return true;
                    }
                }
            }else{
                if(tile instanceof BenchTile)
                    ((BenchTile)tile).addItem(stack);
                return true;
            }
        }
        return super.onBlockActivated(worldIn, pos, state, player, handIn, facing, hitX, hitY, hitZ);
    }

    @Override
    protected double getSeatHeight(){
        return 0.7;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand){
        if(!world.isAirBlock(pos.offset(placer.getHorizontalFacing())) || !world.isAirBlock(pos.offset(placer.getHorizontalFacing().rotateY())) || !world.isAirBlock(pos.offset(placer.getHorizontalFacing()).offset(placer.getHorizontalFacing().rotateY())))
            return Blocks.AIR.getDefaultState();
        return this.getDefaultState().withProperty(ROTATION, placer.getHorizontalFacing());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack){
        EnumFacing facing = placer.getHorizontalFacing();
        List<BlockPos> others = new ArrayList<>(4);
        List<BenchTile> tiles = new ArrayList<>(4);
        others.add(pos);
        TileEntity tile = worldIn.getTileEntity(pos);
        if(tile instanceof BenchTile){
            tiles.add((BenchTile)tile);
            ((BenchTile)tile).shape = facing.rotateY().getHorizontalIndex();
        }
        BlockPos pos1 = pos.offset(facing);
        others.add(pos1);
        worldIn.setBlockState(pos1, state.withProperty(BenchBlock.VISIBLE, false));
        tile = worldIn.getTileEntity(pos1);
        if(tile instanceof BenchTile){
            tiles.add((BenchTile)tile);
            ((BenchTile)tile).shape = facing.rotateY().getHorizontalIndex();
        }
        pos1 = pos.offset(facing.rotateY());
        others.add(pos1);
        worldIn.setBlockState(pos1, state.withProperty(BenchBlock.VISIBLE, false));
        tile = worldIn.getTileEntity(pos1);
        if(tile instanceof BenchTile){
            tiles.add((BenchTile)tile);
            ((BenchTile)tile).shape = facing.rotateYCCW().getHorizontalIndex();
        }
        pos1 = pos.offset(facing).offset(facing.rotateY());
        others.add(pos1);
        worldIn.setBlockState(pos1, state.withProperty(BenchBlock.VISIBLE, false));
        tile = worldIn.getTileEntity(pos1);
        if(tile instanceof BenchTile){
            tiles.add((BenchTile)tile);
            ((BenchTile)tile).shape = facing.rotateYCCW().getHorizontalIndex();
        }

        for(BenchTile tile1 : tiles){
            tile1.setOthers(others);
            tile1.markDirty();
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state){
        TileEntity tile = worldIn.getTileEntity(pos);
        if(tile instanceof BenchTile)
            for(BlockPos other : ((BenchTile)tile).getOthers())
                if(worldIn.getBlockState(other).getBlock() == this)
                    worldIn.setBlockState(other, Blocks.AIR.getDefaultState());
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
        TileEntity tile = source.getTileEntity(pos);
        if(tile instanceof BenchTile)
            return SHAPES[((BenchTile)tile).shape];
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
    public boolean hasTileEntity(IBlockState state){
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state){
        return new BenchTile();
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
