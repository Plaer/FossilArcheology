package net.fossilsarch.common.item;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.fossilsarch.common.entity.EntityDinoEgg;
import net.fossilsarch.common.entity.EntityNautilus;
import net.fossilsarch.common.io.EnumDinoType;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;


public class ItemAncientEgg extends Item{

	private Icon[] dinoEgg = new Icon[ItemAncientEgg.TypeCount];
	
	public ItemAncientEgg(int i){
		super(i);
		setHasSubtypes(true);
		setMaxDamage(0);
		maxStackSize=1;
	}

	@Override
	public Icon getIconFromDamage(int i)
    {
        if (i<TypeCount) return dinoEgg[i];
        else return dinoEgg[2];	//Fall back on an awesome t-rex egg!
    }
	
    @Override
    public void registerIcons(IconRegister icon)
    {
        for (int var4 = 0; var4 < EnumDinoType.values().length; ++var4)
        {
            dinoEgg[var4] = icon.registerIcon(EnumDinoType.values()[var4].getEggTexture());
        }
    }
	
    @Override
    public String getItemDisplayName(ItemStack par1ItemStack)
    {
    	return StatCollector.translateToLocal("EggName.Pre")+StatCollector.translateToLocal(GetTypeFromInt(par1ItemStack.getItemDamage()).getDinoName())+StatCollector.translateToLocal("EggName.Post");
    }

	public ItemStack onItemRightClick(ItemStack itemstack, World world,EntityPlayer entityplayer )
    {
        float var4 = 1.0F;
        float var5 = entityplayer.prevRotationPitch + (entityplayer.rotationPitch - entityplayer.prevRotationPitch) * var4;
        float var6 = entityplayer.prevRotationYaw + (entityplayer.rotationYaw - entityplayer.prevRotationYaw) * var4;
        double var7 = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * (double)var4;
        double var9 = entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * (double)var4 + 1.62D - (double)entityplayer.yOffset;
        double var11 = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * (double)var4;
        Vec3 var13 = world.getWorldVec3Pool().getVecFromPool(var7, var9, var11);
        float var14 = MathHelper.cos(-var6 * 0.017453292F - (float)Math.PI);
        float var15 = MathHelper.sin(-var6 * 0.017453292F - (float)Math.PI);
        float var16 = -MathHelper.cos(-var5 * 0.017453292F);
        float var17 = MathHelper.sin(-var5 * 0.017453292F);
        float var18 = var15 * var16;
        float var20 = var14 * var16;
        double var21 = 5.0D;
        Vec3 var23 = var13.addVector((double)var18 * var21, (double)var17 * var21, (double)var20 * var21);
        MovingObjectPosition var24 = world.rayTraceBlocks_do_do(var13, var23, true, true);

        if (var24 == null)
        {
            return itemstack;
        }
        else
        {
            Vec3 var25 = entityplayer.getLook(var4);
            boolean var26 = false;
            float var27 = 1.0F;
            List var28 = world.getEntitiesWithinAABBExcludingEntity(entityplayer, entityplayer.boundingBox.addCoord(var25.xCoord * var21, var25.yCoord * var21, var25.zCoord * var21).expand((double)var27, (double)var27, (double)var27));
            Iterator var29 = var28.iterator();

            while (var29.hasNext())
            {
                Entity var30 = (Entity)var29.next();

                if (var30.canBeCollidedWith())
                {
                    float var31 = var30.getCollisionBorderSize();
                    AxisAlignedBB var32 = var30.boundingBox.expand((double)var31, (double)var31, (double)var31);

                    if (var32.isVecInside(var13))
                    {
                        var26 = true;
                    }
                }
            }

            if (var26)
            {
                return itemstack;
            }
            else
            {
                if (var24.typeOfHit == EnumMovingObjectType.TILE)
                {
                    int var35 = var24.blockX;
                    int var33 = var24.blockY;
                    int var34 = var24.blockZ;

                    if (!world.isRemote)
                    {
                        if (world.getBlockId(var35, var33, var34) == Block.snow.blockID)
                        {
                            --var33;
                        }

                        if (!spawnCreature(world, this.GetTypeFromInt(itemstack.getItemDamage()), (double)((float)var35 + 0.5F), (double)((float)var33 + 1.0F), (double)((float)var34 + 0.5F))) return itemstack;
                    }

                    if (!entityplayer.capabilities.isCreativeMode)
                    {
                        --itemstack.stackSize;
                    }
                }

                return itemstack;
            }
        }


	}

	    /**
	     * Spawns the creature specified by the egg's type in the location specified by the last three parameters.
	     * Parameters: world, entityID, x, y, z.
	     */
	    public static boolean spawnCreature(World par0World, EnumDinoType enumDinoType, double par2, double par4, double par6)
	    {
	            Entity var8;
	            if (enumDinoType==EnumDinoType.Nautilus) var8=new EntityNautilus(par0World);
	            else var8 = new EntityDinoEgg(par0World,enumDinoType);
	            
	            if (var8 != null)
	            {
	                var8.setLocationAndAngles(par2, par4, par6, par0World.rand.nextFloat() * 360.0F, 0.0F);

	                par0World.spawnEntityInWorld(var8);
	            }

	            return var8 != null;
	        
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