package com.supermartijn642.benched.generators;

import com.supermartijn642.benched.BenchType;
import com.supermartijn642.core.generator.RecipeGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import net.neoforged.neoforge.common.Tags;

/**
 * Created 28/12/2022 by SuperMartijn642
 */
public class BenchedRecipeGenerator extends RecipeGenerator {

    public BenchedRecipeGenerator(ResourceCache cache){
        super("benched", cache);
    }

    @Override
    public void generate(){
        for(BenchType type : BenchType.values()){
            this.shaped(type.getBlock())
                .pattern("AAA")
                .pattern("ABA")
                .pattern("B B")
                .input('A', type.getCraftingIngredient())
                .input('B', Tags.Items.RODS_WOODEN)
                .unlockedBy(type.getCraftingIngredient());
        }
    }
}
