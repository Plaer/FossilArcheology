
// Java generated by Techne
// This is the first stage to more output options
// and therefore a very basic Render-File
// I hope it will be useful, in any case, leave
// feedback so I can improve on it
// - ZeuX
      package net.fossilsarch.client.render;

import net.fossilsarch.client.model.ModelBrachiosaurus;
import net.fossilsarch.common.entity.EntityBrachiosaurus;
import net.fossilsarch.common.entity.EntityDinosaurce;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderBrachiosaurus extends RenderLiving
{

    public RenderBrachiosaurus(float f)
    {
        super(new ModelBrachiosaurus(), f);
    }
    private float func_77034_a(float par1, float par2, float par3)
    {
        float var4;

        for (var4 = par2 - par1; var4 < -180.0F; var4 += 360.0F)
        {
            ;
        }

        while (var4 >= 180.0F)
        {
            var4 -= 360.0F;
        }

        return par1 + par3 * var4;
    }
    public void renderNew(EntityBrachiosaurus Entitybrachiosaurus, double d, double d1, double d2, 
            float f, float f1)
    {
    	GL11.glPushMatrix();
        GL11.glDisable(2884 /*GL_CULL_FACE*/);
        mainModel.onGround = renderSwingProgress(Entitybrachiosaurus, f1);
        if(renderPassModel != null)
        {
            renderPassModel.onGround = mainModel.onGround;
        }
        mainModel.isRiding = Entitybrachiosaurus.isRiding();
        if(renderPassModel != null)
        {
            renderPassModel.isRiding = mainModel.isRiding;
        }
        try
        {
        	 float var10 = this.func_77034_a(Entitybrachiosaurus.prevRenderYawOffset, Entitybrachiosaurus.renderYawOffset, f1);
             float var11 = this.func_77034_a(Entitybrachiosaurus.prevRotationYawHead, Entitybrachiosaurus.rotationYawHead, f1);
             float var12 = Entitybrachiosaurus.prevRotationPitch + (Entitybrachiosaurus.rotationPitch - Entitybrachiosaurus.prevRotationPitch) * f1;
             this.renderLivingAt(Entitybrachiosaurus, d, d1, d2);
             float var13 = this.handleRotationFloat(Entitybrachiosaurus, f1);
             this.rotateCorpse(Entitybrachiosaurus, var13, (Entitybrachiosaurus.isModelized())?var11:var10, f1);
             float var14 = 0.0625F;
             GL11.glEnable(GL12.GL_RESCALE_NORMAL);
             GL11.glScalef(((EntityDinosaurce)Entitybrachiosaurus).getGLX(), -((EntityDinosaurce)Entitybrachiosaurus).getGLY(), ((EntityDinosaurce)Entitybrachiosaurus).getGLZ());
             this.preRenderCallback(Entitybrachiosaurus, f1);
             GL11.glTranslatef(0.0F, -24.0F * var14 - 0.0078125F, 0.0F);
             float var15 = Entitybrachiosaurus.prevLimbSwingAmount + (Entitybrachiosaurus.limbSwingAmount - Entitybrachiosaurus.prevLimbSwingAmount) * f1;
             float var16 = Entitybrachiosaurus.limbSwing - Entitybrachiosaurus.limbSwingAmount * (1.0F - f1);

             if (Entitybrachiosaurus.isChild())
             {
                 var16 *= 3.0F;
             }

             if (var15 > 1.0F)
             {
                 var15 = 1.0F;
             }

             GL11.glEnable(GL11.GL_ALPHA_TEST);
             this.mainModel.setLivingAnimations(Entitybrachiosaurus, var16, var15, f1);
             this.renderModel(Entitybrachiosaurus, var16, var15, var13, var11 - var10, var12, var14);
             float var19;
             int var18;
             float var20;
             float var22;

             for (int var17 = 0; var17 < 4; ++var17)
             {
                 var18 = this.shouldRenderPass(Entitybrachiosaurus, var17, f1);

                 if (var18 > 0)
                 {
                     this.renderPassModel.setLivingAnimations(Entitybrachiosaurus, var16, var15, f1);
                     this.renderPassModel.render(Entitybrachiosaurus, var16, var15, var13, var11 - var10, var12, var14);

                     if (var18 == 15)
                     {
                         var19 = (float)Entitybrachiosaurus.ticksExisted + f1;
                         this.bindTexture(new ResourceLocation("%blur%/misc/glint.png"));
                         GL11.glEnable(GL11.GL_BLEND);
                         var20 = 0.5F;
                         GL11.glColor4f(var20, var20, var20, 1.0F);
                         GL11.glDepthFunc(GL11.GL_EQUAL);
                         GL11.glDepthMask(false);

                         for (int var21 = 0; var21 < 2; ++var21)
                         {
                             GL11.glDisable(GL11.GL_LIGHTING);
                             var22 = 0.76F;
                             GL11.glColor4f(0.5F * var22, 0.25F * var22, 0.8F * var22, 1.0F);
                             GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
                             GL11.glMatrixMode(GL11.GL_TEXTURE);
                             GL11.glLoadIdentity();
                             float var23 = var19 * (0.001F + (float)var21 * 0.003F) * 20.0F;
                             float var24 = 0.33333334F;
                             GL11.glScalef(var24, var24, var24);
                             GL11.glRotatef(30.0F - (float)var21 * 60.0F, 0.0F, 0.0F, 1.0F);
                             GL11.glTranslatef(0.0F, var23, 0.0F);
                             GL11.glMatrixMode(GL11.GL_MODELVIEW);
                             this.renderPassModel.render(Entitybrachiosaurus, var16, var15, var13, var11 - var10, var12, var14);
                         }

                         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                         GL11.glMatrixMode(GL11.GL_TEXTURE);
                         GL11.glDepthMask(true);
                         GL11.glLoadIdentity();
                         GL11.glMatrixMode(GL11.GL_MODELVIEW);
                         GL11.glEnable(GL11.GL_LIGHTING);
                         GL11.glDisable(GL11.GL_BLEND);
                         GL11.glDepthFunc(GL11.GL_LEQUAL);
                     }

                     GL11.glDisable(GL11.GL_BLEND);
                     GL11.glEnable(GL11.GL_ALPHA_TEST);
                 }
             }

             this.renderEquippedItems(Entitybrachiosaurus, f1);
             float var26 = Entitybrachiosaurus.getBrightness(f1);
             var18 = this.getColorMultiplier(Entitybrachiosaurus, var26, f1);
             OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
             GL11.glDisable(GL11.GL_TEXTURE_2D);
             OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

             if ((var18 >> 24 & 255) > 0 || Entitybrachiosaurus.hurtTime > 0 || Entitybrachiosaurus.deathTime > 0)
             {
                 GL11.glDisable(GL11.GL_TEXTURE_2D);
                 GL11.glDisable(GL11.GL_ALPHA_TEST);
                 GL11.glEnable(GL11.GL_BLEND);
                 GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                 GL11.glDepthFunc(GL11.GL_EQUAL);

                 if (Entitybrachiosaurus.hurtTime > 0 || Entitybrachiosaurus.deathTime > 0)
                 {
                     GL11.glColor4f(var26, 0.0F, 0.0F, 0.4F);
                     this.mainModel.render(Entitybrachiosaurus, var16, var15, var13, var11 - var10, var12, var14);

                     for (int var27 = 0; var27 < 4; ++var27)
                     {
                         if (this.inheritRenderPass(Entitybrachiosaurus, var27, f1) >= 0)
                         {
                             GL11.glColor4f(var26, 0.0F, 0.0F, 0.4F);
                             this.renderPassModel.render(Entitybrachiosaurus, var16, var15, var13, var11 - var10, var12, var14);
                         }
                     }
                 }

                 if ((var18 >> 24 & 255) > 0)
                 {
                     var19 = (float)(var18 >> 16 & 255) / 255.0F;
                     var20 = (float)(var18 >> 8 & 255) / 255.0F;
                     float var29 = (float)(var18 & 255) / 255.0F;
                     var22 = (float)(var18 >> 24 & 255) / 255.0F;
                     GL11.glColor4f(var19, var20, var29, var22);
                     this.mainModel.render(Entitybrachiosaurus, var16, var15, var13, var11 - var10, var12, var14);

                     for (int var28 = 0; var28 < 4; ++var28)
                     {
                         if (this.inheritRenderPass(Entitybrachiosaurus, var28, f1) >= 0)
                         {
                             GL11.glColor4f(var19, var20, var29, var22);
                             this.renderPassModel.render(Entitybrachiosaurus, var16, var15, var13, var11 - var10, var12, var14);
                         }
                     }
                 }

                 GL11.glDepthFunc(GL11.GL_LEQUAL);
                 GL11.glDisable(GL11.GL_BLEND);
                 GL11.glEnable(GL11.GL_ALPHA_TEST);
                 GL11.glEnable(GL11.GL_TEXTURE_2D);
             }

             GL11.glDisable(GL12.GL_RESCALE_NORMAL);
         }
         catch (Exception var25)
         {
             var25.printStackTrace();
         }

         OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
         GL11.glEnable(GL11.GL_TEXTURE_2D);
         OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
         GL11.glEnable(GL11.GL_CULL_FACE);
         GL11.glPopMatrix();
         this.passSpecialRender(Entitybrachiosaurus, d, d1, d2);
    }

    public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2, 
            float f, float f1)
    {
        renderNew((EntityBrachiosaurus)entityliving, d, d1, d2, f, f1);
    }

    public void doRender(Entity entity, double d, double d1, double d2, 
            float f, float f1)
    {
        renderNew((EntityBrachiosaurus)entity, d, d1, d2, f, f1);
    }
    
	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		EntityBrachiosaurus brach = (EntityBrachiosaurus)entity;
		
		if (brach.isModelized())
			return new ResourceLocation(brach.getModelTexture());
		
		return new ResourceLocation("fossilsarch:entity/Brachiosaurus.png");
	}
}