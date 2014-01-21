package net.fossilsarch.client.render;

import net.fossilsarch.common.entity.EntityAncientJavelin;
import net.fossilsarch.common.entity.EntityJavelin;
import net.fossilsarch.common.item.ItemJavelin;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderJavelin extends Render
{
    public RenderJavelin()
    {
    }

    public void renderJavelin(EntityJavelin entityjavelin, double d, double d1, double d2,
            float f, float f1)
    {
    	switch (entityjavelin.SelfMaterial){
    	case STONE:
            this.bindTexture(new ResourceLocation("/skull/StoneJavelin.png"));
    		break;
    	case IRON:
    		this.bindTexture(new ResourceLocation("/skull/IronJavelin.png"));
    		break;
    	case GOLD:
    		this.bindTexture(new ResourceLocation("/skull/GoldJavelin.png"));
    		break;
    	case EMERALD:
    		this.bindTexture(new ResourceLocation("/skull/DiamondJavelin.png"));
    		break;
    	case WOOD:
    	default:
    		this.bindTexture(new ResourceLocation("/skull/WoodenJavelin.png"));
    	}
    	if (entityjavelin instanceof EntityAncientJavelin) this.bindTexture(new ResourceLocation("/skull/AncientJavelin.png"));
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        GL11.glRotatef((entityjavelin.prevRotationYaw + (entityjavelin.rotationYaw - entityjavelin.prevRotationYaw) * f1) - 90F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(entityjavelin.prevRotationPitch + (entityjavelin.rotationPitch - entityjavelin.prevRotationPitch) * f1, 0.0F, 0.0F, 1.0F);
        Tessellator tessellator = Tessellator.instance;
        int i = 0;
        float f2 = 0.0F;
        float f3 = 0.5F;
        float f4 = (float)(0 + i * 10) / 32F;
        float f5 = (float)(5 + i * 10) / 32F;
        float f6 = 0.0F;
        float f7 = 0.15625F;
        float f8 = (float)(5 + i * 10) / 32F;
        float f9 = (float)(10 + i * 10) / 32F;
        //float f10 = 0.05625F;/*Origin*/
        float f10 = 0.16875F;/*Correct*/
        //float f10 = 0.5625F;/*Joking*/
        GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        float f11 = (float)entityjavelin.arrowShake - f1;
        if (f11 > 0.0F)
        {
            float f12 = -MathHelper.sin(f11 * 3F) * f11;
            GL11.glRotatef(f12, 0.0F, 0.0F, 1.0F);
        }
        GL11.glRotatef(45F, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(f10, f10, f10);
        GL11.glTranslatef(-4F, 0.0F, 0.0F);
        GL11.glNormal3f(f10, 0.0F, 0.0F);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-7D, -2D, -2D, f6, f8);
        tessellator.addVertexWithUV(-7D, -2D, 2D, f7, f8);
        tessellator.addVertexWithUV(-7D, 2D, 2D, f7, f9);
        tessellator.addVertexWithUV(-7D, 2D, -2D, f6, f9);
        tessellator.draw();
        GL11.glNormal3f(-f10, 0.0F, 0.0F);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-7D, 2D, -2D, f6, f8);
        tessellator.addVertexWithUV(-7D, 2D, 2D, f7, f8);
        tessellator.addVertexWithUV(-7D, -2D, 2D, f7, f9);
        tessellator.addVertexWithUV(-7D, -2D, -2D, f6, f9);
        tessellator.draw();
        for (int j = 0; j < 4; j++)
        {
            GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
            GL11.glNormal3f(0.0F, 0.0F, f10);
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(-8D, -2D, 0.0D, f2, f4);
            tessellator.addVertexWithUV(8D, -2D, 0.0D, f3, f4);
            tessellator.addVertexWithUV(8D, 2D, 0.0D, f3, f5);
            tessellator.addVertexWithUV(-8D, 2D, 0.0D, f2, f5);
            tessellator.draw();
        }

        GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        GL11.glPopMatrix();
    }

    public void doRender(Entity entity, double d, double d1, double d2,
            float f, float f1)
    {
        renderJavelin((EntityJavelin)entity, d, d1, d2, f, f1);
    }

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		EntityJavelin javelinEnt = ((EntityJavelin)entity);
		ItemJavelin javelin = (ItemJavelin) javelinEnt.GetJavelinByMaterial();
		
		if (javelin.isAncient()) return new ResourceLocation("AncientJavelin");
		else if (javelinEnt.SelfMaterial == EnumToolMaterial.GOLD) return new ResourceLocation("GoldJavelin");
		else if (javelinEnt.SelfMaterial == EnumToolMaterial.EMERALD) return new ResourceLocation("DiamondJavelin");
		else if (javelinEnt.SelfMaterial == EnumToolMaterial.IRON) return new ResourceLocation("IronJavelin");
		else if (javelinEnt.SelfMaterial == EnumToolMaterial.STONE)return new ResourceLocation("StoneJavelin");
		else return new ResourceLocation("WoodJavelin");
	}
}
