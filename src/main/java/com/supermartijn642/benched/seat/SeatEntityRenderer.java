package com.supermartijn642.benched.seat;

import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

/**
 * Created 12/26/2020 by SuperMartijn642
 */
public class SeatEntityRenderer extends Render<SeatEntity> {
    public SeatEntityRenderer(RenderManager renderManager){
        super(renderManager);
    }

    @Override
    public ResourceLocation getEntityTexture(SeatEntity entity){
        return null;
    }

    @Override
    public boolean shouldRender(SeatEntity livingEntity, ICamera camera, double camX, double camY, double camZ){
        return false;
    }
}
