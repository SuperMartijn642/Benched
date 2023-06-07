package com.supermartijn642.benched.blocks;

import com.supermartijn642.benched.seat.SeatBlock;
import com.supermartijn642.core.block.BlockProperties;
import com.supermartijn642.core.block.BlockShape;
import com.supermartijn642.core.block.EntityHoldingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
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

import java.util.Locale;

/**
 * Created 7/10/2020 by SuperMartijn642
 */
public class BenchBlock extends SeatBlock implements EntityHoldingBlock, SimpleWaterloggedBlock {

    private static final BlockShape SHAPE_REGULAR = BlockShape.or(BlockShape.createBlockShape(0.25, 0, 0, 7.25, 8, 15.25),
        BlockShape.createBlockShape(8, 0, 0, 16, 15, 16));
    private static final BlockShape SHAPE_MIRRORED = SHAPE_REGULAR.flip(Direction.Axis.X);
    private static final BlockShape[][] SHAPES = new BlockShape[][]{
        {SHAPE_REGULAR.rotate(Direction.Axis.Y), SHAPE_MIRRORED.rotate(Direction.Axis.Y), SHAPE_MIRRORED.rotate(Direction.Axis.Y).rotate(Direction.Axis.Y).rotate(Direction.Axis.Y), SHAPE_REGULAR.rotate(Direction.Axis.Y).rotate(Direction.Axis.Y).rotate(Direction.Axis.Y)},
        {SHAPE_MIRRORED.rotate(Direction.Axis.Y).rotate(Direction.Axis.Y), SHAPE_REGULAR, SHAPE_REGULAR.rotate(Direction.Axis.Y).rotate(Direction.Axis.Y), SHAPE_MIRRORED}
    };

    public static final EnumProperty<Direction.Axis> AXIS = EnumProperty.create("axis", Direction.Axis.class, Direction.Axis.X, Direction.Axis.Z);
    public static final EnumProperty<Part> PART = EnumProperty.create("part", Part.class);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public BenchBlock(){
        super(false, BlockProperties.create().mapColor(MapColor.COLOR_BROWN).sound(SoundType.WOOD).destroyTime(1.5f).explosionResistance(6));
        this.registerDefaultState(this.defaultBlockState().setValue(AXIS, Direction.Axis.X).setValue(PART, Part.LOW_X_LOW_Z).setValue(WATERLOGGED, false));
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
        Direction.Axis benchAxis = state.getValue(BenchBlock.AXIS);
        BenchBlock.Part benchPart = state.getValue(BenchBlock.PART);
        double xOffset = benchAxis == Direction.Axis.Z ? 1 - 2 * benchPart.getXOffset() : 0;
        double zOffset = benchAxis == Direction.Axis.X ? 1 - 2 * benchPart.getZOffset() : 0;
        return new Vec3(pos.getX() + 0.5 - xOffset * 0.25, pos.getY() + 0.2, pos.getZ() + 0.5 - zOffset * 0.25);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context){
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getHorizontalDirection();
        if(!canBeReplaced(level, pos.relative(facing)) || !canBeReplaced(level, pos.relative(facing.getClockWise())) || !canBeReplaced(level, pos.relative(facing).relative(facing.getClockWise())))
            return null;

        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState().setValue(AXIS, facing.getAxis())
            .setValue(PART, facing.getAxis() == Direction.Axis.X ? facing.getAxisDirection() == Direction.AxisDirection.POSITIVE ? Part.LOW_X_LOW_Z : Part.HIGH_X_HIGH_Z : facing.getAxisDirection() == Direction.AxisDirection.POSITIVE ? Part.HIGH_X_LOW_Z : Part.LOW_X_HIGH_Z)
            .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    private static boolean canBeReplaced(Level level, BlockPos pos){
        return level.isEmptyBlock(pos) || level.getBlockState(pos).getBlock() == Blocks.WATER;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack){
        Part mainPart = state.getValue(PART);
        BlockPos lowPos = pos.offset(-mainPart.getXOffset(), 0, -mainPart.getZOffset());

        for(Part part : Part.values()){
            if(part != mainPart){
                BlockPos partPos = lowPos.offset(part.getXOffset(), 0, part.getZOffset());
                boolean waterlogged = level.getFluidState(partPos).getType() == Fluids.WATER;
                level.setBlock(partPos, state.setValue(PART, part).setValue(WATERLOGGED, waterlogged), 1 | 2);
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving){
        if(state.hasBlockEntity() && (!state.is(newState.getBlock()) || !newState.hasBlockEntity())){
            BlockEntity entity = level.getBlockEntity(pos);
            if(entity instanceof BenchBlockEntity)
                ((BenchBlockEntity)entity).dropItems();

            // Remove the other bench blocks
            Part mainPart = state.getValue(PART);
            BlockPos lowPos = pos.offset(-mainPart.getXOffset(), 0, -mainPart.getZOffset());

            for(Part part : Part.values()){
                if(part != mainPart){
                    BlockPos partPos = lowPos.offset(part.getXOffset(), 0, part.getZOffset());
                    BlockState partState = level.getBlockState(partPos);
                    if(partState.getBlock() == this)
                        level.setBlock(partPos, partState.getValue(WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState(), 1 | 2);
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context){
        Direction.Axis axis = state.getValue(AXIS);
        Part part = state.getValue(PART);
        return SHAPES[axis == Direction.Axis.X ? 0 : 1][part.ordinal()].getUnderlying();
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter worldIn, BlockPos pos){
        return Shapes.empty();
    }

    public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos){
        return 1F;
    }

    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos){
        return true;
    }

    @Override
    public BlockEntity createNewBlockEntity(BlockPos pos, BlockState state){
        return new BenchBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block,BlockState> builder){
        builder.add(AXIS, PART, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state){
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos){
        if(state.getValue(WATERLOGGED))
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    public enum Part implements StringRepresentable {
        LOW_X_LOW_Z(0, 0), LOW_X_HIGH_Z(0, 1), HIGH_X_LOW_Z(1, 0), HIGH_X_HIGH_Z(1, 1);

        private final String name = this.name().toLowerCase(Locale.ROOT);
        private final int xOffset, zOffset;

        Part(int xOffset, int zOffset){
            this.xOffset = xOffset;
            this.zOffset = zOffset;
        }

        @Override
        public String getSerializedName(){
            return this.name;
        }

        public int getXOffset(){
            return this.xOffset;
        }

        public int getZOffset(){
            return this.zOffset;
        }
    }
}
