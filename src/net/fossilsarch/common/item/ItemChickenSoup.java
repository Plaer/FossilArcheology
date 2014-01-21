package net.fossilsarch.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class ItemChickenSoup extends ForgeItem {
	public static final int BASEICONLOC=58;
	public static final Item CONTAINER=Item.bucketEmpty;
	
	private Icon cookedIcon;
	private Icon rawIcon;
	
	public ItemChickenSoup(int i) {
		super(i);
		setHasSubtypes(true);
		setMaxDamage(0);
		maxStackSize=1;
	}
	
	@Override
	public Icon getIconFromDamage(int i)
    {
		return (i==0)?cookedIcon:rawIcon;
    }
	public String getItemNameIS(ItemStack itemstack)
    {
		if (itemstack.getItemDamage()==1) return "CookedChickenSoup";
		else return "RawChickenSoup";
    }    

}
