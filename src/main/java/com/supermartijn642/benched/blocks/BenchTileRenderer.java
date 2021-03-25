package com.supermartijn642.benched.blocks;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.supermartijn642.core.ClientUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

import java.util.Random;

/**
 * Created 3/25/2021 by SuperMartijn642
 */
public class BenchTileRenderer extends TileEntityRenderer<BenchTile> {

    public BenchTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn){
        super(rendererDispatcherIn);
    }

    @Override
    public void render(BenchTile tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay){
        BlockState state = tile.getBlockState();
        if(tile.items.isEmpty() || !(state.getBlock() instanceof BenchBlock))
            return;

        Random random = new Random(tile.getPos().getX() * 11 + tile.getPos().getY() * 13 + tile.getPos().getZ() * 17);

        Direction benchDirection = state.get(BenchBlock.ROTATION);
        Direction direction = Direction.byHorizontalIndex(tile.shape);

        matrixStack.push();
        matrixStack.translate(0.5, 0.9, 0.5);
        matrixStack.translate(0.2 * direction.getXOffset(), 0, 0.2 * direction.getZOffset());
        matrixStack.rotate(new Quaternion(0, 180 - benchDirection.getHorizontalAngle(), 0, true));
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

        for(int i = 0; i < tile.items.size(); i++){
            ItemStack stack = tile.items.get(i);
            if(stack.isEmpty())
                continue;

            matrixStack.push();
            matrixStack.rotate(new Quaternion(90, 0, 0, true));
            matrixStack.rotate(new Quaternion(0, 0, random.nextFloat() * 360, true));
            matrixStack.translate(0, -0.1, 0);


            IBakedModel model = renderer.getItemModelWithOverrides(stack, tile.getWorld(), null);
            ClientUtils.getMinecraft().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND, false, matrixStack, buffer, combinedLight, OverlayTexture.NO_OVERLAY, model);

            matrixStack.pop();

            matrixStack.translate(0, 0.03, 0);
        }

        matrixStack.pop();
    }
}
