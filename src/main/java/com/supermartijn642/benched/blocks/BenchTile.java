package com.supermartijn642.benched.blocks;

import com.supermartijn642.benched.Benched;
import com.supermartijn642.benched.BenchedConfig;
import com.supermartijn642.core.block.BaseTileEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 11/1/2020 by SuperMartijn642
 */
public class BenchTile extends BaseTileEntity {

    private final List<BlockPos> others = new ArrayList<>();
    public int shape = 0;
    public NonNullList<ItemStack> items = NonNullList.create();

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

    public boolean addItem(ItemStack stack){
        if(stack.isEmpty() || stack.getItem() instanceof BlockItem || this.items.size() >= BenchedConfig.maxStackedItems.get())
            return false;

        ItemStack copy = stack.copy();
        copy.setCount(1);
        this.items.add(copy);

        this.dataChanged();

        stack.shrink(1);

        return true;
    }

    public ItemStack removeItem(){
        if(this.items.size() == 0)
            return ItemStack.EMPTY;

        this.dataChanged();

        return this.items.remove(this.items.size() - 1);
    }

    public void dropItems(){
        InventoryHelper.dropItems(this.world, this.pos, this.items);
        this.items.clear();
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
        ListNBT items = new ListNBT();
        this.items.forEach(item -> items.add(item.write(new CompoundNBT())));
        compound.put("items", items);
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
        this.items.clear();
        ListNBT items = compound.contains("items") ? (ListNBT)compound.get("items") : new ListNBT();
        items.forEach(tag -> this.items.add(ItemStack.read((CompoundNBT)tag)));
    }
}
