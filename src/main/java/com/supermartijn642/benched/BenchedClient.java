package com.supermartijn642.benched;

import com.supermartijn642.benched.blocks.BenchBlockEntityRenderer;
import com.supermartijn642.benched.seat.SeatEntityRenderer;
import com.supermartijn642.core.registry.ClientRegistrationHandler;
import net.fabricmc.api.ClientModInitializer;

/**
 * Created 12/26/2020 by SuperMartijn642
 */
public class BenchedClient implements ClientModInitializer {

    @Override
    public void onInitializeClient(){
        ClientRegistrationHandler handler = ClientRegistrationHandler.get("benched");
        handler.registerCustomBlockEntityRenderer(() -> Benched.bench_tile, BenchBlockEntityRenderer::new);
        handler.registerEntityRenderer(() -> Benched.seat_entity, SeatEntityRenderer::new);
    }
}
