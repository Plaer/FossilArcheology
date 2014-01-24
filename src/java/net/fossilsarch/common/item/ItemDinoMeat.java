package net.fossilsarch.common.item;

import java.util.List;

import net.fossilsarch.common.io.EnumDinoType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;



public class ItemDinoMeat extends ForgeItemFood {
	public static final int TypeCount=EnumDinoType.values().length;
	public ItemDinoMeat(int i, int j, float f, boolean flag) {
		super(i, j, f, flag);
		setHasSubtypes(true);
		setMaxDamage(0);
		maxStackSize=64;
	}
	
    @Override
    public String getItemDisplayName(ItemStack par1ItemStack)
    {
    	return StatCollector.translateToLocal("MeatName.Pre")+StatCollector.translateToLocal(getDinosaur(par1ItemStack))+StatCollector.translateToLocal("MeatName.Post");
    }

	public String getDinosaur(ItemStack itemstack)
    {
		if (itemstack.getItemDamage() < EnumDinoType.values().length)
			return GetTypeFromInt(itemstack.getItemDamage()).getDinoName();
		else
			return "entity.unknown.name";
    }
	private EnumDinoType GetTypeFromInt(int data){
		EnumDinoType[] resultArray=EnumDinoType.values();
		return resultArray[data];
	}
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < EnumDinoType.values().length; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }
}
