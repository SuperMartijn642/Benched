package com.supermartijn642.benched;

import com.supermartijn642.benched.blocks.BenchBlock;
import com.supermartijn642.benched.blocks.BenchTile;
import com.supermartijn642.benched.seat.SeatEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.function.Consumer;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod("benched")
public class Benched {

    @ObjectHolder(value = "benched:acacia_bench", registryName = "minecraft:block")
    public static Block acacia_bench;
    @ObjectHolder(value = "benched:birch_bench", registryName = "minecraft:block")
    public static Block birch_bench;
    @ObjectHolder(value = "benched:dark_oak_bench", registryName = "minecraft:block")
    public static Block dark_oak_bench;
    @ObjectHolder(value = "benched:jungle_bench", registryName = "minecraft:block")
    public static Block jungle_bench;
    @ObjectHolder(value = "benched:bench", registryName = "minecraft:block")
    public static Block oak_bench;
    @ObjectHolder(value = "benched:spruce_bench", registryName = "minecraft:block")
    public static Block spruce_bench;
    @ObjectHolder(value = "benched:crimson_bench", registryName = "minecraft:block")
    public static Block crimson_bench;
    @ObjectHolder(value = "benched:warped_bench", registryName = "minecraft:block")
    public static Block warped_bench;

    @ObjectHolder(value = "benched:bench_tile", registryName = "minecraft:block_entity_type")
    public static BlockEntityType<BenchTile> bench_tile;

    @ObjectHolder(value = "benched:seat_entity", registryName = "minecraft:entity_type")
    public static EntityType<SeatEntity> seat_entity;

    public Benched(){
        runRegistry(ForgeRegistries.BLOCKS, RegistryEvents::onBlockRegistry);
        runRegistry(ForgeRegistries.BLOCK_ENTITIES, RegistryEvents::onTileRegistry);
        runRegistry(ForgeRegistries.ITEMS, RegistryEvents::onItemRegistry);
        runRegistry(ForgeRegistries.ENTITIES, RegistryEvents::onEntityRegistry);

        BenchedConfig.init();
    }

    private static <T> void runRegistry(IForgeRegistry<T> registry, Consumer<DeferredRegister<T>> registrationHandler){
        DeferredRegister<T> deferredRegister = DeferredRegister.create(registry, "benched");
        deferredRegister.register(FMLJavaModLoadingContext.get().getModEventBus());
        registrationHandler.accept(deferredRegister);
    }

    public static class RegistryEvents {

        public static void onBlockRegistry(DeferredRegister<Block> registry){
            registry.register("acacia_bench", () -> new BenchBlock("acacia_bench"));
            registry.register("birch_bench", () -> new BenchBlock("birch_bench"));
            registry.register("dark_oak_bench", () -> new BenchBlock("dark_oak_bench"));
            registry.register("jungle_bench", () -> new BenchBlock("jungle_bench"));
            registry.register("bench", () -> new BenchBlock("bench")); // oak_bench
            registry.register("spruce_bench", () -> new BenchBlock("spruce_bench"));
            registry.register("crimson_bench", () -> new BenchBlock("crimson_bench"));
            registry.register("warped_bench", () -> new BenchBlock("warped_bench"));
        }

        public static void onTileRegistry(DeferredRegister<BlockEntityType<?>> registry){
            registry.register("bench_tile", () -> BlockEntityType.Builder.of(BenchTile::new, acacia_bench, birch_bench, dark_oak_bench, jungle_bench, oak_bench, spruce_bench, crimson_bench, warped_bench).build(null));
        }

        public static void onItemRegistry(DeferredRegister<Item> registry){
            registry.register("acacia_bench", () -> new BlockItem(acacia_bench, new Item.Properties().tab(CreativeModeTab.TAB_SEARCH)));
            registry.register("birch_bench", () -> new BlockItem(birch_bench, new Item.Properties().tab(CreativeModeTab.TAB_SEARCH)));
            registry.register("dark_oak_bench", () -> new BlockItem(dark_oak_bench, new Item.Properties().tab(CreativeModeTab.TAB_SEARCH)));
            registry.register("jungle_bench", () -> new BlockItem(jungle_bench, new Item.Properties().tab(CreativeModeTab.TAB_SEARCH)));
            registry.register("bench", () -> new BlockItem(oak_bench, new Item.Properties().tab(CreativeModeTab.TAB_SEARCH)));
            registry.register("spruce_bench", () -> new BlockItem(spruce_bench, new Item.Properties().tab(CreativeModeTab.TAB_SEARCH)));
            registry.register("crimson_bench", () -> new BlockItem(crimson_bench, new Item.Properties().tab(CreativeModeTab.TAB_SEARCH)));
            registry.register("warped_bench", () -> new BlockItem(warped_bench, new Item.Properties().tab(CreativeModeTab.TAB_SEARCH)));
        }

        public static void onEntityRegistry(DeferredRegister<EntityType<?>> registry){
            registry.register("seat_entity", () -> EntityType.Builder.of((o, world) -> new SeatEntity(world), MobCategory.MISC).build(""));
        }
    }

}
