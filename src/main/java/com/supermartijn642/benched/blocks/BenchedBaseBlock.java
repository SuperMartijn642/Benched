package com.supermartijn642.benched.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

/**
 * Created 12/27/2020 by SuperMartijn642
 */
public class BenchedBaseBlock extends Block {

    public BenchedBaseBlock(Material material, MapColor color, String registryName){
        super(material, color);
        this.setRegistryName(registryName);
    }

}
