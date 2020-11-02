package com.supermartijn642.benched;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 11/1/2020 by SuperMartijn642
 */
public class BenchTile extends TileEntity {

    private final List<BlockPos> others = new ArrayList<>();
    public int shape = 0;

    public BenchTile(){
        super();
    }

    public void setOthers(List<BlockPos> others){
        for(BlockPos pos : others)
            if(!pos.equals(this.pos))
                this.others.add(pos);
    }

    public List<BlockPos> getOthers(){
        return this.others;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound){
        super.writeToNBT(compound);
        if(this.others.size() >= 3){
            compound.setInteger("other1X", this.others.get(0).getX());
            compound.setInteger("other1Y", this.others.get(0).getY());
            compound.setInteger("other1Z", this.others.get(0).getZ());
            compound.setInteger("other2X", this.others.get(1).getX());
            compound.setInteger("other2Y", this.others.get(1).getY());
            compound.setInteger("other2Z", this.others.get(1).getZ());
            compound.setInteger("other3X", this.others.get(2).getX());
            compound.setInteger("other3Y", this.others.get(2).getY());
            compound.setInteger("other3Z", this.others.get(2).getZ());
        }
        compound.setInteger("shape", this.shape);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound){
        super.readFromNBT(compound);
        this.others.clear();
        if(compound.hasKey("other1X"))
            this.others.add(new BlockPos(compound.getInteger("other1X"), compound.getInteger("other1Y"), compound.getInteger("other1Z")));
        if(compound.hasKey("other2X"))
            this.others.add(new BlockPos(compound.getInteger("other2X"), compound.getInteger("other2Y"), compound.getInteger("other2Z")));
        if(compound.hasKey("other3X"))
            this.others.add(new BlockPos(compound.getInteger("other3X"), compound.getInteger("other3Y"), compound.getInteger("other3Z")));
        this.shape = compound.getInteger("shape");
    }

    @Override
    public NBTTagCompound getUpdateTag(){
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag){
        this.readFromNBT(tag);
    }
}
