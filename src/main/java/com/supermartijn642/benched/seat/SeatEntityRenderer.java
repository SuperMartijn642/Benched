package com.supermartijn642.benched.seat;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

/**
 * Created 12/26/2020 by SuperMartijn642
 */
public class SeatEntityRenderer extends EntityRenderer<SeatEntity,EntityRenderState> {

    public SeatEntityRenderer(EntityRendererProvider.Context context){
        super(context);
    }

    @Override
    public boolean shouldRender(SeatEntity livingEntityIn, Frustum camera, double camX, double camY, double camZ){
        return false;
    }

    @Override
    public EntityRenderState createRenderState(){
        return new EntityRenderState();
    }
}
