package com.supermartijn642.benched.seat;

import com.supermartijn642.core.ClientUtils;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.util.ResourceLocation;

/**
 * Created 12/26/2020 by SuperMartijn642
 */
public class SeatEntityRenderer extends EntityRenderer<SeatEntity> {

    public SeatEntityRenderer(){
        super(ClientUtils.getMinecraft().getEntityRenderDispatcher());
    }

    @Override
    public ResourceLocation getTextureLocation(SeatEntity entity){
        return null;
    }

    @Override
    public boolean shouldRender(SeatEntity livingEntity, ICamera camera, double camX, double camY, double camZ){
        return false;
    }
}
