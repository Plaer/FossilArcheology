// Date: 2011/10/16  08:01:53
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX





package net.fossilsarch.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelDinoEgg extends ModelBase
{
	public ModelDinoEgg()
    {
      egg4 = new ModelRenderer(this, 24, 18);
      egg4.addBox(-1.5F, -9F, -1.5F, 3, 3, 3);
      egg4.setRotationPoint(0F, 24.13333F, 0F);
      egg4.rotateAngleX = 0F;
      egg4.rotateAngleY = 0F;
      egg4.rotateAngleZ = 0F;
      egg4.mirror = false;
      egg3 = new ModelRenderer(this, 24, 24);
      egg3.addBox(-2.5F, -8F, -2.5F, 5, 3, 5);
      egg3.setRotationPoint(0F, 24.13333F, 0F);
      egg3.rotateAngleX = 0F;
      egg3.rotateAngleY = 0F;
      egg3.rotateAngleZ = 0F;
      egg3.mirror = false;
      egg2 = new ModelRenderer(this, 0, 21);
      egg2.addBox(-3F, -6F, -3F, 6, 5, 6);
      egg2.setRotationPoint(0F, 24.13333F, 0F);
      egg2.rotateAngleX = 0F;
      egg2.rotateAngleY = 0F;
      egg2.rotateAngleZ = 0F;
      egg2.mirror = false;
      egg1 = new ModelRenderer(this, 0, 14);
      egg1.addBox(-2.5F, -2F, -2.5F, 5, 2, 5);
      egg1.setRotationPoint(0F, 24.13333F, 0F);
      egg1.rotateAngleX = 0F;
      egg1.rotateAngleY = 0F;
      egg1.rotateAngleZ = 0F;
      egg1.mirror = false;
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
      super.render(entity, f, f1, f2, f3, f4, f5);
      setRotationAngles(f, f1, f2, f3, f4, f5,entity);
      egg4.render(f5);
      egg3.render(f5);
      egg2.render(f5);
      egg1.render(f5);
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity par7Entity)
    {
      super.setRotationAngles(f, f1, f2, f3, f4, f5, par7Entity);
      /*egg4.rotateAngleX = MathHelper.cos(f / (1.919107651F * 1 )) * 0.0872664625997165 * f1 + 0 ;
     egg4.rotateAngleY = MathHelper.cos(f / (1.919107651F * 1 )) * 0.0872664625997165 * f1 + 0 ;*/
    }

    //fields
      ModelRenderer egg4;
      ModelRenderer egg3;
      ModelRenderer egg2;
      ModelRenderer egg1;
}