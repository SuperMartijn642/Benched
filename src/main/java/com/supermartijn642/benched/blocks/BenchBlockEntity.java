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

/**
 * Created 11/1/2020 by SuperMartijn642
 */
public class BenchBlockEntity extends BaseBlockEntity {

    public NonNullList<ItemStack> items = NonNullList.create();

    public BenchBlockEntity(BlockPos pos, BlockState state){
        super(Benched.bench_tile, pos, state);
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

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state){
        super.preRemoveSideEffects(pos, state);
        Containers.dropContents(this.level, this.worldPosition, this.items);
        this.items.clear();
    }

    @Override
    protected void writeData(ValueOutput output){
        this.items.forEach(output.list("items", ItemStack.CODEC)::add);
    }

    @Override
    protected void readData(ValueInput input){
        this.items.clear();
        input.list("items", ItemStack.CODEC).ifPresent(items -> items.stream().forEach(this.items::add));
    }
}
