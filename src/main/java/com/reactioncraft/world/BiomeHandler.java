package com.reactioncraft.world;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class BiomeHandler
{

	@SuppressWarnings("unused")
	@SubscribeEvent
	public void registerBiomes(RegistryEvent.Register<Biome> registryEvent)
	{
		registerBiomes(registryEvent.getRegistry());
	}

	private void registerBiomes(IForgeRegistry<Biome> biomes)
	{
		BiomeGenReactionDesert reactionDesert_BIOME = new BiomeGenReactionDesert(new Biome.BiomeProperties("Reaction Desert")
				.setBaseHeight(0.125F)
				.setHeightVariation(0.05F)
				.setTemperature(2.0F)
				.setRainfall(0.0F)
				.setRainDisabled());
		reactionDesert_BIOME.setRegistryName("reaction_desert");
		biomes.register(reactionDesert_BIOME);
		BiomeManager.addBiome(BiomeType.DESERT, new BiomeEntry(reactionDesert_BIOME, 1000));
		BiomeManager.addSpawnBiome(reactionDesert_BIOME);
		BiomeDictionary.addTypes(reactionDesert_BIOME, Type.DRY);
	    BiomeManager.addVillageBiome(reactionDesert_BIOME, true);
	}
}