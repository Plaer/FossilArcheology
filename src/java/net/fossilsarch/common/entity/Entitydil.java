package net.fossilsarch.common.entity;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;

import net.fossilsarch.mod_Fossil;
import net.fossilsarch.client.render.RenderRaptor;
import net.fossilsarch.common.ai.DinoAIFollowOwner;
import net.fossilsarch.common.ai.DinoAIGrowup;
import net.fossilsarch.common.ai.DinoAIStarvation;
import net.fossilsarch.common.ai.DinoAITargetNonTamedExceptSelfClass;
import net.fossilsarch.common.ai.DinoAIUseFeeder;
import net.fossilsarch.common.handlers.FossilGuiHandler;
import net.fossilsarch.common.io.EnumDinoEating;
import net.fossilsarch.common.io.EnumDinoType;
import net.fossilsarch.common.io.EnumOrderType;
import net.minecraft.block.Block;
import net.minecraft.block.StepSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.src.ModLoader;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
public class Entitydil extends EntityDinosaurce{
    private boolean looksWithInterest;
    
	public final float HuntLimit=getHungerLimit()*4/5;
    private float field_25048_b;
    private float field_25054_c;
    private boolean isWolfShaking;
    private boolean field_25052_g;
    private float timeWolfIsShaking;
    private float prevTimeWolfIsShaking;
	public ItemStack ItemInMouth=null;
	public int LearningChestTick=900;
	//public int SubType=0;
	public int BreedTick=3000;
	public boolean PreyChecked=false;
	public boolean SupportChecked=false;
	public Vector MemberList=new Vector();
	public float SwingAngle=RenderRaptor.SwingBackSignal;	
	public int FleeingTick=0;
	
	private static Method fallInvoke = null; 
	
	public Entitydil(World world) {
		this(world, randomSpawnAge(world.rand));
		this.OrderStatus = EnumOrderType.FreeMove;
	}
	
	public Entitydil(World world, int spawnAge)
    {
        super(world);
        this.setDinoAge(spawnAge);
        SelfType=EnumDinoType.dilphosaur;
        this.SelfType=EnumDinoType.dilphosaur;
        looksWithInterest = false;
        setSize(0.6F, 1.1F);
        this.setHealth(10+this.getDinoAge());
		this.attackStrength=2+this.getDinoAge();
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(0, new DinoAIGrowup(this, 8));
		this.tasks.addTask(0, new DinoAIStarvation(this));
         this.tasks.addTask(1, new EntityAILeapAtTarget(this, 0.4F));       
        this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityTRex.class, 8.0F, 1, 1.2f));

        this.tasks.addTask(3, new EntityAIAttackOnCollide(this, 1, true));
        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(5, new DinoAIFollowOwner(this, 1, 5F, 2.0F));
        this.tasks.addTask(6, new DinoAIUseFeeder(this,1,24,this.HuntLimit, EnumDinoEating.Carnivorous));
        this.tasks.addTask(7, new EntityAIWander(this, 1));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(4, new DinoAITargetNonTamedExceptSelfClass(this, EntityLiving.class, 50, false));
        
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
		float factor = super.getAIMoveSpeed();
		
		if (this.isSelfAngry()) {
			factor *= 2.0f;
		} else if (this.OrderStatus == EnumOrderType.Follow) {
			Entity player = this.getOwner();
			if (player != null && player.getDistanceToEntity(this) < 5.0f)
				factor *= 2.0f;
		}
		
		return factor;
	}
	
	private static int randomSpawnAge(Random rand) {
		boolean isChild = rand.nextInt(4) == 0;
		
		if (isChild)
			return rand.nextInt(4);
		else
			return rand.nextInt(5)+4;
	}
	
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.3f);
    }
	
    @Override
    public boolean attackEntityAsMob(Entity par1Entity)
    {
        if (rand.nextInt(16)<4 && par1Entity instanceof EntityLiving) ((EntityLiving)par1Entity).addPotionEffect(new PotionEffect(Potion.blindness.id, rand.nextInt(110)+10, 0));
        return super.attackEntityAsMob(par1Entity);
    }
    public boolean isAIEnabled()
    {
        return true;
    }
	protected boolean canTriggerWalking()
    {
        return false;
    }

	protected void jump()
    {
        motionY = 0.41999998688697815D*(1+this.getDinoAge()/16);;
        if(isSprinting())
        {
            float f = rotationYaw * 0.01745329F;
            motionX -= MathHelper.sin(f) * 0.2F;
            motionZ += MathHelper.cos(f) * 0.2F;
        }
        isAirBorne = true;
    }
	public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setInteger("LearningChestTick",LearningChestTick);
        nbttagcompound.setBoolean("Angry", isSelfAngry());
		//nbttagcompound.setInteger("SubType", this.SubType);


    }
	public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        setSelfAngry(nbttagcompound.getBoolean("Angry"));
        setSelfSitting(nbttagcompound.getBoolean("Sitting"));
        /*if (nbttagcompound.hasKey("SubType")){
        	this.SubType=nbttagcompound.getInteger("SubType");    	
        }*/
		InitSize();
		OrderStatus=EnumOrderType.values()[nbttagcompound.getByte("OrderStatus")];
        String s = nbttagcompound.getString("Owner");

        if(s.length() > 0)
        {
            setOwner(s);
            setTamed(true);
		}

    }
	protected boolean canDespawn()
    {
        return false;
    }
	protected String getLivingSound()
    {
		if (worldObj.getClosestPlayerToEntity(this, 8D)!=null){
			if (rand.nextInt(3)==0) return "fossilsarch:DiloCall";
			return "fossilsarch:DiloLiving";
		}else return null;

    }
	protected String getHurtSound()
    {
        return "fossilsarch:DiloHurt";
		//return "raptor_living";
    }

    protected String getDeathSound()
    {
        return "fossilsarch:DiloDeath";
    }
	public boolean getCanSpawnHere()
    {
        return worldObj.checkNoEntityCollision(boundingBox) && worldObj.getCollidingBoundingBoxes(this, boundingBox).size() == 0 && !worldObj.isAnyLiquid(boundingBox);
    }
	protected void updateEntityActionState()
    {
        super.updateEntityActionState();
        /*
         * Level 3 AI seqrance:
         */

        
        //1.Confirm owner(Notice:Owner is not equal to leader)
        //If the name of player is not exists(ex.Mutiplay),the step 2 won't execult.
		EntityPlayer entityplayer = worldObj.getPlayerEntityByName(getOwnerName());	
		//2.(Leader only)Scan area and check if there are enemy attacking owner
		//3.If still get nothing to do,follow owner if order state is "Follow"
        if(!hasAttacked && !hasPath() && isTamed() && ridingEntity == null)
        {

            if(entityplayer != null)
            {
				if (OrderStatus==OrderStatus.Follow){
					float f = entityplayer.getDistanceToEntity(this);
					if(f > 5F)
					{
						getPathOrWalkableBlock(entityplayer, f);
					}
				}
            } else
            if(!isInWater())
            {
                setSelfSitting(true);
            }
        } else

		
        if(isInWater())
        {
            setSelfSitting(false);
        }
        if(!worldObj.isRemote)
        {
        	this.dataWatcher.updateObject(18, Integer.valueOf((int)this.getHealth()));
        }
    }
	
	/*public void onLivingUpdate()
    {
        super.onLivingUpdate();
      
        //1.If something in mouth,check if it's eat-able.
		if (ItemInMouth!=null){
			if (this.CheckEatable(ItemInMouth.itemID)){
				ItemFood tmp=(ItemFood)ItemInMouth.getItem();
				if (EatTick<=0){
					if (HandleEating(30)){
						heal(((ItemFood)(ItemInMouth.getItem())).getHealAmount());
						ItemInMouth=null;
					}
				}
			}
		}else{
			//2.If nothing in mouth,check if there are something pick-able around.
			if (!hasPath() && OrderStatus==EnumOrderType.FreeMove && isTamed()){
				FindAndHoldItems(-1,6);
			}else{
				//3.If nothing around or self is not tamed,check if self is able to hunt or use feeder
				if (!isSelfSitting()&& !hasPath() && (OrderStatus==EnumOrderType.FreeMove||OrderStatus==EnumOrderType.Follow)){
							if (HuntForPrey(16D)){
								if (!FindAndHoldItems(Item.porkRaw.shiftedIndex,32)) FindAndHoldItems(Item.porkCooked.shiftedIndex,32);
							}else{
								FindFeeder(16);
							}
				}
			}
			//4.If self is tamed and still get nothing to do in this step,try learn how to open chests
			if(getAITarget() == null && !hasPath() && (!isTamed() || OrderStatus==EnumOrderType.FreeMove))
			{
				//if (!HuntForPigs(16D))
				//mod_Fossil.ShowMessage("Finding Chest.");
				if (!isLeartChest()) 
					if (!FindChest(16)) HuntForPrey(16D);
				else HuntForPrey(16D);
			}
		}
		//5.(Not yet tested)Looking at player if player is holding something can eat.
        looksWithInterest = false;
        if(getAITarget()!=null && !hasPath() && !isSelfAngry())
        {
            Entity entity = getAITarget();
            if(entity instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)entity;
                ItemStack itemstack = entityplayer.inventory.getCurrentItem();
                if(itemstack != null)
                {
                    if(isTamed() && (this.CheckEatable(itemstack.itemID)))//watching at meat when tamed.
                    {
                        looksWithInterest = true;
                    }
                }
            }
        }

    }*/
	public void onUpdate()
    {
        super.onUpdate();
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
    public boolean getSelfShaking()
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
	
	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float i)//being attack
    {
		Entity entity = damagesource.getEntity();
		boolean isPlayerAttack=false;
        setSelfSitting(false);//stand up(dog)
        if(entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))//(if attacker not player or arrow)
        {
            i = (i + 1) / 2;
        }
        if(super.attackEntityFrom(damagesource, i))
        {
            if(!isSelfAngry())
            {
                if(entity instanceof EntityPlayer)
                {
					setTamed(false);
					setOwner("");
					ItemInMouth=null;
                    //setSelfAngry(true);
                    this.setTarget((EntityLiving)entity);
                    this.PreyChecked=true;
					isPlayerAttack=true;
                }
                if((entity instanceof EntityArrow) && ((EntityArrow)entity).shootingEntity != null)
                {
                    entity = ((EntityArrow)entity).shootingEntity;
                }
                if(entity instanceof EntityLiving)
                {
                	this.setTarget((EntityLiving)entity);
                }
            } else
            if(entity != this && entity != null)
            {
                entityToAttack = entity;
            }
            return true;
        } else
        {
            return false;
        }
    }
	protected Entity findPlayerToAttack()
    {
        /*if(isSelfAngry())
        {
            return worldObj.getClosestPlayerToEntity(this, 16D);
        } else
        {*/
            return null;
        //}
    }
	
	@Override
	protected void attackEntity(Entity entity, float f)
    {
		//mod_Fossil.ShowMessage(new StringBuilder().append("Range:").append(f).toString());
		//mod_Fossil.ShowMessage(new StringBuilder().append("GLsize:").append(GLsizeX).toString());
		//mod_Fossil.ShowMessage(new StringBuilder().append("BondingX:").append(boundingBox.minX).append(",").append(boundingBox.maxX).toString());
        if (entity.isDead) this.setTarget(null);
		if(f > 2.0F && f < 5.0F && rand.nextInt(10) == 0)
        {
            if(onGround)
            {
                double d = entity.posX - posX;
                double d1 = entity.posZ - posZ;
                float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
                motionX = (d / (double)f1) * 0.5D * 0.80000001192092896D + motionX * 0.20000000298023224D;
                motionZ = (d1 / (double)f1) * 0.5D * 0.80000001192092896D + motionZ * 0.20000000298023224D;
                worldObj.playSoundAtEntity(this,"fossilsarch:Raptor_attack",getSoundVolume() * 2.0F, 1.0F);
                jump();
            }
        } else
        if((double)f < 1.9F && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY)
        {
            attackTime = 20;
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), 2+this.getDinoAge());
            if (rand.nextInt(16)<4 && entity instanceof EntityLiving) ((EntityLiving)entity).addPotionEffect(new PotionEffect(Potion.blindness.id, rand.nextInt(110)+10, 0));
        }
    }
	public boolean interact(EntityPlayer entityplayer)
    {
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		if (itemstack != null){
			if (FMLCommonHandler.instance().getSide().isClient()){
				if (itemstack.itemID==mod_Fossil.DinoPedia.itemID){
		        	//this.ShowPedia(entityplayer);			//old function
		        	EntityDinosaurce.pediaingDino=this;		//new function
		        	mod_Fossil.callGUI(entityplayer, FossilGuiHandler.DINOPEDIA_GUI_ID, worldObj, (int)(this.posX), (int)(this.posY), (int)(this.posZ));
				return true;
			}
			}

			if (isTamed()){
				if (this.EOCInteract(itemstack, entityplayer)) return true;
				
			}
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
				if(entityplayer.username.equalsIgnoreCase(getOwnerName()))
				{
					if(!worldObj.isRemote)
					{
						isJumping = false;
						setPathToEntity(null);
						this.SetOrder(EnumOrderType.values()[(mod_Fossil.EnumToInt(OrderStatus)+1)%3]);
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
		}
		return false;

    }

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
            timeWolfIsShaking = 0.0F;
            prevTimeWolfIsShaking = 0.0F;
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
			this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.6D);
        } else
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & -3)));
            this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.3D);
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
			//mod_Fossil.ShowMessage("A Utahraptor won't trust mankind anymore.");
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & -5)));
        }
    }
	protected void fall(float f)
    {
        //super.fall(f);
		
		if(riddenByEntity != null)
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
        if(i > 0)
        {
            attackEntityFrom(DamageSource.fall, 0);
            int j = worldObj.getBlockId(MathHelper.floor_double(posX), MathHelper.floor_double(posY - 0.20000000298023224D - (double)yOffset), MathHelper.floor_double(posZ));
            if(j > 0)
            {
                StepSound stepsound = Block.blocksList[j].stepSound;
                worldObj.playSoundAtEntity(this, stepsound.getBreakSound(), stepsound.getVolume() * 0.5F, stepsound.getPitch() * 0.75F);
            }
        }
    }
	protected void updateWanderPath()
    {
        boolean flag = false;
        int i = -1;
        int j = -1;
        int k = -1;
        float f = -99999F;
		if (OrderStatus==OrderStatus.FreeMove || !isTamed()){
			for(int l = 0; l < 10; l++)
			{
				int i1 = MathHelper.floor_double((posX + (double)rand.nextInt(13)) - 6D);
				int j1 = MathHelper.floor_double((posY + (double)rand.nextInt(7)) - 3D);
				int k1 = MathHelper.floor_double((posZ + (double)rand.nextInt(13)) - 6D);
				float f1 = getBlockPathWeight(i1, j1, k1);
				if(f1 > f)
				{
					f = f1;
					i = i1;
					j = j1;
					k = k1;
					flag = true;
				}
			}

			if(flag)
			{
				setPathToEntity(worldObj.getEntityPathToXYZ(this, i, j, k, 10F,true,false,true,false));
			}
		}
    }

	private void InitSize(){
		setSize((float)(0.3F+0.15*(float)this.getDinoAge()),(float)(0.3F+0.15*(float)this.getDinoAge()));
		setPosition(posX,posY,posZ);
	}

	public boolean CheckSpace(){
		return !isEntityInsideOpaqueBlock();
	}
	
	@Override
	public boolean HandleEating(int FoodValue){
		if (this.getHunger()>=getHungerLimit()) {
			return false;
		}
		this.increaseHunger(FoodValue);
		showHeartsOrSmokeFX(false);
		if (this.getHunger()>=getHungerLimit()) this.setHunger(getHungerLimit());
		return true;
	}

	public boolean isLeartChest(){
		return (LearningChestTick==0);
	}

	@Override
	public void ShowPedia(EntityPlayer checker){
		//if (worldObj.isRemote) return;
		PediaTextCorrection(SelfType,checker);
		if (this.isTamed()){
				mod_Fossil.ShowMessage(new StringBuilder().append(OwnerText).append(this.getOwnerName()).toString(),checker);
				mod_Fossil.ShowMessage(new StringBuilder().append(AgeText).append(this.getDinoAge()).toString(),checker);
				mod_Fossil.ShowMessage(new StringBuilder().append(HelthText).append(this.getHealth()).append("/").append(20).toString(),checker);
				mod_Fossil.ShowMessage(new StringBuilder().append(HungerText).append(this.getHunger()).append("/").append(this.getHungerLimit()).toString(),checker);
			if (this.LearningChestTick<=0) {
				mod_Fossil.ShowMessage(EnableChestText,checker);
			}
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
			if (this.LearningChestTick<=0)
				resultList.add(EnableChestText);
			if (!resultList.isEmpty()) {
				result=new String[1];
				result=resultList.toArray(result);
			}
		}
		return result;
	}
	/*@Override
	public void HandleBreed(){
		if (this.age>3){
			this.BreedTick--;
			if (this.BreedTick==0){
				int GroupAmount=0;
				List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(32D, 32D, 32D));
				for (int i=0;i<list.size();i++){
					if (list.get(i) instanceof Entitydil) GroupAmount++;
				}
				if (GroupAmount>50) GroupAmount=50;
				if (GroupAmount>80) return;
				if (new Random().nextInt(100)<GroupAmount){
					EntityDinoEgg entityegg=null;
					if (this.isTamed()) entityegg = (EntityDinoEgg)new EntityDinoEgg(worldObj,this.SelfType,(IDino)this);
					else entityegg = (EntityDinoEgg)new EntityDinoEgg(worldObj,this.SelfType);
					entityegg.setLocationAndAngles(this.posX+(new Random().nextInt(3)-1), this.posY, this.posZ+(new Random().nextInt(3)-1), worldObj.rand.nextFloat() * 360F, 0.0F);
					if(	worldObj.checkIfAABBIsClear(entityegg.boundingBox) && worldObj.getCollidingBoundingBoxes(entityegg, entityegg.boundingBox).size() == 0) worldObj.spawnEntityInWorld(entityegg);
					showHeartsOrSmokeFX(true);
				}
				this.BreedTick=3000;
			}
		}
	}*/
	public void SetOrder(EnumOrderType input){
		this.OrderStatus=input;
	}

	@Override
	public EntityAgeable createChild(EntityAgeable entityanimal) {
		return new Entitydil(worldObj, 0);
	}

	public boolean IsIdle(){
		return (this.motionX<0.03125F)&&(this.motionY<0.03125F)&&(this.motionZ<0.03125F);
	}
	
	@Override
	public void updateSize(boolean shouldAddAge) {
		// TODO Auto-generated method stub
		
	}
	@Override
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
		return (float) (0.2F+0.1*this.getDinoAge());
	}
	@Override
	public float getGLY() {
		return (float)(0.32F+0.1*this.getDinoAge());
	}


}