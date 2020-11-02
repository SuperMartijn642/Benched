package com.supermartijn642.benched;

import net.minecraftforge.client.model.obj.OBJLoader;

/**
 * Created 11/2/2020 by SuperMartijn642
 */
public class ClientProxy {

    public static void init(){
        OBJLoader.INSTANCE.addDomain("benched");
    }

}
