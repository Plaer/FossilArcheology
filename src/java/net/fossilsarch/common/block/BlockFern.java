package net.fossilsarch.common.block;
import java.util.Random;

import net.fossilsarch.mod_Fossil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockFern extends BlockFlower
{
	boolean lv2=false;
	private Icon[] FernIcons = new Icon[13];
		    public static int[] FernPics = {38,
        39,
        40,
        41,
        42,
        43,
        26,
        27,
        71, 72, 73, 74, 58};
			
    protected BlockFern(int i)
    {
        super(i, Material.plants);
        setTickRandomly(true);
        float f = 0.5F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
    }
	public BlockFern(int i, boolean flag)
    {
       this(i);
	   lv2=flag;
    }
	
    @Override
    public void registerIcons(IconRegister register)
    {
    	FernIcons[0] = register.registerIcon("fossilsarch:Fern0");
    	FernIcons[1] = register.registerIcon("fossilsarch:Fern1");
    	FernIcons[2] = register.registerIcon("fossilsarch:Fern2");
    	FernIcons[3] = register.registerIcon("fossilsarch:Fern3");
    	FernIcons[4] = register.registerIcon("fossilsarch:Fern4");
    	FernIcons[5] = register.registerIcon("fossilsarch:Fern5");
    	FernIcons[6] = register.registerIcon("fossilsarch:TopFern0");
    	FernIcons[7] = register.registerIcon("fossilsarch:TopFern1");
    	FernIcons[8] = register.registerIcon("fossilsarch:TopFern2");
    	FernIcons[9] = register.registerIcon("fossilsarch:TopFern3");
    	FernIcons[10] = register.registerIcon("fossilsarch:TopFern4");
    	FernIcons[11] = register.registerIcon("fossilsarch:TopFern5");
    	FernIcons[12] = register.registerIcon("fossilsarch:TopFern6");
    }

	@Override
    protected boolean canThisPlantGrowOnThisBlockID(int i)
    {
        return i == Block.grass.blockID;
    }
	@Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        super.updateTick(world, i, j, k, random);
		int l = world.getBlockMetadata(i, j, k);
        if(CheckUnderTree(world,i,j,k))
        {

            if((this.CheckSubType(l)==0 && l<5)||
            	(this.CheckSubType(l)==1 && l%7<3))
            {
                //float f = getGrowthRate(world, i, j, k);
                if(random.nextInt(10)>1 )
                {
                    l++;
					if (!lv2 && l%7>3){
						if (world.isAirBlock(i, j+1, k)){
							world.setBlock(i, j+1, k, mod_Fossil.FernUpper.blockID);
							world.setBlockMetadataWithNotify(i, j+1, k, 5+7*this.CheckSubType(l), 3);							
						}else
						{
							if ((world.getBlockId(i, j+1, k) != mod_Fossil.FernUpper.blockID) && (world.getBlockId(i, j-1, k) != mod_Fossil.Ferns.blockID)) l--;
						}

					}
					if (l==1 && new Random().nextInt(10)<5) l+=7;					
                    world.setBlockMetadataWithNotify(i, j, k, l, 3);
                }else{
                	if (!lv2 && random.nextInt(100)<5) this.breakBlock(world, i, j, k, blockID, l);
                }
            }
        }
		//Expend
		int i1 = world.getBlockId(i, j, k);
			if (l%7>=3){
				for (int xShift=-1;xShift<=1;xShift++){
					for(int yShift=-1;yShift<=1;yShift++){
						for (int zShift=-1;zShift<=1;zShift++){
							if (xShift!=0 || zShift!=0 || yShift!=0){
								if(world.getBlockId(i+xShift, yShift+j, k+zShift) != this.blockID && world.getBlockId(i+xShift, yShift+j-1, k+zShift) == Block.grass.blockID && (world.isAirBlock(i+xShift, yShift+j, k+zShift)||world.getBlockId(i+xShift, yShift+j, k+zShift) == Block.tallGrass.blockID) && BlockFern.CheckUnderTree(world,i+xShift,yShift+j,k+zShift) && (new Random().nextInt(10)<=5))
								{
									world.setBlock(i+xShift, yShift+j, k+zShift, this.blockID);
									world.setBlockMetadataWithNotify(i+xShift, yShift+j, k+zShift, 0+7*(new Random().nextInt(1)), 3);
								}
							}
						}
					}
				}
			}
    }

    public void fertilize(World world, int i, int j, int k)
    {
        world.setBlockMetadataWithNotify(i, j, k, 5+7*(new Random().nextInt(1)), 3);
    }

    private float getGrowthRate(World world, int i, int j, int k)
    {
        float f = 1.0F;
        int l = world.getBlockId(i, j, k - 1);
        int i1 = world.getBlockId(i, j, k + 1);
        int j1 = world.getBlockId(i - 1, j, k);
        int k1 = world.getBlockId(i + 1, j, k);
        int l1 = world.getBlockId(i - 1, j, k - 1);
        int i2 = world.getBlockId(i + 1, j, k - 1);
        int j2 = world.getBlockId(i + 1, j, k + 1);
        int k2 = world.getBlockId(i - 1, j, k + 1);
        boolean flag = j1 == blockID || k1 == blockID;
        boolean flag1 = l == blockID || i1 == blockID;
        boolean flag2 = l1 == blockID || i2 == blockID || j2 == blockID || k2 == blockID;
        for(int l2 = i - 1; l2 <= i + 1; l2++)
        {
            for(int i3 = k - 1; i3 <= k + 1; i3++)
            {
                int j3 = world.getBlockId(l2, j - 1, i3);
                float f1 = 0.0F;
                if(j3 == Block.grass.blockID)
                {
                    f1 = 1.0F;
                    if(world.getBlockMetadata(l2, j - 1, i3) > 0)
                    {
                        f1 = 3F;
                    }
                }
                if(l2 != i || i3 != k)
                {
                    f1 /= 4F;
                }
                f += f1;
            }

        }

        if(flag2 || flag && flag1)
        {
            f /= 2.0F;
        }
        return f;
    }

    public Icon getIcon(int i, int j)
    {
        if(j < 0)
        {
			j = 0;
        }
        if (j%7==0) return this.FernIcons[0];
        else{
        		if (!lv2) {
        			return this.FernIcons[j];
        		}
        		else return this.FernIcons[j+2];
        }

    }

    @Override
    public int getRenderType()
    {
        return 6;
    }

    @Override
    public int idDropped(int i, Random random,int unused)
    {
            return -1;
    }

    @Override
    public int quantityDropped(Random random)
    {
        return 1;
    }
    
	public static boolean CheckUnderTree(World wld,int x,int y,int z){
		for (int i=y;i<=128;i++){
			if (wld.getBlockId(x, i, z)==Block.leaves.blockID) return true;
		}
		return false;
	}
	
	@Override
	public boolean canBlockStay(World world, int i, int j, int k)
    {
		boolean res=true;
		if (lv2) return (world.getBlockId(i, j-1, k)==mod_Fossil.Ferns.blockID);
        else{
			int l=world.getBlockMetadata(i, j, k);
			res=(world.getBlockId(i, j-1, k) == Block.grass.blockID && BlockFern.CheckUnderTree(world,i,j,k));
			if (l%7>3) res &= (world.getBlockId(i, j+1, k) == mod_Fossil.FernUpper.blockID);
			return res;
		}
    }
	
	public int CheckSubType(int metaData){
		if (metaData>7) return 1;
		else return 0;
	}
}