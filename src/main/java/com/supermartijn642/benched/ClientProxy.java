package com.supermartijn642.benched;

import com.supermartijn642.benched.blocks.BenchTileRenderer;
import com.supermartijn642.benched.seat.SeatEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Created 12/26/2020 by SuperMartijn642
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientProxy {

    @SubscribeEvent
    public static void onSetup(FMLClientSetupEvent e){
        RenderingRegistry.registerEntityRenderingHandler(Benched.seat_entity, SeatEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(Benched.bench_tile, BenchTileRenderer::new);
    }
}
