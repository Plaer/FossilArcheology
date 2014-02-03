package net.fossilsarch.common.tileentity;

import net.fossilsarch.mod_Fossil;
import net.fossilsarch.common.block.BlockAnalyzer;
import net.fossilsarch.common.io.EnumDinoType;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityAnalyzer extends TileEntity implements IInventory, ISidedInventory{
	private ItemStack analyzerItemStacks[]=new ItemStack[13];
    public int analyzerBurnTime=0;
    public int currentItemBurnTime=100;
    public int analyzerCookTime=0;
	private int RawIndex=-1;
	private int SpaceIndex=-1;
	public TileEntityAnalyzer(){
		
	}
	public int getSizeInventory()
    {
        return analyzerItemStacks.length;
    }
	public ItemStack getStackInSlot(int i)
    {
        return analyzerItemStacks[i];
    }
	public ItemStack decrStackSize(int i, int j)
    {
        if(analyzerItemStacks[i] != null)
        {
            if(analyzerItemStacks[i].stackSize <= j)
            {
                ItemStack itemstack = analyzerItemStacks[i];
                analyzerItemStacks[i] = null;
                return itemstack;
            }
            ItemStack itemstack1 = analyzerItemStacks[i].splitStack(j);
            if(analyzerItemStacks[i].stackSize == 0)
            {
                analyzerItemStacks[i] = null;
            }
            return itemstack1;
        } else
        {
            return null;
        }
    }
	public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        analyzerItemStacks[i] = itemstack;
        if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
    }
	public String getInvName()
    {
        return "Analyzer";
    }
	public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        analyzerItemStacks = new ItemStack[getSizeInventory()];
        for(int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            byte byte0 = nbttagcompound1.getByte("Slot");
            if(byte0 >= 0 && byte0 < analyzerItemStacks.length)
            {
                analyzerItemStacks[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        analyzerBurnTime = nbttagcompound.getShort("BurnTime");
        analyzerCookTime = nbttagcompound.getShort("CookTime");
        currentItemBurnTime = 100;
    }
	public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("BurnTime", (short)analyzerBurnTime);
        nbttagcompound.setShort("CookTime", (short)analyzerCookTime);
        NBTTagList nbttaglist = new NBTTagList();
        for(int i = 0; i < analyzerItemStacks.length; i++)
        {
            if(analyzerItemStacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                analyzerItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }
	public int getInventoryStackLimit()
    {
        return 64;
    }
	public int getCookProgressScaled(int i)
    {
        return (analyzerCookTime * i) / 200;
    }
	public int getBurnTimeRemainingScaled(int i)
    {
        if(currentItemBurnTime == 0)
        {
            currentItemBurnTime = 100;
        }
        return (analyzerBurnTime * i) / currentItemBurnTime;
    }
	public boolean isBurning()
    {
        return analyzerBurnTime > 0;
    }
	public void updateEntity()
    {
        boolean flag = analyzerBurnTime > 0;
        boolean flag1 = false;
        if(analyzerBurnTime > 0)
        {
            analyzerBurnTime--;
        }
        if(!worldObj.isRemote)
        {
            if(analyzerBurnTime == 0 && canSmelt())
            {
                currentItemBurnTime = analyzerBurnTime = 100;
                if(analyzerBurnTime > 0)
                {
                    flag1 = true;
                    //if(furnaceItemStacks[1] != null)
                   // {
                      //  if(furnaceItemStacks[1].getItem().hasContainerItem())
                      //  {
                      //      furnaceItemStacks[1] = new ItemStack(furnaceItemStacks[1].getItem().getContainerItem());
                      //  } else
                     //   {
                     //       furnaceItemStacks[1].stackSize--;
                     //   }
                    //    if(furnaceItemStacks[1].stackSize == 0)
                    //    {
                    //        furnaceItemStacks[1] = null;
                   //     }
                   // }
                }
            }
            if(isBurning() && canSmelt())
            {
                analyzerCookTime++;
                if(analyzerCookTime == 200)
                {
                    analyzerCookTime = 0;
                    smeltItem();
                    flag1 = true;
                }
            } else
            {
				analyzerCookTime = 0;

            }
            if(flag != (analyzerBurnTime > 0))
            {
                flag1 = true;
                BlockAnalyzer.updateFurnaceBlockState(analyzerBurnTime > 0, worldObj, xCoord, yCoord, zCoord);
			}
        }
        if(flag1)
        {
            onInventoryChanged();
        }
    }
	
	public boolean isValidInput(int itemID) {
		if (itemID == mod_Fossil.biofossil.itemID ||
				itemID == mod_Fossil.relic.itemID ||
				itemID == mod_Fossil.RawDinoMeat.itemID ||
				itemID == Item.porkRaw.itemID ||
				itemID == Item.beefRaw.itemID ||
				itemID == Item.egg.itemID ||
				itemID == Item.chickenRaw.itemID ||
				itemID == Block.cloth.blockID ||
				itemID == mod_Fossil.IcedMeat.itemID)
			return true;
		
		return false;
	}
	
	private boolean canSmelt()
    {
		SpaceIndex=-1;
		RawIndex=-1;
        for (int i=0;i<9;i++){
			if (analyzerItemStacks[i]!=null){
				int tmp=analyzerItemStacks[i].getItem().itemID;
				if (isValidInput(tmp)) {
					RawIndex=i;
					break;
				}
			}
		}
		if (RawIndex==-1) return false;
		else{
			for (int i=12;i>8;i--){
				if (analyzerItemStacks[i]==null){
					SpaceIndex=i;
					break;
				}
			}
			return (SpaceIndex!=-1 && RawIndex!=-1);
		}
    }
	public void smeltItem()
    {
        if(!canSmelt())
        {
            return;
        }
        //ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(furnaceItemStacks[0].getItem().shiftedIndex);
		ItemStack itemstack=null;
		int chance=new java.util.Random().nextInt(100);
		if (analyzerItemStacks[RawIndex].getItem()==mod_Fossil.biofossil){
			if(chance<=60) itemstack=new ItemStack(Item.dyePowder, 3, 15);
			if(chance>60 && chance<=80) itemstack=new ItemStack(mod_Fossil.FernSeed,3);
			if (chance>80){
				int chanceDNA=new java.util.Random().nextInt(EnumDinoType.values().length-1);
				if (chanceDNA >= EnumDinoType.Mosasaurus.ordinal()) chanceDNA++;
				itemstack=new ItemStack(mod_Fossil.DNA,1,chanceDNA);
			}
			//random get of DNA
		}
		if (analyzerItemStacks[RawIndex].getItem().itemID==Block.cloth.blockID){
			if (new java.util.Random().nextInt(50)<=30) itemstack=new ItemStack(Item.silk,4);
			else itemstack=new ItemStack(mod_Fossil.AnimalDNA,1,1);
		}
		if (analyzerItemStacks[RawIndex].getItem()==mod_Fossil.RawDinoMeat){	
			itemstack=new ItemStack(mod_Fossil.DNA,4,analyzerItemStacks[RawIndex].getItemDamage());
		}
		if (analyzerItemStacks[RawIndex].getItem()==Item.porkRaw){	
			itemstack=new ItemStack(mod_Fossil.AnimalDNA,2,0);
		}
		if (analyzerItemStacks[RawIndex].getItem()==Item.beefRaw){	
			itemstack=new ItemStack(mod_Fossil.AnimalDNA,2,2);
		}
		if (analyzerItemStacks[RawIndex].getItem()==Item.egg){	
			itemstack=new ItemStack(mod_Fossil.AnimalDNA,1,3);
		}
		if (analyzerItemStacks[RawIndex].getItem()==Item.chickenRaw){	
			itemstack=new ItemStack(mod_Fossil.AnimalDNA,1,3);
		}
		if (analyzerItemStacks[RawIndex].getItem()==mod_Fossil.IcedMeat){
			
			if (chance<=33) itemstack=new ItemStack(mod_Fossil.AnimalDNA,1,4);
			if (chance>33 && chance<66) itemstack=new ItemStack(mod_Fossil.AnimalDNA,1,5);
			if (itemstack==null)itemstack=new ItemStack(Item.beefRaw);
		}
		if (analyzerItemStacks[RawIndex].getItem()==mod_Fossil.relic){	
			if (chance<=40)itemstack=new ItemStack(Block.gravel,6);
			if (chance>40 && chance<=70)itemstack=new ItemStack(mod_Fossil.stoneboard,1);
			if (chance>70 && chance<=90)itemstack=new ItemStack(Item.flint,2);
			if (chance>90)itemstack=new ItemStack(mod_Fossil.BrokenSword,1);
		}
		if (itemstack!=null){
			if(itemstack.itemID==Item.dyePowder.itemID || itemstack.itemID==mod_Fossil.FernSeed.itemID||itemstack.itemID==Item.flint.itemID||itemstack.itemID==Block.gravel.blockID){
				for (int i=12;i>8;i--){
					if (analyzerItemStacks[i]!=null){
						if (itemstack.itemID==analyzerItemStacks[i].itemID){
							if (analyzerItemStacks[i].stackSize+itemstack.stackSize <= analyzerItemStacks[i].getMaxStackSize()){
								analyzerItemStacks[i].stackSize+=itemstack.stackSize;
								itemstack.stackSize=0;
								break;
							}else{
								itemstack.stackSize-=(analyzerItemStacks[i].getMaxStackSize()-analyzerItemStacks[i].stackSize);
								analyzerItemStacks[i].stackSize=analyzerItemStacks[i].getMaxStackSize();
							}
						}
					}
				}
			}
			if (itemstack.stackSize!=0){
				if (analyzerItemStacks[SpaceIndex]==null) analyzerItemStacks[SpaceIndex] = itemstack.copy();
				//else analyzerItemStacks[SpaceIndex].stackSize++;
			}
			analyzerItemStacks[RawIndex].stackSize--;
			if(analyzerItemStacks[RawIndex].stackSize==0) analyzerItemStacks[RawIndex]=null;
		}
    }
	private int getItemBurnTime(ItemStack itemstack)
    {
        return 100;
    }
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }
    public void openChest()
    {
    }

    public void closeChest()
    {
    }

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return null;
	}
	
    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
    	if (side == 1){
    		return new int[] { 9, 10, 11, 12};
    	} else {
    		return new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8};
    	}
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
    	return isItemValidForSlot(i, itemstack);
    }
    
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
    	return (i >= 9);
    }
    
	@Override
	public boolean isInvNameLocalized() {
		return true;
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		if (i >= 9)
			return false;
		
		return this.isValidInput(itemstack.itemID);
	}
}