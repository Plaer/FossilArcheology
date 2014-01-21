package net.fossilsarch.common.ai;

import net.fossilsarch.common.FossilOptions;
import net.fossilsarch.common.entity.EntityDinosaurce;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.DamageSource;

public class DinoAIStarvation extends EntityAIBase {
	EntityDinosaurce mover=null;
	public DinoAIStarvation(EntityDinosaurce arg){
		mover=arg;
	}
	@Override
	public boolean shouldExecute() {
		if (!FossilOptions.DinoHunger) return false;
		mover.decreaseHungerTick();
		return (mover.getHungerTick()<=0)&&(mover.worldObj.difficultySetting > 0 && mover.worldObj.getClosestPlayerToEntity(mover, 24D)!=null);
	}
	public void startExecuting() {
		mover.setHungerTick(mover.HungerTickLimit);
		mover.decreaseHunger();
		if (mover.getHunger()<=0) handleStarvation();
	}
	private void handleStarvation() {
		mover.attackEntityFrom(DamageSource.starve, 20);
		
	}
}
