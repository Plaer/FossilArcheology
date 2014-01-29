// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.fossilsarch.common.entity;

import java.util.List;
import java.util.Random;

import net.fossilsarch.mod_Fossil;
import net.fossilsarch.common.FossilOptions;
import net.fossilsarch.common.PigBossSpeaker;
import net.fossilsarch.common.PigmenSpeaker;
import net.fossilsarch.common.io.EnumPigBossSpeaks;
import net.fossilsarch.common.io.EnumPigmenSpeaks;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

// Referenced classes of package net.minecraft.src:
//            EntityZombie, World, NBTTagCompound, EntityPlayer, 
//            AxisAlignedBB, Entity, Item, ItemStack

public class EntityPigBoss extends EntityZombie
{

    public EntityPigBoss(World world)
    {
        super(world);
        angerLevel = 0;
        randomSoundDelay = 0;
        isImmuneToFire = true;
        this.setAIMoveSpeed(0.5f);
        
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        
        if (this.worldObj.provider.isHellWorld)
        	this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(100.0D);
        else
        	this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(200.0D);
        
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setAttribute(5.0D);
    }
    
    public void heal(int i)
    {
        if(this.getHealth() <= 0)
        {
            return;
        }
        this.setHealth(this.getHealth() + i);
        if (worldObj.provider.isHellWorld){
            if(this.getHealth() > 100)
            {
                this.setHealth(100);
            }
        }else{
            if(this.getHealth() > 200)
            {
                this.setHealth(200);
            }
        }

        //heartsLife = heartsHalvesLife / 2;
    }
    public void onUpdate()
    {
        //moveSpeed = playerToAttack == null ? 0.5F : 0.95F;
        if(randomSoundDelay > 0 && --randomSoundDelay == 0)
        {
            worldObj.playSoundAtEntity(this, "mob.zombiepig.zpigangry", getSoundVolume() * 2.0F, ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) * 1.8F);
        }
        BlockTimeInteract();
        super.onUpdate();
    }

    public boolean getCanSpawnHere()
    {
    	if (!FossilOptions.ShouldAnuSpawn)return false;
    	boolean result=worldObj.difficultySetting > 0 && worldObj.checkNoEntityCollision(boundingBox) && worldObj.getCollidingBoundingBoxes(this, boundingBox).size() == 0 && !worldObj.isAnyLiquid(boundingBox);
        if (!result) return false;
        List list = worldObj.getEntitiesWithinAABB(EntityPigBoss.class, AxisAlignedBB.getAABBPool().getAABB(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(200D, 200D, 200D));
        result=(list.size()<2);
        return result;
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setShort("Anger", (short)angerLevel);
        nbttagcompound.setInteger("AttackMode", this.getAttackMode());
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        angerLevel = nbttagcompound.getShort("Anger");
        if (nbttagcompound.hasKey("AttackMode")) this.SetAttackMode(nbttagcompound.getInteger("AttackMode"));
    }

    protected Entity findPlayerToAttack()
    {
    	Entity Target=super.findPlayerToAttack();
    	if (Target instanceof EntityPlayer && ((EntityPlayer) Target).getHealth()>0){
    		Mouth.SendSpeech(EnumPigBossSpeaks.Hello);
    		if (!worldObj.provider.isHellWorld) ((EntityPlayer) Target).addStat(mod_Fossil.PigbossOnEarth, 1);
    	}
    	return Target;
    }

    public void onLivingUpdate()
    {

        super.onLivingUpdate();
        if (this.FireballCount<50) {
        	this.FireballCount++;
        }
        if (this.FireballCount>50 && this.getAttackMode()==Ranged && this.getAITarget()!=null){
        	this.setPathToEntity(null);
        	this.faceEntity(this.getAITarget(),30,30);
        }
        if (this.getAttackMode()!=Ranged) {
        	this.FireballCount=0;
        	if (new Random().nextInt(5000)<=5 && worldObj.getClosestPlayerToEntity(this, 16D)!=null) {
        		
        		this.SkillSwordQi();
        	}
        }
        //Zombiepig Searching
        if (this.getAITarget()!=null && new Random().nextInt(100)<=25){
        	List list = worldObj.getEntitiesWithinAABB(EntityPigZombie.class, AxisAlignedBB.getAABBPool().getAABB(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(16D, 4D, 16D));
            if(!list.isEmpty() && list.size()>=5)
            {
                this.CallSolders(list, this.getAITarget());
            }
        }
        //pig searching
        if (this.getAITarget()==null && new Random().nextInt(100)<=20 && !this.worldObj.provider.isHellWorld){
        	List list = worldObj.getEntitiesWithinAABB(EntityPig.class, AxisAlignedBB.getAABBPool().getAABB(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(16D, 4D, 16D));
        	if (list.size()>=3) this.TranferPigs(list);
        }
        
    }
    
    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float i)
    {
    		Entity entity = damagesource.getEntity();
    		if (entity instanceof EntityGhast) return false;
	        if(entity instanceof EntityPlayer)
	        {
	            becomeAngryAt(entity);
	            if (i!=0){
	            ItemStack PlayerWeapon = ((EntityPlayer)entity).inventory.getCurrentItem();
	            if (PlayerWeapon==null){
	            	if (this.getAttackMode()!=Melee){
	            		Mouth.SendSpeech(EnumPigBossSpeaks.BareHand);
	            		this.SetAttackMode(Melee);
	            		return super.attackEntityFrom(damagesource, i);
	            	}
	            }else{
		            if (PlayerWeapon.getItem() instanceof ItemSword && this.getAttackMode()!=Melee){
		            	Mouth.SendSpeech(EnumPigBossSpeaks.Draw);
		            	this.SetAttackMode(Melee);
		            	return super.attackEntityFrom(damagesource, i);
		            }
		            if (damagesource.damageType=="arrow" && this.getAttackMode()!=Ranged){
		            	Mouth.SendSpeech(EnumPigBossSpeaks.Coward);
		            	this.SetAttackMode(Ranged);
		            	return super.attackEntityFrom(damagesource, i);
		            }	
		            if (!(PlayerWeapon.getItem() instanceof ItemBow) && !(PlayerWeapon.getItem() instanceof ItemSword)){
		            		double PlayerDistance=Math.sqrt(this.getDistanceSqToEntity(worldObj.getClosestPlayerToEntity(this,24D)));
		                    if (PlayerDistance>6 && this.getAttackMode()!=Ranged) {
		                    	if (worldObj.provider.isHellWorld) Mouth.SendSpeech(EnumPigBossSpeaks.LeartHere);
		                    	else Mouth.SendSpeech(EnumPigBossSpeaks.LeartThere);
		                    	this.SetAttackMode(Ranged);
		                    	return super.attackEntityFrom(damagesource, i);
		                    }
	
		                    if(PlayerDistance<6 &&this.getAttackMode()!=Melee){
		                    	Mouth.SendSpeech(EnumPigBossSpeaks.UnknownRanged);
		                    		this.SetAttackMode(Melee);
		                    		return super.attackEntityFrom(damagesource, i);
		                    }
		            }
	            }	            
	           }else{
	            	if(this.getAttackMode()!=Ranged){
	            		Mouth.SendSpeech(EnumPigBossSpeaks.UnknownMelee);
	            		this.SetAttackMode(Ranged);
	            		return super.attackEntityFrom(damagesource, i);
	            	}
	           }

	        
	        }
        return super.attackEntityFrom(damagesource, i);

    }
    
    @Override
	protected void attackEntity(Entity entity, float f)
    {
		if (this.getAttackMode()==Melee) {
			super.attackEntity(entity, f);
			return;
		}
		if (this.getAttackMode()==Ranged){
			if (this.FireballCount>=100){
				if (new Random().nextInt(1000)<=950){
		            double d5 = entity.posX - posX;
		            double d6 = (entity.boundingBox.minY + (double)(entity.height / 2.0F)) - (posY + (double)(height / 2.0F));
		            double d7 = entity.posZ - posZ;
		            worldObj.playSoundAtEntity(this, "mob.ghast.fireball", 10F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
		            EntityLargeFireball entityfireball = new EntityLargeFireball(worldObj, this, d5, d6, d7);
	                double d8 = 4D;
	                Vec3 vec3d = getLook(1.0F);
	                entityfireball.posX = posX + vec3d.xCoord * d8;
	                entityfireball.posY = posY + 0.8D;
	                entityfireball.posZ = posZ + vec3d.zCoord * d8;
	                worldObj.spawnEntityInWorld(entityfireball);
				}else SkillFireballRain(entity);
                this.FireballCount=0;
			}else this.FireballCount++;
			return;
		}
    }
    private void becomeAngryAt(Entity entity)
    {
        /*playerToAttack = entity;
        angerLevel = 400 + rand.nextInt(400);
        randomSoundDelay = rand.nextInt(40);*/
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
    	if (this.getAttackMode()==Melee) return new ItemStack(Item.swordIron);
        return defaultHeldItem;
    }
    public int getAttackMode(){
    	return this.AttackType;
    }
    public void SetAttackMode(int newmode){
    	if (newmode<2) {
    		this.AttackType=newmode;
    		if (newmode==Melee) this.setAIMoveSpeed(0.9f);
    		if (newmode==Ranged) this.setAIMoveSpeed(0.5f);
    	}
    }
    public void SwitchAttackMode(){
    	SetAttackMode((this.AttackType+1)%2);
    }
	private float GetDistanceWithEntity(Entity target){
		return (float)(Math.sqrt(Math.pow((double)(posX-target.posX),2)+Math.pow((double)(posY-target.posY),2)+Math.pow((double)(posZ-target.posZ),2)));
	}
	private void BlockTimeInteract(){
		if (!worldObj.provider.isHellWorld){
			for(int i=(int)(Math.round(posX)-1);i<=(int)(Math.round(posX)+1);i++){
				for(int k=(int)(Math.round(posZ)-1);k<=(int)(Math.round(posZ)+1);k++){
					int targetID=worldObj.getBlockId(i, (int)(Math.round(posY)-1), k);
					if (targetID==Block.stone.blockID||targetID==Block.cobblestone.blockID) worldObj.setBlock(i, (int)(Math.round(posY)-1), k, Block.netherrack.blockID);
					if (targetID==Block.dirt.blockID||targetID==Block.grass.blockID||targetID==Block.sand.blockID||targetID==Block.gravel.blockID) worldObj.setBlock(i, (int)(Math.round(posY)-1), k, Block.slowSand.blockID);
					if (targetID==Block.ice.blockID) worldObj.setBlock(i, (int)(Math.round(posY)-1), k, Block.obsidian.blockID);
					if (targetID==Block.blockClay.blockID) worldObj.setBlock(i, (int)(Math.round(posY)-1), k, Block.glowStone.blockID);
					if (i!=Math.round(posX) && k!=(Math.round(posZ))){
						if (new Random().nextInt(2000)<=1 && worldObj.isAirBlock(i, (int)(Math.round(posY)), k)) worldObj.setBlock(i, (int)(Math.round(posY)), k, Block.fire.blockID);
					}
					/*for (int j=(int)Math.round(boundingBox.minY);j<=(int)Math.round(boundingBox.maxY);j++){
						targetID=worldObj.getBlockId(i, j, k);
						if (targetID==Block.waterStill.blockID) worldObj.setBlockWithNotify(Block.lavaStill.blockID, i, j, k);
						if (targetID==Block.waterMoving.blockID) worldObj.setBlockWithNotify(Block.waterMoving.blockID, i, j, k);
					}*/
					
				}
			}
			//mod_Fossil.ShowMessage(new StringBuilder().append(worldObj.worldInfo.getWorldTime()).toString());
			if (worldObj.getWorldInfo().getWorldTime()>19000 || worldObj.getWorldInfo().getWorldTime()<17000) worldObj.getWorldInfo().setWorldTime(17000);
		}
	}
	
    protected boolean canDespawn()
    {
        return (!worldObj.provider.isHellWorld);
    }
    private void CallSolders(List enviro,Entity target){
    	Mouth.SendSpeech(EnumPigBossSpeaks.summon);
    	for(int j = 0; j < enviro.size(); j++)
        {
            Entity entity1 = (Entity)enviro.get(j);
            if(entity1 instanceof EntityPigZombie)
            {
            	if (((EntityPigZombie) entity1).getAITarget()==null){
	                EntityPigZombie entitypigzombie = (EntityPigZombie)entity1;
	                entitypigzombie.setAttackTarget((EntityLiving)target);
	                new PigmenSpeaker(null).SendSpeech(EnumPigmenSpeaks.AnuSommon);
            	}
            }
        }
    }
    private void TranferPigs(List enviro){
    	Mouth.SendSpeech(EnumPigBossSpeaks.Trans);
    	for(int j = 0; j < enviro.size(); j++)
        {
            Entity entity1 = (Entity)enviro.get(j);
            if(entity1 instanceof EntityPig)
            {
            	entity1.onStruckByLightning(new EntityLightningBolt(worldObj,entity1.posX,entity1.posY,entity1.posZ));
            }
        }
    }
    private void SkillSwordQi(){
    	List list = worldObj.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getAABBPool().getAABB(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(32D, 4D, 32D));
    	if (!list.isEmpty()){
    		Mouth.SendSpeech(EnumPigBossSpeaks.Qi);
            worldObj.playSoundEffect(posX, posY, posZ, "random.explode", 6F, (1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
            worldObj.spawnParticle("hugeexplosion", posX, posY, posZ, 0.0D, 0.0D, 0.0D);
        	for(int j = 0; j < list.size(); j++)
            {
                EntityLivingBase entity1 = (EntityLivingBase)list.get(j);
                double d = posX - entity1.posX;
                double d1;
                for(d1 = posZ - entity1.posZ; d * d + d1 * d1 < 0.0001D; d1 = (Math.random() - Math.random()) * 0.01D)
                {
                    d = (Math.random() - Math.random()) * 0.01D;
                }
                if (entity1!=this) entity1.knockBack(this, 0, d*5, d1*5);
                if (entity1 instanceof EntityPlayer){
                	if (new Random().nextInt(1000)>=950) ((EntityPlayer)entity1).inventory.dropAllItems();
                }
            }
    	}
    }
    public void knockBack(Entity entity, int i, double d, double d1){
    	if (new Random().nextInt(100)<25) return;
    	else super.knockBack(entity, i, d, d1);
    }
    public void SkillFireballRain(Entity target){
    	EntityLargeFireball entityfireball;
		Mouth.SendSpeech(EnumPigBossSpeaks.FireRain);
	    double d5 = target.posX - posX;
	    double d6 = (target.boundingBox.minY + (double)(target.height / 2.0F)) - (posY + (double)(height / 2.0F));
	    double d7 = target.posZ - posZ;
		for (int i=1;i<=16;i++){
			entityfireball = new EntityLargeFireball(worldObj, this, d5, d6,d7);
			entityfireball.posX += (double)(new Random().nextInt(30)-10);
			entityfireball.posY = posY + 15.0D;
			entityfireball.posZ += (double)(new Random().nextInt(30)-10);
	        worldObj.spawnEntityInWorld(entityfireball);
		}
	}
    private final int Melee=0;
    private final int Ranged=1;
    private int angerLevel;
    private int randomSoundDelay;
    public int AttackType=Ranged;
    public int FireballCount=0;
    private final ItemStack defaultHeldItem=null;
    public PigBossSpeaker Mouth=new PigBossSpeaker();
}
