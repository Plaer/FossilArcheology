package net.fossilsarch.common.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
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
		return (i==1)?cookedIcon:rawIcon;
    }
	
    @Override
    public void registerIcons(IconRegister icon)
    {
    	cookedIcon = icon.registerIcon("fossilsarch:CookedChickenSoup");
    	rawIcon = icon.registerIcon("fossilsarch:RawChickenSoup");
    }
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack)
    {
		if (itemstack.getItemDamage()==1) return "item.CookedChickenSoup";
		else return "item.RawChickenSoup";
    }    

	@Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
		par3List.add(new ItemStack(this.itemID, 1, 0));
		par3List.add(new ItemStack(this.itemID, 1, 1));
    }
}
