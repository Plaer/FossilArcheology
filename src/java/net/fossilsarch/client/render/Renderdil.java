
      package net.fossilsarch.client.render;

import java.util.Random;

import net.fossilsarch.client.model.Modeldil;
import net.fossilsarch.common.entity.Entitydil;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class Renderdil extends RenderLiving
{
	public Renderdil(ModelBase modelbase, float f)
    {
        super(modelbase, f); 
    }
	public void RenderDino(Entitydil entityUta, double d, double d1, double d2, 
            float f, float f1)
    {
        //super.doRenderLiving(EntityUta, d, d1, d2, f, f1);
		GL11.glPushMatrix();
        GL11.glDisable(2884 /*GL_CULL_FACE*/);
        this.bindTexture(this.getEntityTexture(entityUta));
        mainModel.onGround = renderSwingProgress(entityUta, f1);
        if(renderPassModel != null)
        {
            renderPassModel.onGround = mainModel.onGround;
        }
        mainModel.isRiding = entityUta.isRiding();
        if(renderPassModel != null)
        {
            renderPassModel.isRiding = mainModel.isRiding;
        }
        try
        {
            float f2 = entityUta.prevRenderYawOffset + (entityUta.renderYawOffset - entityUta.prevRenderYawOffset) * f1;
            float f3 = entityUta.prevRotationYaw + (entityUta.rotationYaw - entityUta.prevRotationYaw) * f1;
            float f4 = entityUta.prevRotationPitch + (entityUta.rotationPitch - entityUta.prevRotationPitch) * f1;
            renderLivingAt(entityUta, d, d1, d2);
            float f5 = handleRotationFloat(entityUta, f1);
            rotateCorpse(entityUta, f5, f2, f1);
            float f6 = 0.0625F;
            GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
            GL11.glScalef(entityUta.getGLX(), -entityUta.getGLY(), entityUta.getGLZ());
            preRenderCallback(entityUta, f1);
            GL11.glTranslatef(0.0F, -24F * f6 - 0.0078125F, 0.0F);
            float f7 = entityUta.prevLimbSwingAmount+ (entityUta.limbSwingAmount - entityUta.prevLimbSwingAmount) * f1;
            float f8 = entityUta.limbSwing - entityUta.limbSwingAmount * (1.0F - f1);
            if(f7 > 1.0F)
            {
                f7 = 1.0F;
            }
            GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
            mainModel.setLivingAnimations(entityUta, f8, f7, f1);
            



				if (entityUta.getEntityToAttack()!=null || entityUta.ItemInMouth!=null){
					((Modeldil)mainModel).OpenMouth(5);
				}else{
					((Modeldil)mainModel).CloseMouth(10);
				}
            ((Modeldil)mainModel).render(entityUta,f8, f7, f5, f3 - f2, f4, f6);			
            for(int i = 0; i < 4; i++)
            {
                if(shouldRenderPass(entityUta, i, f1)>=0)
                {
                    renderPassModel.render(entityUta,f8, f7, f5, f3 - f2, f4, f6);
                    GL11.glDisable(3042 /*GL_BLEND*/);
                    GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
                }
            }

            renderEquippedItems(entityUta, f1);
            float f9 = entityUta.getBrightness(f1);
            int j = getColorMultiplier(entityUta, f9, f1);
            if((j >> 24 & 0xff) > 0 || entityUta.hurtTime > 0 || entityUta.deathTime > 0)
            {
                GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
                GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
                GL11.glEnable(3042 /*GL_BLEND*/);
                GL11.glBlendFunc(770, 771);
                GL11.glDepthFunc(514);
                if(entityUta.hurtTime > 0 || entityUta.deathTime > 0)
                {
                    GL11.glColor4f(f9, 0.0F, 0.0F, 0.4F);
                    ((Modeldil)mainModel).render(entityUta,f8, f7, f5, f3 - f2, f4, f6);
                    for(int k = 0; k < 4; k++)
                    {
                        if(shouldRenderPass(entityUta, k, f1)>=0)
                        {
                            GL11.glColor4f(f9, 0.0F, 0.0F, 0.4F);
                            renderPassModel.render(entityUta,f8, f7, f5, f3 - f2, f4, f6);
                        }
                    }

                }
                if((j >> 24 & 0xff) > 0)
                {
                    float f10 = (float)(j >> 16 & 0xff) / 255F;
                    float f11 = (float)(j >> 8 & 0xff) / 255F;
                    float f12 = (float)(j & 0xff) / 255F;
                    float f13 = (float)(j >> 24 & 0xff) / 255F;
                    GL11.glColor4f(f10, f11, f12, f13);
                    ((Modeldil)mainModel).render(entityUta,f8, f7, f5, f3 - f2, f4, f6);

                    for(int l = 0; l < 4; l++)
                    {
                        if(inheritRenderPass(entityUta, l, f1)>=0)
                        {
                            GL11.glColor4f(f10, f11, f12, f13);
                            renderPassModel.render(entityUta,f8, f7, f5, f3 - f2, f4, f6);
                        }
                    }

                }
                GL11.glDepthFunc(515);
                GL11.glDisable(3042 /*GL_BLEND*/);
                GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
                GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
            }
            GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        GL11.glEnable(2884 /*GL_CULL_FACE*/);
        GL11.glPopMatrix();
        passSpecialRender(entityUta, d, d1, d2);
    }

    public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2, 
            float f, float f1)
    {
        RenderDino((Entitydil)entityliving, d, d1, d2, f, f1);
    }

    public void doRender(Entity entity, double d, double d1, double d2, 
            float f, float f1)
    {
        RenderDino((Entitydil)entity, d, d1, d2, f, f1);
    }

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		Entitydil dino = (Entitydil)entity;
		
		if (dino.getEntityToAttack() == null) return new ResourceLocation("/mob/UtaCalm");
		else return new ResourceLocation("/mobo/UtaAttack");
	}
}
