package net.fossilsarch.common.block;
//all java files for minecraft should have this.

import java.util.Random;
//because we are using random values below.




import net.fossilsarch.mod_Fossil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.item.Item;

public class BlockFossil extends BlockStone{
//extend Blockstone makes this block acts like a stone.that make us only need to write the functions which is different with stone.
	public BlockFossil(int i){
		super(i);
	}

	public int idDropped(int i, Random random,int j)
	//when calling this function independently,it can drop 1 item with returned ID value.
    {
		int chance=(int)(new Random().nextInt(20000));
			if (chance >=20 && chance<=30) return mod_Fossil.Gen.itemID;
			if (chance <=4500) return mod_Fossil.biofossil.itemID;
			if (chance>4500 && chance<=9800) return mod_Fossil.relic.itemID;		
			if (chance>9800 && chance<=17800) return Item.bone.itemID;
			//items using shiftedIndex for ID.
			if (chance>17800 && chance<=19800) return mod_Fossil.blockSkull.blockID;
			//while blocks using blockID for ID.
			if (chance>19800 && chance<=19900) return mod_Fossil.BrokenSword.itemID;
			if (chance>19900 && chance<=20100) return mod_Fossil.Brokenhelmet.itemID;				
        return Block.cobblestone.blockID;
    }

}