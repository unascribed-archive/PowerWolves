package com.gameminers.powerwolves.gui;

import java.util.List;

import gminers.glasspane.component.PaneImage;
import gminers.kitchensink.Rendering;
import gminers.kitchensink.Strings;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.gameminers.powerwolves.entity.EntityPowerWolf;
import com.gameminers.powerwolves.enums.SpecialWolfType;
import com.gameminers.powerwolves.inventory.ContainerWolf;
import com.google.common.collect.Lists;

@SideOnly(Side.CLIENT)
public class GuiWolfInventory extends GuiContainer
{
    private static final ResourceLocation wolfGuiTextures = new ResourceLocation("powerwolves", "textures/gui/wolf_inv.png");

	private IInventory inventory1;
    private IInventory inventory2;
    private EntityPowerWolf wolf;
    private float mouseX;
    private float mouseY;

    public GuiWolfInventory(IInventory inventory1, IInventory inventory2, EntityPowerWolf wolf) {
        super(new ContainerWolf(inventory1, inventory2, wolf));
        this.inventory1 = inventory1;
        this.inventory2 = inventory2;
        this.wolf = wolf;
        this.allowUserInput = false;
    }

    protected void drawGuiContainerForegroundLayer(int unused, int unused2) {
        this.fontRendererObj.drawString(wolf.hasCollar() ? wolf.getCommandSenderName() : I18n.format("entity.powerwolves.wolf.name"), 8, 6, 4210752);
        this.fontRendererObj.drawString(this.inventory1.hasCustomInventoryName() ? this.inventory1.getInventoryName() : I18n.format(this.inventory1.getInventoryName(), new Object[0]), 8, this.ySize - 96 + 2, 4210752);
        int x = 83;
        int y = 21;
        int width = 82;
        int height = 40;
        int color = -267386864;
        GL11.glTranslatef(0.0F, 0.0F, 35.0F);
        drawGradientRect(x - 3, y - 4, x + width + 3, y - 3, -267386864, -267386864);
        drawGradientRect(x - 3, y + height + 3, x + width + 3, y + height + 4, -267386864, -267386864);
        
        drawGradientRect(x - 3, y - 3, x + width + 3, y + height + 3, -267386864, -267386864);
        drawGradientRect(x - 4, y - 3, x - 3, y + height + 3, -267386864, -267386864);
        drawGradientRect(x + width + 3, y - 3, x + width + 4, y + height + 3, -267386864, -267386864);
        
        int color1 = 1347420415;
        int color2 = 1344798840;
        drawGradientRect(x - 3, y - 3 + 1, x - 3 + 1, y + height + 3 - 1, 1347420415, 1344798840);
        drawGradientRect(x + width + 2, y - 3 + 1, x + width + 3, y + height + 3 - 1, 1347420415, 1344798840);
        
        drawGradientRect(x - 3, y - 3, x + width + 3, y - 3 + 1, 1347420415, 1347420415);
        drawGradientRect(x - 3, y + height + 2, x + width + 3, y + height + 3, 1344798840, 1344798840);
        int healthX = x-3;
        int healthY = 65;
        Rendering.drawRect(healthX, healthY, healthX+88, healthY+5, 0xFFFF5555);
        int size = (int) ((wolf.getHealth()/wolf.getMaxHealth())*88);
        Rendering.drawRect(healthX, healthY, healthX+size, healthY+5, 0xFF55FF55);
        
        int textX = 83;
        int textY = 20;
        drawCenteredString(fontRendererObj, wolf.getType().getFriendlyName(), textX+41, textY, 0xFFFFFFFF);
        textY+=12;
        if (!wolf.hasCollar()) {
        	drawCenteredString(fontRendererObj, "\u00A7cNo Collar", textX+41, textY, 0xFFFFFFFF);
        	textY+=12;
        }
        SpecialWolfType type = wolf.getSpecialType();
        if (type != null) {
        	drawCenteredString(fontRendererObj, "Special: \u00A7e"+type.getDescription(), textX+41, textY, 0xFFFFFFFF);
        	textY+=12;
        }
    }

    private int zoom = 28;
    
	protected void drawGuiContainerBackgroundLayer(float unused, int unused2, int unused3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(wolfGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        GL11.glPushMatrix();
        int x = k+26;
        int y = l+70;
        int boxWidth = 52;
        int boxHeight = 52;
        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int scaleFactor = res.getScaleFactor();
        GL11.glScissor(x*scaleFactor, (height-y)*scaleFactor, boxWidth*scaleFactor, boxHeight*scaleFactor);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glTranslatef(0, -14+(zoom/2), 0);
        GuiInventory.func_147046_a(k + 51, l + 60, zoom, (float)(k + 51) - this.mouseX, (float)(l + 75 - 50) - this.mouseY, this.wolf);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef(0, 0, 400);
        String s = "Zoom: \u00A7e"+((zoom-28)+100)+"%";
        GL11.glScalef(0.5f, 0.5f, 1f);
        fontRendererObj.drawStringWithShadow(s, ((x+boxWidth)*2)-(fontRendererObj.getStringWidth(s)+2), (y*2)-10, 0xFFFFFF);
        GL11.glPopMatrix();
    }

	@Override
	public void updateScreen() {
		super.updateScreen();
		int wheel = Mouse.getDWheel();
		zoom += wheel/60;
        if (zoom < 28) {
        	zoom = 28;
        } else if (zoom > 128) {
        	zoom = 128;
        }
	}
	
    public void drawScreen(int mouseX, int p_73863_2_, float p_73863_3_) {
        this.mouseX = (float)mouseX;
        this.mouseY = (float)p_73863_2_;
        super.drawScreen(mouseX, p_73863_2_, p_73863_3_);
    }
}