package com.supermartijn642.benched;

import com.supermartijn642.benched.blocks.BenchBlockEntity;
import com.supermartijn642.benched.generators.BenchedLanguageGenerator;
import com.supermartijn642.benched.generators.BenchedLootTableGenerator;
import com.supermartijn642.benched.generators.BenchedRecipeGenerator;
import com.supermartijn642.benched.generators.BenchedTagGenerator;
import com.supermartijn642.benched.seat.SeatEntity;
import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.block.BaseBlock;
import com.supermartijn642.core.block.BaseBlockEntityType;
import com.supermartijn642.core.registry.GeneratorRegistrationHandler;
import com.supermartijn642.core.registry.RegistrationHandler;
import com.supermartijn642.core.registry.RegistryEntryAcceptor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

import java.util.Arrays;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod(modid = "@mod_id@", name = "@mod_name@", version = "@mod_version@", dependencies = "required-after:forge@@forge_dependency@;required-after:supermartijn642corelib@@core_library_dependency@;required-after:supermartijn642configlib@@config_library_dependency@")
public class Benched {

    @RegistryEntryAcceptor(namespace = "benched", identifier = "bench_tile", registry = RegistryEntryAcceptor.Registry.BLOCK_ENTITY_TYPES)
    public static BaseBlockEntityType<BenchBlockEntity> bench_tile;

    @RegistryEntryAcceptor(namespace = "benched", identifier = "seatentity", registry = RegistryEntryAcceptor.Registry.ENTITY_TYPES)
    public static EntityEntry seat_entity;

    public Benched(){
        BenchedConfig.init();

        register();
        if(CommonUtils.getEnvironmentSide().isClient())
            BenchedClient.register();
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
        handler.registerEntityEntry("seatentity", () -> EntityEntryBuilder.create().entity(SeatEntity.class).id(new ResourceLocation("benched", "seatentity"), 0).name("Seat").tracker(64, 20, false).build());
    }

    private static void registerGenerators(){
        GeneratorRegistrationHandler handler = GeneratorRegistrationHandler.get("benched");
        handler.addGenerator(BenchedLanguageGenerator::new);
        handler.addGenerator(BenchedLootTableGenerator::new);
        handler.addGenerator(BenchedRecipeGenerator::new);
        handler.addGenerator(BenchedTagGenerator::new);
    }
}
