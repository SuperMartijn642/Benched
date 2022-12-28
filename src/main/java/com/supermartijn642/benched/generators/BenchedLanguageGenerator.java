package com.supermartijn642.benched.generators;

import com.supermartijn642.benched.BenchType;
import com.supermartijn642.core.generator.LanguageGenerator;
import com.supermartijn642.core.generator.ResourceCache;

/**
 * Created 28/12/2022 by SuperMartijn642
 */
public class BenchedLanguageGenerator extends LanguageGenerator {

    public BenchedLanguageGenerator(ResourceCache cache){
        super("benched", cache, "en_us");
    }

    @Override
    public void generate(){
        for(BenchType type : BenchType.values()){
            this.block(type.getBlock(), type.getTranslation() + "  Picnic Bench");
        }
    }
}
