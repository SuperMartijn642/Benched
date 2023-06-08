package com.supermartijn642.benched.seat;

import com.supermartijn642.benched.Benched;
import com.supermartijn642.benched.blocks.BenchBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

/**
 * Created 12/26/2020 by SuperMartijn642
 */
public class SeatEntity extends Entity {

    private double seatHeight;

    public SeatEntity(Level level){
        super(Benched.seat_entity, level);
    }

    public SeatEntity(Level level, BlockPos pos, double seatHeight){
        super(Benched.seat_entity, level);
        this.seatHeight = seatHeight;
        this.setPos(pos.getX() + 0.5, pos.getY() + seatHeight, pos.getZ() + 0.5);
    }

    @Override
    public void tick(){
        super.tick();

        if(!this.level().isClientSide && (this.getPassengers().isEmpty() || !(this.level().getBlockState(this.blockPosition()).getBlock() instanceof BenchBlock)))
            this.discard();
    }

    @Override
    protected void defineSynchedData(){
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound){
        if(compound.contains("seatHeight"))
            this.seatHeight = compound.getDouble("seatHeight");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound){
        compound.putDouble("seatHeight", this.seatHeight);
    }

    @Override
    public double getPassengersRidingOffset(){
        return -0.5 + this.seatHeight;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(){
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
