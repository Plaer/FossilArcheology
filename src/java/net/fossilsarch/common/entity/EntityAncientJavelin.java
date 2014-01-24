package net.fossilsarch.common.entity;

import net.fossilsarch.mod_Fossil;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityAncientJavelin extends EntityJavelin {
	private boolean lighteningShot = false;

	public EntityAncientJavelin(World world) {
		super(world);

	}

	public EntityAncientJavelin(World world, double d, double d1, double d2) {
		super(world, d, d1, d2);

	}

	public EntityAncientJavelin(World world, EntityLivingBase entityliving,
			float f, EnumToolMaterial material) {
		super(world, entityliving, f);
		this.SelfMaterial = material;

	}

	public EntityAncientJavelin(World world, EntityLivingBase entityliving, float f) {
		super(world, entityliving, f);
	}
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setBoolean("lighteningShot", lighteningShot);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		if(nbttagcompound.hasKey("lighteningShot")) this.lighteningShot=nbttagcompound.getBoolean("lighteningShot");
	}

	public void onUpdate() {
		super.onUpdate();
		if (this.inGround && !this.lighteningShot) {
			this.worldObj.addWeatherEffect(new EntityLightningBolt(
					this.worldObj, this.posX, this.posY, this.posZ));
			this.lighteningShot = true;
		}
	}
	protected boolean addJavelinToPlayer(EntityPlayer player){
    	ItemStack javelin;

			javelin=new ItemStack(mod_Fossil.AncientJavelin,1);

		return player.inventory.addItemStackToInventory(javelin);
	}
}
