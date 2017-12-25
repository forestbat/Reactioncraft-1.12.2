package com.reactioncraft.desert.common.biome;

import java.util.Random;
import com.reactioncraft.desert.common.*;
import com.reactioncraft.integration.instances.IntegratedBlocks;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.*;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenDesertWells;
import net.minecraft.world.gen.feature.WorldGenFossils;

public class BiomeGenReactionDesert extends BiomeDesert
{		
	//private static final IBlockState Ore = IntegratedBlocks.DesertBlocks.getDefaultState().withProperty(BlockDesertMulti.TYPE, EnumDesertBlocks.three2);
	
    public BiomeGenReactionDesert(BiomeProperties par1)
    {
        super(par1);
        this.spawnableCreatureList.clear();
        this.topBlock    = IntegratedBlocks.DarkSand.getDefaultState();
        this.fillerBlock = IntegratedBlocks.DarkSand.getDefaultState();
    }
    
    @Override
    public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int x, int z, double noiseVal) 
    {
        this.generateBiomeTerrain(world, rand, primer, x, z, noiseVal);
        int localX = Math.floorMod(x, 16);
        int localZ = Math.floorMod(z, 16);
        for (int y = 0; y < 255; ++y) 
        {
            if (primer.getBlockState(localZ, y, localX).getBlock() == Blocks.STONE) 
            {
            	primer.setBlockState(localZ, y, localX , IntegratedBlocks.DesertBlocks.getDefaultState().withProperty(BlockDesertMulti.TYPE, EnumDesertBlocks.three2));
            }
        }
    }
    
    @Override
    public void decorate(World worldIn, Random rand, BlockPos pos)
    {
        super.decorate(worldIn, rand, pos);

        if(net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, rand, pos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.DESERT_WELL))
        if (rand.nextInt(1000) == 0)
        {
            int i = rand.nextInt(16) + 8;
            int j = rand.nextInt(16) + 8;
            BlockPos blockpos = worldIn.getHeight(pos.add(i, 0, j)).up();
            (new WorldGenDesertWells()).generate(worldIn, rand, blockpos);
        }

        if(net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, rand, pos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.FOSSIL))
        if (rand.nextInt(64) == 0)
        {
            (new WorldGenFossils()).generate(worldIn, rand, pos);
        }
    }
}