
// Java generated by Techne
// This is the first stage to more output options
// and therefore a very basic Render-File
// I hope it will be useful, in any case, leave
// feedback so I can improve on it
// - ZeuX
      package net.fossilsarch.client.render;

import net.fossilsarch.client.model.ModelMosasaurus;
import net.fossilsarch.common.entity.EntityMosasaurus;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderMosasaurus extends RenderLiving
{

    public RenderMosasaurus(float f)
    {
        super(new ModelMosasaurus(), f);
        this.setRenderPassModel(new ModelMosasaurus());
    }
    protected int shouldRenderPass(EntityLiving entityliving, int i, float f)
    {
    	EntityMosasaurus MStemp=(EntityMosasaurus)entityliving;
    	//if (MStemp.Hunger<=400){
    		return setHungryEyesBrightness(MStemp,i,f);
    	//}else return false;
    }
    public int setHungryEyesBrightness(EntityMosasaurus entitymosasaurus, int i, float f){
        if(i != 0)
        {
            return -1;
        }
    	this.bindTexture(new ResourceLocation("fossilsarch:entity/RenderPassMosasaurus.png"));
    	float f1 = 300.0F;
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, f1*100);
        return 1;
    }
    public void renderModelMosasaurus(EntityLiving entityliving, double d, double d1, double d2, 
            float f, float f1)
    {
        super.doRenderLiving(entityliving, d, d1, d2, f, f1);
    }

    public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2, 
            float f, float f1)
    {
    	renderCow((EntityMosasaurus)entityliving, d, d1, d2, f, f1);
    }

    public void doRender(Entity entity, double d, double d1, double d2, 
            float f, float f1)
    {
    	renderCow((EntityMosasaurus)entity, d, d1, d2, f, f1);
    }
	/*protected void renderEquippedItems(EntityLiving entityliving, float f)
    {
		float AgeOffset=(1.0F+0.0F*((float)((EntityMosasaurus)entityliving).getDinoAge()));
		ItemStack itemstack = ((EntityMosasaurus)entityliving).ItemInMouth;
        if(itemstack != null)
        {
            GL11.glPushMatrix();
            ((ModelMosasaurus)mainModel).Upper_Jaws.postRender(0.625F);
            if(itemstack.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType()))
            {
                float f1 = 0.5F;
                GL11.glTranslatef(0.0F, 0.4F, -0.3125F);
                f1 *= 0.75F;
                GL11.glRotatef(20F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(f1*AgeOffset, -f1*AgeOffset, f1*AgeOffset);
            } else
            if(Item.itemsList[itemstack.itemID].isFull3D())
            {
                float f2 = 0.625F;
                GL11.glTranslatef(0.0F, 0.4F, 0.0F);
                GL11.glScalef(f2*AgeOffset, -f2*AgeOffset, f2*AgeOffset);
                GL11.glRotatef(-100F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
            } else
            {
                float f3 = 0.375F;
                GL11.glTranslatef(0.25F, 0.4F, -0.1875F);
                GL11.glScalef(f3*AgeOffset, f3*AgeOffset, f3*AgeOffset);
                GL11.glRotatef(60F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(20F, 0.0F, 0.0F, 1.0F);
            }
            //renderManager.itemRenderer.renderItem(entityliving, itemstack);
            GL11.glPopMatrix();
        }
	}*/
    public void renderCow(EntityMosasaurus entityMosasaurus, double d, double d1, double d2, 
            float f, float f1)
    {
		
        //super.doRenderLiving(entityMosasaurus, d, d1, d2, f, f1);

    	
		GL11.glPushMatrix();
        GL11.glDisable(2884 /*GL_CULL_FACE*/);
        mainModel.onGround = renderSwingProgress(entityMosasaurus, f1);
        if(renderPassModel != null)
        {
            renderPassModel.onGround = mainModel.onGround;
        }
        mainModel.isRiding = entityMosasaurus.isRiding();
        if(renderPassModel != null)
        {
            renderPassModel.isRiding = mainModel.isRiding;
        }
        try
        {

        	
            float f2 = entityMosasaurus.prevRenderYawOffset + (entityMosasaurus.renderYawOffset - entityMosasaurus.prevRenderYawOffset) * f1;
            float f3 = entityMosasaurus.prevRotationYaw + (entityMosasaurus.rotationYaw - entityMosasaurus.prevRotationYaw) * f1;
            float f4 = entityMosasaurus.prevRotationPitch + (entityMosasaurus.rotationPitch - entityMosasaurus.prevRotationPitch) * f1;
            renderLivingAt(entityMosasaurus, d, d1, d2);
            float f5 = handleRotationFloat(entityMosasaurus, f1);
            rotateCorpse(entityMosasaurus, f5, f2, f1);

            float f6 = 0.0625F;
            GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
            GL11.glScalef(entityMosasaurus.getGLX(), -entityMosasaurus.getGLY(), entityMosasaurus.getGLZ());
            preRenderCallback(entityMosasaurus, f1);
            GL11.glTranslatef(0.0F, -24F * f6 - 0.0078125F, 0.0F);
            float f7 = entityMosasaurus.prevLimbSwingAmount+ (entityMosasaurus.limbSwingAmount - entityMosasaurus.prevLimbSwingAmount) * f1;
            float f8 = entityMosasaurus.limbSwing - entityMosasaurus.limbSwingAmount * (1.0F - f1);
            if(f7 > 1.0F)
            {
                f7 = 1.0F;
            }
            GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
            mainModel.setLivingAnimations(entityMosasaurus, f8, f7, f1);
            ((ModelMosasaurus)mainModel).render(entityMosasaurus,f8, f7, f5, f3 - f2, f4, f6);
            for(int i = 0; i < 4; i++)
            {
                if(shouldRenderPass(entityMosasaurus, i, f1)>=0)
                {
                   ((ModelMosasaurus)renderPassModel).render(entityMosasaurus,f8, f7, f5, f3 - f2, f4, f6);
                   GL11.glDisable(3042 /*GL_BLEND*/);
                   GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
                }
            }

            renderEquippedItems(entityMosasaurus, f1);
            float f9 = entityMosasaurus.getBrightness(f1);
            int j = getColorMultiplier(entityMosasaurus, f9, f1);
            if((j >> 24 & 0xff) > 0 || entityMosasaurus.hurtTime > 0 || entityMosasaurus.deathTime > 0)
            {
                GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
                GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
                GL11.glEnable(3042 /*GL_BLEND*/);
                GL11.glBlendFunc(770, 771);
                GL11.glDepthFunc(514);
                if(entityMosasaurus.hurtTime > 0 || entityMosasaurus.deathTime > 0)
                {
                    GL11.glColor4f(f9, 0.0F, 0.0F, 0.4F);
                    ((ModelMosasaurus)mainModel).render(entityMosasaurus,f8, f7, f5, f3 - f2, f4, f6);
                    for(int k = 0; k < 4; k++)
                    {
                        if(inheritRenderPass(entityMosasaurus, k, f1)>=0)
                        {
                            GL11.glColor4f(f9, 0.0F, 0.0F, 0.4F);
                            ((ModelMosasaurus)renderPassModel).render(entityMosasaurus,f8, f7, f5, f3 - f2, f4, f6);
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
                    ((ModelMosasaurus)mainModel).render(entityMosasaurus,f8, f7, f5, f3 - f2, f4, f6);
                    for(int l = 0; l < 4; l++)
                    {
                        if(inheritRenderPass(entityMosasaurus, l, f1)>=0)
                        {
                            GL11.glColor4f(f10, f11, f12, f13);
                            ((ModelMosasaurus)renderPassModel).render(entityMosasaurus,f8, f7, f5, f3 - f2, f4, f6);
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
        passSpecialRender(entityMosasaurus, d, d1, d2);
    }
    
	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return new ResourceLocation( "fossilsarch:entity/Mosasaurus.png");
	}
}