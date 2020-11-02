package com.supermartijn642.benched;

import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.BasicState;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Created 11/2/2020 by SuperMartijn642
 */
@Mod.EventBusSubscriber(modid = "benched", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientProxy {

    public static void init(){
        OBJLoader.INSTANCE.addDomain("benched");
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent e){
//        IUnbakedModel unbakedModel = ModelLoaderRegistry.getModelOrMissing(new ResourceLocation("benched:bench.obj"));
//        IBakedModel model = unbakedModel.bake(e.getModelLoader(), ModelLoader.defaultTextureGetter(), new BasicState(unbakedModel.getDefaultState(), false), DefaultVertexFormats.BLOCK);
//        for(Boolean value1 : BenchBlock.VISIBLE.getAllowedValues())
//            for(Direction value2 : BenchBlock.ROTATION.getAllowedValues())
//                e.getModelRegistry().put(new ModelResourceLocation("benched:bench", BenchBlock.VISIBLE.getName() + "=" + value1 + "," + BenchBlock.ROTATION.getName() + "=" + value2), model);
//
//        model = unbakedModel.bake(e.getModelLoader(), ModelLoader.defaultTextureGetter(), new BasicState(unbakedModel.getDefaultState(), false), DefaultVertexFormats.ITEM);
//        e.getModelRegistry().put(new ModelResourceLocation("benched:bench", "inventory"), model);
    }

}
