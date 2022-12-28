package com.supermartijn642.benched.blocks;

import com.mojang.blaze3d.platform.GlStateManager;
import com.supermartijn642.core.render.CustomBlockEntityRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

import java.util.Random;

/**
 * Created 3/25/2021 by SuperMartijn642
 */
public class BenchBlockEntityRenderer implements CustomBlockEntityRenderer<BenchBlockEntity> {

    @Override
    public void render(BenchBlockEntity entity, float partialTicks, int combinedOverlay){
        BlockState state = entity.getBlockState();
        if(entity.items.isEmpty() || !(state.getBlock() instanceof BenchBlock))
            return;

        Random random = new Random(entity.getBlockPos().getX() * 11 + entity.getBlockPos().getY() * 13 + entity.getBlockPos().getZ() * 17);

        Direction benchDirection = state.getValue(BenchBlock.ROTATION);
        Direction direction = Direction.from2DDataValue(entity.shape);

        GlStateManager.pushMatrix();
        GlStateManager.translated(0.5, 0.9, 0.5);
        GlStateManager.translated(0.2 * direction.getStepX(), 0, 0.2 * direction.getStepZ());
        GlStateManager.rotated(180 - benchDirection.toYRot(), 0, 1, 0);
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

        for(int i = 0; i < entity.items.size(); i++){
            ItemStack stack = entity.items.get(i);
            if(stack.isEmpty())
                continue;

            GlStateManager.pushMatrix();
            GlStateManager.rotated(90, 1, 0, 0);
            GlStateManager.rotated(random.nextFloat() * 360, 0, 0, 1);
            GlStateManager.translated(0, -0.1, 0);

            renderer.renderStatic(stack, ItemCameraTransforms.TransformType.GROUND);

            GlStateManager.popMatrix();

            GlStateManager.translated(0, 0.03, 0);
        }

        GlStateManager.popMatrix();
    }
}
