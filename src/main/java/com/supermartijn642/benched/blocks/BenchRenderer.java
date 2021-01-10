package com.supermartijn642.benched.blocks;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.util.Random;

/**
 * Created 1/10/2021 by SuperMartijn642
 */
public class BenchRenderer extends TileEntityRenderer<BenchTile> {

    private ModelRenderer modelRenderer;

    public BenchRenderer(TileEntityRendererDispatcher rendererDispatcherIn){
        super(rendererDispatcherIn);
    }

    @Override
    public void render(BenchTile tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn){
        BlockState state = tile.getBlockState();
        if(!state.get(BenchBlock.VISIBLE))
            return;

        BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRendererDispatcher();
        IBakedModel model = blockRenderer.getModelForState(state);
//        matrixStackIn.push();
//        matrixStackIn.translate(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ());
//        IVertexBuilder builder = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource().getBuffer(RenderType.getSolid());
//        blockRenderer.getBlockModelRenderer().renderModel(tile.getWorld(), model, state, tile.getPos(), matrixStackIn, builder, false, new Random(), 42L, combinedOverlayIn, EmptyModelData.INSTANCE);
//        matrixStackIn.pop();
    }
}
