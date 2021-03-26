package com.supermartijn642.benched.blocks;

import com.supermartijn642.core.ClientUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import java.util.Random;

/**
 * Created 3/25/2021 by SuperMartijn642
 */
public class BenchTileRenderer extends TileEntitySpecialRenderer<BenchTile> {

    @Override
    public void render(BenchTile tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha){
        IBlockState state = tile.getBlockState();
        if(tile.items.isEmpty() || !(state.getBlock() instanceof BenchBlock))
            return;

        Random random = new Random(tile.getPos().getX() * 11 + tile.getPos().getY() * 13 + tile.getPos().getZ() * 17);

        EnumFacing benchDirection = state.getValue(BenchBlock.ROTATION);
        EnumFacing direction = EnumFacing.getHorizontal(tile.shape);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.translate(0.5, 0.9, 0.5);
        GlStateManager.translate(0.2 * direction.getFrontOffsetX(), 0, 0.2 * direction.getFrontOffsetZ());
        GlStateManager.rotate(180 - benchDirection.getHorizontalAngle(), 0, 1, 0);

        for(int i = 0; i < tile.items.size(); i++){
            ItemStack stack = tile.items.get(i);
            if(stack.isEmpty())
                continue;

            GlStateManager.pushMatrix();
            GlStateManager.rotate(90, 1, 0, 0);
            GlStateManager.rotate(random.nextFloat() * 360, 0, 0, 1);
            GlStateManager.translate(0, -0.1, 0);

            ClientUtils.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.GROUND);

            GlStateManager.popMatrix();

            GlStateManager.translate(0, 0.03, 0);
        }

        GlStateManager.popMatrix();
    }
}
