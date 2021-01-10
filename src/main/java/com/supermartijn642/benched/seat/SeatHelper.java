package com.supermartijn642.benched.seat;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created 12/26/2020 by SuperMartijn642
 */
public class SeatHelper {

    public static void sitPlayerDown(World world, BlockPos pos, EntityPlayer player){
        Block block = world.getBlockState(pos).getBlock();
        if(block instanceof SeatBlock){
            List<SeatEntity> entities = world.getEntitiesWithinAABB(SeatEntity.class, new AxisAlignedBB(pos).shrink(0.1));

            SeatEntity entity;
            if(entities.isEmpty()){
                double seatHeight = ((SeatBlock)block).getSeatHeight();
                entity = new SeatEntity(world, pos, seatHeight);
                world.spawnEntity(entity);
            }
            else
                entity = entities.get(0);

            if(entity.getPassengers().isEmpty())
                player.startRiding(entity);
        }
    }

}
