package com.supermartijn642.benched;

import com.supermartijn642.benched.blocks.BenchTileRenderer;
import com.supermartijn642.benched.seat.SeatEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Created 12/26/2020 by SuperMartijn642
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientProxy {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers e){
        e.registerBlockEntityRenderer(Benched.bench_tile, context -> new BenchTileRenderer());
        e.registerEntityRenderer(Benched.seat_entity, SeatEntityRenderer::new);
    }
}
