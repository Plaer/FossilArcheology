package net.fossilsarch.common.ai;

import net.fossilsarch.common.entity.EntityDinosaurce;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.passive.EntityTameable;

public class DinoAITargetNonTamedExceptSelfClass extends EntityAINearestAttackableTarget {

	private EntityTameable targetEntity;
	
	public DinoAITargetNonTamedExceptSelfClass(
			EntityTameable par1EntityTameable, Class par2Class,
			int par4, boolean par5) {
		super(par1EntityTameable, par2Class, par4, par5);
		
		targetEntity = par1EntityTameable;
	}
    public boolean shouldExecute()
    {
    	boolean result=super.shouldExecute();
    	if (result && this.targetEntity instanceof EntityDinosaurce && ((EntityDinosaurce)this.targetEntity).SelfType==((EntityDinosaurce)this.taskOwner).SelfType){
    		return false;
    	}
    	return result;
    }
}
