package net.fossilsarch.common.entity;
import java.util.*;

import net.fossilsarch.mod_Fossil;
import net.fossilsarch.common.io.EnumStoneboard;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
public class EntityStoneboard extends Entity implements IEntityAdditionalSpawnData
{
	private int tickCounter1;
    public int direction;
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public EnumStoneboard art;
	
	public EntityStoneboard(World world)
    {
        super(world);
        tickCounter1 = 0;
        direction = 0;
        yOffset = 0.0F;
        setSize(0.5F, 0.5F);
    }
	public EntityStoneboard(World world, int i, int j, int k, int l)
    {
        this(world);
        xPosition = i;
        yPosition = j;
        zPosition = k;
        ArrayList arraylist = new ArrayList();
        EnumStoneboard aenumart[] = EnumStoneboard.values();
        int i1 = aenumart.length;
        for(int j1 = 0; j1 < i1; j1++)
        {
            EnumStoneboard enumart = aenumart[j1];
            art = enumart;
            setDirection(l);
            if(onValidSurface())
            {
                arraylist.add(enumart);
            }
        }

        if(arraylist.size() > 0)
        {
            art = (EnumStoneboard)arraylist.get(rand.nextInt(arraylist.size()));
        }
        setDirection(l);
    }
    
    @Override
	protected void entityInit()
    {
    }
	
	public void setDirection(int i)
    {
        direction = i;
        prevRotationYaw = rotationYaw = i * 90;
        float f = art.sizeX;
        float f1 = art.sizeY;
        float f2 = art.sizeX;
        if(i == 0 || i == 2)
        {
            f2 = 0.5F;
            this.rotationYaw = this.prevRotationYaw = (float)(Direction.rotateOpposite[i] * 90);
        } else
        {
            f = 0.5F;
        }
        f /= 32F;
        f1 /= 32F;
        f2 /= 32F;
        float f3 = (float)xPosition + 0.5F;
        float f4 = (float)yPosition + 0.5F;
        float f5 = (float)zPosition + 0.5F;
        float f6 = 0.5625F;
        if(i == 2)
        {
            f5 -= f6;
        }
        if(i == 1)
        {
            f3 -= f6;
        }
        if(i == 0)
        {
            f5 += f6;
        }
        if(i == 3)
        {
            f3 += f6;
        }
        if(i == 2)
        {
            f3 -= func_411_c(art.sizeX);
        }
        if(i == 1)
        {
            f5 += func_411_c(art.sizeX);
        }
        if(i == 0)
        {
            f3 += func_411_c(art.sizeX);
        }
        if(i == 3)
        {
            f5 -= func_411_c(art.sizeX);
        }
        f4 += func_411_c(art.sizeY);
        setPosition(f3, f4, f5);
        float f7 = -0.00625F;
        boundingBox.setBounds(f3 - f - f7, f4 - f1 - f7, f5 - f2 - f7, f3 + f + f7, f4 + f1 + f7, f5 + f2 + f7);
    }
	private float func_411_c(int i)
    {
        if(i == 32)
        {
            return 0.5F;
        }
        return i != 64 ? 0.0F : 0.5F;
    }
	
	@Override
	public void onUpdate()
    {
        if(tickCounter1++ == 100 && !worldObj.isRemote)
        {
            tickCounter1 = 0;
            if(!onValidSurface())
            {
                setDead();
                worldObj.spawnEntityInWorld(new EntityItem(worldObj, posX, posY, posZ, new ItemStack(mod_Fossil.stoneboard)));
            }
        }
    }
	public boolean onValidSurface()
    {
        if(worldObj.getCollidingBoundingBoxes(this, boundingBox).size() > 0)
        {
            return false;
        }
        int i = art.sizeX / 16;
        int j = art.sizeY / 16;
        int k = xPosition;
        int l = yPosition;
        int i1 = zPosition;
        if(direction == 0)
        {
            k = MathHelper.floor_double(posX - (double)((float)art.sizeX / 32F));
        }
        if(direction == 1)
        {
            i1 = MathHelper.floor_double(posZ - (double)((float)art.sizeX / 32F));
        }
        if(direction == 2)
        {
            k = MathHelper.floor_double(posX - (double)((float)art.sizeX / 32F));
        }
        if(direction == 3)
        {
            i1 = MathHelper.floor_double(posZ - (double)((float)art.sizeX / 32F));
        }
        l = MathHelper.floor_double(posY - (double)((float)art.sizeY / 32F));
        for(int j1 = 0; j1 < i; j1++)
        {
            for(int k1 = 0; k1 < j; k1++)
            {
                Material material;
                if(direction == 0 || direction == 2)
                {
                    material = worldObj.getBlockMaterial(k + j1, l + k1, zPosition);
                } else
                {
                    material = worldObj.getBlockMaterial(xPosition, l + k1, i1 + j1);
                }
                if(!material.isSolid())
                {
                    return false;
                }
            }

        }

        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox);
        for(int l1 = 0; l1 < list.size(); l1++)
        {
            if(list.get(l1) instanceof EnumStoneboard)
            {
                return false;
            }
        }

        return true;
    }
	
	@Override
	public boolean canBeCollidedWith()
    {
        return true;
    }

	@Override
    public boolean attackEntityFrom(DamageSource damagesource, float i)
    {
        if(!isDead && !worldObj.isRemote)
        {
            setDead();
            setBeenAttacked();
            worldObj.spawnEntityInWorld(new EntityItem(worldObj, posX, posY, posZ, new ItemStack(mod_Fossil.stoneboard)));
        }
        return true;
    }
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setByte("Dir", (byte)direction);
        nbttagcompound.setString("Motive", art.title);
        nbttagcompound.setInteger("TileX", xPosition);
        nbttagcompound.setInteger("TileY", yPosition);
        nbttagcompound.setInteger("TileZ", zPosition);
    }
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        direction = nbttagcompound.getByte("Dir");
        xPosition = nbttagcompound.getInteger("TileX");
        yPosition = nbttagcompound.getInteger("TileY");
        zPosition = nbttagcompound.getInteger("TileZ");
        String s = nbttagcompound.getString("Motive");
        EnumStoneboard aenumart[] = EnumStoneboard.values();
        int i = aenumart.length;
        for(int j = 0; j < i; j++)
        {
            EnumStoneboard enumart = aenumart[j];
            if(enumart.title.equals(s))
            {
                art = enumart;
            }
        }

        if(art == null)
        {
            art = EnumStoneboard.Portol;
        }
        setDirection(direction);
    }
	
	@Override
	public void moveEntity(double d, double d1, double d2)
    {
        if(!worldObj.isRemote && d * d + d1 * d1 + d2 * d2 > 0.0D)
        {
            setDead();
            worldObj.spawnEntityInWorld(new EntityItem(worldObj, posX, posY, posZ, new ItemStack(mod_Fossil.stoneboard)));
        }
    }

	@Override
    public void addVelocity(double d, double d1, double d2)
    {
        if(!worldObj.isRemote && d * d + d1 * d1 + d2 * d2 > 0.0D)
        {
            setDead();
            worldObj.spawnEntityInWorld(new EntityItem(worldObj, posX, posY, posZ, new ItemStack(mod_Fossil.stoneboard)));
        }
    }
	
	@Override
	public void writeSpawnData(ByteArrayDataOutput data) {
		data.writeInt(this.xPosition);
		data.writeInt(this.yPosition);
		data.writeInt(this.zPosition);
		data.writeInt(this.direction);
		data.writeInt(this.art.ordinal());
	}
	
	@Override
	public void readSpawnData(ByteArrayDataInput data) {
		this.xPosition = data.readInt();
		this.yPosition = data.readInt();
		this.zPosition = data.readInt();
		int dir = data.readInt();
		int artIndex = data.readInt();
		this.art=EnumStoneboard.values()[artIndex];
		
        setDirection(dir);
	}
}