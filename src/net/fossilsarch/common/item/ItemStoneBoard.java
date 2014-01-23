package net.fossilsarch.common.item;

import net.fossilsarch.common.entity.EntityStoneboard;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;



public class ItemStoneBoard extends Item{
	public ItemStoneBoard(int i){
		super(i);
        this.setCreativeTab(CreativeTabs.tabDecorations);
	}
    
    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
    	if (par7 == 0)
        {
            return false;
        }
        else if (par7 == 1)
        {
            return false;
        }
        else
        {
            int var11 = Direction.facingToDirection[par7];
            EntityStoneboard var12 = new EntityStoneboard(par3World, par4, par5, par6, var11);
            
            if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack))
            {
                return false;
            }
            else
            {
                if (var12 != null && var12.onValidSurface())
                {
                    if (!par3World.isRemote)
                    {
                        par3World.spawnEntityInWorld(var12);
                    }

                    --par1ItemStack.stackSize;
                }

                return true;
            }
        }
    }
}