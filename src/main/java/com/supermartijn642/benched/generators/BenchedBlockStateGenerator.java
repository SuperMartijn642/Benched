package com.supermartijn642.benched.generators;

import com.supermartijn642.benched.BenchType;
import com.supermartijn642.benched.blocks.BenchBlock;
import com.supermartijn642.core.generator.BlockStateGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import net.minecraft.core.Direction;

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
            String bench = type.getIdentifier() + "_bench";
            String benchMirrored = type.getIdentifier() + "_bench_mirrored";
            this.blockState(type.getBlock())
                .variantsForAllExcept((state, variant) -> {
                    Direction.Axis axis = state.get(BenchBlock.AXIS);
                    BenchBlock.Part part = state.get(BenchBlock.PART);
                    if(axis == Direction.Axis.X){
                        switch(part){
                            case LOW_X_LOW_Z -> variant.model(benchMirrored, 0, 90);
                            case LOW_X_HIGH_Z -> variant.model(bench, 0, 270);
                            case HIGH_X_LOW_Z -> variant.model(bench, 0, 90);
                            case HIGH_X_HIGH_Z -> variant.model(benchMirrored, 0, 270);
                        }
                    }
                    if(axis == Direction.Axis.Z){
                        switch(part){
                            case LOW_X_LOW_Z -> variant.model(bench);
                            case LOW_X_HIGH_Z -> variant.model(benchMirrored);
                            case HIGH_X_LOW_Z -> variant.model(benchMirrored, 0, 180);
                            case HIGH_X_HIGH_Z -> variant.model(bench, 0, 180);
                        }
                    }
                }, BenchBlock.WATERLOGGED);
        }
    }
}
