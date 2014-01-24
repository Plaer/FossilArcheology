package net.fossilsarch.common.item;

import net.fossilsarch.common.entity.EntityDinosaurce;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemCarrotOnAStick;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemWhip extends ItemCarrotOnAStick {

	public ItemWhip(int par1) {
		super(par1);
		// TODO Auto-generated constructor stub
	}
    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (par3EntityPlayer.isRiding() && par3EntityPlayer.ridingEntity instanceof EntityDinosaurce)
        {
        	EntityDinosaurce var4 = (EntityDinosaurce)par3EntityPlayer.ridingEntity;

            if (var4.getRidingHandler().func_82633_h() && par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage() >= 7)
            {
                var4.getRidingHandler().func_82632_g();
                par1ItemStack.damageItem(7, par3EntityPlayer);

                if (par1ItemStack.stackSize == 0)
                {
                    return new ItemStack(Item.fishingRod);
                }
            }
        }

        return par1ItemStack;
    }
}
