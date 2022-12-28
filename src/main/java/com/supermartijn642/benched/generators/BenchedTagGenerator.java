package com.supermartijn642.benched.generators;

import com.supermartijn642.benched.BenchType;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.generator.TagGenerator;

/**
 * Created 28/12/2022 by SuperMartijn642
 */
public class BenchedTagGenerator extends TagGenerator {

    public BenchedTagGenerator(ResourceCache cache){
        super("benched", cache);
    }

    @Override
    public void generate(){
        for(BenchType type : BenchType.values())
            this.blockMineableWithPickaxe().add(type.getBlock());
    }
}
