package com.supermartijn642.benched;

import com.supermartijn642.benched.blocks.BenchBlock;
import com.supermartijn642.core.block.BaseBlock;
import com.supermartijn642.core.item.BaseBlockItem;
import com.supermartijn642.core.item.CreativeItemGroup;
import com.supermartijn642.core.item.ItemProperties;
import com.supermartijn642.core.registry.RegistrationHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.function.Supplier;

/**
 * Created 28/12/2022 by SuperMartijn642
 */
public enum BenchType {

    ACACIA("Acacia", () -> Items.ACACIA_PLANKS),
    BIRCH("Birch", () -> Items.BIRCH_PLANKS),
    DARK_OAK("Dark Oak", () -> Items.DARK_OAK_PLANKS),
    JUNGLE("Jungle", () -> Items.JUNGLE_PLANKS),
    OAK("Oak", () -> Items.OAK_PLANKS),
    SPRUCE("Spruce", () -> Items.SPRUCE_PLANKS);

    private final String translation;
    private final Supplier<Item> craftingIngredient;
    private BenchBlock block;
    private BaseBlockItem item;

    BenchType(String translation, Supplier<Item> craftingIngredient){
        this.translation = translation;
        this.craftingIngredient = craftingIngredient;
    }

    public String getIdentifier(){
        return this.name().toLowerCase();
    }

    public String getTranslation(){
        return this.translation;
    }

    public Item getCraftingIngredient(){
        return this.craftingIngredient.get();
    }

    public BaseBlock getBlock(){
        return this.block;
    }

    public BaseBlockItem getItem(){
        return this.item;
    }

    public void registerBlock(RegistrationHandler.Helper<Block> helper){
        this.block = helper.register(this == OAK ? "bench" : this.getIdentifier() + "_bench", new BenchBlock());
    }

    public void registerItem(RegistrationHandler.Helper<Item> helper){
        this.item = helper.register(this == OAK ? "bench" : this.getIdentifier() + "_bench", new BaseBlockItem(this.block, ItemProperties.create().group(CreativeItemGroup.getDecoration())));
    }
}
