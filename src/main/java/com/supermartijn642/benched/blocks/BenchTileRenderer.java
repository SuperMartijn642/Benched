package com.supermartijn642.benched.blocks;

import com.mojang.blaze3d.platform.GlStateManager;
import com.supermartijn642.core.ClientUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

import java.util.Random;

/**
 * Created 3/25/2021 by SuperMartijn642
 */
public class BenchTileRenderer extends TileEntityRenderer<BenchTile> {

    @Override
    public void render(BenchTile tile, double x, double y, double z, float partialTicks, int destroyStage){
        BlockState state = tile.getBlockState();
        if(tile.items.isEmpty() || !(state.getBlock() instanceof BenchBlock))
            return;

        Random random = new Random(tile.getPos().getX() * 11 + tile.getPos().getY() * 13 + tile.getPos().getZ() * 17);

        Direction benchDirection = state.get(BenchBlock.ROTATION);
        Direction direction = Direction.byHorizontalIndex(tile.shape);

        GlStateManager.pushMatrix();
        GlStateManager.translated(x, y, z);
        GlStateManager.translated(0.5, 0.9, 0.5);
        GlStateManager.translated(0.2 * direction.getXOffset(), 0, 0.2 * direction.getZOffset());
        GlStateManager.rotated(180 - benchDirection.getHorizontalAngle(), 0, 1, 0);

        for(int i = 0; i < tile.items.size(); i++){
            ItemStack stack = tile.items.get(i);
            if(stack.isEmpty())
                continue;

            GlStateManager.pushMatrix();
            GlStateManager.rotated(90, 1, 0, 0);
            GlStateManager.rotated(random.nextFloat() * 360, 0, 0, 1);
            GlStateManager.translated(0, -0.1, 0);

            ClientUtils.getMinecraft().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND);

            GlStateManager.popMatrix();

            GlStateManager.translated(0, 0.03, 0);
        }

        GlStateManager.popMatrix();
    }
}
