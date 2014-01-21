package net.fossilsarch.common.entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;

import net.fossilsarch.mod_Fossil;
import net.fossilsarch.client.render.RenderRaptor;
import net.fossilsarch.common.ai.DinoAIFollowOwner;
import net.fossilsarch.common.ai.DinoAIGrowup;
import net.fossilsarch.common.ai.DinoAILearnChest;
import net.fossilsarch.common.ai.DinoAIPickItem;
import net.fossilsarch.common.ai.DinoAIStarvation;
import net.fossilsarch.common.ai.DinoAITargetNonTamedExceptSelfClass;
import net.fossilsarch.common.ai.DinoAIUseFeeder;
import net.fossilsarch.common.dinos.IHighIntellegent;
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
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.src.ModLoader;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;

public class EntityRaptor extends EntityDinosaurce implements IHighIntellegent {
	private boolean looksWithInterest;

	public final float HuntLimit = getHungerLimit() * 4 / 5;
	private float field_25048_b;
	private float field_25054_c;
	private boolean isWolfShaking;
	private boolean field_25052_g;
	private float timeWolfIsShaking;
	private float prevTimeWolfIsShaking;
	public ItemStack ItemInMouth = null;

	public int BreedTick = 3000;
	public boolean PreyChecked = false;
	public boolean SupportChecked = false;
	public Vector MemberList = new Vector();
	public float SwingAngle = RenderRaptor.SwingBackSignal;
	public int FleeingTick = 0;
	public int DoorOpeningTick = 0;
	
	private static Method fallInvoke = null; 

	public EntityRaptor(World world) {
		super(world);
		SelfType = EnumDinoType.Raptor;
		looksWithInterest = false;
		this.CheckSkin();
		setSize(0.3F, 0.3F);
		this.setHunger(getHungerLimit());
		this.getNavigator().setAvoidsWater(true);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(0, new DinoAIGrowup(this, 8));
		this.tasks.addTask(0, new DinoAIStarvation(this));

		this.tasks.addTask(1, new EntityAILeapAtTarget(this, 0.4F));
		this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityTRex.class,
				8.0F, 0.3F, 0.35F));
		this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityBrachiosaurus.class,
				8.0F, 0.3F, 0.35F));
		this.tasks.addTask(3, new EntityAIAttackOnCollide(this, 1.0f,
				true));
		this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
		this.tasks.addTask(5, new DinoAIFollowOwner(this, 1.0f, 5F,
				2.0F));
		this.tasks.addTask(6, new DinoAIUseFeeder(this, 1.0f, 24,
				this.HuntLimit, EnumDinoEating.Carnivorous));
        this.tasks.addTask(6, new DinoAIPickItem(this,Item.porkRaw,1.0f,24,this.HuntLimit));
        this.tasks.addTask(6, new DinoAIPickItem(this,Item.beefRaw,1.0f,24,this.HuntLimit));
        this.tasks.addTask(6, new DinoAIPickItem(this,Item.chickenRaw,1.0f,24,this.HuntLimit));
        this.tasks.addTask(6, new DinoAIPickItem(this,Item.porkCooked,1.0f,24,this.HuntLimit));
        this.tasks.addTask(6, new DinoAIPickItem(this,Item.beefCooked,1.0f,24,this.HuntLimit));
        this.tasks.addTask(6, new DinoAIPickItem(this,Item.chickenCooked,1.0f,24,this.HuntLimit));
        this.tasks.addTask(6, new DinoAIPickItem(this,mod_Fossil.RawDinoMeat,1.0f,24,this.HuntLimit));
        this.tasks.addTask(6, new DinoAIPickItem(this,mod_Fossil.CookedDinoMeat,1.0f,24,this.HuntLimit));
		this.tasks.addTask(7, new EntityAIWander(this, 0.3F));
		this.tasks.addTask(7, new DinoAILearnChest(this));

		this.tasks.addTask(8, new EntityAIWatchClosest(this,
				EntityPlayer.class, 8.0F));
		this.tasks.addTask(9, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
		this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
		this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(4, new DinoAITargetNonTamedExceptSelfClass(
				this, EntityLiving.class, 50, false));
		
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
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setAttribute(2.0D + this.getDinoAge());
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(20.0D);
    }

	public boolean isAIEnabled() {
		return true;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(this.CHEST_LEARNING_PROGRESS_DATA_INDEX,
				new Byte((byte) 0));
	}

	protected boolean canTriggerWalking() {
		return false;
	}

	protected void jump() {
		motionY = 0.41999998688697815D * (1 + this.getDinoAge() / 16);
		;
		if (isSprinting()) {
			float f = rotationYaw * 0.01745329F;
			motionX -= MathHelper.sin(f) * 0.2F;
			motionZ += MathHelper.cos(f) * 0.2F;
		}
		isAirBorne = true;
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		if (ItemInMouth != null) {
			nbttagcompound.setShort("Itemid", (short) ItemInMouth.itemID);
			nbttagcompound.setByte("ItemCount", (byte) ItemInMouth.stackSize);
			nbttagcompound.setShort("ItemDamage",
					(short) ItemInMouth.getItemDamage());
		} else {
			nbttagcompound.setShort("Itemid", (short) -1);
			nbttagcompound.setByte("ItemCount", (byte) 0);
			nbttagcompound.setShort("ItemDamage", (short) 0);
		}
		nbttagcompound.setBoolean("LearntChest", this.isLeartChest());
		nbttagcompound.setBoolean("Angry", isSelfAngry());
		nbttagcompound.setBoolean("Sitting", isSelfSitting());
		nbttagcompound.setInteger("SubType", this.getSubSpecies());
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		short Itemid = nbttagcompound.getShort("Itemid");
		byte ItemCount = nbttagcompound.getByte("ItemCount");
		short ItemDamage = nbttagcompound.getShort("ItemDamage");
		if (Itemid != -1)
			ItemInMouth = new ItemStack((int) Itemid, (int) ItemCount,
					(int) ItemDamage);
		else
			ItemInMouth = null;

		this.setHungerTick(nbttagcompound.getInteger("HungerTick"));

		this.setLearntChest(nbttagcompound.getBoolean("LearntChest"));
		setSelfAngry(nbttagcompound.getBoolean("Angry"));
		setSelfSitting(nbttagcompound.getBoolean("Sitting"));

		if (nbttagcompound.hasKey("SubType")) {
			this.setSubSpecies(nbttagcompound.getInteger("SubType"));
		}
		InitSize();


	}

	protected boolean canDespawn() {
		return false;
	}

	protected String getLivingSound() {
		// return "mob.wolf.growl";
		if (this.isTamed())
			return "raptor_living_friendly";
		else
			return "raptor_living_wild";
	}

	protected String getHurtSound() {
		return "Raptor_hurt";
		// return "raptor_living";
	}

	protected String getDeathSound() {
		return "raptor_death";
	}

	public boolean getCanSpawnHere() {
		return worldObj.checkNoEntityCollision(boundingBox)
				&& worldObj.getCollidingBoundingBoxes(this, boundingBox).size() == 0
				&& !worldObj.isAnyLiquid(boundingBox);
	}

	/*
	 * public void onLivingUpdate() { super.onLivingUpdate(); //this.OpenDoor();
	 * //1.If something in mouth,check if it's eat-able. if (ItemInMouth!=null){
	 * if (this.CheckEatable(ItemInMouth.itemID)){ ItemFood
	 * tmp=(ItemFood)ItemInMouth.getItem(); if (EatTick<=0){ if
	 * (HandleEating(30)){
	 * heal(((ItemFood)(ItemInMouth.getItem())).getHealAmount());
	 * ItemInMouth=null; } } } }else{ //2.If nothing in mouth,check if there are
	 * something pick-able around. if (!hasPath() &&
	 * OrderStatus==EnumOrderType.FreeMove && isTamed()){
	 * FindAndHoldItems(-1,6); } //3.If nothing around or self is not
	 * tamed,check if self is able to hunt or use feeder if (!hasPath() &&
	 * (OrderStatus
	 * ==EnumOrderType.FreeMove||OrderStatus==EnumOrderType.Follow)){ if
	 * (HuntForPrey(16D)){ if (!FindAndHoldItems(Item.porkRaw.shiftedIndex,32))
	 * FindAndHoldItems(Item.porkCooked.shiftedIndex,32); }else{ FindFeeder(16);
	 * } } }
	 * 
	 * }
	 */
	public void onUpdate() {
		super.onUpdate();
		/*
		 * Level 1 AI seq.
		 */
		// HangleChestLearning();
		HandleBreed();
		field_25054_c = field_25048_b;
		if (looksWithInterest) {
			field_25048_b = field_25048_b + (1.0F - field_25048_b) * 0.4F;
		} else {
			field_25048_b = field_25048_b + (0.0F - field_25048_b) * 0.4F;
		}
		if (looksWithInterest) {
			numTicksToChaseTarget = 10;
		}
		
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setAttribute(2.0D + this.getDinoAge());
	}

	public boolean getSelfShaking() {
		return false;
	}

	public float getShadingWhileShaking(float f) {
		return 0.75F + ((prevTimeWolfIsShaking + (timeWolfIsShaking - prevTimeWolfIsShaking)
				* f) / 2.0F) * 0.25F;
	}

	public float getShakeAngle(float f, float f1) {
		float f2 = (prevTimeWolfIsShaking
				+ (timeWolfIsShaking - prevTimeWolfIsShaking) * f + f1) / 1.8F;
		if (f2 < 0.0F) {
			f2 = 0.0F;
		} else if (f2 > 1.0F) {
			f2 = 1.0F;
		}
		return MathHelper.sin(f2 * 3.141593F)
				* MathHelper.sin(f2 * 3.141593F * 11F) * 0.15F * 3.141593F;
	}

	public float getEyeHeight() {
		return height * 0.8F;
	}

	public int getVerticalFaceSpeed() {
		if (isSelfSitting()) {
			return 20;
		} else {
			return super.getVerticalFaceSpeed();
		}
	}

	protected boolean isMovementCeased() {
		return isSelfSitting() || field_25052_g;
	}

	public boolean attackEntityFrom(DamageSource damagesource, int i)// being
																		// attack
	{
		Entity entity = damagesource.getEntity();
		boolean isPlayerAttack = false;
		setSelfSitting(false);// stand up(dog)
		if (entity != null && !(entity instanceof EntityPlayer)
				&& !(entity instanceof EntityArrow))// (if attacker not player
													// or arrow)
		{
			i = (i + 1) / 2;
		}
		if (super.attackEntityFrom(damagesource, i)) {
			if (!isSelfAngry()) {
				if (entity instanceof EntityPlayer) {
					setTamed(false);
					setOwner("");
					ItemInMouth = null;
					// setSelfAngry(true);
					this.PreyChecked = true;
					isPlayerAttack = true;
				}
				if ((entity instanceof EntityArrow)
						&& ((EntityArrow) entity).shootingEntity != null) {
					entity = ((EntityArrow) entity).shootingEntity;
				}
				if (entity instanceof EntityLiving) {
					this.setAttackTarget((EntityLiving) entity);
				}
			} else if (entity != this && entity != null) {
				entityToAttack = entity;
			}
			return true;
		} else {
			return false;
		}
	}

	protected Entity findPlayerToAttack() {
		/*
		 * if(isSelfAngry()) { return worldObj.getClosestPlayerToEntity(this,
		 * 16D); } else {
		 */
		return null;
		// }
	}

	protected void attackEntity(Entity entity, float f) {
		// mod_Fossil.ShowMessage(new
		// StringBuilder().append("Range:").append(f).toString());
		// mod_Fossil.ShowMessage(new
		// StringBuilder().append("GLsize:").append(GLsizeX).toString());
		// mod_Fossil.ShowMessage(new
		// StringBuilder().append("BondingX:").append(boundingBox.minX).append(",").append(boundingBox.maxX).toString());
		if (entity.isDead)
			this.setAttackTarget(null);
		if (f > 2.0F && f < 5.0F && rand.nextInt(10) == 0) {
			if (onGround) {
				double d = entity.posX - posX;
				double d1 = entity.posZ - posZ;
				float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
				motionX = (d / (double) f1) * 0.5D * 0.80000001192092896D
						+ motionX * 0.20000000298023224D;
				motionZ = (d1 / (double) f1) * 0.5D * 0.80000001192092896D
						+ motionZ * 0.20000000298023224D;
				worldObj.playSoundAtEntity(this, "Raptor_attack",
						getSoundVolume() * 2.0F, 1.0F);
				jump();
			}
		} else if ((double) f < 1.9F
				&& entity.boundingBox.maxY > boundingBox.minY
				&& entity.boundingBox.minY < boundingBox.maxY) {
			attackTime = 20;
			entity.attackEntityFrom(DamageSource.causeMobDamage(this), 2 + this.getDinoAge());
		}
	}

	public boolean interact(EntityPlayer entityplayer) {
		ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		if (itemstack != null) {
			if (itemstack.getItem().getItemUseAction(itemstack) == EnumAction.bow)
				return false;
			if (FMLCommonHandler.instance().getSide().isClient()){
				if (itemstack.itemID == mod_Fossil.DinoPedia.itemID) {
		        	//this.ShowPedia(entityplayer);			//old function
		        	EntityDinosaurce.pediaingDino=this;		//new function
		        	mod_Fossil.callGUI(entityplayer, FossilGuiHandler.DINOPEDIA_GUI_ID, worldObj, (int)(this.posX), (int)(this.posY), (int)(this.posZ));
				return true;
			}
			}

			if (isTamed()) {
				if (itemstack.itemID == mod_Fossil.ChickenEss.itemID) {
					if (this.getDinoAge() >= 8 || this.getHunger() <= 0)
						return false;
					itemstack.stackSize--;
					if (itemstack.stackSize <= 0) {
						entityplayer.inventory.setInventorySlotContents(
								entityplayer.inventory.currentItem, null);
					}
					entityplayer.inventory
							.addItemStackToInventory(new ItemStack(
									Item.glassBottle, 1));
					this.setDinoAgeTick(GROW_TIME_COUNT);
					this.setHunger(1 + new Random().nextInt(this.getHunger()));
					return true;
				}
				if (ItemInMouth == null) {
					ItemInMouth = new ItemStack(itemstack.getItem(), 1,
							itemstack.getItemDamage());
					itemstack.stackSize--;
					if (itemstack.stackSize <= 0) {
						entityplayer.inventory.setInventorySlotContents(
								entityplayer.inventory.currentItem, null);
					}
					return true;
				}

			}
		} else {
			if (ItemInMouth != null) {
				/*
				 * if(worldObj.multiplayerWorld) { return false; }
				 */
				int i = ItemInMouth.stackSize;
				if (entityplayer.inventory.addItemStackToInventory(ItemInMouth)) {
					ModLoader.onItemPickup(entityplayer, ItemInMouth);
					worldObj.playSoundAtEntity(
							entityplayer,
							"random.pop",
							0.2F,
							((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				} else
					return false;
				ItemInMouth = null;
				return true;
			} else {
				if (entityplayer.username.equalsIgnoreCase(getOwnerName())) {
					if (!worldObj.isRemote) {
						isJumping = false;
						setPathToEntity(null);
						this.SetOrder(EnumOrderType.values()[(mod_Fossil
								.EnumToInt(OrderStatus) + 1) % 3]);
						SendOrderMessage(OrderStatus);
						switch (OrderStatus) {
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

	public void handleHealthUpdate(byte byte0) {
		if (byte0 == 7) {
			showHeartsOrSmokeFX(true);
		} else if (byte0 == 6) {
			showHeartsOrSmokeFX(false);
		} else if (byte0 == 8) {
			field_25052_g = true;
			timeWolfIsShaking = 0.0F;
			prevTimeWolfIsShaking = 0.0F;
		} else {
			super.handleHealthUpdate(byte0);
		}
	}

	public int getMaxSpawnedInChunk() {
		return 100;
	}

	public boolean isSelfAngry() {
		return (dataWatcher.getWatchableObjectByte(16) & 2) != 0;
	}

	public boolean isSelfSitting() {
		return (dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	public void setSelfAngry(boolean flag) {
		byte byte0 = dataWatcher.getWatchableObjectByte(16);
		if (flag) {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (byte0 | 2)));
		} else {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (byte0 & -3)));
		}
	}

	public void setSelfSitting(boolean flag) {
		byte byte0 = dataWatcher.getWatchableObjectByte(16);
		if (flag) {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (byte0 | 1)));
		} else {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (byte0 & -2)));
		}
	}

	public void setTamed(boolean flag) {
		byte byte0 = dataWatcher.getWatchableObjectByte(16);
		if (flag) {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (byte0 | 4)));
		} else {
			ItemInMouth = null;
			// mod_Fossil.ShowMessage("A velociraptor won't trust mankind anymore.");
			dataWatcher.updateObject(16, Byte.valueOf((byte) (byte0 & -5)));
		}
	}

	protected void fall(float f) {
		// super.fall(f);
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
		
		int i = (int) Math.ceil(f - 3F);
		if (i > 0) {
			attackEntityFrom(DamageSource.fall, 0);
			int j = worldObj.getBlockId(
					MathHelper.floor_double(posX),
					MathHelper.floor_double(posY - 0.20000000298023224D
							- (double) yOffset), MathHelper.floor_double(posZ));
			if (worldObj.isRemote) return;
			if (j > 0) {
				StepSound stepsound = Block.blocksList[j].stepSound;
				worldObj.playSoundAtEntity(this, stepsound.getBreakSound(),
						stepsound.getVolume() * 0.5F,
						stepsound.getPitch() * 0.75F);
			}
		}
	}

	protected void updateWanderPath() {
		boolean flag = false;
		int i = -1;
		int j = -1;
		int k = -1;
		float f = -99999F;
		if (OrderStatus == OrderStatus.FreeMove || !isTamed()) {
			for (int l = 0; l < 10; l++) {
				int i1 = MathHelper.floor_double((posX + (double) rand
						.nextInt(13)) - 6D);
				int j1 = MathHelper.floor_double((posY + (double) rand
						.nextInt(7)) - 3D);
				int k1 = MathHelper.floor_double((posZ + (double) rand
						.nextInt(13)) - 6D);
				float f1 = getBlockPathWeight(i1, j1, k1);
				if (f1 > f) {
					f = f1;
					i = i1;
					j = j1;
					k = k1;
					flag = true;
				}
			}

			if (flag) {
				setPathToEntity(worldObj.getEntityPathToXYZ(this, i, j, k, 10F,
						true, false, true, false));
			}
		}
	}

	private void InitSize() {
		this.CheckSkin();
		setSize((float) (0.3F + 0.1 * (float) this.getAge()),
				(float) (0.3F + 0.1 * (float) this.getAge()));
		setPosition(posX, posY, posZ);
	}

	@Override
	public boolean HandleEating(int FoodValue) {
		if (this.getHunger() >= getHungerLimit()) {
			return false;
		}
		this.increaseHunger(FoodValue);
		showHeartsOrSmokeFX(false);
		if (this.getHunger() >= getHungerLimit())
			this.setHunger(getHungerLimit());
		return true;
	}

	public boolean isLeartChest() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 16) == 0;
	}

	public void ChangeSubType(int type) {
		if (type <= 2 && type >= 0) {
			this.setSubSpecies(type);
			this.CheckSkin();
		}
	}

	@Override
	public void ShowPedia(EntityPlayer checker) {
		//if (worldObj.isRemote) return;
		PediaTextCorrection(SelfType,checker);
		if (this.isTamed()) {

				mod_Fossil.ShowMessage(new StringBuilder().append(OwnerText)
						.append(this.getOwnerName()).toString(),checker);
				mod_Fossil.ShowMessage(new StringBuilder().append(AgeText)
						.append(this.getDinoAge()).toString(),checker);
				mod_Fossil.ShowMessage(new StringBuilder().append(HelthText)
						.append(this.getHealth()).append("/").append(20).toString(),checker);
				mod_Fossil.ShowMessage(new StringBuilder().append(HungerText)
						.append(this.getHunger()).append("/")
						.append(this.getHungerLimit()).toString(),checker);

			if (this.isLeartChest()) {
				mod_Fossil.ShowMessage(EnableChestText,checker);
			}
		} else {

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
			if (this.isLeartChest())
				resultList.add(EnableChestText);
			if (!resultList.isEmpty()) {
				result=new String[1];
				result=resultList.toArray(result);
			}
		}
		return result;
	}
	/*
	 * public void HandleBreed(){ if (this.age>3){ this.BreedTick--; if
	 * (this.BreedTick==0){ int GroupAmount=0; List list =
	 * worldObj.getEntitiesWithinAABBExcludingEntity(this,
	 * boundingBox.expand(32D, 32D, 32D)); for (int i=0;i<list.size();i++){ if
	 * (list.get(i) instanceof EntityRaptor) GroupAmount++; } if
	 * (GroupAmount>50) GroupAmount=50; if (GroupAmount>80) return; if (new
	 * Random().nextInt(100)<GroupAmount){ EntityDinoEgg entityegg=null; if
	 * (this.isTamed()) entityegg = (EntityDinoEgg)new
	 * EntityDinoEgg(worldObj,SelfType,(IDino)this); else entityegg =
	 * (EntityDinoEgg)new EntityDinoEgg(worldObj,SelfType);
	 * entityegg.setLocationAndAngles(this.posX+(new Random().nextInt(3)-1),
	 * this.posY, this.posZ+(new Random().nextInt(3)-1),
	 * worldObj.rand.nextFloat() * 360F, 0.0F); if(
	 * worldObj.checkIfAABBIsClear(entityegg.boundingBox) &&
	 * worldObj.getCollidingBoundingBoxes(entityegg,
	 * entityegg.boundingBox).size() == 0)
	 * worldObj.spawnEntityInWorld(entityegg); showHeartsOrSmokeFX(true); }
	 * this.BreedTick=3000; } } }
	 */
	public void SetOrder(EnumOrderType input) {
		this.OrderStatus = input;
	}

	public boolean HandleEating(int FoodValue, boolean FernFlag) {
		return this.HandleEating(FoodValue);
	}

	public boolean CheckEatable(int itemIndex) {
		if (!(Item.itemsList[itemIndex] instanceof ItemFood))
			return false;
		Item tmp = Item.itemsList[itemIndex];
		boolean result = false;
		result = (tmp == Item.beefCooked) || (tmp == Item.beefRaw)
				|| (tmp == Item.fishCooked) || (tmp == Item.fishRaw)
				|| (tmp == Item.chickenCooked) || (tmp == Item.chickenRaw)
				|| (tmp == Item.porkRaw) || (tmp == Item.porkCooked);
		return result;
	}

	@Override
	public EntityAgeable createChild(EntityAgeable entityanimal) {
		return new EntityRaptor(worldObj);
	}

	public boolean IsIdle() {
		return (this.motionX < 0.03125F) && (this.motionY < 0.03125F)
				&& (this.motionZ < 0.03125F);
	}

	public void updateSize(boolean shouldAddAge) {

	}

	public EnumOrderType getOrderType() {
		return this.OrderStatus;
	}

	@Override
	protected int foodValue(Item asked) {

		return 0;
	}

	@Override
	public void HandleEating(Item food) {

	}

	@Override
	public void HoldItem(Item itemGot) {

	}

	@Override
	public void setLearntChest(boolean var) {
		byte var2 = this.dataWatcher
				.getWatchableObjectByte(this.CHEST_LEARNING_PROGRESS_DATA_INDEX);

		if (var) {
			this.dataWatcher.updateObject(
					this.CHEST_LEARNING_PROGRESS_DATA_INDEX,
					Byte.valueOf((byte) (var2 & -17)));
		} else {
			this.dataWatcher.updateObject(
					this.CHEST_LEARNING_PROGRESS_DATA_INDEX,
					Byte.valueOf((byte) (var2 | 16)));
		}

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