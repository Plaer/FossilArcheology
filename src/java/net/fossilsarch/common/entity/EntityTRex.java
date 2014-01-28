package net.fossilsarch.common.entity;
import java.util.*;

import net.fossilsarch.mod_Fossil;
import net.fossilsarch.common.ai.DinoAIAvoidEntityWhenYoung;
import net.fossilsarch.common.ai.DinoAIFollowOwner;
import net.fossilsarch.common.ai.DinoAIGrowup;
import net.fossilsarch.common.ai.DinoAIPickItem;
import net.fossilsarch.common.ai.DinoAIStarvation;
import net.fossilsarch.common.ai.DinoAITargetNonTamedExceptSelfClass;
import net.fossilsarch.common.ai.DinoAIUseFeeder;
import net.fossilsarch.common.ai.DinoAIWander;
import net.fossilsarch.common.block.BlockBreakingRule;
import net.fossilsarch.common.handlers.FossilGuiHandler;
import net.fossilsarch.common.io.EnumDinoEating;
import net.fossilsarch.common.io.EnumDinoType;
import net.fossilsarch.common.io.EnumOrderType;
import net.fossilsarch.common.io.EnumSituation;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
public class EntityTRex extends EntityDinosaurce{
	
	public final int Areas=15;
	public final float HuntLimit=getHungerLimit()*(4F/5F);
    private boolean looksWithInterest;
    private float field_25048_b;
    private float field_25054_c;
    private boolean field_25052_g;
	//public int ScreamTick=0;
	public boolean Screaming=false;
	public int SkillTick=0;
	public int WeakToDeath=0;
	public int TooNearMessageTick=0;
	public boolean SneakScream=false;
	private final BlockBreakingRule blockBreakingBehavior=new BlockBreakingRule(worldObj,this,3,5.0f);
	
	public EntityTRex(World world) {
		this(world, randomSpawnAge(world.rand));
	}
	
	public EntityTRex(World world, int age)
    {
        super(world);
        
        this.setDinoAge(age);
        SelfType=EnumDinoType.TRex;
        looksWithInterest = false;
        setSize(0.5F, 0.5F);
        this.setHealth(10);     
		attackStrength=4+this.getDinoAge();
        //this.getNavigator().setAvoidsWater(true);
		this.tasks.addTask(0, new DinoAIGrowup(this, 8,23));
		this.tasks.addTask(0, new DinoAIStarvation(this));

        this.tasks.addTask(1, new DinoAIAvoidEntityWhenYoung(this, EntityPlayer.class, 8.0F, 0.3F, 0.35F));
        this.tasks.addTask(2, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(3, new EntityAIAttackOnCollide(this, 1.0f, true));
        this.tasks.addTask(4, new DinoAIFollowOwner(this, 1.0f, 5F, 2.0F));
        this.tasks.addTask(5, new DinoAIUseFeeder(this,1.0f,24,this.HuntLimit,EnumDinoEating.Carnivorous));
        this.tasks.addTask(6, new DinoAIWander(this, 1.0f));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new DinoAIPickItem(this,Item.porkRaw,1.0f,24,this.HuntLimit));
        this.tasks.addTask(8, new DinoAIPickItem(this,Item.beefRaw,1.0f,24,this.HuntLimit));
        this.tasks.addTask(8, new DinoAIPickItem(this,Item.chickenRaw,1.0f,24,this.HuntLimit));
        this.tasks.addTask(8, new DinoAIPickItem(this,Item.porkCooked,1.0f,24,this.HuntLimit));
        this.tasks.addTask(8, new DinoAIPickItem(this,Item.beefCooked,1.0f,24,this.HuntLimit));
        this.tasks.addTask(8, new DinoAIPickItem(this,Item.chickenCooked,1.0f,24,this.HuntLimit));
        this.tasks.addTask(8, new DinoAIPickItem(this,mod_Fossil.RawDinoMeat,1.0f,24,this.HuntLimit));
        this.tasks.addTask(8, new DinoAIPickItem(this,mod_Fossil.CookedDinoMeat,1.0f,24,this.HuntLimit));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new DinoAITargetNonTamedExceptSelfClass(this, EntityLiving.class, 50, false));
    }
	
	
	private static int randomSpawnAge(Random random) {
		return random.nextInt(8) + 16;
	}
	
    @Override
    protected void applyEntityAttributes() {
    	super.applyEntityAttributes();
    	
    	this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(200.0);
    	this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.3);
    }
	
	public int getHungerLimit(){
		return 500;
	}
    public boolean isAIEnabled()
    {
        return (!this.isModelized() && this.riddenByEntity==null && !this.isWeak());
    }
	protected boolean canTriggerWalking()
    {
        return false;
    }
	public boolean isYoung(){
		return this.getDinoAge()<=3;
	}
	public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("Angry", isSelfAngry());
        nbttagcompound.setBoolean("Sitting", isSelfSitting());
		nbttagcompound.setInteger("WeakToDeath", WeakToDeath);
    }
	public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);

        setSelfAngry(nbttagcompound.getBoolean("Angry"));
        setSelfSitting(nbttagcompound.getBoolean("Sitting"));
		InitSize();
		WeakToDeath=nbttagcompound.getInteger("WeakToDeath");

    }
	protected boolean canDespawn()
    {
        return false;
    }
	protected String getLivingSound()
    {
		if (worldObj.getClosestPlayerToEntity(this, 8D)!=null) return "fossilsarch:TRex_Living";
		else return null;
    }
	protected String getHurtSound()
    {
        return "fossilsarch:TRex_hit";
    }

    protected String getDeathSound()
    {
        return "fossilsarch:TRex_Death";
    }
	protected void updateEntityActionState()
    {

		//super.updateEntityActionState();

    }
    public boolean getCanSpawnHere()
    {
        return worldObj.checkNoEntityCollision(boundingBox) && worldObj.getCollidingBoundingBoxes(this, boundingBox).size() == 0 && !worldObj.isAnyLiquid(boundingBox);
    }
    public boolean isInWater()
    {
    	if (this.onGround) return false;
    	else return super.isInWater();
    }
	public void onUpdate()
    {
		//fleeingTick=0;
        super.onUpdate();
        blockBreakingBehavior.execute();
        if (!(this.getHealth()<=0)){	
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
    }
    public void applyEntityCollision(Entity entity){
		if (entity instanceof EntityLiving && !(entity instanceof EntityPlayer)){
			if ((this.riddenByEntity!=null || this.isSelfAngry()) && this.onGround && this.getDinoAge()>3){
				this.onKillEntity((EntityLiving)entity);
				((EntityLiving)entity).attackEntityFrom(DamageSource.causeMobDamage(this), 10);
				return;
			}
		}
		super.applyEntityCollision(entity);
	}
	public boolean getSelfShaking()
    {
        return false;
    }
	public float getInterestedAngle(float f)
    {
        return (field_25054_c + (field_25048_b - field_25054_c) * f) * 0.15F * 3.141593F;
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
	private void handleScream(){
		Entity target=this.getAttackTarget();
		if (target==null) {
			this.Screaming=false;
			return;
		}
		double distance=this.getDistanceSqToEntity(target);
		if (distance<=(double)(this.width * 4.0F * this.width * 4.0F)){
			this.Screaming=true;
		}else this.Screaming=false;
	}
	protected boolean isMovementCeased()
    {
        return isSelfSitting() || field_25052_g;
    }
	public boolean attackEntityFrom(DamageSource damagesource, int i)//being attack
    {
		if (damagesource.getEntity()==this) return false;
		Entity entity = damagesource.getEntity();
		if (damagesource.damageType.equals("arrow") && this.getDinoAge()>=3) return false;
		//if (damagesource.damageType.equals("arrow") && this.age>=3) return super.attackEntityFrom(damagesource, i/2);
		if (i<6 && entity!=null && this.getDinoAge()>=3) return false;
		if ((i==20) && (entity==null)) return super.attackEntityFrom(damagesource, 200);
		this.setTarget((EntityLiving)entity);
		return super.attackEntityFrom(damagesource, i);
		
    }
    protected void damageEntity(DamageSource damagesource, float i)
    {
        i = applyArmorCalculations(damagesource, i);
        i = applyPotionDamageCalculations(damagesource, i);
        this.setHealth(this.getHealth() - i);
    }
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
    protected void attackEntity(Entity entity, float f)
    {
		//mod_Fossil.ShowMessage(new StringBuilder().append("Start attack").append(entity.toString()).append(",").append(f).append(",").append(width*1.6).toString());
        this.faceEntity(entity, 30F, 30F);
    	if (!hasPath()){
        	this.setPathToEntity(worldObj.getPathEntityToEntity(this, this.getEntityToAttack(), f, true, false, true, false));
        }
    	if(f > width*1.6 /*&& f < Areas && rand.nextInt(10) == 0*/)
        {
    		
            if(onGround)
            {
                double d = entity.posX - posX;
                double d1 = entity.posZ - posZ;
                float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
                motionX = (d / (double)f1) * 0.5D * 0.80000001192092896D + motionX * 0.20000000298023224D;
                motionZ = (d1 / (double)f1) * 0.5D * 0.80000001192092896D + motionZ * 0.20000000298023224D;
                if (getDinoAge()<=3) motionY = 0.40000000596046448D;
				//this.ScreamTick=15;
				if (f<(Areas/3)){
					if (!Screaming){
						if (getDinoAge()>=3) worldObj.playSoundAtEntity(this,"fossilsarch:TRex_scream",getSoundVolume() * 2.0F, 1.0F);
						Screaming=true;
					}
				}
            }
        } else
        /*if((double)f <= width*1.6 && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY)*/
        {
        	//if (this.Prey==null) this.Bite(entity);
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), attackStrength);
        	//entity.attackEntityFrom(DamageSource.causeMobDamage(this), 1);
            //if (entity.isDead && this.Prey==entity) this.Prey=null;
        }
    }
	public void onKillEntity(EntityLiving entityliving)
    {
		super.onKillEntity(entityliving);
		if (getDinoAge()>=3) worldObj.playSoundAtEntity(this,"fossilsarch:TRex_scream",getSoundVolume() * 2.0F, 1.0F);
        if (!isWeak()){
			if(entityliving instanceof EntityPig){
				HandleEating(30);
				//return;
			}
			if(entityliving instanceof EntitySheep){
				HandleEating(35);
				//return;
			}
			if(entityliving instanceof EntityCow){
				HandleEating(50);
				//return;
			}
			if(entityliving instanceof EntityChicken){
				HandleEating(20);
				//return;
			}
			if(entityliving instanceof EntityMob){
				HandleEating(20);
				//return;
			}
			heal(5);
        }
    }
	public boolean interact(EntityPlayer entityplayer)
    {
		ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		if (FMLCommonHandler.instance().getSide().isClient()){
					if (itemstack!=null && itemstack.itemID==mod_Fossil.DinoPedia.itemID){
			        	//this.ShowPedia(entityplayer);			//old function
			        	EntityDinosaurce.pediaingDino=this;		//new function
			        	mod_Fossil.callGUI(entityplayer, FossilGuiHandler.DINOPEDIA_GUI_ID, worldObj, (int)(this.posX), (int)(this.posY), (int)(this.posZ));
			return true;
		}
		}

		if (this.EOCInteract(itemstack, entityplayer)) return true;
		if (!this.isTamed()){
			if (itemstack!=null){
				
				if (itemstack.itemID==mod_Fossil.Gen.itemID){
			       if (this.isWeak() && !this.isTamed() && getDinoAge()>3){    		   
			    			   this.heal(200);
			    			   this.HandleEating(500);
			    			   this.WeakToDeath=0;
			    			   this.setTamed(true);
			    			   this.setOwner(entityplayer.username);
			    			   itemstack.stackSize--;
			    			   return true;	
			       }else{
			    	   if (!this.isWeak()){
			    		   SendStatusMessage(EnumSituation.GemErrorHealth,this.SelfType);
			    		   itemstack.stackSize--;
			    	   }
			    	   if (this.getDinoAge()<=3) SendStatusMessage(EnumSituation.GemErrorYoung,this.SelfType);
			    	   return false;
			       }
		 	   }else return false;
			}else return false;
		}else{
			 if(!worldObj.isRemote && (riddenByEntity == null || riddenByEntity == entityplayer))
		        {
				 	entityplayer.rotationYaw=this.rotationYaw;
		            entityplayer.mountEntity(this);
		            this.setPathToEntity(null);
		            this.renderYawOffset=this.rotationYaw;
		            return true;
		        } else
		        {
		            return false;
		        }
		}
    }


	public int getMaxSpawnedInChunk()
    {
        return 200;
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
		if (flag!=isSelfAngry()){
			if(flag)
			{
				dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 | 2)));

			} else
			{
				dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & -3)));
			}
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
			//mod_Fossil.ShowMessage("A raptor won't trust mankind anymore.");
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & -5)));
        }
    }

	private void InitSize(){
		setSize((float)(0.5F+0.5125*(float)getDinoAge()),(float)(0.5F+0.5125*(float)getDinoAge()));
		setPosition(posX,posY,posZ);
		attackStrength=4+getDinoAge();
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

	public void updateRiderPosition()
    {
        if(riddenByEntity == null)
        {
            return;
        } 
        if (this.riddenByEntity!=null)
        {
            riddenByEntity.setPosition(posX, posY+this.getGLY()*1.5, posZ);
        }

    }

	private void Flee(Entity EscapeFrom,int range){
		int DistanceX=new Random().nextInt(range)+1;
		int DistanceZ=(int)Math.round(Math.sqrt(Math.pow(range,2)-Math.pow(DistanceX,2)));
		int TargetX=0;
		int TargetY=0;
		int TargetZ=0;
        boolean flag = false;
        int i = -1;
        int j = -1;
        int k = -1;
        float f = -99999F;
		if (EscapeFrom.posX<=posX) TargetX=(int)Math.round(posX)+DistanceX;
		else TargetX=(int)Math.round(posX)-DistanceX;
		if (EscapeFrom.posZ<=posZ) TargetZ=(int)Math.round(posZ)+DistanceZ;
		else TargetZ=(int)Math.round(posZ)-DistanceZ;
		for(int ic=128;ic>0;ic--){
			if (!worldObj.isAirBlock(TargetX, ic,TargetZ)){
				TargetY=ic;
				break;
			}
		}
		setTamed(false);
		setSelfSitting(false);
		this.setPathToEntity(worldObj.getEntityPathToXYZ(this, TargetX, TargetY, TargetZ, range, true, false, true, false));

		//mod_Fossil.ShowMessage(new StringBuilder().append("Escaping to:").append(TargetX).append(",").append(TargetY).append(",").append(TargetZ).toString());

	}

	public void onLivingUpdate()
	{
		if (!this.isWeak())handleScream();
		else HandleWeak();
		super.onLivingUpdate();
	}

    private boolean isBaby() {
		return this.getDinoAge()<3;
	}
	protected void jump()
    {
    	if (!this.isInWater())
    	{
            if (this.riddenByEntity!=null) this.motionY+=0.41999998688697815D*1.5;
            else super.jump();
    	}
    	else
    		if (!this.onGround)this.motionY-=0.1D;
    }
	public void heal(int i)
    {
        if(this.getHealth() <= 0)
        {
            return;
        }
        this.setHealth(this.getHealth() + i);
        
        if (this.getHealth() > 200)
        	this.setHealth(200);
        //heartsLife = heartsHalvesLife / 2;
    }
	public boolean isWeak(){
		return false;
		//return ((this.getHunger()==1 || this.getHealth()<=20) && this.getDinoAge()>3 && !this.isTamed());
	}
	private void HandleWeak(){
		if (worldObj.isRemote) return;
		WeakToDeath++;
		if (WeakToDeath>=200) {
			this.attackEntityFrom(DamageSource.generic, 20);
			//mod_Fossil.ShowMessage("Died:Weak");
			return;
		}
		this.setTarget(null);
		this.setPathToEntity(null);
		this.setSelfAngry(false);
	}

		public void ShowPedia(EntityPlayer checker){
			//if (worldObj.isRemote) return;
			PediaTextCorrection(SelfType,checker);
			if (this.isTamed()){

					mod_Fossil.ShowMessage(new StringBuilder().append(OwnerText).append(this.getOwnerName()).toString(),checker);
					mod_Fossil.ShowMessage(new StringBuilder().append(AgeText).append(this.getDinoAge()).toString(),checker);
					mod_Fossil.ShowMessage(new StringBuilder().append(HelthText).append(this.getHealth()).append("/").append(20).toString(),checker);
					mod_Fossil.ShowMessage(new StringBuilder().append(HungerText).append(this.getHunger()).append("/").append(this.getHungerLimit()).toString(),checker);


				if (this.getDinoAge()>=4 && this.isTamed()) mod_Fossil.ShowMessage(RidiableText,checker);
			}else{
				if (!this.isWeak())mod_Fossil.ShowMessage(CautionText,checker);
				else mod_Fossil.ShowMessage(WeakText,checker);
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
				if (this.getDinoAge()>=4 && this.isTamed())
					resultList.add(RidiableText);
				if (!this.isWeak() && !this.isTamed()) resultList.add(CautionText);
				if (!resultList.isEmpty()) {
					result=new String[1];
					result=resultList.toArray(result);
				}
			}
			return result;
		}
		public void SetOrder(EnumOrderType input){
			this.OrderStatus=input;
		}

		public boolean HandleEating(int FoodValue, boolean FernFlag) {
			return HandleEating(FoodValue);
		}

		public boolean CheckEatable(int itemIndex) {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public EntityAgeable createChild(EntityAgeable entityanimal) {
			return new EntityTRex(worldObj);
		}

		@Override
	    public float getAIMoveSpeed()
	    {
	    	float f = 1.0F;
	        if (this.getDinoAge()<3){
		        f = super.getAIMoveSpeed();
		        if (fleeingTick > 0)
		        {
		            f *= 3.0F;
		        }
	        }else{
	        	if (this.riddenByEntity!=null && this.riddenByEntity instanceof EntityPlayerSP){
	        		EntityPlayerSP Rider=(EntityPlayerSP)this.riddenByEntity;
	        		if (Rider.movementInput.sneak) f=5f;
	        	}
	        }
	        return f;
	    }

		@Override
		public void updateSize(boolean shouldAddAge) {
			// TODO Auto-generated method stub
			
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
			return (float)(0.5F+0.5125*this.getDinoAge());
		}
		@Override
		public float getGLY() {
			return (float)(0.5F+0.5125*this.getDinoAge());
		}
}