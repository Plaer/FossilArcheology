package net.fossilsarch.common.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.fossilsarch.mod_Fossil;
import net.fossilsarch.common.FossilOptions;
import net.fossilsarch.common.ai.DinoAIControlledByPlayer;
import net.fossilsarch.common.ai.DinoAIEatLeavesWithHeight;
import net.fossilsarch.common.ai.DinoAIFollowOwner;
import net.fossilsarch.common.ai.DinoAIGrowup;
import net.fossilsarch.common.ai.DinoAIStarvation;
import net.fossilsarch.common.ai.DinoAIUseFeederWithHeight;
import net.fossilsarch.common.ai.DinoAIWander;
import net.fossilsarch.common.handlers.FossilGuiHandler;
import net.fossilsarch.common.io.EnumDinoType;
import net.fossilsarch.common.io.EnumOrderType;
import net.fossilsarch.common.io.EnumSituation;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
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
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;

public class EntityBrachiosaurus extends EntityDinosaurce  {
	public boolean isTamed=false;
	public final float HuntLimit=getHungerLimit()*4/5;
	public String OwnerName;
	final float PUSHDOWN_HARDNESS=5.0F;
	protected final int AGE_LIMIT=36;
	
	public EntityBrachiosaurus(World world) {
		this(world, randomSpawnAge(world.rand));
		this.OrderStatus = EnumOrderType.FreeMove;
	}
	
	public EntityBrachiosaurus(World world, int dinoAge) {
		super(world);
        this.setDinoAge(dinoAge+dinoAge);
		this.SelfType=EnumDinoType.Brachiosaurus;
		this.setSize(1.1f, 1.6F);
		this.setHunger(getHungerLimit());
		this.setHealth(8);
        this.getNavigator().setAvoidsWater(true);
		this.tasks.addTask(0, new DinoAIGrowup(this, AGE_LIMIT));
		this.tasks.addTask(0, new DinoAIStarvation(this));
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.ridingHandler=new DinoAIControlledByPlayer(this, 0.34F));
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0f, true));
        this.tasks.addTask(5, new DinoAIFollowOwner(this, 1, 5F, 2.0F));
        this.tasks.addTask(6, new DinoAIEatLeavesWithHeight(this,1.0f,24,this.HuntLimit));
        this.tasks.addTask(6, new DinoAIUseFeederWithHeight(this,1.0f,24,this.HuntLimit));
        this.tasks.addTask(7, new DinoAIWander(this, 1.0f));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(9, new EntityAILookIdle(this));	
	}
	
	private static int randomSpawnAge(Random rand) {
		boolean isChild = rand.nextInt(4) == 0;
		if (isChild)
			return rand.nextInt(4);
		else
			return rand.nextInt(33)+4;
	}
	
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(20.0D + 10.0D*this.getDinoAge());
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.3f);
    }
    
    @Override
    public float getAIMoveSpeed() {
    	
    	float speed = super.getAIMoveSpeed();
    	
    	return speed * (0.3f + 0.05f*this.getDinoAge());
    }
	
	public int getHungerLimit(){
		return 500;
	}
    public boolean isAIEnabled()
    {
        return (!this.isModelized());
    }
    
    @Override
	public boolean attackEntityFrom(DamageSource damagesource, float i)
    {
		if (this.modelizedDrop()) return true;
		return super.attackEntityFrom(damagesource, i);
    }
    
    @Override
	protected String getLivingSound()
    {
		if (worldObj.getClosestPlayerToEntity(this, 16D)!=null) return "fossilsarch:Brach_living";
		else return null;

    }
    
    @Override
	protected String getHurtSound()
    {
        return "mob.cow.hurt";
    }

    @Override
    protected String getDeathSound()
    {
        return "fossilsarch:Brach_death";
    }
    
	@Override
	public String getOwnerName() {
		return this.OwnerName;
	}

	@Override
	public void setOwner(String s) {
		this.OwnerName=s.toString();

	}

	@Override
	public boolean isTamed() {
		return this.isTamed; 
	}

	@Override
	public void setTamed(boolean flag) {
		this.isTamed=flag;

	}

	public void SetOrder(EnumOrderType input) {
		this.OrderStatus=input;

	}

	public boolean HandleEating(int FoodValue){
		return HandleEating(FoodValue,false);
	}
	public boolean HandleEating(int FoodValue,boolean FernFlag){
		if (this.getHunger()>=getHungerLimit()) {
			if (isTamed() && !FernFlag) SendStatusMessage(EnumSituation.Full,this.SelfType);
			return false;
		}
		this.increaseHunger(FoodValue);
		showHeartsOrSmokeFX(false);
		if (this.getHunger()>=getHungerLimit()) this.setHunger(getHungerLimit());
		return true;
	}

	public boolean CheckEatable(int itemIndex) {
		Item tmp=Item.itemsList[itemIndex];
		boolean result=false;
		result=
				(tmp==Item.wheat)||
				(tmp==Item.melon);
		return result;
}


	@Override
	public void ShowPedia(EntityPlayer checker) {
		PediaTextCorrection(SelfType,checker);
		if (this.isTamed()){
				mod_Fossil.ShowMessage(new StringBuilder().append(OwnerText).append(this.getOwnerName()).toString(),checker);
				mod_Fossil.ShowMessage(new StringBuilder().append(AgeText).append(this.getDinoAge()).toString(),checker);
				mod_Fossil.ShowMessage(new StringBuilder().append(HelthText).append(this.getHealth()).append("/").append(getMaxHealth()).toString(),checker);
				mod_Fossil.ShowMessage(new StringBuilder().append(HungerText).append(this.getHunger()).append("/").append(this.getHungerLimit()).toString(),checker);

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
			if ((this.isTamed() && this.getDinoAge() > 4 && riddenByEntity == null))
				resultList.add(RidiableText);
			if (!resultList.isEmpty()) {
				result=new String[1];
				result=resultList.toArray(result);
			}
		}
		return result;
	}
	
	public boolean interact(EntityPlayer entityplayer)
    {
		if (this.isModelized()) return modelizedInteract(entityplayer);
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        if(itemstack != null && itemstack.itemID==Item.wheat.itemID)
            {

					if(this.CheckEatable(itemstack.itemID) && HandleEating(10))
					{
						itemstack.stackSize--;
						if(itemstack.stackSize <= 0)
						{
							entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
						}
						heal(3);

					}

				return true;
		}
        if (FMLCommonHandler.instance().getSide().isClient()){
	        if (itemstack!=null && itemstack.itemID==mod_Fossil.DinoPedia.itemID){
	        	//this.ShowPedia(entityplayer);			//old function
	        	EntityDinosaurce.pediaingDino=this;		//new function
	        	mod_Fossil.callGUI(entityplayer, FossilGuiHandler.DINOPEDIA_GUI_ID, worldObj, (int)(this.posX), (int)(this.posY), (int)(this.posZ));
	        	return true;
	        }
        }
		if (itemstack!=null && itemstack.itemID==mod_Fossil.ChickenEss.itemID){
			if (this.getDinoAge()>=12 || this.getHunger()<=0)return false;
			itemstack.stackSize--;
            if(itemstack.stackSize <= 0)
            {
                entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
            }
            entityplayer.inventory.addItemStackToInventory(new ItemStack(Item.glassBottle,1));
			this.setDinoAgeTick(GROW_TIME_COUNT);
			this.setHunger(1+new Random().nextInt(this.getHunger()));
			return true;
		}
        if(itemstack != null && itemstack.itemID==Item.stick.itemID){
        	if(entityplayer.username.equalsIgnoreCase(getOwnerName())){				
				 if(!worldObj.isRemote)
				{
					isJumping = false;
					setPathToEntity(null);
					OrderStatus=EnumOrderType.values()[(mod_Fossil.EnumToInt(OrderStatus)+1)%3];
					SendOrderMessage(OrderStatus);
				}
				return true;
				
			}
        }
		if (this.isTamed() && this.getDinoAge()>=2 &&!worldObj.isRemote&& (riddenByEntity == null || riddenByEntity == entityplayer)){
		 	entityplayer.rotationYaw=this.rotationYaw;
            entityplayer.mountEntity(this);
            this.setPathToEntity(null);
            this.renderYawOffset=this.rotationYaw;
            return true;
		}
        return false;
    }
	@Override
	public EntityAgeable createChild(EntityAgeable entityanimal) {
		return null;
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setByte("OrderStatus", (byte)(mod_Fossil.EnumToInt(this.OrderStatus)));
    }
	public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
		InitSize();
		OrderStatus=EnumOrderType.values()[nbttagcompound.getByte("OrderStatus")];
    }
	private void InitSize(){
		updateSize(false);
		setPosition(posX,posY,posZ);
	}
	public void onUpdate(){
		super.onUpdate();
		if (this.getDinoAge()>=4) BlockInteractive();		
		
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(20.0D + 10.0D*this.getDinoAge());
		
	}
	protected boolean isMovementCeased()
	    {
	        return isSelfSitting();
	    }

    public void applyEntityCollision(Entity entity){
    	if (this.isModelized()) return;
		if (entity instanceof EntityLivingBase && !(entity instanceof EntityPlayer)){
			if (this.onGround && ((EntityLivingBase)entity).getEyeHeight()<this.getHalfHeight()){
				this.onKillEntity((EntityLivingBase)entity);
				((EntityLivingBase)entity).attackEntityFrom(DamageSource.causeMobDamage(this), 10);
				return;
			}
		}
		super.applyEntityCollision(entity);
	}

	public void setSelfSitting(boolean b) {
		if (b) this.OrderStatus=EnumOrderType.Stay;
		else this.OrderStatus=EnumOrderType.FreeMove;
		
	}

	public boolean isSelfSitting() {
		return (this.OrderStatus==EnumOrderType.Stay);
	}

	public float getEyeHeight()
    {
		return (4+this.getDinoAge()/1.8F);
    }
	public float getHalfHeight(){
		return getEyeHeight()/2;
	}

	public void BlockInteractive(){
		if (!FossilOptions.TRexBreakingBlocks) return;
		if (this.isModelized()) return;
		int ObjectTall=0;
		int TmpID=0;
		int HeightCount=0;
		int k=0;
		for (int j=(int)Math.round(boundingBox.minX)-1;j<=(int)Math.round(boundingBox.maxX)+1;j++){
			for (HeightCount=0;HeightCount<=(int)getHalfHeight();HeightCount++){
				for (int l=(int)Math.round(boundingBox.minZ)-1;l<=(int)Math.round(boundingBox.maxZ)+1;l++){
					k=(int)Math.round(boundingBox.minY)+HeightCount;
					TmpID=worldObj.getBlockId(j, k, l);
					if (Block.blocksList[TmpID]!=null && Block.blocksList[TmpID].getBlockHardness(worldObj,(int)posX,(int)posY,(int)posZ)<this.PUSHDOWN_HARDNESS){
						ObjectTall=GetObjectTall(j,k,l);
						if (ObjectTall>0 && !isObjectTooTall(ObjectTall+HeightCount)) DestroyTower(j,k,l,ObjectTall);
					}
				}
			}
		}
	}
	private boolean isObjectTooTall(int targetX,int targetY,int targetZ){
		return (GetObjectTall(targetX,targetY,targetZ)>getHalfHeight());
	}
	private boolean isObjectTooTall(int ObjectTall){
		float HalfHeight=getHalfHeight();
		return ((float)ObjectTall>HalfHeight);
	}
	private int GetObjectTall(int targetX,int targetY,int targetZ){
		int count=0;
		while(!worldObj.isAirBlock(targetX, targetY+count, targetZ)){
			count++;
		}
		return count;
	}
	private void DestroyTower(int targetX,int bottomY,int targetZ,int ObjectTall){
		int TmpID=0;
		for (int y=bottomY;y<=bottomY+ObjectTall;y++){
			TmpID=worldObj.getBlockId(targetX, y, targetZ);
			worldObj.playAuxSFX(2001, targetX, y, targetZ, TmpID);
			worldObj.setBlock(targetX, y, targetZ,0);
		}
	}
	public void updateRiderPosition()
    {
        if(riddenByEntity == null)
        {
            return;
        } else
        {
            riddenByEntity.setPosition(posX, posY+getHalfHeight()*1.2, posZ);
            return;
        }
    }
	protected void updateWanderPath()
    {
        boolean flag = false;
        int i = -1;
        int j = -1;
        int k = -1;
        float f = -99999F;
		if (OrderStatus==EnumOrderType.FreeMove || !isTamed()){
			for(int l = 0; l < 10+this.getDinoAge(); l++)
			{
				int i1 = MathHelper.floor_double((posX + (double)rand.nextInt(24+(int)(width*width*4))) - (12D+(width*width*2)));
				int j1 = MathHelper.floor_double((posY + (double)rand.nextInt(7)) - 3D);
				int k1 = MathHelper.floor_double((posZ + (double)rand.nextInt(24+(int)(width*width*4))) - (12D+(width*width*2)));
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
				setPathToEntity(worldObj.getEntityPathToXYZ(this, i, j, k, 10F, true, false, true, false));
			}
		}
    }
	private void HandleRiding(){

			if(riddenByEntity != null)
	        {

				if (this.riddenByEntity instanceof EntityPlayerSP){
					if (this.onGround){
						if (((EntityPlayerSP)this.riddenByEntity).movementInput.jump){
							this.jump();
							((EntityPlayerSP)this.riddenByEntity).movementInput.jump=false;
						}							
							this.rotationYaw-=(float) ((float)(((EntityPlayerSP) this.riddenByEntity).moveStrafing)*5);
				            for(; this.rotationYaw < -180F; this.rotationYaw += 360F) { }
				            for(; this.rotationYaw >= 180F; this.rotationYaw -= 360F) { }
							this.moveForward=((EntityPlayerSP) this.riddenByEntity).moveForward;
							//if (this.SneakScream) this.SneakScream=false;
					}
				}
	        }

	}
	@Override
	public void updateSize(boolean shouldAddAge) {
		if (shouldAddAge && this.getDinoAge()<this.AGE_LIMIT) this.increaseDinoAge();
		setSize((float)(1.1F+0.2*(float)this.getDinoAge()),(float)(1.6F+0.15*(float)this.getDinoAge()));
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(20.0D + 10.0D*this.getDinoAge());
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
		return (float)(1.5F+0.3*(float)this.getDinoAge());
	}
	@Override
	public float getGLY() {
		return (float)(1.5F+0.3*(float)this.getDinoAge());
	}
}
