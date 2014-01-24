package net.fossilsarch.common.item;

import java.util.Random;

import net.fossilsarch.common.entity.EntityAncientJavelin;
import net.fossilsarch.common.entity.EntityJavelin;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class ItemJavelin extends ForgeItem
{
	public EnumToolMaterial SelfMaterial;
	private boolean isAncient=false;
	
    public ItemJavelin(int i,EnumToolMaterial material)
    {
        super(i);
        maxStackSize = 64;
        //setMaxDamage(384);
        this.hasSubtypes=true;
        SelfMaterial=material;
    }
    
    public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer entityplayer, int i)
    {
        boolean flag = EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, itemstack) > 0;
        if (flag || entityplayer.inventory.hasItem(this.itemID))
        {
            int j = getMaxItemUseDuration(itemstack) - i;
            float f = (float)j / 20F;
            f = (f * f + f * 2.0F) / 3F;
            if ((double)f < 0.10000000000000001D)
            {
                return;
            }
            if (f > 1.0F)
            {
                f = 1.0F;
            }
            EntityJavelin entityjavelin ;
            
            if (!this.isAncient)entityjavelin= new EntityJavelin(world, entityplayer, f * 2.0F,this.SelfMaterial);
            else entityjavelin= new EntityAncientJavelin(world, entityplayer, f * 2.0F,this.SelfMaterial);
            
            if (f == 1.0F)
            {
                entityjavelin.arrowCritical = true;
            }
            int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, itemstack);
            if (k > 0)
            {
                entityjavelin.setDamage(entityjavelin.getDamage() + (double)k * 0.5D + 0.5D);
            }
            int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, itemstack);
            if (l > 0)
            {
                entityjavelin.setKnockbackStrength(l);
            }
            if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, itemstack) > 0)
            {
                entityjavelin.setFire(100);
            }
            //itemstack.damageItem(1, entityplayer);
            world.playSoundAtEntity(entityplayer, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
            if (!flag)
            {
                entityplayer.inventory.consumeInventoryItem(this.itemID);
            }
            else
            {
            	entityjavelin.canBePickedUp = 2;
            }
            if (!world.isRemote)
            {
                world.spawnEntityInWorld(entityjavelin);
            }
        }
    }

    public ItemStack onFoodEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        return itemstack;
    }

    public int getMaxItemUseDuration(ItemStack itemstack)
    {
        return 0x11940;
    }

    public EnumAction getItemUseAction(ItemStack itemstack)
    {
        return EnumAction.bow;
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (entityplayer.capabilities.isCreativeMode || entityplayer.inventory.hasItem(this.itemID))
        {
            entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
        }
        return itemstack;
    }

    public int getItemEnchantability()
    {
        return 1;
    }
	public boolean isAncient() {
		return isAncient;
	}
	public Item setAncient(boolean isAncient) {
		this.isAncient = isAncient;
		return this;
	}
}
