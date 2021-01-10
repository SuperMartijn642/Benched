package com.supermartijn642.benched.blocks;

import com.supermartijn642.benched.Benched;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 11/1/2020 by SuperMartijn642
 */
public class BenchTile extends BenchedBaseTile {

    private final List<BlockPos> others = new ArrayList<>();
    public int shape = 0;

    public BenchTile(){
        super(Benched.bench_tile);
    }

    public void setOthers(List<BlockPos> others){
        for(BlockPos pos : others)
            if(!pos.equals(this.pos))
                this.others.add(pos);
        this.dataChanged();
    }

    public List<BlockPos> getOthers(){
        return this.others;
    }

    @Override
    protected CompoundNBT writeData(){
        CompoundNBT compound = new CompoundNBT();
        if(this.others.size() >= 3){
            compound.putInt("other1X", this.others.get(0).getX());
            compound.putInt("other1Y", this.others.get(0).getY());
            compound.putInt("other1Z", this.others.get(0).getZ());
            compound.putInt("other2X", this.others.get(1).getX());
            compound.putInt("other2Y", this.others.get(1).getY());
            compound.putInt("other2Z", this.others.get(1).getZ());
            compound.putInt("other3X", this.others.get(2).getX());
            compound.putInt("other3Y", this.others.get(2).getY());
            compound.putInt("other3Z", this.others.get(2).getZ());
        }
        compound.putInt("shape", this.shape);
        return compound;
    }

    @Override
    protected void readData(CompoundNBT compound){
        this.others.clear();
        if(compound.contains("other1X"))
            this.others.add(new BlockPos(compound.getInt("other1X"), compound.getInt("other1Y"), compound.getInt("other1Z")));
        if(compound.contains("other2X"))
            this.others.add(new BlockPos(compound.getInt("other2X"), compound.getInt("other2Y"), compound.getInt("other2Z")));
        if(compound.contains("other3X"))
            this.others.add(new BlockPos(compound.getInt("other3X"), compound.getInt("other3Y"), compound.getInt("other3Z")));
        this.shape = compound.getInt("shape");
    }
}
