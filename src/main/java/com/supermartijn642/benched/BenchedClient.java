package com.supermartijn642.benched;

import com.supermartijn642.benched.blocks.BenchBlockEntityRenderer;
import com.supermartijn642.benched.seat.SeatEntity;
import com.supermartijn642.benched.seat.SeatEntityRenderer;
import com.supermartijn642.core.registry.ClientRegistrationHandler;
import net.minecraftforge.client.model.obj.OBJLoader;

/**
 * Created 12/26/2020 by SuperMartijn642
 */
public class BenchedClient {

    public static void register(){
        OBJLoader.INSTANCE.addDomain("benched");
        ClientRegistrationHandler handler = ClientRegistrationHandler.get("benched");
        handler.registerCustomBlockEntityRenderer(() -> Benched.bench_tile, BenchBlockEntityRenderer::new);
        handler.registerEntityRenderer(SeatEntity.class, () -> new SeatEntityRenderer());
    }
}
