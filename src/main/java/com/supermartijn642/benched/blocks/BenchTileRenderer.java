package com.supermartijn642.benched.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

/**
 * Created 3/25/2021 by SuperMartijn642
 */
public class BenchTileRenderer implements BlockEntityRenderer<BenchTile> {

    @Override
    public void render(BenchTile tile, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay){
        BlockState state = tile.getBlockState();
        if(tile.items.isEmpty() || !(state.getBlock() instanceof BenchBlock))
            return;

        Random random = new Random(tile.getBlockPos().getX() * 11 + tile.getBlockPos().getY() * 13 + tile.getBlockPos().getZ() * 17);

        Direction benchDirection = state.getValue(BenchBlock.ROTATION);
        Direction direction = Direction.from2DDataValue(tile.shape);

        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.9, 0.5);
        matrixStack.translate(0.2 * direction.getStepX(), 0, 0.2 * direction.getStepZ());
        matrixStack.mulPose(new Quaternion(0, 180 - benchDirection.toYRot(), 0, true));
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

        for(int i = 0; i < tile.items.size(); i++){
            ItemStack stack = tile.items.get(i);
            if(stack.isEmpty())
                continue;

            matrixStack.pushPose();
            matrixStack.mulPose(new Quaternion(90, 0, 0, true));
            matrixStack.mulPose(new Quaternion(0, 0, random.nextFloat() * 360, true));
            matrixStack.translate(0, -0.1, 0);


            BakedModel model = renderer.getModel(stack, tile.getLevel(), null, 0);
            renderer.render(stack, ItemTransforms.TransformType.GROUND, false, matrixStack, buffer, combinedLight, OverlayTexture.NO_OVERLAY, model);

            matrixStack.popPose();

            matrixStack.translate(0, 0.03, 0);
        }

        matrixStack.popPose();
    }
}
