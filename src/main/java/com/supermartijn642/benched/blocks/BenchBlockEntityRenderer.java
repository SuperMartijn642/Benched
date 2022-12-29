package com.supermartijn642.benched.blocks;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.render.CustomBlockEntityRenderer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import java.util.Random;

/**
 * Created 3/25/2021 by SuperMartijn642
 */
public class BenchBlockEntityRenderer implements CustomBlockEntityRenderer<BenchBlockEntity> {

    @Override
    public void render(BenchBlockEntity entity, float partialTicks, int combinedOverlay, float alpha){
        IBlockState state = entity.getBlockState();
        if(entity.items.isEmpty() || !(state.getBlock() instanceof BenchBlock))
            return;

        Random random = new Random(entity.getPos().getX() * 11 + entity.getPos().getY() * 13 + entity.getPos().getZ() * 17);

        EnumFacing benchDirection = state.getValue(BenchBlock.ROTATION);
        EnumFacing direction = EnumFacing.getHorizontal(entity.shape);

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.5, 0.9, 0.5);
        GlStateManager.translate(0.2 * direction.getFrontOffsetX(), 0, 0.2 * direction.getFrontOffsetZ());
        GlStateManager.rotate(180 - benchDirection.getHorizontalAngle(), 0, 1, 0);
        RenderItem renderer = ClientUtils.getItemRenderer();

        for(int i = 0; i < entity.items.size(); i++){
            ItemStack stack = entity.items.get(i);
            if(stack.isEmpty())
                continue;

            GlStateManager.pushMatrix();
            GlStateManager.rotate(90, 1, 0, 0);
            GlStateManager.rotate(random.nextFloat() * 360, 0, 0, 1);
            GlStateManager.translate(0, -0.1, 0);

            renderer.renderItem(stack, ItemCameraTransforms.TransformType.GROUND);

            GlStateManager.popMatrix();

            GlStateManager.translate(0, 0.03, 0);
        }

        GlStateManager.popMatrix();
    }
}
