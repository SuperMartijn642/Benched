package com.supermartijn642.benched.seat;

import com.supermartijn642.core.block.BaseBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

/**
 * Created 12/26/2020 by SuperMartijn642
 */
public abstract class SeatBlock extends BaseBlock {

    public SeatBlock(Properties properties, String registryName, boolean saveTileData){
        super(registryName, saveTileData, properties);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit){
        if(!worldIn.isClientSide)
            SeatHelper.sitPlayerDown(worldIn, pos, player);
        return ActionResultType.sidedSuccess(worldIn.isClientSide);
    }

    protected abstract double getSeatHeight();
}
