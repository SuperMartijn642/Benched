package com.supermartijn642.benched;

import com.supermartijn642.benched.blocks.BenchBlockEntity;
import com.supermartijn642.benched.generators.*;
import com.supermartijn642.benched.seat.SeatEntity;
import com.supermartijn642.core.block.BaseBlock;
import com.supermartijn642.core.block.BaseBlockEntityType;
import com.supermartijn642.core.registry.GeneratorRegistrationHandler;
import com.supermartijn642.core.registry.RegistrationHandler;
import com.supermartijn642.core.registry.RegistryEntryAcceptor;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.Arrays;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class Benched implements ModInitializer {

    @RegistryEntryAcceptor(namespace = "benched", identifier = "bench_tile", registry = RegistryEntryAcceptor.Registry.BLOCK_ENTITY_TYPES)
    public static BaseBlockEntityType<BenchBlockEntity> bench_tile;

    @RegistryEntryAcceptor(namespace = "benched", identifier = "seat_entity", registry = RegistryEntryAcceptor.Registry.ENTITY_TYPES)
    public static EntityType<SeatEntity> seat_entity;

    @Override
    public void onInitialize(){
        BenchedConfig.init();

        register();
        registerGenerators();
    }

    private static void register(){
        RegistrationHandler handler = RegistrationHandler.get("benched");
        // Blocks and items
        for(BenchType type : BenchType.values()){
            handler.registerBlockCallback(type::registerBlock);
            handler.registerItemCallback(type::registerItem);
        }
        // Block entity type
        handler.registerBlockEntityType("bench_tile", () -> {
            BaseBlock[] blocks = Arrays.stream(BenchType.values()).map(BenchType::getBlock).toArray(BaseBlock[]::new);
            return BaseBlockEntityType.create(BenchBlockEntity::new, blocks);
        });
        // Seat entity
        handler.registerEntityType("seat_entity", () -> EntityType.Builder.of((type, level) -> new SeatEntity(level), MobCategory.MISC).sized(0, 0).build(""));
    }

    private static void registerGenerators(){
        GeneratorRegistrationHandler handler = GeneratorRegistrationHandler.get("benched");
        handler.addGenerator(BenchedModelGenerator::new);
        handler.addGenerator(BenchedBlockStateGenerator::new);
        handler.addGenerator(BenchedLanguageGenerator::new);
        handler.addGenerator(BenchedLootTableGenerator::new);
        handler.addGenerator(BenchedRecipeGenerator::new);
        handler.addGenerator(BenchedTagGenerator::new);
    }
}
