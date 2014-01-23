// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.fossilsarch.common.tileentity;
import java.util.*;

import net.fossilsarch.mod_Fossil;
import net.fossilsarch.common.entity.EntityDinosaurce;
import net.fossilsarch.common.entity.EntityPterosaur;
import net.fossilsarch.common.entity.EntityRaptor;
import net.fossilsarch.common.entity.EntityTRex;
import net.fossilsarch.common.entity.EntityTriceratops;
import net.fossilsarch.common.io.EnumDinoType;
import net.fossilsarch.common.io.EnumOrderType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;


// Referenced classes of package net.minecraft.src:
//            TileEntity, NBTTagCompound, World, Material

public class TileEntityDrum extends TileEntity
{
	final String DRUM="Drum.";
	final String MSG="Msg.";
	final String ORDER="Order.";
	final String HEAD="Head";
	final String MIDDLE="Middle";
	final String TAIL="Tail";
	final String TREXMSG=MSG+"TRex.";
	final String DINO="Dino.";
	public EnumOrderType Order=EnumOrderType.Stay;
    public TileEntityDrum()
    {
        note = 0;
        previousRedstoneState = false;
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        //nbttagcompound.setByte("note", note);
		nbttagcompound.setByte("Order", (byte)mod_Fossil.EnumToInt(Order));
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        /*note = nbttagcompound.getByte("note");
        if(note < 0)
        {
            note = 0;
        }
        if(note > 24)
        {
            note = 24;
        }*/
		Order=EnumOrderType.values()[nbttagcompound.getByte("Order")];
    }


    public void triggerNote(World par1World, int par2, int par3, int par4)
    {
        if (par1World.getBlockMaterial(par2, par3 + 1, par4) == Material.air)
        {
            Material var5 = par1World.getBlockMaterial(par2, par3 - 1, par4);
            byte var6 = 0;

            if (var5 == Material.rock)
            {
                var6 = 1;
            }

            if (var5 == Material.sand)
            {
                var6 = 2;
            }

            if (var5 == Material.glass)
            {
                var6 = 3;
            }

            if (var5 == Material.wood)
            {
                var6 = 4;
            }

            par1World.addBlockEvent(par2, par3, par4,mod_Fossil.Dump.blockID, var6, this.note);
        }
    }
    private String GetOrderString(){
    	return StatCollector.translateToLocal(ORDER+Order.toString());
    }
	public void TriggerOrder(EntityPlayer checker){
		Order=Order.Next();
		worldObj.playSoundEffect(xCoord,yCoord,zCoord,"drum_single",8.0F, (float)Math.pow(2D, (double)(Order.ToInt()-1)));
		String msgHead=StatCollector.translateToLocal(DRUM+ORDER+HEAD);
		String OrderName=GetOrderString();
		mod_Fossil.ShowMessage(new StringBuilder().append(msgHead).append(OrderName).toString(),checker);
        this.onInventoryChanged();
		
	}
	public boolean SendOrder(int UsingID,EntityPlayer player){
		String Type="";
		String OrderString="";
		final String MSGHEAD=StatCollector.translateToLocal(DRUM+MSG+HEAD);
		final String MSGMIDDLE=StatCollector.translateToLocal(DRUM+MSG+MIDDLE);
		final String MSGTAIL=StatCollector.translateToLocal(DRUM+MSG+TAIL);
		worldObj.playSoundEffect(xCoord,yCoord,zCoord,"drum_triple",8.0F, (float)Math.pow(2D, (double)(Order.ToInt()-1)));
		if (UsingID!=Item.stick.itemID && UsingID!=Item.bone.itemID && UsingID!=mod_Fossil.SkullStick.itemID && UsingID!=Item.arrow.itemID) return false;
		if(UsingID == Item.stick.itemID){
			OrderTri();
			Type=EntityDinosaurce.GetNameByEnum(EnumDinoType.Triceratops, true);
		}
		if(UsingID == Item.bone.itemID){
			OrderRaptor();
			Type=EntityDinosaurce.GetNameByEnum(EnumDinoType.Raptor, true);
		}
		if(UsingID == Item.arrow.itemID){
			OrderPTS();
			Type=EntityDinosaurce.GetNameByEnum(EnumDinoType.Pterosaur, true);
		}
		if(UsingID == mod_Fossil.SkullStick.itemID){
			OrderTRex(player);
		}

			OrderString=GetOrderString();
		if (UsingID != mod_Fossil.SkullStick.itemID){

				mod_Fossil.ShowMessage(new StringBuilder().append(MSGHEAD).append(Type).append(MSGMIDDLE).append(OrderString).append(MSGTAIL).toString(),player);

			return true;
		}else{
			String TmpMsg=StatCollector.translateToLocal(DRUM+TREXMSG+String.valueOf(Order.ToInt()+1));
			mod_Fossil.ShowMessage(TmpMsg,player);
			return true;
		}
	}
	private void OrderRaptor(){
		List list = worldObj.getEntitiesWithinAABB(EntityRaptor.class, AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord, xCoord + 1.0D, yCoord + 1.0D, zCoord + 1.0D).expand(30D, 4D, 30D));
		Iterator iterator = list.iterator();
        do
        {
            if(!iterator.hasNext())
            {
                 break;
            }
            Entity entity1 = (Entity)iterator.next();
            EntityDinosaurce entityDino = (EntityDinosaurce)entity1;
            if (entityDino.isTamed())entityDino.SetOrder(Order);
        } while(true);
	}
	private void OrderPTS(){
		List list = worldObj.getEntitiesWithinAABB(EntityPterosaur.class, AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord, xCoord + 1.0D, yCoord + 1.0D, zCoord + 1.0D).expand(30D, 4D, 30D));
		Iterator iterator = list.iterator();
        do
        {
            if(!iterator.hasNext())
            {
                 break;
            }
            Entity entity1 = (Entity)iterator.next();
            EntityDinosaurce entityDino = (EntityDinosaurce)entity1;
            if (entityDino.isTamed())entityDino.SetOrder(Order);
        } while(true);
	}
	private void OrderTri(){
		List list = worldObj.getEntitiesWithinAABB(EntityTriceratops.class, AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord, xCoord + 1.0D, yCoord + 1.0D, zCoord + 1.0D).expand(30D, 4D, 30D));
		Iterator iterator = list.iterator();
        do
        {
            if(!iterator.hasNext())
            {
                 break;
            }
            Entity entity1 = (Entity)iterator.next();
            EntityDinosaurce entityDino = (EntityDinosaurce)entity1;
			if (entityDino.isTamed())entityDino.SetOrder(Order);
        } while(true);
	}
	private void OrderTRex(EntityPlayer player){
		List list = worldObj.getEntitiesWithinAABB(EntityTRex.class, AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord, xCoord + 1.0D, yCoord + 1.0D, zCoord + 1.0D).expand(50D, 4D, 50D));
		Iterator iterator = list.iterator();
        do
        {
            if(!iterator.hasNext())
            {
                 break;
            }
            Entity entity1 = (Entity)iterator.next();
            EntityTRex entityTRex = (EntityTRex)entity1;
			if (entityTRex.getDinoAge()>=3 && !entityTRex.isTamed()){
				entityTRex.setSelfAngry(true);
				entityTRex.setTarget(player);
			}
        } while(true);
	}
    public byte note;

    public boolean previousRedstoneState;
}
