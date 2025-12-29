package com.supermartijn642.benched.seat;

import com.supermartijn642.benched.Benched;
import com.supermartijn642.benched.blocks.BenchBlock;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

/**
 * Created 12/26/2020 by SuperMartijn642
 */
public class SeatEntity extends Entity {

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

        if(!this.level().isClientSide() && (this.getPassengers().isEmpty() || !(this.level().getBlockState(this.blockPosition()).getBlock() instanceof BenchBlock)))
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
    protected void readAdditionalSaveData(ValueInput input){
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output){
    }

    @Override
    protected void positionRider(Entity entity, MoveFunction moveFunction){
        Vec3 position = this.position();
        moveFunction.accept(entity, position.x, position.y - 0.3, position.z);
    }
}
