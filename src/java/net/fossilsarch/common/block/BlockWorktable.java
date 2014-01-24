package net.fossilsarch.common.block;

import java.util.Random;

import net.fossilsarch.mod_Fossil;
import net.fossilsarch.common.handlers.FossilGuiHandler;
import net.fossilsarch.common.tileentity.TileEntityWorktable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWorktable extends BlockContainer
{
	private Random furnaceRand;
    private final boolean isActive;
    private static boolean keepFurnaceInventory = false;
    
    public Icon worktable_top_on ;
    public Icon worktable_top_off;
    public Icon worktable_bottom;
    public Icon worktable_side1;
    public Icon worktable_side2;

	public BlockWorktable(int i, boolean flag)
    {
        super(i, Material.rock);
        furnaceRand = new Random();
        isActive = flag;
    }
	
    @Override
    public void registerIcons(IconRegister register)
    {
    	this.worktable_top_on = register.registerIcon("fossilsarch:WorktableOn");
    	this.worktable_top_off = register.registerIcon("fossilsarch:WorktableOff");
    	this.worktable_bottom = register.registerIcon("fossilsarch:WorktableBottom");
    	this.worktable_side1 = register.registerIcon("fossilsarch:WorktableSide1");
    	this.worktable_side2 = register.registerIcon("fossilsarch:WorktableSide2");
    }

	@Override
	public int idDropped(int i, Random random,int unusedj)
    {
        return mod_Fossil.blockworktableIdle.blockID;
    }

	@Override
	public void onBlockAdded(World world, int i, int j, int k)
    {
        super.onBlockAdded(world, i, j, k);
        setDefaultDirection(world, i, j, k);
    }

	private void setDefaultDirection(World world, int i, int j, int k)
    {
        if(world.isRemote)
        {
            return;
        }
        int l = world.getBlockId(i, j, k - 1);
        int i1 = world.getBlockId(i, j, k + 1);
        int j1 = world.getBlockId(i - 1, j, k);
        int k1 = world.getBlockId(i + 1, j, k);
        byte byte0 = 3;
        if(Block.opaqueCubeLookup[l] && !Block.opaqueCubeLookup[i1])
        {
            byte0 = 3;
        }
        if(Block.opaqueCubeLookup[i1] && !Block.opaqueCubeLookup[l])
        {
            byte0 = 2;
        }
        if(Block.opaqueCubeLookup[j1] && !Block.opaqueCubeLookup[k1])
        {
            byte0 = 5;
        }
        if(Block.opaqueCubeLookup[k1] && !Block.opaqueCubeLookup[j1])
        {
            byte0 = 4;
        }
        world.setBlockMetadataWithNotify(i, j, k, byte0, 3);
    }

	@Override
	public Icon getIcon(int i, int j)
    {
        if(i == 1)
        {
            if (isActive) return this.worktable_top_on;
			else return this.worktable_top_off;

        }
        if(i == 0)
        {
            return this.worktable_bottom;

        }
        if(i == 3 || i == 2)

        {
            return this.worktable_side2;

        } else
        {
            return this.worktable_side1;

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
        	mod_Fossil.callGUI(par5EntityPlayer,FossilGuiHandler.WORKTABLE_GUI_ID,par1World, par2, par3, par4);

            return true;
        }
    }

	public static void updateFurnaceBlockState(boolean flag, World world, int i, int j, int k)
    {
        int l = world.getBlockMetadata(i, j, k);
        TileEntity tileentity = world.getBlockTileEntity(i, j, k);
        keepFurnaceInventory = true;

        if(flag)
        {
            world.setBlock(i, j, k, mod_Fossil.blockworktableActive.blockID);
        } else
        {
            world.setBlock(i, j, k, mod_Fossil.blockworktableIdle.blockID);
        }
        keepFurnaceInventory = false;

        world.setBlockMetadataWithNotify(i, j, k, l, 3);
        tileentity.validate();
        world.setBlockTileEntity(i, j, k, tileentity);
    }

	public TileEntity createNewTileEntity(World par1World)
    {
        return new TileEntityWorktable();

    }

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLivingBase entityliving, ItemStack itemstack)
    {
        int l = MathHelper.floor_double((double)((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        if(l == 0)
        {
            world.setBlockMetadataWithNotify(i, j, k, 2, 3);
        }
        if(l == 1)
        {
            world.setBlockMetadataWithNotify(i, j, k, 5, 3);
        }
        if(l == 2)
        {
            world.setBlockMetadataWithNotify(i, j, k, 3, 3);
        }
        if(l == 3)
        {
            world.setBlockMetadataWithNotify(i, j, k, 4, 3);
        }
    }

	@Override
	public void breakBlock(World world, int i, int j, int k,int par5, int par6)
    {
        if(!keepFurnaceInventory)

        {
            TileEntityWorktable tileentityfurnace = (TileEntityWorktable)world.getBlockTileEntity(i, j, k);
label0:
            for(int l = 0; l < tileentityfurnace.getSizeInventory(); l++)
            {
                ItemStack itemstack = tileentityfurnace.getStackInSlot(l);
                if(itemstack == null)
                {
                    continue;
                }
                float f = furnaceRand.nextFloat() * 0.8F + 0.1F;
                float f1 = furnaceRand.nextFloat() * 0.8F + 0.1F;
                float f2 = furnaceRand.nextFloat() * 0.8F + 0.1F;
                do
                {
                    if(itemstack.stackSize <= 0)
                    {
                        continue label0;
                    }
                    int i1 = furnaceRand.nextInt(21) + 10;
                    if(i1 > itemstack.stackSize)
                    {
                        i1 = itemstack.stackSize;
                    }
                    itemstack.stackSize -= i1;
                    EntityItem entityitem = new EntityItem(world, (float)i + f, (float)j + f1, (float)k + f2, new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage()));
                    float f3 = 0.05F;
                    entityitem.motionX = (float)furnaceRand.nextGaussian() * f3;
                    entityitem.motionY = (float)furnaceRand.nextGaussian() * f3 + 0.2F;
                    entityitem.motionZ = (float)furnaceRand.nextGaussian() * f3;
                    world.spawnEntityInWorld(entityitem);
                } while(true);
            }

        }
        super.breakBlock(world, i, j, k,par5, par6);
    }
}
