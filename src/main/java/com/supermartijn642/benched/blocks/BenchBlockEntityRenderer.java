package com.supermartijn642.benched.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.render.CustomBlockEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.AxisAngle4d;
import org.joml.Quaternionf;

import java.util.Random;

/**
 * Created 3/25/2021 by SuperMartijn642
 */
public class BenchBlockEntityRenderer implements CustomBlockEntityRenderer<BenchBlockEntity> {

    @Override
    public void render(BenchBlockEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay){
        BlockState state = entity.getBlockState();
        if(entity.items.isEmpty() || !(state.getBlock() instanceof BenchBlock))
            return;

        Random random = new Random(entity.getBlockPos().getX() * 11 + entity.getBlockPos().getY() * 13 + entity.getBlockPos().getZ() * 17);

        Direction benchDirection = state.getValue(BenchBlock.ROTATION);
        Direction direction = Direction.from2DDataValue(entity.shape);

        poseStack.pushPose();
        poseStack.translate(0.5, 0.9, 0.5);
        poseStack.translate(0.2 * direction.getStepX(), 0, 0.2 * direction.getStepZ());
        poseStack.mulPose(new Quaternionf(new AxisAngle4d(Math.toRadians(180 - benchDirection.toYRot()), 0, 1, 0)));
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

        for(int i = 0; i < entity.items.size(); i++){
            ItemStack stack = entity.items.get(i);
            if(stack.isEmpty())
                continue;

            poseStack.pushPose();
            poseStack.mulPose(new Quaternionf(new AxisAngle4d(Math.PI / 2, 1, 0, 0)));
            poseStack.mulPose(new Quaternionf(new AxisAngle4d(random.nextFloat() * 2 * Math.PI, 0, 0, 1)));
            poseStack.translate(0, -0.1, 0);


            BakedModel model = renderer.getModel(stack, entity.getLevel(), null, 0);
            renderer.render(stack, ItemDisplayContext.GROUND, false, poseStack, bufferSource, combinedLight, OverlayTexture.NO_OVERLAY, model);

            poseStack.popPose();

            poseStack.translate(0, 0.03, 0);
        }

        poseStack.popPose();
    }
}
