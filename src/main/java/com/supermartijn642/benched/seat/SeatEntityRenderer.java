package com.supermartijn642.benched.seat;

import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * Created 12/26/2020 by SuperMartijn642
 */
public class SeatEntityRenderer extends EntityRenderer<SeatEntity> {
    public SeatEntityRenderer(EntityRendererManager renderManager){
        super(renderManager);
    }

    @Nullable
    @Override
    protected ResourceLocation getTextureLocation(SeatEntity entity){
        return null;
    }

    @Override
    public boolean shouldRender(SeatEntity livingEntity, ICamera camera, double camX, double camY, double camZ){
        return false;
    }
}
