package com.supermartijn642.benched.blocks;

import com.supermartijn642.benched.seat.SeatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created 7/10/2020 by SuperMartijn642
 */
public class BenchBlock extends SeatBlock implements EntityBlock, SimpleWaterloggedBlock {

    private static final VoxelShape SHAPE3 =
        Shapes.or(Shapes.box(0, 0, 0, 1, 17 / 32d, 29 / 32d),
            Shapes.box(0, 0, 0, 1, 28.5 / 32d, 9 / 16d)),
        SHAPE1 =
            Shapes.or(Shapes.box(0, 0, 3 / 32d, 1, 17 / 32d, 1),
                Shapes.box(0, 0, 7 / 16d, 1, 28.5 / 32d, 1)),
        SHAPE2 =
            Shapes.or(Shapes.box(0, 0, 0, 29 / 32d, 17 / 32d, 1),
                Shapes.box(0, 0, 0, 9 / 16d, 28.5 / 32d, 1)),
        SHAPE4 =
            Shapes.or(Shapes.box(3 / 32d, 0, 0, 1, 17 / 32d, 1),
                Shapes.box(7 / 16d, 0, 0, 1, 28.5 / 32d, 1));
    private static final VoxelShape[] SHAPES = new VoxelShape[]{SHAPE1, SHAPE2, SHAPE3, SHAPE4};

    public static final BooleanProperty VISIBLE = BooleanProperty.create("visible");
    public static final EnumProperty<Direction> ROTATION = EnumProperty.create("rotation", Direction.class, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public BenchBlock(String registryName){
        super(Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).strength(1.5f, 6).harvestLevel(0).harvestTool(ToolType.AXE), registryName, false);
        this.registerDefaultState(this.defaultBlockState().setValue(VISIBLE, true).setValue(ROTATION, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit){
        if(!worldIn.isClientSide){
            ItemStack stack = player.getItemInHand(handIn);
            BlockEntity tile = worldIn.getBlockEntity(pos);
            if(stack.isEmpty()){
                if(player.isCrouching() && tile instanceof BenchTile){
                    stack = ((BenchTile)tile).removeItem();
                    if(!stack.isEmpty()){
                        player.setItemInHand(handIn, stack);
                        return InteractionResult.CONSUME;
                    }
                }
            }else{
                if(tile instanceof BenchTile)
                    ((BenchTile)tile).addItem(stack);
                return InteractionResult.CONSUME;
            }
        }
        return super.use(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    protected double getSeatHeight(){
        return 0.7;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context){
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getHorizontalDirection();
        if(!canBeReplaced(world, pos.relative(facing)) || !canBeReplaced(world, pos.relative(facing.getClockWise())) || !canBeReplaced(world, pos.relative(facing).relative(facing.getClockWise())))
            return null;

        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState().setValue(ROTATION, context.getHorizontalDirection()).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    private static boolean canBeReplaced(Level world, BlockPos pos){
        return world.isEmptyBlock(pos) || world.getBlockState(pos).getBlock() == Blocks.WATER;
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack){
        Direction facing = placer.getDirection();
        List<BlockPos> others = new ArrayList<>(4);
        List<BenchTile> tiles = new ArrayList<>(4);
        others.add(pos);
        BlockEntity tile = worldIn.getBlockEntity(pos);
        if(tile instanceof BenchTile){
            tiles.add((BenchTile)tile);
            ((BenchTile)tile).shape = facing.getClockWise().get2DDataValue();
        }
        BlockPos pos1 = pos.relative(facing);
        others.add(pos1);
        FluidState fluidstate = worldIn.getFluidState(pos1);
        worldIn.setBlockAndUpdate(pos1, state.setValue(BenchBlock.VISIBLE, false).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER));
        tile = worldIn.getBlockEntity(pos1);
        if(tile instanceof BenchTile){
            tiles.add((BenchTile)tile);
            ((BenchTile)tile).shape = facing.getClockWise().get2DDataValue();
        }
        pos1 = pos.relative(facing.getClockWise());
        others.add(pos1);
        fluidstate = worldIn.getFluidState(pos1);
        worldIn.setBlockAndUpdate(pos1, state.setValue(BenchBlock.VISIBLE, false).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER));
        tile = worldIn.getBlockEntity(pos1);
        if(tile instanceof BenchTile){
            tiles.add((BenchTile)tile);
            ((BenchTile)tile).shape = facing.getCounterClockWise().get2DDataValue();
        }
        pos1 = pos.relative(facing).relative(facing.getClockWise());
        others.add(pos1);
        fluidstate = worldIn.getFluidState(pos1);
        worldIn.setBlockAndUpdate(pos1, state.setValue(BenchBlock.VISIBLE, false).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER));
        tile = worldIn.getBlockEntity(pos1);
        if(tile instanceof BenchTile){
            tiles.add((BenchTile)tile);
            ((BenchTile)tile).shape = facing.getCounterClockWise().get2DDataValue();
        }

        for(BenchTile tile1 : tiles){
            tile1.setOthers(others);
            tile1.setChanged();
        }
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving){
        if(state.hasBlockEntity() && (!state.is(newState.getBlock()) || !newState.hasBlockEntity())){
            BlockEntity tile = worldIn.getBlockEntity(pos);
            if(tile instanceof BenchTile){
                ((BenchTile)tile).dropItems();
                for(BlockPos other : ((BenchTile)tile).getOthers()){
                    BlockState state1 = worldIn.getBlockState(other);
                    if(state1.getBlock() == this){
                        worldIn.setBlockAndUpdate(other,
                            state1.getValue(WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context){
        BlockEntity tile = worldIn.getBlockEntity(pos);
        if(tile instanceof BenchTile)
            return SHAPES[((BenchTile)tile).shape];
        return Shapes.empty();
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter worldIn, BlockPos pos){
        return Shapes.empty();
    }

    @OnlyIn(Dist.CLIENT)
    public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos){
        return 1F;
    }

    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos){
        return true;
    }

    @Override
    public RenderShape getRenderShape(BlockState state){
        return state.getValue(VISIBLE) ? RenderShape.MODEL : RenderShape.INVISIBLE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state){
        return new BenchTile(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block,BlockState> builder){
        builder.add(VISIBLE, ROTATION, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state){
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos){
        if(stateIn.getValue(WATERLOGGED))
            worldIn.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }
}
