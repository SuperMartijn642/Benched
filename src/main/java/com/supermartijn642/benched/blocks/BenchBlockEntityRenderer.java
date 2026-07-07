package com.supermartijn642.benched.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.render.CustomBlockEntityRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;

import java.util.Random;

/**
 * Created 3/25/2021 by SuperMartijn642
 */
public class BenchBlockEntityRenderer implements CustomBlockEntityRenderer<BenchBlockEntity,BenchBlockEntityRenderer.State> {

    private static final Random RANDOM = new Random();

    @Override
    public State createStateHolder(){
        return new State();
    }

    @Override
    public void updateState(State state, BenchBlockEntity entity, UpdateContext context){
        if(entity.items.isEmpty()){
            state.hasItems = false;
            return;
        }
        state.hasItems = true;
        state.pos = entity.getBlockPos();
        state.blockState = entity.getBlockState();
        if(state.items == null || entity.items.size() > state.items.length)
            state.items = new ItemStackRenderState[entity.items.size()];
        for(int i = 0; i < entity.items.size(); i++){
            ItemStackRenderState stackRenderState = state.items[i];
            if(stackRenderState == null){
                stackRenderState = new ItemStackRenderState();
                state.items[i] = stackRenderState;
            }
            ClientUtils.getMinecraft().getItemModelResolver().updateForTopItem(
                stackRenderState,
                entity.items.get(i),
                ItemDisplayContext.GROUND,
                entity.getLevel(),
                null,
                (int)entity.getBlockPos().asLong() + i
            );
        }
    }

    @Override
    public void submit(SubmitNodeCollector output, State state, RenderContext context){
        if(!state.hasItems)
            return;

        RANDOM.setSeed(state.pos.getX() * 11L + state.pos.getY() * 13L + state.pos.getZ() * 17L);
        RANDOM.nextDouble();

        Direction.Axis benchAxis = state.blockState.getValue(BenchBlock.AXIS);
        BenchBlock.Part benchPart = state.blockState.getValue(BenchBlock.PART);
        double xOffset = benchAxis == Direction.Axis.Z ? 1 - 2 * benchPart.getXOffset() : 0;
        double zOffset = benchAxis == Direction.Axis.X ? 1 - 2 * benchPart.getZOffset() : 0;

        PoseStack poseStack = context.poseStack();
        poseStack.pushPose();
        poseStack.translate(0.5, 0.95, 0.5);
        poseStack.translate(0.25 * xOffset, 0, 0.25 * zOffset);

        for(int i = 0; i < state.items.length; i++){
            ItemStackRenderState stack = state.items[i];
            if(stack.isEmpty())
                continue;

            poseStack.pushPose();
            poseStack.mulPose(new Quaternionf().setAngleAxis(Math.PI / 2, 1, 0, 0));
            poseStack.mulPose(new Quaternionf().setAngleAxis(RANDOM.nextDouble() * Math.PI * 2, 0, 0, 1));
            poseStack.translate(0, -0.1, 0);

            ModelFeatureRenderer.CrumblingOverlay breakingOverlay = context.breakingOverlay();
            stack.submit(poseStack, output, context.packedLight(), breakingOverlay == null ? OverlayTexture.NO_OVERLAY : breakingOverlay.progress(), 0);

            poseStack.popPose();

            poseStack.translate(0, 0.03, 0);
        }

        poseStack.popPose();
    }

    public static class State {
        boolean hasItems = false;
        BlockPos pos;
        BlockState blockState;
        ItemStackRenderState[] items;
    }
}
