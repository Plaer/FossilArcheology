package net.fossilsarch.common.entity;

import java.util.*;

import net.fossilsarch.mod_Fossil;
import net.fossilsarch.common.ai.DinoAIControlledByPlayer;
import net.fossilsarch.common.ai.DinoAIEatFerns;
import net.fossilsarch.common.ai.DinoAIFollowOwner;
import net.fossilsarch.common.ai.DinoAIGrowup;
import net.fossilsarch.common.ai.DinoAIPickItem;
import net.fossilsarch.common.ai.DinoAIStarvation;
import net.fossilsarch.common.ai.DinoAIUseFeeder;
import net.fossilsarch.common.ai.DinoAIWander;
import net.fossilsarch.common.handlers.FossilGuiHandler;
import net.fossilsarch.common.io.EnumDinoEating;
import net.fossilsarch.common.io.EnumDinoType;
import net.fossilsarch.common.io.EnumOrderType;
import net.fossilsarch.common.io.EnumSituation;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;

public class EntityTriceratops extends EntityDinosaurce {
	private boolean looksWithInterest;
	public final float HuntLimit = getHungerLimit() * 4 / 5;
	private float field_25048_b;
	private float field_25054_c;
	private boolean field_25052_g;
	protected final int AGE_LIMIT = 12;
	public int RushTick = 0;
	public int BreedTick = 3000;
	public boolean Running = false;

	public EntityTriceratops(World world) {
		this(world, randomSpawnAge(world.rand));
		this.setSubSpecies(worldObj.rand.nextInt(3)+1);
		this.OrderStatus=EnumOrderType.FreeMove;
	}
	
	public EntityTriceratops(World world, int age) {
		super(world);
		
		this.setDinoAge(age);
		
		setSize((float) (1.5F + 0.3 * (float) this.getDinoAge()),
				(float) (1.5F + 0.3 * (float) this.getDinoAge()));
		
		this.OrderStatus = EnumOrderType.FreeMove;
		SelfType = EnumDinoType.Triceratops;
		looksWithInterest = false;

		this.updateSize(false);
		this.setHealth(8+age);
		this.getNavigator().setAvoidsWater(true);
		this.tasks.addTask(0, new DinoAIGrowup(this, AGE_LIMIT));
		this.tasks.addTask(0, new DinoAIStarvation(this));
		// this.tasks.addTask(1,new DinoAIHandleRiding(this,null,4));
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2,
				this.ridingHandler = new DinoAIControlledByPlayer(this, 0.34F));
		this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0f,
				true));
		this.tasks.addTask(5, new DinoAIFollowOwner(this, 1.0f, 5F,
				2.0F));
		this.tasks.addTask(6, new DinoAIEatFerns(this, this.HuntLimit));
		this.tasks.addTask(6, new DinoAIUseFeeder(this, 1.0f, 24,
				this.HuntLimit, EnumDinoEating.Herbivorous));
		this.tasks.addTask(7, new DinoAIPickItem(this, Item.wheat,
				1.0f, 24, this.HuntLimit));
		this.tasks.addTask(7, new DinoAIPickItem(this, Item.appleRed,
				1.0f, 24, this.HuntLimit));
		this.tasks.addTask(8, new DinoAIWander(this, 1.0f));
		this.tasks.addTask(9, new EntityAIWatchClosest(this,
				EntityPlayer.class, 8.0F));
		this.tasks.addTask(10, new EntityAILookIdle(this));
	}
	
	@Override
	public float getAIMoveSpeed() {
		float speed = super.getAIMoveSpeed();
		
		speed *= (0.7f + 0.2f*this.getDinoAge());
		
		if (this.isSelfAngry()) speed *= 2.0f;
		else speed *= 0.5f;
		
		return speed;
	}
	
	private static int randomSpawnAge(Random random) {
		boolean isChild = random.nextInt(4) == 0;
		if (isChild) {
			return random.nextInt(4);
		} else {
			return random.nextInt(9) + 4;
		}
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();	
		
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(20);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.3D);
	}
	
	public int getHungerLimit(){
		return 500;
	}
	public boolean isAIEnabled() {
		return (!this.isModelized());
	}

	public boolean isBaby() {
		return this.getDinoAge() <= 4;
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setInteger("SubSpecies", this.getSubSpecies());
		nbttagcompound.setBoolean("Angry", isSelfAngry());
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.setSubSpecies(nbttagcompound.getInteger("SubSpecies"));
		CheckSkin();
		
		boolean angry = nbttagcompound.getBoolean("Angry");
		if (!this.isModelized())
			setSelfAngry(angry);
		InitSize();
	}

	protected boolean canDespawn() {
		return false;
	}

	protected String getLivingSound() {
		if (worldObj.getClosestPlayerToEntity(this, 8.0D) != null)
			return "fossilsarch:tri_roar";

		else
			return null;

	}

	protected String getHurtSound() {
		return "mob.cow.hurt";
	}

	protected String getDeathSound() {
		return "fossilsarch:tri_death";
	}

	public void onUpdate() {
		super.onUpdate();
		field_25054_c = field_25048_b;
		if (looksWithInterest) {
			field_25048_b = field_25048_b + (1.0F - field_25048_b) * 0.4F;
		} else {
			field_25048_b = field_25048_b + (0.0F - field_25048_b) * 0.4F;
		}
		if (looksWithInterest) {
			numTicksToChaseTarget = 10;
		}
	}

	public void onLivingUpdate() {
		super.onLivingUpdate();
		// this.HandleRiding();
	}

	public float getInterestedAngle(float f) {
		return (field_25054_c + (field_25048_b - field_25054_c) * f) * 0.15F * 3.141593F;
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

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float i) {
		if (this.modelizedDrop())
			return true;
		Entity entity = damagesource.getEntity();
		setSelfSitting(false);
		if (entity != null && !(entity instanceof EntityPlayer)
				&& !(entity instanceof EntityArrow)) {
			i = (i + 1) / 2;
		}
		if (super.attackEntityFrom(damagesource, i)) {
			if (!isTamed() && !isSelfAngry()) {
				if (entity instanceof EntityPlayer) {
					setSelfAngry(true);
					entityToAttack = entity;
				}
				if ((entity instanceof EntityArrow)
						&& ((EntityArrow) entity).shootingEntity != null) {
					entity = ((EntityArrow) entity).shootingEntity;
				}
			} else if (entity != this && entity != null) {
				if (isTamed()
						&& (entity instanceof EntityPlayer)
						&& ((EntityPlayer) entity).username
								.equalsIgnoreCase(getOwnerName())) {
					return true;
				}
				entityToAttack = entity;
			}
			return true;
		} else {
			return false;
		}
	}

	protected Entity findPlayerToAttack() {
		if (isSelfAngry()) {
			return worldObj.getClosestPlayerToEntity(this, 16D);
		} else {
			return null;
		}
	}

	public boolean interact(EntityPlayer entityplayer) {
		if (this.isModelized())
			return modelizedInteract(entityplayer);
		ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		if (itemstack != null && itemstack.itemID == Item.wheat.itemID) {
			// if (EatTick<=0){
			if (this.CheckEatable(itemstack.itemID) && HandleEating(10)) {
				itemstack.stackSize--;
				if (itemstack.stackSize <= 0) {
					entityplayer.inventory.setInventorySlotContents(
							entityplayer.inventory.currentItem, null);
				}
				heal(3);

			}

			return true;
		}
		if (FMLCommonHandler.instance().getSide().isClient()) {
			if (itemstack != null
					&& itemstack.itemID == mod_Fossil.DinoPedia.itemID) {
				// this.ShowPedia(entityplayer); //old function
				EntityDinosaurce.pediaingDino = this; // new function
				mod_Fossil
						.callGUI(entityplayer,
								FossilGuiHandler.DINOPEDIA_GUI_ID, worldObj,
								(int) (this.posX), (int) (this.posY),
								(int) (this.posZ));
				return true;
			}
		}

		if (this.EOCInteract(itemstack, entityplayer))
			return true;
		if (itemstack != null && itemstack.itemID == Item.stick.itemID) {
			if (entityplayer.username.equalsIgnoreCase(getOwnerName())) {
				if (!worldObj.isRemote) {
					isJumping = false;
					setPathToEntity(null);
					OrderStatus = EnumOrderType.values()[(mod_Fossil
							.EnumToInt(OrderStatus) + 1) % 3];
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
		if (this.isTamed() && this.getDinoAge() > 4 && !worldObj.isRemote
				&& (riddenByEntity == null || riddenByEntity == entityplayer)) {
			entityplayer.rotationYaw = this.rotationYaw;
			entityplayer.mountEntity(this);
			this.setPathToEntity(null);
			this.renderYawOffset = this.rotationYaw;
			return true;
		}
		return false;
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
			dataWatcher.updateObject(16, Byte.valueOf((byte) (byte0 & -5)));
		}
	}

	public boolean getCanSpawnHere() {
		return worldObj.checkNoEntityCollision(boundingBox)
				&& worldObj.getCollidingBoundingBoxes(this, boundingBox).size() == 0
				&& !worldObj.isAnyLiquid(boundingBox);
	}

	private void InitSize() {
		updateSize(false);
		setPosition(posX, posY, posZ);
	}

	private void ChangeTexture() {
		CheckSkin();
	}

	public void updateRiderPosition() {
		if (riddenByEntity == null) {
			return;
		} else {
			riddenByEntity.setPosition(posX, posY + this.getGLY() * 0.65 + 0.07
					* (12 - this.getDinoAge()), posZ);
			// riddenByEntity.setRotation(this.rotationYaw,riddenByEntity.rotationPitch);
			// ((EntityLiving)riddenByEntity).renderYawOffset=this.rotationYaw;
			return;
		}
	}

	public boolean HandleEating(int FoodValue) {
		return HandleEating(FoodValue, false);
	}

	public boolean HandleEating(int FoodValue, boolean FernFlag) {
		if (this.getHunger() >= getHungerLimit()) {
			if (isTamed() && !FernFlag)
				SendStatusMessage(EnumSituation.Full, this.SelfType);
			return false;
		}
		this.increaseHunger(FoodValue);
		showHeartsOrSmokeFX(false);
		if (this.getHunger() >= getHungerLimit())
			this.setHunger(getHungerLimit());
		return true;
	}

	/*
	 * protected void HandleStarvation(){ if (this.isDead) return; if
	 * (Hunger<=0) { if (isTamed()) SendStatusMessage(EnumSituation.Starve);
	 * attackEntityFrom(DamageSource.starve, 20); return; } if
	 * (Hunger==HUNGER_LIMIT/2 && HungerTick==HungerTickLimit/10) { if
	 * (isTamed()) SendStatusMessage(EnumSituation.Hungry); return; }
	 * 
	 * }
	 */
	private boolean FindWheats(int range) {
		if (!isSelfSitting()) {
			List NearBy = worldObj.getEntitiesWithinAABBExcludingEntity(this,
					boundingBox.expand(1.0D, 0.0D, 1.0D));
			if (NearBy != null) {
				for (int i = 0; i < NearBy.size(); i++) {
					if (NearBy.get(i) instanceof EntityItem) {
						EntityItem ItemInRange = (EntityItem) NearBy.get(i);
						if (ItemInRange.getEntityItem().itemID == Item.wheat.itemID) {
							HandleEating(10);
							worldObj.playSoundAtEntity(this, "random.pop",
									0.2F,
									((new Random().nextFloat() - new Random()
											.nextFloat()) * 0.7F + 1.0F) * 2.0F);
							ItemInRange.setDead();
							return true;
						}
					}
				}
			}
			EntityItem TargetItem = null;
			List searchlist = worldObj.getEntitiesWithinAABB(
					EntityItem.class,
					AxisAlignedBB
							.getAABBPool()
							.getAABB(posX, posY, posZ,
									posX + 1.0D, posY + 1.0D, posZ + 1.0D)
							.expand(range, 4D, range));
			Iterator iterator = searchlist.iterator();
			do {
				if (!iterator.hasNext()) {
					break;
				}
				Entity entity1 = (Entity) iterator.next();
				if (entity1 instanceof EntityItem) {
					EntityItem Tmp = (EntityItem) entity1;
					if (Tmp.getEntityItem().getItem().itemID == Item.wheat.itemID) {
						if (TargetItem != null) {
							if (GetDistanceWithEntity(Tmp) < GetDistanceWithEntity(TargetItem)) {
								TargetItem = Tmp;
							}
						} else {
							TargetItem = Tmp;
						}
					}
				}
			} while (true);
			if (TargetItem != null) {
				setPathToEntity(worldObj.getPathEntityToEntity(this,
						TargetItem, (float) range, true, false, true, false));
				return true;
			} else
				return false;
		} else
			return false;
	}

	/*
	 * private boolean FindFeeder(int range){ if(range>6) range=6;
	 * TileEntityFeeder target=null; if (GetNearestTileEntity(2,range)!=null){
	 * target=(TileEntityFeeder)GetNearestTileEntity(2,range); if
	 * (GetDistanceWithTileEntity((TileEntity)target)>3){
	 * //mod_Fossil.ShowMessage(new
	 * StringBuilder().append("Setting Path:").append
	 * (target.xCoord).append(",").
	 * append(target.yCoord).append(",").append(target.zCoord).toString());
	 * setPathToEntity
	 * (worldObj.getEntityPathToXYZ((Entity)this,(int)Math.round(target
	 * .xCoord),(
	 * int)Math.round(target.yCoord),(int)Math.round(target.zCoord),(float
	 * )range, true, false, true, false));
	 * 
	 * }else{ if (target.CheckIsEmpty(this))return false;
	 * FaceToCoord(target.xCoord,target.yCoord,target.zCoord);
	 * target.Feed(this); this.setTarget(null); } return true; }else return
	 * false; } private TileEntity GetNearestTileEntity(int targetType,int
	 * range){ final int Chest=0; final int Furance=1; final int Feeder=2;
	 * TileEntity result=null; TileEntity tmp=null; if (range>6) range=6; float
	 * Distance=range+1; for (int
	 * i=(int)Math.round(posX)-range;i<=(int)Math.round(posX)+range;i++){ for
	 * (int
	 * j=(int)Math.round(posY)-range/2;j<=(int)Math.round(posY)+range/2;j++){
	 * for (int
	 * k=(int)Math.round(posZ)-range;k<=(int)Math.round(posZ)+range;k++){
	 * //mod_Fossil.ShowMessage(new
	 * StringBuilder().append(worldObj.getBlockTileEntity(i,j,k)).toString());
	 * //mod_Fossil.ShowMessage(new
	 * StringBuilder().append(i).append(",").append(
	 * j).append(",").append(k).toString());
	 * tmp=worldObj.getBlockTileEntity(i,j,k); if (tmp!=null){ if((tmp
	 * instanceof
	 * TileEntityFeeder)&&(!((TileEntityFeeder)tmp).CheckIsEmpty(this))){ float
	 * TempDis=GetDistanceWithTileEntity(tmp); //mod_Fossil.ShowMessage(new
	 * StringBuilder().append(TempDis).toString()); if (TempDis<Distance) {
	 * Distance=TempDis; result=tmp; } } } } } } return result; }
	 */

	/*
	 * private void HandleRiding(){ if(riddenByEntity != null) {
	 * 
	 * if (this.riddenByEntity instanceof EntityPlayerSP){ if (this.onGround){
	 * if (((EntityPlayerSP)this.riddenByEntity).movementInput.jump){
	 * this.jump();
	 * ((EntityPlayerSP)this.riddenByEntity).movementInput.jump=false; }
	 * //this.riderMovingStraif=(int) (((EntityPlayerSP)
	 * this.riddenByEntity).moveStrafing); //this.moveForward=((EntityPlayerSP)
	 * this.riddenByEntity).moveForward*this.moveSpeed; //if (this.SneakScream)
	 * this.SneakScream=false; } } }
	 * 
	 * }
	 */
	public void applyEntityCollision(Entity entity) {
		if (entity instanceof EntityLivingBase && !(entity instanceof EntityPlayer)) {
			if (this.riddenByEntity != null && this.onGround) {
				this.onKillEntity((EntityLivingBase) entity);
				((EntityLivingBase) entity).attackEntityFrom(
						DamageSource.causeMobDamage(this), 10);
				return;
			}
		}
		super.applyEntityCollision(entity);
	}

	public void BlockInteractive() {
		for (int j = (int) Math.round(boundingBox.minX) - 1; j <= (int) Math
				.round(boundingBox.maxX) + 1; j++) {
			for (int k = (int) Math.round(boundingBox.minY); k <= (int) Math
					.round(boundingBox.maxY); k++) {
				for (int l = (int) Math.round(boundingBox.minZ) - 1; l <= (int) Math
						.round(boundingBox.maxZ) + 1; l++) {
					if (!worldObj.isAirBlock(j, k, l) /*
													 * && new
													 * Random().nextInt(10)==5
													 */) {
						int blockid = worldObj.getBlockId(j, k, l);
						if (!this.inWater) {
							if (!this.isTamed() || this.riddenByEntity != null) {

								if (Block.blocksList[blockid].getBlockHardness(
										worldObj, (int) posX, (int) posY,
										(int) posZ) < 5.0
										|| blockid == Block.wood.blockID) {
									if (new Random().nextInt(10) == 5)
										Block.blocksList[blockid]
												.dropBlockAsItem(worldObj, j,
														k, l, 1, 0);
									worldObj.setBlock(j, k, l, 0);
									this.RushTick = 10;
								}

							} else {
								if (blockid == Block.wood.blockID
										|| blockid == Block.leaves.blockID) {
									worldObj.setBlock(j, k, l, 0);
									this.RushTick = 10;
								}
							}
						}

					}
				}
			}
		}
	}

	public boolean isWheat(ItemStack par1ItemStack) {
		return par1ItemStack != null
				&& par1ItemStack.itemID == Item.wheat.itemID;
	}

	public void ShowPedia(EntityPlayer checker) {
		// if (!isClientWorld()) return;
		PediaTextCorrection(SelfType, checker);
		if (this.isTamed()) {
			mod_Fossil.ShowMessage(new StringBuilder().append(OwnerText)
					.append(this.getOwnerName()).toString(), checker);
			mod_Fossil.ShowMessage(
					new StringBuilder().append(AgeText)
							.append(this.getDinoAge()).toString(), checker);
			mod_Fossil.ShowMessage(new StringBuilder().append(HelthText)
					.append(this.getHealth()).append("/").append(20).toString(),
					checker);
			mod_Fossil.ShowMessage(
					new StringBuilder().append(HungerText)
							.append(this.getHunger()).append("/")
							.append(this.getHungerLimit()).toString(), checker);

			if ((this.isTamed() && this.getDinoAge() > 4 && riddenByEntity == null))
				mod_Fossil.ShowMessage(RidiableText, checker);
		} else {
			mod_Fossil.ShowMessage(UntamedText, checker);
		}

	}

	public void SetOrder(EnumOrderType input) {
		this.OrderStatus = input;
	}

	public boolean CheckEatable(int itemIndex) {
		Item tmp = Item.itemsList[itemIndex];
		boolean result = false;
		result = (tmp == Item.wheat) || (tmp == Item.melon);
		return result;
	}

	@Override
	public EntityAgeable createChild(EntityAgeable entityanimal) {

		return new EntityTriceratops(worldObj, 0);
	}
	
	/*
	 * public float getSpeedModifier() { float f = 1.0F; if
	 * (this.getDinoAge()<3){ f = super.getSpeedModifier(); if (fleeingTick > 0)
	 * { f *= 3.0F; } }else{ if (this.riddenByEntity!=null &&
	 * this.riddenByEntity instanceof EntityPlayerSP){ EntityPlayerSP
	 * Rider=(EntityPlayerSP)this.riddenByEntity; if (Rider.movementInput.sneak)
	 * { if (this.RushTick==0)f=5f; else f*=(3F/4F); } } } return f; }
	 */

	private boolean FindFren(int range) {
		float TempDis = range * 2;
		int targetX = 0;
		int targetY = 0;
		int targetZ = 0;
		for (int i = -range; i <= range; i++) {
			for (int j = -2; j <= 2; j++) {
				for (int k = -range; k <= range; k++) {
					if (worldObj.getBlockId((int) Math.round(posX + i),
							(int) Math.round(posY + j),
							(int) Math.round(posZ + k)) == mod_Fossil.Ferns.blockID) {
						if (TempDis > GetDistanceWithXYZ(posX + i, posY + j,
								posZ + k)) {
							TempDis = GetDistanceWithXYZ(posX + i, posY + j,
									posZ + k);
							targetX = i;
							targetY = j;
							targetZ = k;
						}
					}
				}
			}
		}
		if (TempDis != range * 2) {
			if (Math.sqrt((targetX) ^ 2 + (targetY) ^ 2 + (targetZ) ^ 2) >= 2) {
				setPathToEntity(worldObj.getEntityPathToXYZ((Entity) this,
						(int) Math.round(posX + targetX),
						(int) Math.round(posY + targetY),
						(int) Math.round(posZ + targetZ), 10F, true, false,
						true, false));
				return true;
			} else {
				// if (EatTick==0){
				FaceToCoord((int) -(posX + targetX), (int) (posY + targetY),
						(int) -(posZ + targetZ));
				HandleEating(10, true);
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						if (worldObj.getBlockId(
								(int) Math.round(posX + targetX + i),
								(int) Math.round(posY + targetY),
								(int) Math.round(posZ + targetZ + j)) == mod_Fossil.Ferns.blockID) {
							worldObj.playAuxSFX(2001,
									(int) Math.round(posX + targetX + i),
									(int) Math.round(posY + targetY),
									(int) Math.round(posZ + targetZ + j),
									Block.tallGrass.blockID);
							worldObj.setBlock(
									(int) Math.round(posX + targetX + i),
									(int) Math.round(posY + targetY),
									(int) Math.round(posZ + targetZ + j), 0);
							if (worldObj.getBlockId(
									(int) Math.round(posX + targetX + i),
									(int) Math.round(posY + targetY) + 1,
									(int) Math.round(posZ + targetZ + j)) == mod_Fossil.FernUpper.blockID)
								worldObj.setBlock(
										(int) Math.round(posX + targetX + i),
										(int) Math.round(posY + targetY) + 1,
										(int) Math.round(posZ + targetZ + j), 0);
							if (worldObj.getBlockId(
									(int) Math.round(posX + targetX + i),
									(int) Math.round(posY + targetY) - 1,
									(int) Math.round(posZ + targetZ + j)) == Block.grass.blockID)
								worldObj.setBlock(
										(int) Math.round(posX + targetX + i),
										(int) Math.round(posY + targetY) - 1,
										(int) Math.round(posZ + targetZ + j),
										Block.dirt.blockID);
						}
					}
					// }

					this.heal(3);
					// EatTick=100;
					this.setPathToEntity(null);
				}
				return true;
			}
		} else
			return false;
	}

	@Override
	public void updateSize(boolean shouldAddAge) {
		if (shouldAddAge && this.getDinoAge() < this.AGE_LIMIT)
			this.increaseDinoAge();
		setSize((float) (1.5F + 0.3 * (float) this.getDinoAge()),
				(float) (1.5F + 0.3 * (float) this.getDinoAge()));
	}

	@Override
	public EnumOrderType getOrderType() {
		return this.OrderStatus;
	}

	@Override
	protected int foodValue(Item asked) {
		if (asked == Item.wheat)
			return 10;
		if (asked == Item.appleRed)
			return 30;
		return 0;
	}

	@Override
	public void HoldItem(Item itemGot) {
		return;

	}

	@Override
	public float getGLX() {
		return (float) (1.5F + 0.3 * (float) this.getDinoAge());
	}

	@Override
	public float getGLY() {
		return (float) (1.5F + 0.3 * (float) this.getDinoAge());
	}
	@Override
	public String[] additionalPediaMessage(){
		String[] result=null;
		if (!this.isTamed()){
			result=new String[1];
			result[0]=UntamedText;
		}else{
			ArrayList<String> resultList=new ArrayList<String>();
			if ((this.isTamed() && this.getDinoAge() > 4 && riddenByEntity == null))
				resultList.add(RidiableText);
			if (!resultList.isEmpty()) {
				result=new String[1];
				result=resultList.toArray(result);
			}
		}
		return result;
	}
}