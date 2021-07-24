package com.supermartijn642.benched.seat;

import com.supermartijn642.core.block.BaseBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Created 12/26/2020 by SuperMartijn642
 */
public abstract class SeatBlock extends BaseBlock {

    public SeatBlock(Properties properties, String registryName, boolean saveTileData){
        super(registryName, saveTileData, properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit){
        if(!worldIn.isClientSide)
            SeatHelper.sitPlayerDown(worldIn, pos, player);
        return InteractionResult.sidedSuccess(worldIn.isClientSide);
    }

    protected abstract double getSeatHeight();
}
