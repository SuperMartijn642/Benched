package com.supermartijn642.benched.blocks;

import com.supermartijn642.core.item.BaseBlockItem;
import com.supermartijn642.core.item.ItemProperties;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created 1/10/2021 by SuperMartijn642
 */
public class BenchItemBlock extends BaseBlockItem {

    public BenchItemBlock(Block bench, ItemProperties properties){
        super(bench, properties);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World level, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
        if(!level.getBlockState(pos).getBlock().isReplaceable(level, pos))
            pos = pos.offset(facing);

        ItemStack stack = player.getHeldItem(hand);
        if(!stack.isEmpty() && player.canPlayerEdit(pos, facing, stack) && level.mayPlace(this.block, pos, false, facing, player)){
            int metadata = this.getMetadata(stack.getMetadata());
            IBlockState state = this.block.getStateForPlacement(level, pos, facing, hitX, hitY, hitZ, metadata, player, hand);

            if(state.getBlock() != Blocks.AIR && this.placeBlockAt(stack, player, level, pos, facing, hitX, hitY, hitZ, state)){
                state = level.getBlockState(pos);
                SoundType soundType = state.getBlock().getSoundType(state, level, pos, player);
                level.playSound(player, pos, soundType.getPlaceSound(), SoundCategory.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
                stack.shrink(1);
            }

            return state.getBlock() == Blocks.AIR ? EnumActionResult.FAIL : EnumActionResult.SUCCESS;
        }else
            return EnumActionResult.FAIL;
    }
}
