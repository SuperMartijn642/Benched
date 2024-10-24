package com.supermartijn642.benched.blocks;

import com.supermartijn642.benched.seat.SeatBlock;
import com.supermartijn642.core.block.BlockProperties;
import com.supermartijn642.core.block.BlockShape;
import com.supermartijn642.core.block.EntityHoldingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 7/10/2020 by SuperMartijn642
 */
public class BenchBlock extends SeatBlock implements EntityHoldingBlock, SimpleWaterloggedBlock {

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

    public static final BooleanProperty VISIBLE = BooleanProperty.create("visible");
    public static final EnumProperty<Direction> ROTATION = EnumProperty.create("rotation", Direction.class, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public BenchBlock(){
        super(false, BlockProperties.create().mapColor(MapColor.COLOR_BROWN).sound(SoundType.WOOD).destroyTime(1.5f).explosionResistance(6));
        this.registerDefaultState(this.defaultBlockState().setValue(VISIBLE, true).setValue(ROTATION, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    protected InteractionFeedback interact(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, Direction hitSide, Vec3 hitLocation){
        if(!level.isClientSide){
            ItemStack stack = player.getItemInHand(hand);
            BlockEntity entity = level.getBlockEntity(pos);
            if(stack.isEmpty()){
                if(player.isCrouching() && entity instanceof BenchBlockEntity){
                    stack = ((BenchBlockEntity)entity).removeItem();
                    if(!stack.isEmpty()){
                        player.setItemInHand(hand, stack);
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
    protected Vec3 getSeatPosition(BlockState state, BlockPos pos){
        return new Vec3(pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context){
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getHorizontalDirection();
        if(!canBeReplaced(level, pos.relative(facing)) || !canBeReplaced(level, pos.relative(facing.getClockWise())) || !canBeReplaced(level, pos.relative(facing).relative(facing.getClockWise())))
            return null;

        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState().setValue(ROTATION, context.getHorizontalDirection()).setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    private static boolean canBeReplaced(Level level, BlockPos pos){
        return level.isEmptyBlock(pos) || level.getBlockState(pos).getBlock() == Blocks.WATER;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack){
        Direction facing = placer.getDirection();
        List<BlockPos> others = new ArrayList<>(4);
        List<BenchBlockEntity> entities = new ArrayList<>(4);
        others.add(pos);
        BlockEntity entity = level.getBlockEntity(pos);
        if(entity instanceof BenchBlockEntity){
            entities.add((BenchBlockEntity)entity);
            ((BenchBlockEntity)entity).shape = facing.getClockWise().get2DDataValue();
        }
        BlockPos pos1 = pos.relative(facing);
        others.add(pos1);
        FluidState fluidstate = level.getFluidState(pos1);
        level.setBlockAndUpdate(pos1, state.setValue(BenchBlock.VISIBLE, false).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER));
        entity = level.getBlockEntity(pos1);
        if(entity instanceof BenchBlockEntity){
            entities.add((BenchBlockEntity)entity);
            ((BenchBlockEntity)entity).shape = facing.getClockWise().get2DDataValue();
        }
        pos1 = pos.relative(facing.getClockWise());
        others.add(pos1);
        fluidstate = level.getFluidState(pos1);
        level.setBlockAndUpdate(pos1, state.setValue(BenchBlock.VISIBLE, false).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER));
        entity = level.getBlockEntity(pos1);
        if(entity instanceof BenchBlockEntity){
            entities.add((BenchBlockEntity)entity);
            ((BenchBlockEntity)entity).shape = facing.getCounterClockWise().get2DDataValue();
        }
        pos1 = pos.relative(facing).relative(facing.getClockWise());
        others.add(pos1);
        fluidstate = level.getFluidState(pos1);
        level.setBlockAndUpdate(pos1, state.setValue(BenchBlock.VISIBLE, false).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER));
        entity = level.getBlockEntity(pos1);
        if(entity instanceof BenchBlockEntity){
            entities.add((BenchBlockEntity)entity);
            ((BenchBlockEntity)entity).shape = facing.getCounterClockWise().get2DDataValue();
        }

        for(BenchBlockEntity bench : entities){
            bench.setOthers(others);
            bench.setChanged();
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving){
        if(state.hasBlockEntity() && (!state.is(newState.getBlock()) || !newState.hasBlockEntity())){
            BlockEntity entity = level.getBlockEntity(pos);
            if(entity instanceof BenchBlockEntity){
                ((BenchBlockEntity)entity).dropItems();
                for(BlockPos other : ((BenchBlockEntity)entity).getOthers()){
                    BlockState state1 = level.getBlockState(other);
                    if(state1.getBlock() == this){
                        level.setBlockAndUpdate(other,
                            state1.getValue(WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context){
        BlockEntity entity = level.getBlockEntity(pos);
        if(entity instanceof BenchBlockEntity)
            return SHAPES[((BenchBlockEntity)entity).shape].getUnderlying();
        return Shapes.empty();
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state){
        return Shapes.empty();
    }

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

    @Override
    public BlockEntity createNewBlockEntity(BlockPos pos, BlockState state){
        return new BenchBlockEntity(pos, state);
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
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random){
        if(state.getValue(WATERLOGGED))
            tickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        return super.updateShape(state, level, tickAccess, pos, direction, neighborPos, neighborState, random);
    }
}
