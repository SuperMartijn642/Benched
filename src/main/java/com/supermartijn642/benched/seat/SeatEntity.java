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

    public SeatEntity(World worldIn){
        super(Benched.seat_entity, worldIn);
    }

    public SeatEntity(World world, BlockPos pos, double seatHeight){
        super(Benched.seat_entity, world);
        this.seatHeight = seatHeight;
        this.setPosition(pos.getX() + 0.5, pos.getY() + seatHeight, pos.getZ() + 0.5);
    }

    @Override
    public void tick(){
        super.tick();

        if(!this.world.isRemote && (this.getPassengers().isEmpty() || !(this.world.getBlockState(this.getPosition()).getBlock() instanceof BenchBlock)))
            this.remove();
    }

    @Override
    protected void registerData(){
    }

    @Override
    protected void readAdditional(CompoundNBT compound){
        if(compound.contains("seatHeight"))
            this.seatHeight = compound.getDouble("seatHeight");
    }

    @Override
    protected void writeAdditional(CompoundNBT compound){
        compound.putDouble("seatHeight", this.seatHeight);
    }

    @Override
    public double getMountedYOffset(){
        return -0.5 + this.seatHeight;
    }

    @Override
    public IPacket<?> createSpawnPacket(){
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
