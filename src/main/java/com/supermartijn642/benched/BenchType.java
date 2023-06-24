package com.supermartijn642.benched;

import com.supermartijn642.benched.blocks.BenchBlock;
import com.supermartijn642.core.block.BaseBlock;
import com.supermartijn642.core.item.BaseBlockItem;
import com.supermartijn642.core.item.CreativeItemGroup;
import com.supermartijn642.core.item.ItemProperties;
import com.supermartijn642.core.registry.RegistrationHandler;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

/**
 * Created 28/12/2022 by SuperMartijn642
 */
public enum BenchType {

    ACACIA("Acacia", () -> Items.ACACIA_PLANKS, "minecraft:block/acacia_log", "minecraft:block/acacia_planks", "minecraft:block/stripped_acacia_log"),
    BIRCH("Birch", () -> Items.BIRCH_PLANKS, "minecraft:block/birch_log", "minecraft:block/birch_planks", "minecraft:block/stripped_birch_log"),
    CRIMSON("Crimson", () -> Items.CRIMSON_PLANKS, "minecraft:block/crimson_stem", "minecraft:block/crimson_planks", "minecraft:block/stripped_crimson_stem"),
    DARK_OAK("Dark Oak", () -> Items.DARK_OAK_PLANKS, "minecraft:block/dark_oak_log", "minecraft:block/dark_oak_planks", "minecraft:block/stripped_dark_oak_log"),
    JUNGLE("Jungle", () -> Items.JUNGLE_PLANKS, "minecraft:block/jungle_log", "minecraft:block/jungle_planks", "minecraft:block/stripped_jungle_log"),
    MANGROVE("Mangrove", () -> Items.JUNGLE_PLANKS, "minecraft:block/mangrove_log", "minecraft:block/mangrove_planks", "minecraft:block/stripped_mangrove_log"),
    OAK("Oak", () -> Items.OAK_PLANKS, "minecraft:block/oak_log", "minecraft:block/oak_planks", "minecraft:block/stripped_oak_log"),
    SPRUCE("Spruce", () -> Items.SPRUCE_PLANKS, "minecraft:block/spruce_log", "minecraft:block/spruce_planks", "minecraft:block/stripped_spruce_log"),
    WARPED("Warped", () -> Items.WARPED_PLANKS, "minecraft:block/warped_stem", "minecraft:block/warped_planks", "minecraft:block/stripped_warped_stem");

    private final String translation;
    private final Supplier<Item> craftingIngredient;
    private final String logsTexture, planksTexture, strippedTexture;
    private BenchBlock block;
    private BaseBlockItem item;

    BenchType(String translation, Supplier<Item> craftingIngredient, String logsTexture, String planksTexture, String strippedTexture){
        this.translation = translation;
        this.craftingIngredient = craftingIngredient;
        this.logsTexture = logsTexture;
        this.planksTexture = planksTexture;
        this.strippedTexture = strippedTexture;
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

    public String getLogsTexture(){
        return this.logsTexture;
    }

    public String getPlanksTexture(){
        return this.planksTexture;
    }

    public String getStrippedTexture(){
        return this.strippedTexture;
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
