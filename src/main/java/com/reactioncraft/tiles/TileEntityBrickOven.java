package com.reactioncraft.tiles;

import com.reactioncraft.ItemHandler;
import com.reactioncraft.api.BrickOvenRecipes;
import com.reactioncraft.itemhandlers.BrickOvenItemHandler;
import net.minecraft.block.BlockFurnace;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class TileEntityBrickOven extends TileEntityBase implements ITickable
{
    /** The number of ticks that the furnace will keep burning */
    private int burnTime;
    /** The number of ticks that a fresh copy of the currently-burning item would keep the furnace burning for */
    private int currentItemBurnTime;
    private int cookTime;
    private int totalCookTime;
    private String customName;
    static final String ITEMS="Item handler",CURRENT_TIME="Current burn time",OUTPUT="Output handler";
    public BrickOvenItemHandler itemHandler=new BrickOvenItemHandler(2,this);
    public ItemHandler outputHandler=new ItemHandler(1,this);
    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        return this.hasCustomName() ? this.customName : "container.furnace";
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName()
    {
        return this.customName != null && !this.customName.isEmpty();
    }

    public void setCustomInventoryName(String p_145951_1_)
    {
        this.customName = p_145951_1_;
    }



    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        itemHandler.deserializeNBT(compound.getCompoundTag(ITEMS));

        outputHandler.deserializeNBT(compound.getCompoundTag(OUTPUT));
        this.burnTime = compound.getInteger("BurnTime");
        this.cookTime = compound.getInteger("CookTime");
        this.totalCookTime = compound.getInteger("CookTimeTotal");
        this.currentItemBurnTime = compound.getInteger(CURRENT_TIME);

        if (compound.hasKey("CustomName", 8))
        {
            this.customName = compound.getString("CustomName");
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("BurnTime", this.burnTime);
        compound.setInteger("CookTime", this.cookTime);
        compound.setInteger("CookTimeTotal", this.totalCookTime);
        compound.setInteger(CURRENT_TIME,currentItemBurnTime);
        compound.setTag(ITEMS,itemHandler.serializeNBT());
        compound.setTag(OUTPUT,outputHandler.serializeNBT());
        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.customName);
        }
        return compound;
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }



    /**
     * Furnace isBurning
     */
    public boolean isBurning()
    {
        return this.burnTime > 0;
    }



    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update()
    {
        boolean flag = this.isBurning();
        boolean flag1 = false;

        if (this.isBurning())
        {
            --this.burnTime;
        }

        if (!this.world.isRemote)
        {

            ItemStack input= itemHandler.getStackInSlot(1);
            ItemStack fuel=itemHandler.getStackInSlot(0);
            if (this.isBurning() || !input.isEmpty() && !fuel.isEmpty())
            {
                if (!this.isBurning() && this.canSmelt())
                {
                    this.burnTime = getItemBurnTime(input);
                    this.currentItemBurnTime = this.burnTime;

                    if (this.isBurning())
                    {
                        flag1 = true;

                        if (!input.isEmpty())
                        {
                            input.shrink(1);

                            if (input.getCount() == 0)
                            {
//                                itemStacks.set(1,one.getItem().getContainerItem(one));
                                itemHandler.setStackInSlot(1,new ItemStack(input.getItem()));
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canSmelt())
                {
                    ++this.cookTime;

                    if (this.cookTime == this.totalCookTime)
                    {
                        this.cookTime = 0;
                        this.totalCookTime = this.getCookTime();
                        this.smeltItem();
                        flag1 = true;
                    }
                }
                else
                {
                    this.cookTime = 0;
                }
            }
            else if (!this.isBurning() && this.cookTime > 0)
            {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
            }

            if (flag != this.isBurning())
            {
                flag1 = true;
                BlockFurnace.setState(this.isBurning(), this.world, this.pos);
            }
        }

        if (flag1)
        {
            this.markDirty();
        }
    }

    public int getCookTime()
    {
        return 200;
    }

    /**
     * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canSmelt()
    {
        if (itemHandler.getStackInSlot(0).isEmpty())
        {
            return false;
        }
        else
        {
            ItemStack itemstack = BrickOvenRecipes.instance().getSmeltingResult(itemHandler.getStackInSlot(0));
            if (itemstack.isEmpty()) return false;
            ItemStack two= outputHandler.getStackInSlot(2);
            if (two.isEmpty()) return true;
            if (!two.isItemEqual(itemstack)) return false;
            int result = two.getCount() + itemstack.getCount();
            return result <= getInventoryStackLimit() && result <=two.getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
        }
    }


    @Nullable
    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString("Brick Oven");
    }

    /**
     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
     */
    public void smeltItem()
    {
        if (this.canSmelt())
        {
            ItemStack input= itemHandler.getStackInSlot(0), two= outputHandler.getStackInSlot(2);
            ItemStack result = BrickOvenRecipes.instance().getSmeltingResult(input);

            if (two.getItem() == result.getItem())
            {
                two.setCount(two.getCount()+result.getCount());
                // Forge BugFix: Results may have multiple items
            }

            if (input.getItem() == Item.getItemFromBlock(Blocks.SPONGE) && input.getMetadata() == 1 && itemHandler.getStackInSlot(1).getItem() == Items.BUCKET)
            {
                itemHandler.setStackInSlot(1,new ItemStack(Items.WATER_BUCKET));
            }
           input.shrink(1);
        }
    }


    public static int getItemBurnTime(ItemStack stack)
    {
        return ForgeEventFactory.getItemBurnTime(stack);
    }

    public static boolean isItemFuel(ItemStack stack)
    {

        return getItemBurnTime(stack) > 0;
    }






    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability== CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, net.minecraft.util.EnumFacing facing)
    {
        if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == EnumFacing.UP)
                return (T) itemHandler;
            else if (facing == EnumFacing.DOWN) return (T) outputHandler;
        }
        return super.getCapability(capability, facing);
    }
}