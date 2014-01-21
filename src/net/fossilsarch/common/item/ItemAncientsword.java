package net.fossilsarch.common.item;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Multimap;

import net.fossilsarch.mod_Fossil;
import net.fossilsarch.common.entity.EntityFriendlyPigZombie;
import net.fossilsarch.common.entity.EntityMLighting;
import net.fossilsarch.common.io.EnumPigmenSpeaks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import cpw.mods.fml.common.FMLCommonHandler;


public class ItemAncientsword extends ItemSword{
	public ItemAncientsword(int i, EnumToolMaterial enumtoolmaterial){
		super (i,enumtoolmaterial);
		maxStackSize=1;
	}
	public ItemAncientsword(int i){
		this(i,EnumToolMaterial.IRON);
	}
    public String getTextureFile()
    {
       return "/skull/Fos_items.png";
    }
    
    /**
     * Gets a map of item attribute modifiers, used by ItemSword to increase hit damage.
     */
    public Multimap getItemAttributeModifiers()
    {
        Multimap multimap = super.getItemAttributeModifiers();
        multimap.removeAll(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", 4.0 + EnumToolMaterial.IRON.getDamageVsEntity()*2.0, 0));
        return multimap;
    }
    
    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    @Override
    public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase target, EntityLivingBase player)
    {
        if (!super.hitEntity(par1ItemStack, target, player))
        	return false;
        
		if (target!=null){		
			if (target.worldObj.difficultySetting > 0){
				if (target instanceof EntityPig||target instanceof EntityPigZombie){
					if (player!=null && player instanceof EntityPlayer && checkHelmet((EntityPlayer)player)){
						if (!target.worldObj.isRemote){
							EntityFriendlyPigZombie entitypigzombie = new EntityFriendlyPigZombie(target.worldObj);
							entitypigzombie.LeaderName=player.getEntityName();
							entitypigzombie.setLocationAndAngles(target.posX, target.posY, target.posZ, target.rotationYaw, target.rotationPitch);
							target.setDead();					
							target.worldObj.spawnEntityInWorld(entitypigzombie);
							entitypigzombie.Mouth.SendSpeech(EnumPigmenSpeaks.LifeFor, entitypigzombie.LeaderName);
						}
					}
				}
			}
		}
		target.worldObj.addWeatherEffect(new EntityMLighting(target.worldObj,target.posX,target.posY,target.posZ));      
		
		return true;
    }

	private EntityPlayer SearchUser(Entity centerRef){
		EntityPlayer tmp=(EntityPlayer)centerRef.worldObj.findNearestEntityWithinAABB(EntityPlayer.class, centerRef.boundingBox.expand(6.0, 6.0, 6.0), centerRef);
		if (tmp==null) return null;
		return tmp;
	}

	private boolean checkHelmet(EntityPlayer testee){
		ItemStack itemstack = testee.inventory.armorInventory[3];
		if (itemstack==null){
			return false;
		}else{
			if (itemstack.itemID!=mod_Fossil.Ancienthelmet.itemID){
				return false;
			}
			return true;
		}
	}
}