package net.fossilsarch.common.block;
import java.util.Random;

import net.fossilsarch.mod_Fossil;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
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
	
    @Override
    public void registerIcons(IconRegister register)
    {
    	skull_face_on = register.registerIcon("fossilsarch:SkullLantern");
    	skull_face_off = register.registerIcon("fossilsarch:Skull");
    	skull_top = register.registerIcon("fossilsarch:SkullTop");
    	skull_side = register.registerIcon("fossilsarch:SkullSide");
    }
    
	@Override
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

	@Override
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLivingBase entityliving, ItemStack par6ItemStack)
    {
        int l = MathHelper.floor_double((double)((entityliving.rotationYaw * 4F) / 360F) + 2.5D) & 3;
        world.setBlockMetadataWithNotify(i, j, k, l, 3);
    }
}