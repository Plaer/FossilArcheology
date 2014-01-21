package net.fossilsarch.common.ai;

import net.fossilsarch.common.entity.EntityBones;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIDeadBones extends EntityAIBase {
    private EntityBones asker;
	public EntityAIDeadBones(EntityBones par1)
    {
        this.asker = par1;
    }
	@Override
	public boolean shouldExecute() {
		return true;
	}
    public void updateTask()
    {
        float Tmp=asker.rotationYaw;
        asker.rotationPitch=45F;
        asker.rotationYaw=Tmp;
    }
}
