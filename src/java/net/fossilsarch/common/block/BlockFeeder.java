// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.fossilsarch.common.block;

import java.util.Random;
// Referenced classes of package net.minecraft.src:
//            BlockContainer, Material, Block, World, 
//            IBlockAccess, TileEntityFurnace, EntityPlayer, TileEntity, 
//            EntityLiving, MathHelper, IInventory, ItemStack, 
//            EntityItem

















import net.fossilsarch.mod_Fossil;
import net.fossilsarch.common.handlers.FossilGuiHandler;
import net.fossilsarch.common.tileentity.TileEntityFeeder;
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

public class BlockFeeder extends BlockContainer 
{
    public Icon feeder_side;
    public Icon feeder_front;
    public Icon feeder_top_on;
    public Icon feeder_top_off;
    
    public BlockFeeder(int i, boolean flag)
    {
        super(i, Material.rock);
        furnaceRand = new Random();
        isActive = flag;
    }
    
    @Override
    public void registerIcons(IconRegister register)
    {
    	this.feeder_front = register.registerIcon("fossilsarch:FeederFront");
    	this.feeder_side = register.registerIcon("fossilsarch:FeederSide");
    	this.feeder_top_on = register.registerIcon("fossilsarch:FeederOn");
    	this.feeder_top_off = register.registerIcon("fossilsarch:FeederOff");
    }

    @Override
    public int idDropped(int i, Random random, int j)
    {
        return mod_Fossil.FeederIdle.blockID;
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
    public Icon getIcon(int side, int metadata)
    {
        if(side == 1 || side==0)
        {
			if(isActive)
			{
				return this.feeder_top_on;
			} else
			{
				return this.feeder_top_off;
			}

        }

        if(side != metadata)
        {
            return this.feeder_side;
        }
		return this.feeder_front;
    }

    @Override
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
        if(!isActive)
        {
            return;
        }
        int l = world.getBlockMetadata(i, j, k);
        float f = (float)i + 0.5F;
        float f1 = (float)j + 0.0F + (random.nextFloat() * 6F) / 16F;
        float f2 = (float)k + 0.5F;
        float f3 = 0.52F;
        float f4 = random.nextFloat() * 0.6F - 0.3F;
        if(l == 4)
        {
            world.spawnParticle("smoke", f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
        } else
        if(l == 5)
        {
            world.spawnParticle("smoke", f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
        } else
        if(l == 2)
        {
            world.spawnParticle("smoke", f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
        } else
        if(l == 3)
        {
            world.spawnParticle("smoke", f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer,int par6, float par7, float par8, float par9)
    {
        if(world.isRemote)
        {
            return true;
        } else
        {
        	mod_Fossil.callGUI(entityplayer,FossilGuiHandler.FEEDER_GUI_ID,world, i, j, k);
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
            world.setBlock(i, j, k, mod_Fossil.FeederActive.blockID);
        } else
        {
            world.setBlock(i, j, k, mod_Fossil.FeederIdle.blockID);
        }
        keepFurnaceInventory = false;
        world.setBlockMetadataWithNotify(i, j, k, l, 3);
        tileentity.validate();
        world.setBlockTileEntity(i, j, k, tileentity);
    }

    public TileEntity createNewTileEntity(World par1World)
    {
        return new TileEntityFeeder();
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
    public void breakBlock(World world, int i, int j, int k, int par5, int par6)
    {
        if(!keepFurnaceInventory)
        {
            TileEntityFeeder tileentityFeeder = (TileEntityFeeder)world.getBlockTileEntity(i, j, k);
label0:
            for(int l = 0; l < tileentityFeeder.getSizeInventory(); l++)
            {
                ItemStack itemstack = tileentityFeeder.getStackInSlot(l);
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
        super.breakBlock(world,i,j,k, par5, par6);
    }

    private Random furnaceRand;
    private final boolean isActive;
    private static boolean keepFurnaceInventory = false;

}
