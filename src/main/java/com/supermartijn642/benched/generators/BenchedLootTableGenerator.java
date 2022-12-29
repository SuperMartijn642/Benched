package com.supermartijn642.benched.generators;

import com.supermartijn642.benched.BenchType;
import com.supermartijn642.core.generator.LootTableGenerator;
import com.supermartijn642.core.generator.ResourceCache;

/**
 * Created 28/12/2022 by SuperMartijn642
 */
public class BenchedLootTableGenerator extends LootTableGenerator {

    public BenchedLootTableGenerator(ResourceCache cache){
        super("benched", cache);
    }

    @Override
    public void generate(){
        for(BenchType type : BenchType.values())
            this.dropSelf(type.getBlock());
    }
}
