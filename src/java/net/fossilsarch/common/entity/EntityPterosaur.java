package net.fossilsarch.common.entity;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;

import net.fossilsarch.mod_Fossil;
import net.fossilsarch.common.ai.DinoAIFollowOwner;
import net.fossilsarch.common.ai.DinoAIGrowup;
import net.fossilsarch.common.ai.DinoAIPickItem;
import net.fossilsarch.common.ai.DinoAIStarvation;
import net.fossilsarch.common.ai.DinoAIUseFeeder;
import net.fossilsarch.common.handlers.FossilGuiHandler;
import net.fossilsarch.common.io.EnumDinoEating;
import net.fossilsarch.common.io.EnumDinoType;
import net.fossilsarch.common.io.EnumOrderType;
import net.fossilsarch.common.io.EnumSituation;
import net.minecraft.block.Block;
import net.minecraft.block.StepSound;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.src.ModLoader;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;

public class EntityPterosaur extends EntityDinosaurce{
	protected  final int AGE_LIMIT = 8;
	public final float HuntLimit=getHungerLimit()*4/5;
    private boolean looksWithInterest;
    private float field_25048_b;
    private float field_25054_c;
    private boolean isWolfShaking;
    private boolean field_25052_g;
	public ItemStack ItemInMouth=null;
	public int LearningChestTick=900;
	public int SubType=0;
	public int BreedTick=3000;
	public float AirSpeed=0.0F;
	public float AirAngle=0.0F;
	public float AirPitch=0.0F;
	public float LastAirPitch=0.0F;
	public boolean Landing=false;
	
	private static Method fallInvoke = null; 
	
	public EntityPterosaur(World world) {
		this(world, randomSpawnAge(world.rand));
		this.OrderStatus = EnumOrderType.FreeMove;
	}
	
	public EntityPterosaur(World world, int age) 
    {
        super(world);
        this.setDinoAge(age);
        SelfType=EnumDinoType.Pterosaur;
        looksWithInterest = false;
        this.CheckSkin();
        setSize(0.8F, 0.8F);
        this.setHealth(10+age);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(0, new DinoAIGrowup(this, AGE_LIMIT));
		this.tasks.addTask(0, new DinoAIStarvation(this));
		// TODO:Breeding
		this.tasks.addTask(1, new EntityAILeapAtTarget(this, 0.4F));
		this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityTRex.class,
				8.0F, 1, 1.2f));
		this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityBrachiosaurus.class,
				8.0F, 1, 1.2f));
		this.tasks.addTask(3, new EntityAIAttackOnCollide(this, 2.0f,
				true));
		this.tasks.addTask(5, new DinoAIFollowOwner(this, 2.0f, 5F,
				2.0F));
		this.tasks.addTask(6, new DinoAIUseFeeder(this, 2.0f, 24,
				this.HuntLimit, EnumDinoEating.Carnivorous));
        this.tasks.addTask(6, new DinoAIPickItem(this,Item.fishRaw,2.0f,24,this.HuntLimit));
        this.tasks.addTask(6, new DinoAIPickItem(this,Item.fishCooked,2.0f,24,this.HuntLimit));
        this.tasks.addTask(6, new DinoAIPickItem(this,mod_Fossil.SJL,2.0f,24,this.HuntLimit));
		this.tasks.addTask(7, new EntityAIWander(this, 0.3F));
		//this.tasks.addTask(8, new EntityAIWatchClosest(this,
				//EntityPlayer.class, 8.0F));
		//this.tasks.addTask(9, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
		this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
		this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
		
		if (fallInvoke == null) {
			try {
				fallInvoke = Entity.class.getMethod("fall", float.class);
			} catch (NoSuchMethodException ex) {
				//Just let this fail and crash out when we try to call it
				//Ideally we should be able to determine this operation successful
				//at compile time.
			}
		}
    }
	
	@Override
	public float getAIMoveSpeed() {
		float speed = super.getAIMoveSpeed();
		
		if (this.isSelfAngry()) speed *= 2.0f;
		
		return speed;
	}
	
	private static int randomSpawnAge(Random random) {
		boolean isChild = random.nextInt(4) == 0;
		if (isChild)
			return random.nextInt(4);
		else
			return random.nextInt(5) + 4;
	}
	
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(20.0f);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(1.0f);
    }

	protected boolean canTriggerWalking()
    {
        return false;
    }

    public boolean isAIEnabled()
    {
    	if (this.isModelized()) return false;
    	if (this.riddenByEntity!=null) return false;
        return true;
    }

	public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("Angry", isSelfAngry());


        nbttagcompound.setFloat("Airspeed",this.AirSpeed);
        nbttagcompound.setFloat("AirAngle",this.AirAngle);
        nbttagcompound.setFloat("AirPitch",this.AirPitch);
    }
	public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);

		
        setSelfAngry(nbttagcompound.getBoolean("Angry"));
        setSelfSitting(nbttagcompound.getBoolean("Sitting"));
		
        if (nbttagcompound.hasKey("SubType")){
        	this.SubType=nbttagcompound.getInteger("SubType");    	
        }
		InitSize();
        this.AirSpeed=nbttagcompound.getFloat("Airspeed");
        this.AirAngle=nbttagcompound.getFloat("AirAngle");
        this.AirPitch=nbttagcompound.getFloat("AirPitch");
    }
	protected boolean canDespawn()
    {
        return false;
    }
	protected String getLivingSound()
    {
            //return "mob.wolf.growl";
		if (worldObj.getClosestPlayerToEntity(this, 8D)!=null) return "fossilsarch:PTS_living";
		else return null;
    }
	protected String getHurtSound()
    {
        return "fossilsarch:PTS_hurt";
		//return "raptor_living";
    }

    protected String getDeathSound()
    {
        return "fossilsarch:raptor_death";
    }
	public boolean getCanSpawnHere()
    {
        return worldObj.checkNoEntityCollision(boundingBox) && worldObj.getCollidingBoundingBoxes(this, boundingBox).size() == 0 && !worldObj.isAnyLiquid(boundingBox);
    }
	
	
	public void onLivingUpdate()
    {
		this.HandleRiding();
		super.onLivingUpdate();
		
		
        //chicken code
        /*if(!onGround && motionY < 0.0D && this.moveForward>=0.1F && this.getDinoAge()>=5)
        {
            motionY *= 0.1D;
        }*/
        


        
	
    }

	public void onUpdate()
    {
        super.onUpdate();
		//HangleChestLearning();
        field_25054_c = field_25048_b;
        if(looksWithInterest)
        {
            field_25048_b = field_25048_b + (1.0F - field_25048_b) * 0.4F;
        } else
        {
            field_25048_b = field_25048_b + (0.0F - field_25048_b) * 0.4F;
        }
        if(looksWithInterest)
        {
            numTicksToChaseTarget = 10;
        }
    }
    /*public boolean getSelfShaking()
    {
        return false;
    }
	public float getShadingWhileShaking(float f)
    {
        return 0.75F + ((prevTimeWolfIsShaking + (timeWolfIsShaking - prevTimeWolfIsShaking) * f) / 2.0F) * 0.25F;
    }
	public float getShakeAngle(float f, float f1)
    {
        float f2 = (prevTimeWolfIsShaking + (timeWolfIsShaking - prevTimeWolfIsShaking) * f + f1) / 1.8F;
        if(f2 < 0.0F)
        {
            f2 = 0.0F;
        } else
        if(f2 > 1.0F)
        {
            f2 = 1.0F;
        }
        return MathHelper.sin(f2 * 3.141593F) * MathHelper.sin(f2 * 3.141593F * 11F) * 0.15F * 3.141593F;
    }
	public float getInterestedAngle(float f)
    {
        return (field_25054_c + (field_25048_b - field_25054_c) * f) * 0.15F * 3.141593F;
    }*/
	public float getEyeHeight()
    {
        return height * 0.8F;
    }
	public int getVerticalFaceSpeed()
    {
        if(isSelfSitting())
        {
            return 20;
        } else
        {
            return super.getVerticalFaceSpeed();
        }
    }
	protected boolean isMovementCeased()
    {
        return isSelfSitting() || field_25052_g;
    }
	/*public boolean attackEntityFrom(Entity entity, int i)//being attack
    {
		boolean isPlayerAttack=false;
        setSelfSitting(false);//stand up(dog)
        if(entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))//(if attacker not player or arrow)
        {
            i = (i + 1) / 2;
        }
        if(super.attackEntityFrom(entity, i))
        {
            if(!isSelfAngry())
            {
                if(entity instanceof EntityPlayer)
                {
					setSelfTamed(false);
					setSelfOwner("");
					ItemInMouth=null;
                    setSelfAngry(true);
                    playerToAttack = entity;
					isPlayerAttack=true;
                }
                if((entity instanceof EntityArrow) && ((EntityArrow)entity).owner != null)
                {
                    entity = ((EntityArrow)entity).owner;
                }
                if(entity instanceof EntityLiving)
                {
                    List list = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityRaptor.class, AxisAlignedBB.getBoundingBoxFromPool(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(32D, 4D, 32D));
                    Iterator iterator = list.iterator();
                    do
                    {
                        if(!iterator.hasNext())
                        {
                            break;
                        }
                        Entity entity1 = (Entity)iterator.next();
                        EntityRaptor entityraptor = (EntityRaptor)entity1;
						if (entityraptor!=this && entityraptor.getTarget()==null){
							setTarget(entity1);
							if (isPlayerAttack){
								entityraptor.setSelfTamed(false);
								entityraptor.setSelfOwner("");
								entityraptor.ItemInMouth=null;
								entityraptor.setSelfAngry(true);
								entityraptor.playerToAttack = entity;
							}
						}
                    } while(true);
                }
            } else
            if(entity != this && entity != null)
            {
                playerToAttack = entity;
            }
            return true;
        } else
        {
            return false;
        }
    }*/
	protected Entity findPlayerToAttack()
    {
        if(isSelfAngry())
        {
            return worldObj.getClosestPlayerToEntity(this, 16D);
        } else
        {
            return null;
        }
    }
	
	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float i)
    {
		if (this.modelizedDrop()) return true;
		return super.attackEntityFrom(damagesource, i);
    }
	protected void attackEntity(Entity entity, float f)
    {
		//mod_Fossil.ShowMessage(new StringBuilder().append("Range:").append(f).toString());
		//mod_Fossil.ShowMessage(new StringBuilder().append("GLsize:").append(GLsizeX).toString());
		//mod_Fossil.ShowMessage(new StringBuilder().append("BondingX:").append(boundingBox.minX).append(",").append(boundingBox.maxX).toString());
        if(f > 2.0F && f < 5.0F && rand.nextInt(10) == 0)
        {
            if(onGround)
            {
                double d = entity.posX - posX;
                double d1 = entity.posZ - posZ;
                float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
                motionX = (d / (double)f1) * 0.5D * 0.80000001192092896D + motionX * 0.20000000298023224D;
                motionZ = (d1 / (double)f1) * 0.5D * 0.80000001192092896D + motionZ * 0.20000000298023224D;
                jump();
            }
        } else
        if((double)f < 1.9F && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY)
        {
            attackTime = 20;
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), 2+this.getDinoAge());
        }
    }
	public void updateRiderPosition()
    {
		float YAxieOffset=-this.getGLY();
        if(riddenByEntity == null)
        {
            return;
        } else
        {
            if (this.onGround)riddenByEntity.setPosition(posX, posY-YAxieOffset*1.1, posZ);
            else if (this.Landing) riddenByEntity.setPosition(posX, posY-YAxieOffset, posZ);
            else riddenByEntity.setPosition(posX, posY-YAxieOffset*0.6, posZ);
           //riddenByEntity.setRotation(this.rotationYaw,riddenByEntity.rotationPitch);
           //((EntityLiving)riddenByEntity).renderYawOffset=this.rotationYaw;
            return;
        }
    }
	public boolean interact(EntityPlayer entityplayer)
    {
		if (this.isModelized()) return modelizedInteract(entityplayer);
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        if (FMLCommonHandler.instance().getSide().isClient()){
        			if (itemstack!=null && itemstack.itemID==mod_Fossil.DinoPedia.itemID){
        	        	//this.ShowPedia(entityplayer);			//old function
        	        	EntityDinosaurce.pediaingDino=this;		//new function
        	        	mod_Fossil.callGUI(entityplayer, FossilGuiHandler.DINOPEDIA_GUI_ID, worldObj, (int)(this.posX), (int)(this.posY), (int)(this.posZ));
			return true;
		}
        }

		if (isTamed()){
			if (itemstack != null){
				if (itemstack.itemID==Item.arrow.itemID){
					if(entityplayer.username.equalsIgnoreCase(getOwnerName()))
					{
						if(!worldObj.isRemote)
						{
							isJumping = false;
							setPathToEntity(null);
							OrderStatus=EnumOrderType.values()[(mod_Fossil.EnumToInt(OrderStatus)+1)%3];
							SendOrderMessage(OrderStatus);
							switch (OrderStatus){
								case Stay:

									setSelfSitting(true);
									break;
								case Follow:

									setSelfSitting(false);
									break;
								case FreeMove:

									setSelfSitting(false);
							}
						}
						return true;
					}
				}
				if (EOCInteract(itemstack,entityplayer))return true;
		        if(itemstack != null && (itemstack.itemID==Item.fishRaw.itemID || itemstack.itemID==Item.fishCooked.itemID||itemstack.itemID==mod_Fossil.SJL.itemID))
	            {

						if(HandleEating(30))
						{
							itemstack.stackSize--;
							if(itemstack.stackSize <= 0)
							{
								entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
							}
							heal(3);

						}
					return true;
	            }else return false;
				/*if (ItemInMouth==null){
					ItemInMouth=itemstack;
					entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
					return true;
				}*/
			}else{
				if (ItemInMouth!=null){
					if(worldObj.isRemote)
			        {
			            return false;
			        }
			        int i = ItemInMouth.stackSize;
			        if(entityplayer.inventory.addItemStackToInventory(ItemInMouth))
			        {
			            ModLoader.onItemPickup(entityplayer, ItemInMouth);
			            worldObj.playSoundAtEntity(entityplayer, "random.pop", 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			        }else return false;
					ItemInMouth=null;
					return true;
				}else{
					if (this.isTamed() && this.getDinoAge()>4 &&!worldObj.isRemote && (riddenByEntity == null || riddenByEntity == entityplayer)){
							entityplayer.moveForward=0;
							entityplayer.rotationYaw=this.rotationYaw;
				            entityplayer.mountEntity(this);
				            this.setPathToEntity(null);
				            this.renderYawOffset=this.rotationYaw;
				            return true;
					}

				}
			}
		}
		return false;

    }

	@Override
	public void handleHealthUpdate(byte byte0)
    {
        if(byte0 == 7)
        {
            showHeartsOrSmokeFX(true);
        } else
        if(byte0 == 6)
        {
            showHeartsOrSmokeFX(false);
        } else
        if(byte0 == 8)
        {
            field_25052_g = true;
        } else
        {
            super.handleHealthUpdate(byte0);
        }
    }
	public int getMaxSpawnedInChunk()
    {
        return 100;
    }
	
	public boolean isSelfAngry()
    {
        return (dataWatcher.getWatchableObjectByte(16) & 2) != 0;
    }
	public boolean isSelfSitting()
    {
        return (dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }
	public void setSelfAngry(boolean flag)
    {
        byte byte0 = dataWatcher.getWatchableObjectByte(16);
        if(flag)
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 | 2)));
            this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(4.0f);
        } else
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & -3)));
            this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(2.0f);
        }
    }
	public void setSelfSitting(boolean flag)
    {
        byte byte0 = dataWatcher.getWatchableObjectByte(16);
        if(flag)
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 | 1)));
        } else
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & -2)));
        }
    }

	public void setTamed(boolean flag)
    {
        byte byte0 = dataWatcher.getWatchableObjectByte(16);
        if(flag)
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 | 4)));
        } else
        {
			ItemInMouth=null;
			SendStatusMessage(EnumSituation.Bytreate);
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & -5)));
        }
    }
	
	@Override
	protected void fall(float f)
    {
		if(riddenByEntity != null && !this.Landing)
        {
			try {
				this.fallInvoke.invoke(riddenByEntity, f);
			} catch (IllegalAccessException e) {
				FMLLog.log(Level.SEVERE, e, "Error invoking fall method.");
			} catch (InvocationTargetException e) {
				FMLLog.log(Level.SEVERE, e, "Error invoking fall method.");
			}
        }
		int i = (int)Math.ceil(f - 3F);
		if (worldObj.isRemote) return;
        if(i > 0)
        {
            int j = worldObj.getBlockId(MathHelper.floor_double(posX), MathHelper.floor_double(posY - 0.20000000298023224D - (double)yOffset), MathHelper.floor_double(posZ));
            if(j > 0)
            {
                StepSound stepsound = Block.blocksList[j].stepSound;
                worldObj.playSoundAtEntity(this, stepsound.getBreakSound(), stepsound.getVolume() * 0.5F, stepsound.getPitch() * 0.75F);
            }
        }
    }

	private void InitSize(){
		this.CheckSkin();
		updateSize(false);
		setPosition(posX,posY,posZ);
	}
	public boolean CheckSpace(){
		return !isEntityInsideOpaqueBlock();
	}
	
	public boolean HandleEating(int FoodValue){
		if (this.getHunger()>=getHungerLimit()) {
			return false;
		}
		this.increaseHunger(FoodValue);
		showHeartsOrSmokeFX(false);
		if (this.getHunger()>=getHungerLimit()) this.setHunger(getHungerLimit());
		return true;
	}

	public void ChangeSubType(int type){
		if (type<=2 && type>=0){
			this.SubType=type;
			this.CheckSkin();
		}
	}
	public void ShowPedia(EntityPlayer checker){
		PediaTextCorrection(SelfType,checker);
		if (this.isTamed()){
				mod_Fossil.ShowMessage(new StringBuilder().append(OwnerText).append(this.getOwnerName()).toString(),checker);
				mod_Fossil.ShowMessage(new StringBuilder().append(AgeText).append(this.getDinoAge()).toString(),checker);
				mod_Fossil.ShowMessage(new StringBuilder().append(HelthText).append(this.getHealth()).append("/").append(20).toString(),checker);
				mod_Fossil.ShowMessage(new StringBuilder().append(HungerText).append(this.getHunger()).append("/").append(this.getHungerLimit()).toString(),checker);
			

			if (this.getDinoAge()>=5) mod_Fossil.ShowMessage(new StringBuilder().append(FlyText).toString(),checker);
			if (this.getDinoAge()>=8) mod_Fossil.ShowMessage(new StringBuilder().append(RidiableText).toString(),checker);
		}else{
			mod_Fossil.ShowMessage(UntamedText,checker);
		}
		
	}
	@Override
	public String[] additionalPediaMessage(){
		String[] result=null;
		if (!this.isTamed()){
			result=new String[1];
			result[0]=UntamedText;
		}else{
			ArrayList<String> resultList=new ArrayList<String>();
			if (this.getDinoAge()>=5)
				resultList.add(FlyText);
			if (this.getDinoAge()>=8)
				resultList.add(RidiableText);
			if (!resultList.isEmpty()) {
				result=new String[1];
				result=resultList.toArray(result);
			}
		}
		return result;
	}

	public void jump(){
		this.motionY=0.8D;
	}

	private void HandleRiding(){
		
		if (this.riddenByEntity==null || !(this.riddenByEntity instanceof EntityClientPlayerMP)) return;
		EntityClientPlayerMP ridder=(EntityClientPlayerMP)this.riddenByEntity;
			this.HandleLanding();
			if (this.onGround || this.inWater){
				//land movement
	    		if (this.AirSpeed!=0.0F) this.AirSpeed=0.0F;
	    		if (this.AirAngle!=0.0F) this.AirAngle=0.0F;
	    		if (this.AirPitch!=0.0F) this.AirPitch=0.0F;
				this.rotationYaw-=(float) ((float)(ridder.moveStrafing)*5);
	            for(; this.rotationYaw < -180F; this.rotationYaw += 360F) { }
	            for(; this.rotationYaw >= 180F; this.rotationYaw -= 360F) { }
				this.setMoveForward(ridder.moveForward);
				
			}else{				
	    		this.AirAngle-=ridder.moveStrafing;
	            if (this.AirAngle>30F) this.AirAngle=30F;
	            if (this.AirAngle<-30F) this.AirAngle=-30F;
	            if (Math.abs(this.AirAngle)>10) this.rotationYaw+=(this.AirAngle>0? 1:-1);
				//if (Math.abs(this.AirAngle)!=30)this.rotationYaw-=ridder.moveStrafing;
	            for(; this.rotationYaw < -180F; this.rotationYaw += 360F) { }
	            for(; this.rotationYaw >= 180F; this.rotationYaw -= 360F) { }
				if (this.Landing){
					AirPitch=0;
					if (!this.isCollidedVertically)this.motionY = -0.2D;
					else this.motionY=0;
					this.setMoveForward(this.AirSpeed);
				}else{
					if ((this.isCollidedHorizontally || this.isCollidedVertically)&& this.AirSpeed!=0){
						this.AirSpeed=0.0F;
						this.setMoveForward(0.0F);
						return;
					}
					//air movement
		    		if (this.AirSpeed==0 && this.moveForward!=0){
		    			this.AirSpeed=this.moveForward;
		    		}
		    		this.AirAngle-=ridder.moveStrafing;
		            if (this.AirAngle>30F) this.AirAngle=30F;
		            if (this.AirAngle<-30F) this.AirAngle=-30F;

					//this.moveForward=ridder.moveForward*this.moveSpeed*100;
		            this.AirPitch-=ridder.moveForward*2;
		            if (this.AirPitch>90) this.AirPitch=90;
		            if (this.AirPitch<-60) this.AirPitch=-60;
		            float RandPitch=(float)(this.AirPitch*(Math.PI/180));
		            if (LastAirPitch>=AirPitch){
			            double SpeedOffset=Math.cos(RandPitch);
			            if (RandPitch<0)SpeedOffset+=1;
			            this.setMoveForward(this.AirSpeed*(float)SpeedOffset);
			            if (this.AirPitch<60 && this.moveForward>0.1F){
			            	this.motionY=Math.sin(RandPitch)*0.4;
			            }
		            }
		            LastAirPitch=AirPitch;
				}
				
			}
	}
	public void SetOrder(EnumOrderType input){
		this.OrderStatus=input;
	}

	@Override
	public EntityAgeable createChild(EntityAgeable entityanimal) {
		return new EntityPterosaur(worldObj, 0);
	}

	public void HandleLanding(){
		if (this.riddenByEntity!=null && !this.isCollidedVertically && !this.onGround){
			if (!Landing){
				if (this.AirPitch>60) this.Landing=true;
			}
		}else{
			this.Landing=false;
		}
	}

	@Override
	public void updateSize(boolean shouldAddAge) {
		if (shouldAddAge && this.getDinoAge()<this.AGE_LIMIT) this.increaseDinoAge();
		setSize((float)(0.8F+0.2*(float)this.getDinoAge()),(float)(0.8F+0.2*(float)this.getDinoAge()));
		
	}
	
	public EnumOrderType getOrderType() {
		return this.OrderStatus;
	}

	@Override
	protected int foodValue(Item asked) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void HandleEating(Item food) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void HoldItem(Item itemGot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getGLX() {
		return (float)(0.8F+0.2*(float)this.getDinoAge());
	}

	@Override
	public float getGLY() {
		return (float)(0.8F+0.2*(float)this.getDinoAge());
	}
}