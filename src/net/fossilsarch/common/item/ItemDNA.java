package net.fossilsarch.common.item;

import java.util.List;

import net.fossilsarch.common.io.EnumDinoType;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;



public class ItemDNA extends Item {
	
	private Icon[] dinoDna = new Icon[TypeCount];
	
	public ItemDNA(int i){
		super(i);
		setHasSubtypes(true);
		setMaxDamage(0);
		maxStackSize=64;
	}
	
	@Override
	public Icon getIconFromDamage(int i)
    {
		if (i<TypeCount) return dinoDna[i];
        else return dinoDna[2];
    }
	
    @Override
    public void registerIcons(IconRegister icon)
    {
        for (int var4 = 0; var4 < EnumDinoType.values().length; ++var4)
        {
        	dinoDna[var4] = icon.registerIcon(EnumDinoType.values()[var4].getDNATexture());
        }
    }

    @Override
    public String getItemDisplayName(ItemStack par1ItemStack)
    {
    	return StatCollector.translateToLocal("DNAName.Pre")+StatCollector.translateToLocal(getDinosaur(par1ItemStack))+StatCollector.translateToLocal("DNAName.Post");
    }

	public String getDinosaur(ItemStack itemstack)
    {
		if (itemstack.getItemDamage() < TypeCount)
			return GetTypeFromInt(itemstack.getItemDamage()).getDinoName();
		else
			return "entity.unknown.name";
    }
	
	private EnumDinoType GetTypeFromInt(int data){
		EnumDinoType[] resultArray=EnumDinoType.values();
		return resultArray[data];
	}
	public static final int TypeCount=EnumDinoType.values().length;
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < EnumDinoType.values().length; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }
}