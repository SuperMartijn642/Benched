package com.supermartijn642.benched.blocks;

import com.supermartijn642.benched.Benched;
import com.supermartijn642.benched.BenchedConfig;
import com.supermartijn642.core.block.BaseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Containers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 11/1/2020 by SuperMartijn642
 */
public class BenchBlockEntity extends BaseBlockEntity {

    private final List<BlockPos> others = new ArrayList<>();
    public int shape = 0;
    public NonNullList<ItemStack> items = NonNullList.create();

    public BenchBlockEntity(BlockPos pos, BlockState state){
        super(Benched.bench_tile, pos, state);
    }

    public void setOthers(List<BlockPos> others){
        for(BlockPos pos : others)
            if(!pos.equals(this.worldPosition))
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
        Containers.dropContents(this.level, this.worldPosition, this.items);
        this.items.clear();
    }

    @Override
    protected void writeData(ValueOutput output){
        if(this.others.size() >= 3){
            output.putInt("other1X", this.others.get(0).getX());
            output.putInt("other1Y", this.others.get(0).getY());
            output.putInt("other1Z", this.others.get(0).getZ());
            output.putInt("other2X", this.others.get(1).getX());
            output.putInt("other2Y", this.others.get(1).getY());
            output.putInt("other2Z", this.others.get(1).getZ());
            output.putInt("other3X", this.others.get(2).getX());
            output.putInt("other3Y", this.others.get(2).getY());
            output.putInt("other3Z", this.others.get(2).getZ());
        }
        output.putInt("shape", this.shape);
        this.items.forEach(output.list("items", ItemStack.CODEC)::add);
    }

    @Override
    protected void readData(ValueInput input){
        this.others.clear();
        this.others.add(new BlockPos(input.getIntOr("other1X", 0), input.getIntOr("other1Y", 0), input.getIntOr("other1Z", 0)));
        this.others.add(new BlockPos(input.getIntOr("other2X", 0), input.getIntOr("other2Y", 0), input.getIntOr("other2Z", 0)));
        this.others.add(new BlockPos(input.getIntOr("other3X", 0), input.getIntOr("other3Y", 0), input.getIntOr("other3Z", 0)));
        this.shape = input.getIntOr("shape", 0);
        this.items.clear();
        input.list("items", ItemStack.CODEC).ifPresent(items -> items.stream().forEach(this.items::add));
    }
}
