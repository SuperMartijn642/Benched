package com.supermartijn642.benched.generators;

import com.supermartijn642.benched.BenchType;
import com.supermartijn642.benched.blocks.BenchBlock;
import com.supermartijn642.core.generator.BlockStateGenerator;
import com.supermartijn642.core.generator.ResourceCache;

/**
 * Created 28/12/2022 by SuperMartijn642
 */
public class BenchedBlockStateGenerator extends BlockStateGenerator {

    public BenchedBlockStateGenerator(ResourceCache cache){
        super("benched", cache);
    }

    @Override
    public void generate(){
        for(BenchType type : BenchType.values()){
            this.blockState(type.getBlock())
                .variantsForProperty(
                    BenchBlock.ROTATION,
                    (state, variant) -> {
                        int rotation = ((int)state.get(BenchBlock.ROTATION).toYRot() + 270) % 360;
                        variant.model(type.getIdentifier() + "_bench", 0, rotation);
                    }
                );
        }
    }
}
