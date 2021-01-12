package com.supermartijn642.benched.seat;

import com.supermartijn642.benched.blocks.BenchBlock;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created 12/26/2020 by SuperMartijn642
 */
public class SeatEntity extends Entity {

    private double seatHeight;

    public SeatEntity(World worldIn){
        super(worldIn);
        this.width = this.height = 0;
        this.noClip = true;
    }

    public SeatEntity(World world, BlockPos pos, double seatHeight){
        super(world);
        this.seatHeight = seatHeight;
        this.setPosition(pos.getX() + 0.5, pos.getY() + seatHeight, pos.getZ() + 0.5);
    }

    @Override
    protected void entityInit(){
    }

    @Override
    public void onEntityUpdate(){
        if(!this.world.isRemote && (this.getPassengers().isEmpty() || !(this.world.getBlockState(this.getPosition().down()).getBlock() instanceof BenchBlock)))
            this.world.removeEntity(this);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound){
        super.writeToNBT(compound);
        compound.setDouble("seatHeight", this.seatHeight);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound){
        super.readFromNBT(compound);
        if(compound.hasKey("seatHeight"))
            this.seatHeight = compound.getDouble("seatHeight");
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound){
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound){
    }

    @Override
    public double getMountedYOffset(){
        return -0.5 + this.seatHeight;
    }
}
