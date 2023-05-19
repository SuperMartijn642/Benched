package com.supermartijn642.benched.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.supermartijn642.core.render.CustomBlockEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

/**
 * Created 3/25/2021 by SuperMartijn642
 */
public class BenchBlockEntityRenderer implements CustomBlockEntityRenderer<BenchBlockEntity> {

    private static final Random RANDOM = new Random();

    @Override
    public void render(BenchBlockEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay){
        BlockState state = entity.getBlockState();
        if(entity.items.isEmpty() || !(state.getBlock() instanceof BenchBlock))
            return;

        BlockPos pos = entity.getBlockPos();
        RANDOM.setSeed(pos.getX() * 11L + pos.getY() * 13L + pos.getZ() * 17L);
        RANDOM.nextDouble();

        Direction.Axis benchAxis = state.getValue(BenchBlock.AXIS);
        BenchBlock.Part benchPart = state.getValue(BenchBlock.PART);
        double xOffset = benchAxis == Direction.Axis.Z ? 1 - 2 * benchPart.getXOffset() : 0;
        double zOffset = benchAxis == Direction.Axis.X ? 1 - 2 * benchPart.getZOffset() : 0;

        poseStack.pushPose();
        poseStack.translate(0.5, 0.95, 0.5);
        poseStack.translate(0.25 * xOffset, 0, 0.25 * zOffset);
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

        for(int i = 0; i < entity.items.size(); i++){
            ItemStack stack = entity.items.get(i);
            if(stack.isEmpty())
                continue;

            poseStack.pushPose();
            poseStack.mulPose(new Quaternion(90, 0, 0, true));
            float random = RANDOM.nextFloat();
            poseStack.mulPose(new Quaternion(0, 0, random * 360, true));
            poseStack.translate(0, -0.1, 0);


            BakedModel model = renderer.getModel(stack, entity.getLevel(), null, 0);
            renderer.render(stack, ItemTransforms.TransformType.GROUND, false, poseStack, bufferSource, combinedLight, OverlayTexture.NO_OVERLAY, model);

            poseStack.popPose();

            poseStack.translate(0, 0.03, 0);
        }

        poseStack.popPose();
    }
}
