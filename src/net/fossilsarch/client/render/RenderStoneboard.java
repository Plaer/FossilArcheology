package net.fossilsarch.client.render;

import java.util.Random;

import net.fossilsarch.common.entity.EntityStoneboard;
import net.fossilsarch.common.io.EnumStoneboard;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderStoneboard extends Render
{
	 private Random rand = new Random();
	public void func_158_a(EntityStoneboard entitypainting, double d, double d1, double d2, 
            float f, float f1)
    {
		if (entitypainting.art==null) return;
        rand.setSeed(187L);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        GL11.glRotatef(f, 0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        this.bindTexture(new ResourceLocation("fossilsarch:gui/pr1.png"));
        EnumStoneboard enumart = entitypainting.art;
        float f2 = 0.0625F;
        GL11.glScalef(f2, f2, f2);
        func_159_a(entitypainting, enumart.sizeX, enumart.sizeY, enumart.offsetX, enumart.offsetY);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }
	private void func_159_a(EntityStoneboard entitypainting, int i, int j, int k, int l)
    {
        float f = (float)(-i) / 2.0F;
        float f1 = (float)(-j) / 2.0F;
        float f2 = 0.5F;
        float f3 = 0.75F;
        float f4 = 0.8125F;
        float f5 = 0.0F;
        float f6 = 0.0625F;
        float f7 = 0.75F;
        float f8 = 0.8125F;
        float f9 = 0.001953125F;
        float f10 = 0.001953125F;
        float f11 = 0.7519531F;
        float f12 = 0.7519531F;
        float f13 = 0.0F;
        float f14 = 0.0625F;

        for (int i1 = 0; i1 < i / 16; ++i1)
        {
            for (int j1 = 0; j1 < j / 16; ++j1)
            {
                float f15 = f + (float)((i1 + 1) * 16);
                float f16 = f + (float)(i1 * 16);
                float f17 = f1 + (float)((j1 + 1) * 16);
                float f18 = f1 + (float)(j1 * 16);
                this.func_160_a(entitypainting, (f15 + f16) / 2.0F, (f17 + f18) / 2.0F);
                float f19 = (float)(k + i - i1 * 16) / 256.0F;
                float f20 = (float)(k + i - (i1 + 1) * 16) / 256.0F;
                float f21 = (float)(l + j - j1 * 16) / 256.0F;
                float f22 = (float)(l + j - (j1 + 1) * 16) / 256.0F;
                Tessellator tessellator = Tessellator.instance;
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, -1.0F);
                tessellator.addVertexWithUV((double)f15, (double)f18, (double)(-f2), (double)f20, (double)f21);
                tessellator.addVertexWithUV((double)f16, (double)f18, (double)(-f2), (double)f19, (double)f21);
                tessellator.addVertexWithUV((double)f16, (double)f17, (double)(-f2), (double)f19, (double)f22);
                tessellator.addVertexWithUV((double)f15, (double)f17, (double)(-f2), (double)f20, (double)f22);
                tessellator.setNormal(0.0F, 0.0F, 1.0F);
                tessellator.addVertexWithUV((double)f15, (double)f17, (double)f2, (double)f3, (double)f5);
                tessellator.addVertexWithUV((double)f16, (double)f17, (double)f2, (double)f4, (double)f5);
                tessellator.addVertexWithUV((double)f16, (double)f18, (double)f2, (double)f4, (double)f6);
                tessellator.addVertexWithUV((double)f15, (double)f18, (double)f2, (double)f3, (double)f6);
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                tessellator.addVertexWithUV((double)f15, (double)f17, (double)(-f2), (double)f7, (double)f9);
                tessellator.addVertexWithUV((double)f16, (double)f17, (double)(-f2), (double)f8, (double)f9);
                tessellator.addVertexWithUV((double)f16, (double)f17, (double)f2, (double)f8, (double)f10);
                tessellator.addVertexWithUV((double)f15, (double)f17, (double)f2, (double)f7, (double)f10);
                tessellator.setNormal(0.0F, -1.0F, 0.0F);
                tessellator.addVertexWithUV((double)f15, (double)f18, (double)f2, (double)f7, (double)f9);
                tessellator.addVertexWithUV((double)f16, (double)f18, (double)f2, (double)f8, (double)f9);
                tessellator.addVertexWithUV((double)f16, (double)f18, (double)(-f2), (double)f8, (double)f10);
                tessellator.addVertexWithUV((double)f15, (double)f18, (double)(-f2), (double)f7, (double)f10);
                tessellator.setNormal(-1.0F, 0.0F, 0.0F);
                tessellator.addVertexWithUV((double)f15, (double)f17, (double)f2, (double)f12, (double)f13);
                tessellator.addVertexWithUV((double)f15, (double)f18, (double)f2, (double)f12, (double)f14);
                tessellator.addVertexWithUV((double)f15, (double)f18, (double)(-f2), (double)f11, (double)f14);
                tessellator.addVertexWithUV((double)f15, (double)f17, (double)(-f2), (double)f11, (double)f13);
                tessellator.setNormal(1.0F, 0.0F, 0.0F);
                tessellator.addVertexWithUV((double)f16, (double)f17, (double)(-f2), (double)f12, (double)f13);
                tessellator.addVertexWithUV((double)f16, (double)f18, (double)(-f2), (double)f12, (double)f14);
                tessellator.addVertexWithUV((double)f16, (double)f18, (double)f2, (double)f11, (double)f14);
                tessellator.addVertexWithUV((double)f16, (double)f17, (double)f2, (double)f11, (double)f13);
                tessellator.draw();
            }
        }
    }
	private void func_160_a(EntityStoneboard entitypainting, float f, float f1)
    {
        int i = MathHelper.floor_double(entitypainting.posX);
        int j = MathHelper.floor_double(entitypainting.posY + (double)(f1 / 16.0F));
        int k = MathHelper.floor_double(entitypainting.posZ);

        if (entitypainting.direction == 2)
        {
            i = MathHelper.floor_double(entitypainting.posX + (double)( f / 16.0F));
        }

        if (entitypainting.direction == 1)
        {
            k = MathHelper.floor_double(entitypainting.posZ - (double)( f / 16.0F));
        }

        if (entitypainting.direction == 0)
        {
            i = MathHelper.floor_double(entitypainting.posX - (double)( f / 16.0F));
        }

        if (entitypainting.direction == 3)
        {
            k = MathHelper.floor_double(entitypainting.posZ + (double)( f / 16.0F));
        }

        int l = this.renderManager.worldObj.getLightBrightnessForSkyBlocks(i, j, k, 0);
        int i1 = l % 65536;
        int j1 = l / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)i1, (float)j1);
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
    }
	
	
	public void doRender(Entity entity, double d, double d1, double d2, 
            float f, float f1)
    {
        func_158_a((EntityStoneboard)entity, d, d1, d2, f, f1);
    }
	
	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return new ResourceLocation("fossilsarch:gui/pr1.png");
	}
}