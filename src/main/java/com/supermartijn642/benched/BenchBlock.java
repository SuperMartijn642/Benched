package com.supermartijn642.benched;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 7/10/2020 by SuperMartijn642
 */
public class BenchBlock extends Block {

    private static final VoxelShape SHAPE3 =
        VoxelShapes.or(VoxelShapes.create(0, 0, 0, 1, 17 / 32d, 29 / 32d),
            VoxelShapes.create(0, 0, 0, 1, 28.5 / 32d, 9 / 16d)),
        SHAPE1 =
            VoxelShapes.or(VoxelShapes.create(0, 0, 3 / 32d, 1, 17 / 32d, 1),
                VoxelShapes.create(0, 0, 7 / 16d, 1, 28.5 / 32d, 1)),
        SHAPE2 =
            VoxelShapes.or(VoxelShapes.create(0, 0, 0, 29 / 32d, 17 / 32d, 1),
                VoxelShapes.create(0, 0, 0, 9 / 16d, 28.5 / 32d, 1)),
        SHAPE4 =
            VoxelShapes.or(VoxelShapes.create(3 / 32d, 0, 0, 1, 17 / 32d, 1),
                VoxelShapes.create(7 / 16d, 0, 0, 1, 28.5 / 32d, 1));
    private static final VoxelShape[] SHAPES = new VoxelShape[]{SHAPE1, SHAPE2, SHAPE3, SHAPE4};

    private static final BooleanProperty VISIBLE = BooleanProperty.create("visible");
    private static final EnumProperty<Direction> ROTATION = EnumProperty.create("rotation", Direction.class, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);

    public BenchBlock(){
        super(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(1.5f, 6).harvestLevel(0).harvestTool(ToolType.AXE));
        this.setRegistryName("bench");
        this.setDefaultState(this.getDefaultState().with(VISIBLE, true).with(ROTATION, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context){
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        Direction facing = context.getPlacementHorizontalFacing();
        if(!world.isAirBlock(pos.offset(facing)) || !world.isAirBlock(pos.offset(facing.rotateY())) || !world.isAirBlock(pos.offset(facing).offset(facing.rotateY())))
            return null;
        return this.getDefaultState().with(ROTATION, context.getPlacementHorizontalFacing());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack){
        Direction facing = placer.getHorizontalFacing();
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
        worldIn.setBlockState(pos1, state.with(BenchBlock.VISIBLE, false));
        tile = worldIn.getTileEntity(pos1);
        if(tile instanceof BenchTile){
            tiles.add((BenchTile)tile);
            ((BenchTile)tile).shape = facing.rotateY().getHorizontalIndex();
        }
        pos1 = pos.offset(facing.rotateY());
        others.add(pos1);
        worldIn.setBlockState(pos1, state.with(BenchBlock.VISIBLE, false));
        tile = worldIn.getTileEntity(pos1);
        if(tile instanceof BenchTile){
            tiles.add((BenchTile)tile);
            ((BenchTile)tile).shape = facing.rotateYCCW().getHorizontalIndex();
        }
        pos1 = pos.offset(facing).offset(facing.rotateY());
        others.add(pos1);
        worldIn.setBlockState(pos1, state.with(BenchBlock.VISIBLE, false));
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
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving){
        TileEntity tile = worldIn.getTileEntity(pos);
        if(tile instanceof BenchTile)
            for(BlockPos other : ((BenchTile)tile).getOthers())
                if(worldIn.getBlockState(other).getBlock() == this)
                    worldIn.setBlockState(other, Blocks.AIR.getDefaultState());
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
        TileEntity tile = worldIn.getTileEntity(pos);
        if(tile instanceof BenchTile)
            return SHAPES[((BenchTile)tile).shape];
        return VoxelShapes.fullCube();
    }

    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos){
        return VoxelShapes.empty();
    }

    @OnlyIn(Dist.CLIENT)
    public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos){
        return 0.7F;
    }

    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos){
        return true;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state){
        return state.get(VISIBLE) ? BlockRenderType.MODEL : BlockRenderType.INVISIBLE;
    }

    @Override
    public boolean hasTileEntity(BlockState state){
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world){
        return new BenchTile();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block,BlockState> builder){
        builder.add(VISIBLE, ROTATION);
    }
}
