package com.supermartijn642.benched;

import com.supermartijn642.benched.blocks.BenchBlock;
import com.supermartijn642.benched.blocks.BenchTile;
import com.supermartijn642.benched.seat.SeatEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod("benched")
public class Benched {

    @ObjectHolder("benched:acacia_bench")
    public static Block acacia_bench;
    @ObjectHolder("benched:birch_bench")
    public static Block birch_bench;
    @ObjectHolder("benched:dark_oak_bench")
    public static Block dark_oak_bench;
    @ObjectHolder("benched:jungle_bench")
    public static Block jungle_bench;
    @ObjectHolder("benched:bench")
    public static Block oak_bench;
    @ObjectHolder("benched:spruce_bench")
    public static Block spruce_bench;
    @ObjectHolder("benched:crimson_bench")
    public static Block crimson_bench;
    @ObjectHolder("benched:warped_bench")
    public static Block warped_bench;

    @ObjectHolder("benched:bench_tile")
    public static TileEntityType<BenchTile> bench_tile;

    @ObjectHolder("benched:seat_entity")
    public static EntityType<SeatEntity> seat_entity;

    public Benched(){
        BenchedConfig.init();
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlockRegistry(final RegistryEvent.Register<Block> e){
            e.getRegistry().register(new BenchBlock("acacia_bench"));
            e.getRegistry().register(new BenchBlock("birch_bench"));
            e.getRegistry().register(new BenchBlock("dark_oak_bench"));
            e.getRegistry().register(new BenchBlock("jungle_bench"));
            e.getRegistry().register(new BenchBlock("bench")); // oak_bench
            e.getRegistry().register(new BenchBlock("spruce_bench"));
            e.getRegistry().register(new BenchBlock("crimson_bench"));
            e.getRegistry().register(new BenchBlock("warped_bench"));
        }

        @SubscribeEvent
        public static void onTileRegistry(final RegistryEvent.Register<TileEntityType<?>> e){
            e.getRegistry().register(TileEntityType.Builder.create(BenchTile::new, acacia_bench, birch_bench, dark_oak_bench, jungle_bench, oak_bench, spruce_bench, crimson_bench, warped_bench).build(null).setRegistryName("bench_tile"));
        }

        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> e){
            e.getRegistry().register(new BlockItem(acacia_bench, new Item.Properties().group(ItemGroup.SEARCH)).setRegistryName(acacia_bench.getRegistryName()));
            e.getRegistry().register(new BlockItem(birch_bench, new Item.Properties().group(ItemGroup.SEARCH)).setRegistryName(birch_bench.getRegistryName()));
            e.getRegistry().register(new BlockItem(dark_oak_bench, new Item.Properties().group(ItemGroup.SEARCH)).setRegistryName(dark_oak_bench.getRegistryName()));
            e.getRegistry().register(new BlockItem(jungle_bench, new Item.Properties().group(ItemGroup.SEARCH)).setRegistryName(jungle_bench.getRegistryName()));
            e.getRegistry().register(new BlockItem(oak_bench, new Item.Properties().group(ItemGroup.SEARCH)).setRegistryName(oak_bench.getRegistryName()));
            e.getRegistry().register(new BlockItem(spruce_bench, new Item.Properties().group(ItemGroup.SEARCH)).setRegistryName(spruce_bench.getRegistryName()));
            e.getRegistry().register(new BlockItem(crimson_bench, new Item.Properties().group(ItemGroup.SEARCH)).setRegistryName(crimson_bench.getRegistryName()));
            e.getRegistry().register(new BlockItem(warped_bench, new Item.Properties().group(ItemGroup.SEARCH)).setRegistryName(warped_bench.getRegistryName()));
        }

        @SubscribeEvent
        public static void onEntityRegistry(RegistryEvent.Register<EntityType<?>> e){
            e.getRegistry().register(EntityType.Builder.create((o, world) -> new SeatEntity(world), EntityClassification.MISC).build("").setRegistryName("seat_entity"));
        }
    }

}
