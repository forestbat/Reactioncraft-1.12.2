package com.reactioncraft.items.tools;

import com.reactioncraft.Reactioncraft;
import com.reactioncraft.registration.instances.ItemIndex;
import net.minecraft.item.ItemStack;


public class ItemObsidianAxe extends ItemBaseAxe
{
    public ItemObsidianAxe(String var1, ToolMaterial var2)
    {
        super(var1, var2);
        this.setCreativeTab(Reactioncraft.ReactioncraftItems);
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
        return par2ItemStack.getItem() == ItemIndex.obsidianingot || super.getIsRepairable(par1ItemStack, par2ItemStack);

    }
}