package com.supermartijn642.benched.generators;

import com.supermartijn642.benched.BenchType;
import com.supermartijn642.core.generator.ModelGenerator;
import com.supermartijn642.core.generator.ResourceCache;

/**
 * Created 28/12/2022 by SuperMartijn642
 */
public class BenchedModelGenerator extends ModelGenerator {

    public BenchedModelGenerator(ResourceCache cache){
        super("benched", cache);
    }

    @Override
    public void generate(){
        for(BenchType type : BenchType.values()){
            // Block model
            this.model(type.getIdentifier() + "_bench")
                .parent("bench_transformed")
                .texture("main", type.getIdentifier() + "_bench");
            // Item model
            this.model("item/" + (type == BenchType.OAK ? "bench" : type.getIdentifier() + "_bench"))
                .parent(type.getIdentifier() + "_bench");
        }
    }
}
