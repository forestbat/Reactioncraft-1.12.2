package com.reactioncraft.common.events;

import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

//Replace with Reactioncraft Tracker Jacker

public class EntityAIEventClass
{
    @SuppressWarnings("unused")
	@SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntityVillager)
        {
            EntityVillager flee = (EntityVillager)event.getEntity();
            flee.tasks.addTask(1, new EntityAIAvoidEntity<>(flee, EntitySheep.class, 2.0F, 0.8D, 1.33D));
        }															//Remember to Replace with Tracker Jacker.
    }
}