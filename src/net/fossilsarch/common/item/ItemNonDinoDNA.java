package net.fossilsarch.common.item;

import java.util.List;

import net.fossilsarch.common.io.EnumAnimalType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;


public class ItemNonDinoDNA extends Item {
	private Icon[] animalDnas = new Icon[6];
	private Icon otherDna;
	
	public ItemNonDinoDNA(int i){
		super(i);
		setHasSubtypes(true);
		setMaxDamage(0);
		maxStackSize=64;
	}
	public Icon getIconFromDamage(int i)
    {
		if (i<TypeCount) return animalDnas[i];
        else return otherDna;
    }
    public String getTextureFile()
    {
       return "/skull/Fos_items.png";
    }
	public String getItemNameIS(ItemStack itemstack)
    {
		switch(GetTypeFromInt(itemstack.getItemDamage())){
			case Pig:
				return "DNAPig";
			case Sheep:
				return "DNASheep";
			case Cow:
				return "DNATCow";
			case Chicken:
				return "DNAChicken";
			case SaberCat:
				return "DNASaberCat";
			default:
				return "AnimalDNA";
		}
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