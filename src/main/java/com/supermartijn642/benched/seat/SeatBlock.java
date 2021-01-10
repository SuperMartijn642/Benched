package com.supermartijn642.benched.seat;

import com.supermartijn642.benched.blocks.BenchedBaseBlock;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created 12/26/2020 by SuperMartijn642
 */
public abstract class SeatBlock extends BenchedBaseBlock {

    public SeatBlock(Material material, MapColor color, String registryName){
        super(material, color, registryName);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
        if(!worldIn.isRemote)
            SeatHelper.sitPlayerDown(worldIn, pos, player);
        return true;
    }

    protected abstract double getSeatHeight();
}
