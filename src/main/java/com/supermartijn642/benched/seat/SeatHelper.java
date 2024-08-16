package com.supermartijn642.benched.seat;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Created 12/26/2020 by SuperMartijn642
 */
public class SeatHelper {

    public static void sitPlayerDown(Level level, BlockPos pos, Player player){
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        if(block instanceof SeatBlock){
            List<SeatEntity> entities = level.getEntitiesOfClass(SeatEntity.class, new AABB(pos).deflate(0.1));

            SeatEntity entity;
            if(entities.isEmpty()){
                Vec3 seatPosition = ((SeatBlock)block).getSeatPosition(state, pos);
                entity = new SeatEntity(level, seatPosition);
                level.addFreshEntity(entity);
            }
            else
                entity = entities.get(0);

            if(entity.getPassengers().isEmpty())
                player.startRiding(entity);
        }
    }
}
