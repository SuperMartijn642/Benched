package com.supermartijn642.benched;

import com.supermartijn642.configlib.ModConfigBuilder;

import java.util.function.Supplier;

/**
 * Created 3/25/2021 by SuperMartijn642
 */
public class BenchedConfig {

    public static final Supplier<Integer> maxStackedItems;

    static{
        ModConfigBuilder builder = new ModConfigBuilder("benched");

        maxStackedItems = builder.comment("How many items can be stacked on top of a bench? 0 means none.").define("maxStackedItems", 4, 0, 20);

        builder.build();
    }

    public static void init(){
        // just to cause this class to load
    }

}
