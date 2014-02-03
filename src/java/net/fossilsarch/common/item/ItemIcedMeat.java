package net.fossilsarch.common.item;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemIcedMeat extends ForgeItemSword {

	public ItemIcedMeat(int i, EnumToolMaterial enumtoolmaterial) {
		super(i, enumtoolmaterial);
		this.setMaxDamage(500);
		this.setMaxStackSize(64);
	}
	
	@Override
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase entityliving1)
    {
        itemstack.damageItem(1000, entityliving1);
        return true;
    }
	
	@Override
    public boolean onBlockDestroyed(ItemStack itemstack, World world, int i, int j, int k, int l, EntityLivingBase entityliving)
    {
        itemstack.damageItem(1000, entityliving);
        return true;
    }
}
