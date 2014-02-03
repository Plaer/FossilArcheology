// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.fossilsarch.common.containers;

import java.util.Iterator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.fossilsarch.common.tileentity.TileEntityFeeder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

// Referenced classes of package net.minecraft.src:
//            Container, Slot, TileEntityDispenser, IInventory, 
//            EntityPlayer

public class ContainerFeeder extends Container
{
	int lastVegValue=0;
	int lastMeatValue=0;
    public ContainerFeeder(IInventory iinventory, TileEntity tileentityfeeder)
    {
        tileEntityFeeder = (TileEntityFeeder) tileentityfeeder;

        addSlotToContainer(new Slot(tileEntityFeeder, 0, 60, 62));
		addSlotToContainer(new Slot(tileEntityFeeder, 1, 104, 62));
        for(int j = 0; j < 3; j++)
        {
            for(int i1 = 0; i1 < 9; i1++)
            {
                addSlotToContainer(new Slot(iinventory, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
            }

        }

        for(int k = 0; k < 9; k++)
        {
            addSlotToContainer(new Slot(iinventory, k, 8 + k * 18, 142));
        }

    }
    
    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.sendProgressBarUpdate(this, 0, this.tileEntityFeeder.VegCurrent);
        par1ICrafting.sendProgressBarUpdate(this, 1, this.tileEntityFeeder.MeatCurrent);
    }
    
    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        Iterator var1 = this.crafters.iterator();

        while (var1.hasNext())
        {
            ICrafting var2 = (ICrafting)var1.next();

            if (this.lastVegValue != this.tileEntityFeeder.VegCurrent)
            {
                var2.sendProgressBarUpdate(this, 0, this.tileEntityFeeder.VegCurrent);
            }

            if (this.lastMeatValue != this.tileEntityFeeder.MeatCurrent)
            {
                var2.sendProgressBarUpdate(this, 1, this.tileEntityFeeder.MeatCurrent);
            }

        }

        this.lastVegValue = this.tileEntityFeeder.VegCurrent;
        this.lastMeatValue = this.tileEntityFeeder.MeatCurrent;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int par1, int par2)
    {
        if (par1 == 0)
        {
            this.tileEntityFeeder.VegCurrent=par2;
        }

        if (par1 == 1)
        {
            this.tileEntityFeeder.MeatCurrent=par2;
        }

    }
    
    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return tileEntityFeeder.isUseableByPlayer(entityplayer);
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
            
            if(i < 2)
            {
                if(!mergeItemStack(itemstack1, 2, 38, false))
                {
                    return null;
                }
            } else if (tileEntityFeeder.MeatValue(itemstack1.itemID) > 0) {
            	if (!mergeItemStack(itemstack1, 0, 1, false))
            		return null;
            } else if (tileEntityFeeder.VegValue(itemstack1.itemID) > 0) {
            	if (!mergeItemStack(itemstack1, 1, 2, false))
            		return null;
            }else {
            	boolean successfullyPlaced;
            	if (i < 29)
            		successfullyPlaced = mergeItemStack(itemstack1, 29, 38, false);
            	else
            		successfullyPlaced = mergeItemStack(itemstack1, 2, 29, false);
            	
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
    private TileEntityFeeder tileEntityFeeder;
}
