package com.supermartijn642.benched.seat;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;

import java.util.List;

/**
 * Created 12/26/2020 by SuperMartijn642
 */
public class SeatHelper {

    public static void sitPlayerDown(Level world, BlockPos pos, Player player){
        Block block = world.getBlockState(pos).getBlock();
        if(block instanceof SeatBlock){
            List<SeatEntity> entities = world.getEntitiesOfClass(SeatEntity.class, new AABB(pos).deflate(0.1));

            SeatEntity entity;
            if(entities.isEmpty()){
                double seatHeight = ((SeatBlock)block).getSeatHeight();
                entity = new SeatEntity(world, pos, seatHeight);
                world.addFreshEntity(entity);
            }
            else
                entity = entities.get(0);

            if(entity.getPassengers().isEmpty())
                player.startRiding(entity);
        }
    }

}
