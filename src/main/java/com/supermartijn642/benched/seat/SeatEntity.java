package com.supermartijn642.benched.seat;

import com.supermartijn642.benched.Benched;
import com.supermartijn642.benched.blocks.BenchBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Created 12/26/2020 by SuperMartijn642
 */
public class SeatEntity extends Entity {

    private double seatHeight;

    public SeatEntity(Level level){
        super(Benched.seat_entity, level);
    }

    public SeatEntity(Level level, Vec3 seatPosition){
        super(Benched.seat_entity, level);
        this.setPos(seatPosition);
    }

    @Override
    public void tick(){
        super.tick();

        if(!this.level().isClientSide && (this.getPassengers().isEmpty() || !(this.level().getBlockState(this.blockPosition()).getBlock() instanceof BenchBlock)))
            this.discard();
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float f){
        return false;
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity){
        return new Vec3(this.getX(), Math.ceil(this.getY()), this.getZ());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder){
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
    protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float p_296362_){
        return new Vec3(0, -0.3 + this.seatHeight, 0);
    }
}
