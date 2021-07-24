package com.supermartijn642.benched;

import com.supermartijn642.benched.blocks.BenchTileRenderer;
import com.supermartijn642.benched.seat.SeatEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Created 12/26/2020 by SuperMartijn642
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientProxy {

    @SubscribeEvent
    public static void onSetup(FMLClientSetupEvent e){
        EntityRenderers.register(Benched.seat_entity, SeatEntityRenderer::new);
        BlockEntityRenderers.register(Benched.bench_tile, context -> new BenchTileRenderer());
    }
}
