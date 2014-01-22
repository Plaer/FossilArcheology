// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.fossilsarch.common.block;

import net.fossilsarch.mod_Fossil;
import net.fossilsarch.common.tileentity.TileEntityDrum;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

// Referenced classes of package net.minecraft.src:
//            BlockContainer, Material, Block, World, 
//            TileEntityNote, EntityPlayer, TileEntity

public class BlockDrum extends BlockContainer
{
	private Icon followDrumSymbol;
	private Icon freeMoveDrumSymbol;
	private Icon stayDrumSymbol;
	private Icon drumSide;
	
    public BlockDrum(int i)
    {
        super(i, Material.wood);
    }
    
    @Override
    public void registerIcons(IconRegister register)
    {
    	this.followDrumSymbol = register.registerIcon("fossilsarch:DrumFollow");
    	this.freeMoveDrumSymbol = register.registerIcon("fossilsarch:DrumFreeMove");
    	this.stayDrumSymbol = register.registerIcon("fossilsarch:DrumStay");
    	this.drumSide = register.registerIcon("fossilsarch:DrumSide");
    }

    @Override
	public Icon getIcon(int sidePar,int metaPar)
    {
        if(sidePar == 1 || sidePar == 0)
        {
        	switch(metaPar){
        	case 1:
        		return followDrumSymbol;
        	case 2:
        		return freeMoveDrumSymbol;
        	case 0:
        	default:
                return stayDrumSymbol;
        	}
        }
        return drumSide;
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        if(l > 0 && Block.blocksList[l].canProvidePower())
        {
            boolean flag = world.isBlockIndirectlyGettingPowered(i, j, k);
            TileEntityDrum tileentityDump = (TileEntityDrum)world.getBlockTileEntity(i, j, k);
            if(tileentityDump.previousRedstoneState != flag)
            {
                if(flag)
                {
                    tileentityDump.triggerNote(world, i, j, k);
                }
                tileentityDump.previousRedstoneState = flag;
            }
        }
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if(par1World.isRemote)
        {
            return true;
        } else
        {
            TileEntityDrum tileentityDump = (TileEntityDrum)par1World.getBlockTileEntity(par2, par3, par4);
			tileentityDump.TriggerOrder(par5EntityPlayer);
			int orderMeta=tileentityDump.Order.ordinal();
			par1World.setBlockMetadataWithNotify(par2, par3, par4,orderMeta, 3);
            return true;
        }
    }

    @Override
    public void onBlockClicked(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isRemote)
        {
            return;
        } else
        {
            TileEntityDrum tileentityDump = (TileEntityDrum)world.getBlockTileEntity(i, j, k);
			if (entityplayer.inventory.getCurrentItem()!=null)tileentityDump.SendOrder(entityplayer.inventory.getCurrentItem().itemID,entityplayer);
            return;
        }
    }

    public TileEntity createNewTileEntity(World par1World)
    {
        return new TileEntityDrum();
    }
}
