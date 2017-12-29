package com.reactioncraft.craftinghandlers;

import com.reactioncraft.registration.instances.ItemIndex;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class MachinesCraftingHandler 
{
	@SuppressWarnings("unused")
	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent event)
	{

		if(event.crafting.getItem() == Items.IRON_AXE)
		{
			ItemStack irondust = new ItemStack(ItemIndex.ironShavings);
			for (int i = 0; i < event.player.inventory.getSizeInventory(); i++)
			{
				ItemStack slotStack = event.player.inventory.getStackInSlot(i);
				if (!slotStack .isEmpty() && (slotStack.getItem() == ItemIndex.hammer || slotStack.getItem() == ItemIndex.bloodstoneHammer))
				{
					event.player.dropItem(irondust, true);
				}
			}
		}
	}
}