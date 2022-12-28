package com.supermartijn642.benched.seat;

import com.supermartijn642.core.block.BaseBlock;
import com.supermartijn642.core.block.BlockProperties;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

/**
 * Created 12/26/2020 by SuperMartijn642
 */
public abstract class SeatBlock extends BaseBlock {

    public SeatBlock(boolean saveTileData, BlockProperties properties){
        super(saveTileData, properties);
    }

    @Override
    protected InteractionFeedback interact(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, Direction hitSide, Vector3d hitLocation){
        if(!level.isClientSide)
            SeatHelper.sitPlayerDown(level, pos, player);
        return InteractionFeedback.SUCCESS;
    }

    protected abstract double getSeatHeight();
}
