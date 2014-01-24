// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.fossilsarch.common.entity;

import java.util.*;

import net.fossilsarch.mod_Fossil;
import net.fossilsarch.common.PigmenSpeaker;
import net.fossilsarch.common.ai.FPZAIFollowOwner;
import net.fossilsarch.common.ai.FPZAIOwnerHurtByTarget;
import net.fossilsarch.common.ai.FPZAIOwnerHurtTarget;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;

// Referenced classes of package net.minecraft.src:
//            EntityZombie, World, NBTTagCompound, EntityPlayer, 
//            AxisAlignedBB, Entity, Item, ItemStack

public class EntityFriendlyPigZombie extends EntityMob
{
    private int randomSoundDelay;
    private static final ItemStack defaultHeldItem=new ItemStack(Item.swordGold, 1);
	public String LeaderName;
	public EntityPlayer Leader=null;
	private boolean dying=false;
	public PigmenSpeaker Mouth=new PigmenSpeaker(this);
	public boolean maskSpeech=true;
	public EntityFriendlyPigZombie(World world){
		super(world);
        randomSoundDelay = 0;
        //attackStrength = 5;
        isImmuneToFire = true;
		LeaderName="Notch";
        this.getNavigator().setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityLiving.class, 2F, false));
        this.tasks.addTask(5, new FPZAIFollowOwner(this, 1.5F, 10.0F, 2.0F));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new FPZAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new FPZAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
		
	}
	
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.23D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(20.0D);
    }
	
	protected boolean isAIEnabled()
    {
        return true;
    }
	  
    public void onUpdate()
    {
    	this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(entityToAttack == null ? 0.5D : 0.95D);
    	
        if(randomSoundDelay > 0 && --randomSoundDelay == 0)
        {
            worldObj.playSoundAtEntity(this, "mob.zombiepig.zpigangry", getSoundVolume() * 2.0F, ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) * 1.8F);
        }
        super.onUpdate();
    }

    public boolean getCanSpawnHere()
    {
        return worldObj.checkNoEntityCollision(boundingBox) && worldObj.getCollidingBoundingBoxes(this, boundingBox).size() == 0 && !worldObj.isAnyLiquid(boundingBox);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setString("LeaderName", LeaderName);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
		LeaderName=nbttagcompound.getString("LeaderName");
		if (this.isTamed()) this.Leader=updateLeader();
		maskSpeech=false;
    }

    /*protected Entity findPlayerToAttack()
    {
        if(angerLevel == 0)
        {
            return null;
        } else
        {
            return super.findPlayerToAttack();
        }
    }*/

    public void onLivingUpdate()
    {
    	if (this.dying) this.sucrife(60);
    	if (this.isTamed() && FMLCommonHandler.instance().getSide().isClient()){
    		this.Leader=updateLeader();
    		if (this.Leader==null) this.dying=true;
    	}
    	
		super.onLivingUpdate();
    }


    protected String getLivingSound()
    {
        return "mob.zombiepig.zpig";
    }

    protected String getHurtSound()
    {
        return "mob.zombiepig.zpighurt";
    }

    protected String getDeathSound()
    {
        return "mob.zombiepig.zpigdeath";
    }

    protected int getDropItemId()
    {
        return Item.porkCooked.itemID;
    }

    public ItemStack getHeldItem()
    {
        return defaultHeldItem;
    }
	private void sucrife(int range){
		/*List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand((double)range, (double)range, (double)range));
            for(int j = 0; j < list.size(); j++)
            {
                Entity entity1 = (Entity)list.get(j);
                if(entity1!=null && entity1 instanceof EntityFriendlyPigZombie && entity1!=this)
                {
                	((EntityFriendlyPigZombie)entity1).maskSpeech=true;
                }
            }
		if (!dying){
			Mouth.SendSpeech(EnumPigmenSpeaks.SelfKill);
			dying=true;
		}*/
		this.attackEntityFrom(DamageSource.generic,20);
		
	}

    public void onStruckByLightning(EntityLightningBolt entitylightningbolt)
    {
    	return;
    }
    
	@Override
	public String getEntityName() {
		return StatCollector.translateToLocal("entity.PigZombie.name");
	}

	public boolean isTamed() {
		return (this.LeaderName!=null && !this.LeaderName.isEmpty());
	}
	private EntityPlayer updateLeader(){
		
		EntityPlayer tmp=worldObj.getPlayerEntityByName(LeaderName);
		if (tmp==null) return null;
		ItemStack itemstack = tmp.inventory.armorItemInSlot(3);
		if (itemstack==null){
			return null;
		}else{
			if (itemstack.itemID!=mod_Fossil.Ancienthelmet.itemID){
				return null;
			}
			return tmp;
		}
	}
	public EntityLivingBase getOwner() {
		return this.Leader;
	}
}
