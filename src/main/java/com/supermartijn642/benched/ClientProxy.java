package com.supermartijn642.benched;

import com.supermartijn642.benched.seat.SeatEntity;
import com.supermartijn642.benched.seat.SeatEntityRenderer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created 12/26/2020 by SuperMartijn642
 */
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy {

    public static void init(FMLInitializationEvent e){
        RenderingRegistry.registerEntityRenderingHandler(SeatEntity.class, SeatEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent e){
        OBJLoader.INSTANCE.addDomain("benched");

        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Benched.acacia_bench), 0, new ModelResourceLocation(Benched.acacia_bench.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Benched.birch_bench), 0, new ModelResourceLocation(Benched.birch_bench.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Benched.dark_oak_bench), 0, new ModelResourceLocation(Benched.dark_oak_bench.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Benched.jungle_bench), 0, new ModelResourceLocation(Benched.jungle_bench.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Benched.oak_bench), 0, new ModelResourceLocation(Benched.oak_bench.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Benched.spruce_bench), 0, new ModelResourceLocation(Benched.spruce_bench.getRegistryName(), "inventory"));
    }
}
