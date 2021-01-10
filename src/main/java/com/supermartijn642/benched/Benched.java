package com.supermartijn642.benched;

import com.supermartijn642.benched.blocks.BenchBlock;
import com.supermartijn642.benched.blocks.BenchItemBlock;
import com.supermartijn642.benched.blocks.BenchTile;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod(modid = Benched.MODID, name = Benched.NAME, version = Benched.VERSION)
public class Benched {

    public static final String MODID = "benched";
    public static final String NAME = "Benched";
    public static final String VERSION = "1.1.0";

    @GameRegistry.ObjectHolder("benched:acacia_bench")
    public static Block acacia_bench;
    @GameRegistry.ObjectHolder("benched:birch_bench")
    public static Block birch_bench;
    @GameRegistry.ObjectHolder("benched:dark_oak_bench")
    public static Block dark_oak_bench;
    @GameRegistry.ObjectHolder("benched:jungle_bench")
    public static Block jungle_bench;
    @GameRegistry.ObjectHolder("benched:bench")
    public static Block oak_bench;
    @GameRegistry.ObjectHolder("benched:spruce_bench")
    public static Block spruce_bench;

    public Benched(){
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e){
        if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            ClientProxy.init(e);
    }

    @Mod.EventBusSubscriber
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlockRegistry(final RegistryEvent.Register<Block> e){
            e.getRegistry().register(new BenchBlock("acacia_bench"));
            e.getRegistry().register(new BenchBlock("birch_bench"));
            e.getRegistry().register(new BenchBlock("dark_oak_bench"));
            e.getRegistry().register(new BenchBlock("jungle_bench"));
            e.getRegistry().register(new BenchBlock("bench")); // oak_bench
            e.getRegistry().register(new BenchBlock("spruce_bench"));

            GameRegistry.registerTileEntity(BenchTile.class, new ResourceLocation(MODID, "bench_tile"));
        }

        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> e){
            e.getRegistry().register(new BenchItemBlock(acacia_bench));
            e.getRegistry().register(new BenchItemBlock(birch_bench));
            e.getRegistry().register(new BenchItemBlock(dark_oak_bench));
            e.getRegistry().register(new BenchItemBlock(jungle_bench));
            e.getRegistry().register(new BenchItemBlock(oak_bench));
            e.getRegistry().register(new BenchItemBlock(spruce_bench));
        }
    }

}
