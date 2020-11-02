package com.supermartijn642.benched;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod(modid = Benched.MODID, name = Benched.NAME, version = Benched.VERSION)
public class Benched {

    public static final String MODID = "benched";
    public static final String NAME = "Benched";
    public static final String VERSION = "1.0.0";

    @GameRegistry.ObjectHolder("benched:bench")
    public static Block bench;

    @Mod.EventBusSubscriber
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlockRegistry(final RegistryEvent.Register<Block> e){
            e.getRegistry().register(new BenchBlock());
            GameRegistry.registerTileEntity(BenchTile.class, new ResourceLocation(MODID, "bench_tile"));
        }

        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> e){
            e.getRegistry().register(new ItemBlock(bench){
                @Override
                public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
                    IBlockState iblockstate = worldIn.getBlockState(pos);
                    Block block = iblockstate.getBlock();

                    if (!block.isReplaceable(worldIn, pos))
                    {
                        pos = pos.offset(facing);
                    }

                    ItemStack itemstack = player.getHeldItem(hand);

                    if (!itemstack.isEmpty() && player.canPlayerEdit(pos, facing, itemstack) && worldIn.mayPlace(this.block, pos, false, facing, player))
                    {
                        int i = this.getMetadata(itemstack.getMetadata());
                        IBlockState iblockstate1 = this.block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, player, hand);

                        if (iblockstate1.getBlock() != Blocks.AIR && placeBlockAt(itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ, iblockstate1))
                        {
                            iblockstate1 = worldIn.getBlockState(pos);
                            SoundType soundtype = iblockstate1.getBlock().getSoundType(iblockstate1, worldIn, pos, player);
                            worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                            itemstack.shrink(1);
                        }

                        return iblockstate1.getBlock() == Blocks.AIR ? EnumActionResult.FAIL : EnumActionResult.SUCCESS;
                    }
                    else
                    {
                        return EnumActionResult.FAIL;
                    }
                }
            }.setRegistryName("bench"));
        }
    }

}
