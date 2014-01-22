// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.fossilsarch.common.item;

import net.fossilsarch.mod_Fossil;
import net.fossilsarch.common.block.BlockFern;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;




// Referenced classes of package net.minecraft.src:
//            Item, World, Block, ItemStack, 
//            EntityPlayer

public class ItemFernSeed extends Item 
{

    public ItemFernSeed(int i, int j)
    {
        super(i);
        field_318_a = j;
    }
    
    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float hitX, float hitY, float hitZ)
    {
        if(l != 1)
        {
            return false;
        }
        int i1 = world.getBlockId(i, j, k);
        if(i1 == Block.grass.blockID && world.isAirBlock(i, j + 1, k) && BlockFern.CheckUnderTree(world,i,j,k))
        {
        	if (!world.isRemote) {
	            world.setBlock(i, j + 1, k, mod_Fossil.Ferns.blockID, field_318_a, 3);
	            itemstack.stackSize--;
        	}
            return true;
        } else
        {
            return false;
        }
    }

    private int field_318_a;
}
