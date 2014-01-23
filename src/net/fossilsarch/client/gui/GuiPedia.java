package net.fossilsarch.client.gui;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.fossilsarch.common.containers.ContainerPedia;
import net.fossilsarch.common.entity.EntityDinosaurce;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;

@SideOnly(Side.CLIENT)
public class GuiPedia extends GuiContainer
{
    private EntityDinosaurce dino;

    public GuiPedia(InventoryPlayer par1InventoryPlayer, EntityDinosaurce par2Dino, World par3World)
    {
        super(new ContainerPedia());
        this.dino = par2Dino;
        this.xSize=178;
        this.ySize=164;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui()
    {
        super.initGui();
        int var1 = (this.width - this.xSize) / 2;
        int var2 = (this.height - this.ySize) / 2;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString(new StringBuilder().append(this.dino.getDinoAge()).append("days").toString(), 110, 31, 4210752);
        this.fontRenderer.drawString(new StringBuilder().append(this.dino.getHealth()).append('/').append(this.dino.getMaxHealth()).toString(), 110, 47, 4210752);
        this.fontRenderer.drawString(new StringBuilder().append(this.dino.getHunger()).append('/').append(this.dino.getHungerLimit()).toString(), 110, 62, 4210752);
        String[] additional=this.dino.additionalPediaMessage();
        if (additional==null) return;
        for (int i=0;i<additional.length && i<=6;i++){
        	this.fontRenderer.drawString(additional[i],104,80+(this.fontRenderer.FONT_HEIGHT*i),4210752);
        }
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(new ResourceLocation("fossilsarch:gui/UIPedia.png"));
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);


            int var5 = (this.width - this.xSize) / 2;
            int var6 = (this.height - this.ySize) / 2;
            GL11.glPushMatrix();
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glPopMatrix();
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.enableStandardItemLighting();
    }
    
    @Override
    public void onGuiClosed()
    {
    	super.onGuiClosed();
    	EntityDinosaurce.pediaingDino=null;
    }
}
