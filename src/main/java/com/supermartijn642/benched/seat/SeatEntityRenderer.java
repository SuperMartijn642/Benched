package com.supermartijn642.benched.seat;

import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

/**
 * Created 12/26/2020 by SuperMartijn642
 */
public class SeatEntityRenderer extends EntityRenderer<SeatEntity> {
    public SeatEntityRenderer(EntityRendererManager renderManager){
        super(renderManager);
    }

    @Override
    public ResourceLocation getTextureLocation(SeatEntity entity){
        return null;
    }

    @Override
    public boolean shouldRender(SeatEntity livingEntityIn, ClippingHelper camera, double camX, double camY, double camZ){
        return false;
    }
}
