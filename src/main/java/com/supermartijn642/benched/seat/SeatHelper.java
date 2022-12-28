package com.supermartijn642.benched.seat;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created 12/26/2020 by SuperMartijn642
 */
public class SeatHelper {

    public static void sitPlayerDown(World level, BlockPos pos, PlayerEntity player){
        Block block = level.getBlockState(pos).getBlock();
        if(block instanceof SeatBlock){
            List<SeatEntity> entities = level.getEntitiesOfClass(SeatEntity.class, new AxisAlignedBB(pos).deflate(0.1));

            SeatEntity entity;
            if(entities.isEmpty()){
                double seatHeight = ((SeatBlock)block).getSeatHeight();
                entity = new SeatEntity(level, pos, seatHeight);
                level.addFreshEntity(entity);
            }else
                entity = entities.get(0);

            if(entity.getPassengers().isEmpty())
                player.startRiding(entity);
        }
    }
}
