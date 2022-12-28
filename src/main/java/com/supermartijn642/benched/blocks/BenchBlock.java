package com.supermartijn642.benched.blocks;

import com.supermartijn642.benched.seat.SeatBlock;
import com.supermartijn642.core.block.BlockProperties;
import com.supermartijn642.core.block.BlockShape;
import com.supermartijn642.core.block.EntityHoldingBlock;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 7/10/2020 by SuperMartijn642
 */
public class BenchBlock extends SeatBlock implements EntityHoldingBlock, IWaterLoggable {

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
        super(false, BlockProperties.create(Material.WOOD, MaterialColor.COLOR_BROWN).destroyTime(1.5f).explosionResistance(6));
        this.registerDefaultState(this.defaultBlockState().setValue(VISIBLE, true).setValue(ROTATION, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    protected InteractionFeedback interact(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, Direction hitSide, Vec3d hitLocation){
        if(!level.isClientSide){
            ItemStack stack = player.getItemInHand(hand);
            TileEntity entity = level.getBlockEntity(pos);
            if(stack.isEmpty()){
                if(player.isSneaking() && entity instanceof BenchBlockEntity){
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
    protected double getSeatHeight(){
        return 0.7;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context){
        World level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getHorizontalDirection();
        if(!canBeReplaced(level, pos.relative(facing)) || !canBeReplaced(level, pos.relative(facing.getClockWise())) || !canBeReplaced(level, pos.relative(facing).relative(facing.getClockWise())))
            return null;

        IFluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState().setValue(ROTATION, context.getHorizontalDirection()).setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    private static boolean canBeReplaced(World level, BlockPos pos){
        return level.isEmptyBlock(pos) || level.getBlockState(pos).getBlock() == Blocks.WATER;
    }

    @Override
    public void setPlacedBy(World level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack){
        Direction facing = placer.getDirection();
        List<BlockPos> others = new ArrayList<>(4);
        List<BenchBlockEntity> entities = new ArrayList<>(4);
        others.add(pos);
        TileEntity entity = level.getBlockEntity(pos);
        if(entity instanceof BenchBlockEntity){
            entities.add((BenchBlockEntity)entity);
            ((BenchBlockEntity)entity).shape = facing.getClockWise().get2DDataValue();
        }
        BlockPos pos1 = pos.relative(facing);
        others.add(pos1);
        IFluidState fluidstate = level.getFluidState(pos1);
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
    public void onRemove(BlockState state, World level, BlockPos pos, BlockState newState, boolean isMoving){
        if(state.hasTileEntity() && (state.getBlock() != newState.getBlock() || !newState.hasTileEntity())){
            TileEntity entity = level.getBlockEntity(pos);
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
    public VoxelShape getShape(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext context){
        TileEntity entity = level.getBlockEntity(pos);
        if(entity instanceof BenchBlockEntity)
            return SHAPES[((BenchBlockEntity)entity).shape].getUnderlying();
        return VoxelShapes.empty();
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, IBlockReader level, BlockPos pos){
        return VoxelShapes.empty();
    }

    @OnlyIn(Dist.CLIENT)
    public float getShadeBrightness(BlockState state, IBlockReader level, BlockPos pos){
        return 1F;
    }

    public boolean propagatesSkylightDown(BlockState state, IBlockReader level, BlockPos pos){
        return true;
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state){
        return state.getValue(VISIBLE) ? BlockRenderType.MODEL : BlockRenderType.INVISIBLE;
    }

    @Override
    public TileEntity createNewBlockEntity(){
        return new BenchBlockEntity();
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block,BlockState> builder){
        builder.add(VISIBLE, ROTATION, WATERLOGGED);
    }

    @Override
    public IFluidState getFluidState(BlockState state){
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, IWorld level, BlockPos currentPos, BlockPos facingPos){
        if(state.getValue(WATERLOGGED))
            level.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }
}
