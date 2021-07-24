package com.supermartijn642.benched.seat;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * Created 12/26/2020 by SuperMartijn642
 */
public class SeatEntityRenderer extends EntityRenderer<SeatEntity> {

    public SeatEntityRenderer(EntityRendererProvider.Context context){
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(SeatEntity entity){
        return null;
    }

    @Override
    public boolean shouldRender(SeatEntity livingEntityIn, Frustum camera, double camX, double camY, double camZ){
        return false;
    }
}
