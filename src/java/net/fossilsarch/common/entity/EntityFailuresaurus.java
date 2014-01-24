package net.fossilsarch.common.entity;

import net.fossilsarch.mod_Fossil;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class EntityFailuresaurus extends EntityZombie{
	public EntityFailuresaurus(World world){
		super(world);
	}
    protected int getDropItemId()
    {
        return mod_Fossil.biofossil.itemID;
    }
    protected void jump()
    {
        return;
    }
    
	@Override
	public String getEntityName() {
		return StatCollector.translateToLocal("entity.failuresaurus.name");
	}
}
