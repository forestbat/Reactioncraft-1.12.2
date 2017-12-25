package com.reactioncraft.bookcase.common;

import com.reactioncraft.reactioncraft;
import com.reactioncraft.core.common.blocks.*;
import com.reactioncraft.integration.instances.*;

//Minecraft
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

public class BlockBookcaseMulti extends BlockBase
{
	public static final PropertyEnum<EnumBookshelf> TYPE = PropertyEnum.<EnumBookshelf>create("type", EnumBookshelf.class);

	public BlockBookcaseMulti(String name, Material materialIn)
	{
		super(materialIn, name);
		this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumBookshelf.one1));
		this.setCreativeTab(reactioncraft.Reactioncraft);
	}

	/**
	 * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
	 * returns the metadata of the dropped item based on the old metadata of the block.
	 */
	public int damageDropped(IBlockState state)
	{
		return ((EnumBookshelf)state.getValue(TYPE)).getMetadata();
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{
		for (EnumBookshelf types : EnumBookshelf.values())
		{
			list.add(new ItemStack(itemIn, 1, types.getMetadata()));
		}
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState state)
	{
		return ((EnumBookshelf)state.getValue(TYPE)).getMetadata();
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(TYPE, EnumBookshelf.byMetadata(meta));
	}

	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {TYPE});
	}

	//Change Meta
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = playerIn.getHeldItem(hand);
		ItemStack stacktoAdd = new ItemStack(Items.BOOK);

		final IBlockState set0 = IntegratedBlocks.bookcases.getDefaultState().withProperty(BlockBookcaseMulti.TYPE, EnumBookshelf.one1);  // Empty
		final IBlockState set1 = IntegratedBlocks.bookcases.getDefaultState().withProperty(BlockBookcaseMulti.TYPE, EnumBookshelf.two2);  // 1/3 full
		final IBlockState set2 = IntegratedBlocks.bookcases.getDefaultState().withProperty(BlockBookcaseMulti.TYPE, EnumBookshelf.two3);  // 2/3 full
		final IBlockState set3 = IntegratedBlocks.bookcases.getDefaultState().withProperty(BlockBookcaseMulti.TYPE, EnumBookshelf.three1);// full


		if(stack != null && playerIn.getHeldItemMainhand().getItem() == Items.BOOK)
		{
			if (state.getValue(BlockBookcaseMulti.TYPE) == EnumBookshelf.one1)
			{
				System.out.println("changed into 1");
				worldIn.setBlockState(pos, set1);
				--stack.stackSize;
				return true;
			}
			if (state.getValue(BlockBookcaseMulti.TYPE) == EnumBookshelf.two2)
			{
				System.out.println("changed into 2");
				worldIn.setBlockState(pos, set2);
				--stack.stackSize;
				return true;
			}
			if (state.getValue(BlockBookcaseMulti.TYPE) == EnumBookshelf.two3)
			{
				System.out.println("changed into 3");
				worldIn.setBlockState(pos, set3);
				--stack.stackSize;
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			if (state.getValue(BlockBookcaseMulti.TYPE) == EnumBookshelf.three1)
			{
				System.out.println("changed back into 2");
				worldIn.setBlockState(pos, set2);
				playerIn.inventory.addItemStackToInventory(stacktoAdd);
				return true;
			}
			if (state.getValue(BlockBookcaseMulti.TYPE) == EnumBookshelf.two3)
			{
				System.out.println("changed back into 2");
				worldIn.setBlockState(pos, set1);
				playerIn.inventory.addItemStackToInventory(stacktoAdd);
				return true;
			}
			if (state.getValue(BlockBookcaseMulti.TYPE) == EnumBookshelf.two2)
			{
				System.out.println("changed back into 1");
				worldIn.setBlockState(pos, set0);
				playerIn.inventory.addItemStackToInventory(stacktoAdd);
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	
	
}