package com.supermartijn642.benched.seat;

import com.supermartijn642.benched.Benched;
import com.supermartijn642.benched.blocks.BenchBlock;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * Created 12/26/2020 by SuperMartijn642
 */
public class SeatEntity extends Entity {

    private double seatHeight;

    public SeatEntity(World level){
        super(Benched.seat_entity, level);
    }

    public SeatEntity(World level, BlockPos pos, double seatHeight){
        super(Benched.seat_entity, level);
        this.seatHeight = seatHeight;
        this.setPos(pos.getX() + 0.5, pos.getY() + seatHeight, pos.getZ() + 0.5);
    }

    @Override
    public void tick(){
        super.tick();

        if(!this.level.isClientSide && (this.getPassengers().isEmpty() || !(this.level.getBlockState(this.blockPosition()).getBlock() instanceof BenchBlock)))
            this.remove();
    }

    @Override
    protected void defineSynchedData(){
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound){
        if(compound.contains("seatHeight"))
            this.seatHeight = compound.getDouble("seatHeight");
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound){
        compound.putDouble("seatHeight", this.seatHeight);
    }

    @Override
    public double getPassengersRidingOffset(){
        return -0.5 + this.seatHeight;
    }

    @Override
    public IPacket<?> getAddEntityPacket(){
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
