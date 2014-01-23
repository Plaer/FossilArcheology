package net.fossilsarch.client.gui;

import net.fossilsarch.mod_Fossil;
import net.fossilsarch.common.containers.ContainerAnalyzer;
import net.fossilsarch.common.tileentity.TileEntityAnalyzer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class GuiAnalyzer extends GuiContainer{
    private TileEntityAnalyzer analyzerInventory;
	public GuiAnalyzer(InventoryPlayer inventoryplayer, TileEntity tileentityanalyzer)
    {
		super(new ContainerAnalyzer(inventoryplayer, tileentityanalyzer));
		analyzerInventory = (TileEntityAnalyzer) tileentityanalyzer;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        fontRenderer.drawString(StatCollector.translateToLocal("tile.analyzerIdle.name"), 19, 6, 0x404040);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, (ySize - 96) + 2, 0x404040);
    }
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f,int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(new ResourceLocation("fossilsarch:gui/UIAnalyzer.png"));
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
		
        int k1 = analyzerInventory.getCookProgressScaled(21);
        drawTexturedModalRect(l + 80, i1 + 22, 177, 18, k1 + 1, 9);
    }

}