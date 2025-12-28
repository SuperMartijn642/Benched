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
            // Block models
            this.model(type.getIdentifier() + "_bench")
                .parent("bench")
                .texture("logs", type.getLogsTexture())
                .texture("planks", type.getPlanksTexture())
                .texture("stripped", type.getStrippedTexture());
            this.model(type.getIdentifier() + "_bench_mirrored")
                .parent("bench_mirrored")
                .texture("logs", type.getLogsTexture())
                .texture("planks", type.getPlanksTexture())
                .texture("stripped", type.getStrippedTexture());
            // Item model
            this.model("item/" + (type == BenchType.OAK ? "bench" : type.getIdentifier() + "_bench"))
                .parent("bench_item")
                .texture("logs", type.getLogsTexture())
                .texture("planks", type.getPlanksTexture())
                .texture("stripped", type.getStrippedTexture());
        }
    }
}
