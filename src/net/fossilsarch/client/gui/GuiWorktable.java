package net.fossilsarch.client.gui;

import net.fossilsarch.mod_Fossil;
import net.fossilsarch.common.containers.ContainerWorktable;
import net.fossilsarch.common.tileentity.TileEntityWorktable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;
public class GuiWorktable extends GuiContainer
{
	private TileEntityWorktable furnaceInventory;
	public GuiWorktable(InventoryPlayer inventoryplayer, TileEntity tileentityfurnace)
    {
        super(new ContainerWorktable(inventoryplayer, tileentityfurnace));
        furnaceInventory = (TileEntityWorktable) tileentityfurnace;
    }
	protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString(mod_Fossil.GetLangTextByKey("block.workbench.Name"), 30, 6, 0x404040);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, (ySize - 96) + 2, 0x404040);
    }
	protected void drawGuiContainerBackgroundLayer(float f,int unusedi, int unusedj)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(new ResourceLocation("/skull/UIPazzle.png"));
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        if(furnaceInventory.isBurning())
        {
            int l = furnaceInventory.getBurnTimeRemainingScaled(12);
            drawTexturedModalRect(j + 82, (k + 36 + 12) - l, 176, 12 - l, 14, l + 2);
        }
        int i1 = furnaceInventory.getCookProgressScaled(24);
        drawTexturedModalRect(j + 79, k + 18, 176, 14, i1 + 1, 16);
    }
}