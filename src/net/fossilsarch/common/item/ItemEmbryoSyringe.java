package net.fossilsarch.common.item;

import java.util.List;

import net.fossilsarch.common.dinos.IViviparous;
import net.fossilsarch.common.entity.EntityPregnantCow;
import net.fossilsarch.common.entity.EntityPregnantPig;
import net.fossilsarch.common.entity.EntityPregnantSheep;
import net.fossilsarch.common.io.EnumEmbyos;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;



public class ItemEmbryoSyringe extends ForgeItem {
	Icon[] embryoTypes = new Icon[5];
	
	public ItemEmbryoSyringe(int i) {
		super(i);
		setHasSubtypes(true);
		setMaxDamage(0);
		maxStackSize=64;
	}

	@Override
	public Icon getIconFromDamage(int i)
    {
		return embryoTypes[i];
    }
	
    @Override
    public void registerIcons(IconRegister icon)
    {
    	for (int i = 0; i < EnumEmbyos.values().length; i++) {
    		embryoTypes[i] = icon.registerIcon(EnumEmbyos.values()[i].getTexture());
    	}
    }
	
    @Override
    public String getItemDisplayName(ItemStack par1ItemStack)
    {
    	return StatCollector.translateToLocal("EmbryoName.Pre")+StatCollector.translateToLocal(getAnimalName(par1ItemStack.getItemDamage()))+StatCollector.translateToLocal("EmbryoName.Post");
    }
	
	public String getAnimalName(int damage)
    {
			if (damage<EnumEmbyos.values().length) return GetEmbyo(damage).getAnimalName();
			return "entity.unknown.name";
    }
	public static EnumEmbyos GetEmbyo(int index){
		return EnumEmbyos.values()[index];
	}
    public boolean itemInteractionForEntity(ItemStack itemstack, EntityLiving entityliving)
    {
        if(entityliving instanceof EntityAnimal && ((EntityAnimal)entityliving).getGrowingAge()==0)
        {
        	IViviparous entityanimal1=null;
        	if (entityliving instanceof EntityPig) entityanimal1=new EntityPregnantPig(entityliving.worldObj);
        	if (entityliving instanceof EntityCow) entityanimal1=new EntityPregnantCow(entityliving.worldObj);
        	if (entityliving instanceof EntitySheep){
        		entityanimal1=new EntityPregnantSheep(entityliving.worldObj);
        		((EntitySheep)entityanimal1).setFleeceColor(((EntitySheep)entityliving).getFleeceColor());
        		((EntitySheep)entityanimal1).setSheared(((EntitySheep)entityliving).getSheared());
        	}
        	if (entityanimal1!=null){
        		entityanimal1.SetEmbyo(GetEmbyo(itemstack.getItemDamage()));
        		((EntityAnimal)entityanimal1).setLocationAndAngles(entityliving.posX, entityliving.posY, entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
        		entityliving.setDead();
        		if (!entityliving.worldObj.isRemote)entityliving.worldObj.spawnEntityInWorld(((EntityAnimal)entityanimal1));
        		itemstack.stackSize--;
        	}
        	return true;
        }else return false;
    }
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < EnumEmbyos.values().length; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }
}
