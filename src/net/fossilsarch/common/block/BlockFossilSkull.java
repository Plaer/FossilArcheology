package net.fossilsarch.common.block;
import java.util.Random;

import net.fossilsarch.mod_Fossil;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
public class BlockFossilSkull extends BlockDirectional{
    private boolean blockType;
    
    public Icon skull_side;
    public Icon skull_top;
    public Icon skull_face_off;
    public Icon skull_face_on;
    
	public BlockFossilSkull(int i, int j, boolean flag){
		super(i, Material.pumpkin);
        setTickRandomly(true);
        blockType = flag;
	}
    public String getTextureFile()
    {
       return "/skull/Fos_terrian.png";
    }
    
	public Icon getIcon(int i, int j)
    {
        if(i == 1)
        {
            return this.skull_side;
        }
        if(i == 0)
        {
            return this.skull_side;
        }
        Icon k = this.skull_face_off;
        if(blockType)
        {
            k=this.skull_face_on;
        }
        if(j == 2 && i == 2)
        {
            return k;
        }
        if(j == 3 && i == 5)
        {
            return k;
        }
        if(j == 0 && i == 3)
        {
            return k;
        }
        if(j == 1 && i == 4)
        {
            return k;
        } else
        {
            return this.skull_top;
        }
    }

	public void onBlockAdded(World world, int i, int j, int k)
    {
        super.onBlockAdded(world, i, j, k);
    }

    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving)
    {
        int l = MathHelper.floor_double((double)((entityliving.rotationYaw * 4F) / 360F) + 2.5D) & 3;
        world.setBlockMetadataWithNotify(i, j, k, l, 3);
    }
	/*    public int idDropped(int i, Random random)
    {
        if (!blockType) return mod_Fossil.blockSkull.blockID;
		else return mod_Fossil.SkullLantern.blockID;
    }*/
}