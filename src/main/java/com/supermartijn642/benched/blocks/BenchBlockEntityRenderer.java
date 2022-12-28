package com.supermartijn642.benched.blocks;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.supermartijn642.core.render.CustomBlockEntityRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

import java.util.Random;

/**
 * Created 3/25/2021 by SuperMartijn642
 */
public class BenchBlockEntityRenderer implements CustomBlockEntityRenderer<BenchBlockEntity> {

    @Override
    public void render(BenchBlockEntity entity, float partialTicks, MatrixStack poseStack, IRenderTypeBuffer bufferSource, int combinedLight, int combinedOverlay){
        BlockState state = entity.getBlockState();
        if(entity.items.isEmpty() || !(state.getBlock() instanceof BenchBlock))
            return;

        Random random = new Random(entity.getBlockPos().getX() * 11 + entity.getBlockPos().getY() * 13 + entity.getBlockPos().getZ() * 17);

        Direction benchDirection = state.getValue(BenchBlock.ROTATION);
        Direction direction = Direction.from2DDataValue(entity.shape);

        poseStack.pushPose();
        poseStack.translate(0.5, 0.9, 0.5);
        poseStack.translate(0.2 * direction.getStepX(), 0, 0.2 * direction.getStepZ());
        poseStack.mulPose(new Quaternion(0, 180 - benchDirection.toYRot(), 0, true));
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

        for(int i = 0; i < entity.items.size(); i++){
            ItemStack stack = entity.items.get(i);
            if(stack.isEmpty())
                continue;

            poseStack.pushPose();
            poseStack.mulPose(new Quaternion(90, 0, 0, true));
            poseStack.mulPose(new Quaternion(0, 0, random.nextFloat() * 360, true));
            poseStack.translate(0, -0.1, 0);


            IBakedModel model = renderer.getModel(stack, entity.getLevel(), null);
            renderer.render(stack, ItemCameraTransforms.TransformType.GROUND, false, poseStack, bufferSource, combinedLight, OverlayTexture.NO_OVERLAY, model);

            poseStack.popPose();

            poseStack.translate(0, 0.03, 0);
        }

        poseStack.popPose();
    }
}
