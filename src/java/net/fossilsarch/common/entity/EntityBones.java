package net.fossilsarch.common.entity;

import java.util.Random;

import net.fossilsarch.mod_Fossil;
import net.fossilsarch.common.ai.EntityAIDeadBones;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityBones extends EntityMob
{
    private static final ItemStack defaultHeldItem;

    public EntityBones(World world)
    {
        super(world);
        this.tasks.addTask(1, new EntityAIDeadBones(this));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(20.0D);
    }

    @Override
    public boolean isAIEnabled()
    {
        return true;
    }
    
    @Override
    protected String getHurtSound()
    {
        return "mob.skeleton.hurt";
    }

    @Override
    protected String getDeathSound()
    {
        return "mob.skeleton.hurt";
    }

    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float i)
    {
        boolean result=super.attackEntityFrom(damagesource, i);
        entityToAttack=null;
        return result;
    }

    @Override
    public boolean canBreatheUnderwater()
    {
        return true;
    }
    
    @Override
    public void onLivingUpdate()
    {
        if (worldObj.isDaytime() && !worldObj.isRemote)
        {
            float f = getBrightness(1.0F);
            if (f > 0.5F && worldObj.canBlockSeeTheSky(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ)) && rand.nextFloat() * 30F < (f - 0.4F) * 2.0F)
            {
                setFire(8);
            }
        }
        super.onLivingUpdate();
    }
    
    @Override
    protected void updateEntityActionState()
    {
    	return;
    }
    
    @Override
    protected boolean canDespawn()
    {
        return false;
    }
    
    @Override
    protected void jump()
    {
    	return;
    }
    
    @Override
    protected Entity findPlayerToAttack()
    {
    	return null;
    }
    
    @Override
    protected void attackEntity(Entity entity, float f)
    {
    	return;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
    }

    @Override
    protected int getDropItemId()
    {
        return mod_Fossil.blockSkull.blockID;
    }

    @Override
    protected void dropFewItems(boolean flag, int i)
    {
        int j = 1;
        for (int k = 0; k < j; k++)
        {
            dropItem(mod_Fossil.blockSkull.blockID, 1);
        }

        j = rand.nextInt(3 + i);
        for (int l = 0; l < j; l++)
        {
            dropItem(Item.bone.itemID, 1);
        }
    }

    @Override
    public ItemStack getHeldItem()
    {
        return defaultHeldItem;
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }

    static
    {
        defaultHeldItem = null;
    }
}
