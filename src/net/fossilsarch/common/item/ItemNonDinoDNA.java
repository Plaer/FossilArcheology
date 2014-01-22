package net.fossilsarch.common.item;

import java.util.List;

import net.fossilsarch.common.io.EnumAnimalType;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;


public class ItemNonDinoDNA extends Item {
	private Icon[] animalDnas = new Icon[TypeCount];
	
	public ItemNonDinoDNA(int i){
		super(i);
		setHasSubtypes(true);
		setMaxDamage(0);
		maxStackSize=64;
	}
	
	@Override
	public Icon getIconFromDamage(int i)
    {
		if (i<TypeCount) return animalDnas[i];
        else return animalDnas[5];
    }
	
    @Override
    public void registerIcons(IconRegister icon)
    {
        for (int var4 = 0; var4 < TypeCount; ++var4)
        {
        	animalDnas[var4] = icon.registerIcon(EnumAnimalType.values()[var4].getDNATexture());
        }
    }
	
    @Override
    public String getItemDisplayName(ItemStack par1ItemStack)
    {
    	return StatCollector.translateToLocal("DNAName.Pre")+StatCollector.translateToLocal(getAnimalName(par1ItemStack))+StatCollector.translateToLocal("DNAName.Post");
    }

	public String getAnimalName(ItemStack itemstack)
    {
		if (itemstack.getItemDamage() >= TypeCount)
			return "entity.unknown.name";
		else
			return GetTypeFromInt(itemstack.getItemDamage()).getAnimalName();
    }
	private EnumAnimalType GetTypeFromInt(int data){
		EnumAnimalType[] resultArray=EnumAnimalType.values();
		return resultArray[data];
	}
	public static final int TypeCount=EnumAnimalType.values().length;
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < TypeCount; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }
}