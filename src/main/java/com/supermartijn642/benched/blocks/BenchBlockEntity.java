package com.supermartijn642.benched.blocks;

import com.supermartijn642.benched.Benched;
import com.supermartijn642.benched.BenchedConfig;
import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.block.BaseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    protected CompoundTag writeData(){
        CompoundTag compound = new CompoundTag();
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
        ListTag items = new ListTag();
        this.items.forEach(item -> items.add(item.save(this.level.registryAccess())));
        compound.put("items", items);
        return compound;
    }

    @Override
    protected void readData(CompoundTag compound){
        this.others.clear();
        if(compound.contains("other1X"))
            this.others.add(new BlockPos(compound.getIntOr("other1X", 0), compound.getIntOr("other1Y", 0), compound.getIntOr("other1Z", 0)));
        if(compound.contains("other2X"))
            this.others.add(new BlockPos(compound.getIntOr("other2X", 0), compound.getIntOr("other2Y", 0), compound.getIntOr("other2Z", 0)));
        if(compound.contains("other3X"))
            this.others.add(new BlockPos(compound.getIntOr("other3X", 0), compound.getIntOr("other3Y", 0), compound.getIntOr("other3Z", 0)));
        this.shape = compound.getIntOr("shape", 0);
        this.items.clear();
        compound.getListOrEmpty("items").stream()
            .map(tag -> ItemStack.parse(CommonUtils.getRegistryAccess(), tag))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .forEach(this.items::add);
    }
}
