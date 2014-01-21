// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.fossilsarch.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.Icon;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;



// Referenced classes of package net.minecraft.src:
//            Block, Material

public class BlockIcedStone extends Block
{
	private Icon icedStone1;
	private Icon icedStone2;
	
    public BlockIcedStone(int i)
    {
        super(i, Material.rock);
        //setTickOnLoad(true);
    }
    public String getTextureFile()
    {
       return "/skull/Fos_terrian.png";
    }
    public int idDropped(int i, Random random, int j)
    {
        return Block.cobblestone.blockID;
    }
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        if((world.getSavedLightValue(EnumSkyBlock.Block, i, j, k) > 11 - Block.lightOpacity[blockID])||
        	(world.canBlockSeeTheSky(i, j+1, k)&&world.isDaytime() /*&& !(world.worldProvider.worldChunkMgr.getBiomeGenAt(i, k) instanceof BiomeGenSnow)*/))
        {
            world.setBlock(i, j, k, Block.stone.blockID);
            return;
        }
    	for (int i2=0;i2<20;i2++){
	        int targetX=new Random().nextInt(3)-1;
	        int targetY=new Random().nextInt(3)-1;
	        int targetZ=new Random().nextInt(3)-1;
	        if ((world.getBlockId(i+targetX, j+targetY, k+targetZ)==Block.waterMoving.blockID)||
	        	(world.getBlockId(i+targetX, j+targetY, k+targetZ)==Block.waterStill.blockID)
	        		){
	        	world.setBlock(i+targetX, j+targetY, k+targetZ, Block.ice.blockID);
	        	return;
	        }
	        if ((world.getBlockId(i+targetX, j+targetY, k+targetZ)==Block.lavaMoving.blockID)||
		        	(world.getBlockId(i+targetX, j+targetY, k+targetZ)==Block.lavaStill.blockID)||
		        	(world.getBlockId(i+targetX, j+targetY, k+targetZ)==Block.fire.blockID)
		        		){
	        		world.setBlock(i, j, k, Block.stone.blockID);
		        	return;
		        }
        }
        
    }
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        if((world.getSavedLightValue(EnumSkyBlock.Block, i, j, k) > 11 - Block.lightOpacity[blockID])||
            	(world.canBlockSeeTheSky(i, j+1, k)&&world.isDaytime() /*&& !(world.worldProvider.worldChunkMgr.getBiomeGenAt(i, k) instanceof BiomeGenSnow)*/))
            {
                world.setBlock(i, j, k, Block.stone.blockID, 0, 2);
                return;
            }
    	int targetX;
        int targetY;
        int targetZ;
    	for (targetX=-1;targetX<=1;targetX++){
        	for (targetY=-1;targetY<=1;targetY++){
            	for (targetZ=-1;targetZ<=1;targetZ++){
			        if ((world.getBlockId(i+targetX, j+targetY, k+targetZ)==Block.waterMoving.blockID)||
			        	(world.getBlockId(i+targetX, j+targetY, k+targetZ)==Block.waterStill.blockID)
			        		){
			        	world.setBlock(i+targetX, j+targetY, k+targetZ, Block.ice.blockID);
			        }
			        if ((world.getBlockId(i+targetX, j+targetY, k+targetZ)==Block.lavaMoving.blockID)||
				        	(world.getBlockId(i+targetX, j+targetY, k+targetZ)==Block.lavaStill.blockID)||
				        	(world.getBlockId(i+targetX, j+targetY, k+targetZ)==Block.fire.blockID)
				        		){
			        		world.setBlock(i, j, k, Block.stone.blockID);
				        	return;
				        }
            	}
        	}
        }
    }
    
    public Icon getIcon(int i, int j)
    {
        if(j == 1)
        {
            return icedStone1;
        } else
        {
            return icedStone2;
        }
    }
}
