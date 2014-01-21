package net.fossilsarch.common.item;

import java.util.List;

import net.fossilsarch.common.io.EnumDinoType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;



public class ItemDNA extends Item {
	
	private Icon genericDna;
	private Icon dinoDna;
	
	public ItemDNA(int i){
		super(i);
		setHasSubtypes(true);
		setMaxDamage(0);
		maxStackSize=64;
	}
	public Icon getIconFromDamage(int i)
    {
		if (i<TypeCount) return dinoDna;
        else return genericDna;
    }
    public String getTextureFile()
    {
       return "/skull/Fos_items.png";
    }
	public String getItemNameIS(ItemStack itemstack)
    {
			switch(GetTypeFromInt(itemstack.getItemDamage())){
			case Triceratops:
				return "DNAtriceratops";
			case Raptor:
				return "DNARaptor";
			case TRex:
				return "DNATRex";
			case Pterosaur:
				return "DNAPterosaur";
			case Nautilus:
				return "DNANautilus";
			case Plesiosaur:
				return "DNAPlesiosaur";
			case Mosasaurus:
				return "DNAMosasaurus";
			case Stegosaurus:
				return "DNAStegosaurus";
			case dilphosaur:
				return "DNAUtahraptor";
			case Brachiosaurus:
				return "DNABrachiosaurus";
			default:
				return "DNA";
		}
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