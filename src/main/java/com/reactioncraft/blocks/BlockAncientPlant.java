package com.reactioncraft.blocks;

import java.util.ArrayList;
import java.util.Random;

import com.reactioncraft.registration.instances.ItemIndex;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAncientPlant extends BlockReactionPlant
{

    public BlockAncientPlant()
    {
        super();
//        float f = 0.5F;
//        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
    }

    @Override
    protected Item getSeed() {
        return ItemIndex.ancientSeeds;
    }

    @Override
    protected Item getCrop() {
        return ItemIndex.ancientFruit;
    }
}
