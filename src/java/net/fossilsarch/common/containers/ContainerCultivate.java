package net.fossilsarch.common.containers;

import java.util.List;

import net.fossilsarch.common.tileentity.TileEntityCultivate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
public class ContainerCultivate extends Container
{
	private TileEntityCultivate furnace;
    private int cookTime;
    private int burnTime;
    private int itemBurnTime;
	public ContainerCultivate(InventoryPlayer inventoryplayer, TileEntity tileentityfurnace)
    {
        cookTime = 0;
        burnTime = 0;
        itemBurnTime = 0;
        furnace = (TileEntityCultivate) tileentityfurnace;
        addSlotToContainer(new Slot(furnace, 0, 49, 20));
        addSlotToContainer(new Slot(furnace, 1, 80, 53));
        addSlotToContainer(new SlotFurnace(inventoryplayer.player, furnace, 2, 116, 21));
        for(int i = 0; i < 3; i++)
        {
            for(int k = 0; k < 9; k++)
            {
                addSlotToContainer(new Slot(inventoryplayer, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
            }

        }

        for(int j = 0; j < 9; j++)
        {
            addSlotToContainer(new Slot(inventoryplayer, j, 8 + j * 18, 142));
        }

    }
	
	@Override
    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.sendProgressBarUpdate(this, 0, this.furnace.furnaceCookTime);
        par1ICrafting.sendProgressBarUpdate(this, 1, this.furnace.furnaceBurnTime);
        par1ICrafting.sendProgressBarUpdate(this, 2, this.furnace.currentItemBurnTime);
    }
	
	@Override
	public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        for(int i = 0; i < crafters.size(); i++)
        {
            ICrafting icrafting = (ICrafting)crafters.get(i);
            if(cookTime != furnace.furnaceCookTime)
            {
                icrafting.sendProgressBarUpdate(this, 0, furnace.furnaceCookTime);
            }
            if(burnTime != furnace.furnaceBurnTime)
            {
                icrafting.sendProgressBarUpdate(this, 1, furnace.furnaceBurnTime);
            }
            if(itemBurnTime != furnace.currentItemBurnTime)
            {
                icrafting.sendProgressBarUpdate(this, 2, furnace.currentItemBurnTime);
            }
        }

        cookTime = furnace.furnaceCookTime;
        burnTime = furnace.furnaceBurnTime;
        itemBurnTime = furnace.currentItemBurnTime;
    }
	
	@Override
	public void updateProgressBar(int i, int j)
    {
        if(i == 0)
        {
            furnace.furnaceCookTime = j;
        }
        if(i == 1)
        {
            furnace.furnaceBurnTime = j;
        }
        if(i == 2)
        {
            furnace.currentItemBurnTime = j;
        }
    }
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return furnace.isUseableByPlayer(entityplayer);
    }
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int i)
    {
		 ItemStack itemstack = null;
	        Slot slot = (Slot)inventorySlots.get(i);
	        if(slot != null && slot.getHasStack())
	        {
	            ItemStack itemstack1 = slot.getStack();
	            itemstack = itemstack1.copy();
	            
	            if (i < 0)
	            	return null;
	            
	            if(i < 3)
	            {
	                if(!mergeItemStack(itemstack1, 3, 39, i >= 2))
	                {
	                    return null;
	                }
	            } else if (furnace.checkSmelt(itemstack1) != null) {
	            	if (!mergeItemStack(itemstack1, 0, 1, false))
	            		return null;
	            } else if (furnace.getItemBurnTime(itemstack1) > 0) {
	            	if (!mergeItemStack(itemstack1, 1, 2, false))
	            		return null;
	            }else {
	            	boolean successfullyPlaced;
	            	if (i < 30)
	            		successfullyPlaced = mergeItemStack(itemstack1, 30, 39, false);
	            	else
	            		successfullyPlaced = mergeItemStack(itemstack1, 3, 30, false);
	            	
	            	if (!successfullyPlaced)
	            		return null;          	
	            }

	            if(itemstack1.stackSize == 0)
	            {
	                slot.putStack(null);
	            } else
	            {
	                slot.onSlotChanged();
	            }
	            if (itemstack1.stackSize == itemstack1.stackSize)
	            {
	                return null;
	            }
	        }
	        return itemstack;
    }
}