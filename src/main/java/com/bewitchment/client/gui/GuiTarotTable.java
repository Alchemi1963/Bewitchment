package com.bewitchment.client.gui;

import com.bewitchment.Bewitchment;
import com.bewitchment.api.registry.Tarot;
import com.bewitchment.common.block.tile.container.ContainerTarotTable;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiTarotTable extends GuiContainer {
	private static final ResourceLocation TEX = new ResourceLocation(Bewitchment.MODID, "textures/gui/tarot_table.png");
	private static final ResourceLocation TEX_FRAME = new ResourceLocation(Bewitchment.MODID, "textures/gui/tarot/frame.png");
	private static final ResourceLocation TEX_FRAME_NUMBER = new ResourceLocation(Bewitchment.MODID, "textures/gui/tarot/frame_number.png");
	
	private final ContainerTarotTable container;
	
	public GuiTarotTable(ContainerTarotTable container) {
		super(container);
		this.container = container;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(TEX);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		for (int i = 0; i < container.toRead.size(); i++) {
			GlStateManager.pushMatrix();
			GlStateManager.color(1, 1, 1);
			Tarot tarot = container.toRead.get(i);
			mc.getTextureManager().bindTexture(tarot.texture);
			int cx = x + (i % 2 == 0 ? 24 : 102);
			int cy = y + (i / 2 == 0 ? 12 : 90);
			GlStateManager.pushMatrix();
			if (tarot.isReversed(container.player)) {
				//todo reverse the card
			}
			drawCard(cx, cy, 48, 64);
			GlStateManager.popMatrix();
			mc.getTextureManager().bindTexture(tarot.getNumber(container.player) < 0 ? TEX_FRAME : TEX_FRAME_NUMBER);
			drawCard(cx, cy, 50, 66);
			GlStateManager.popMatrix();
			if (tarot.getNumber(container.player) > -1) {
				int num = tarot.getNumber(container.player);
				String number = Math.min(99, num) + (num > 99 ? "+" : "");
				drawCenteredString(mc.fontRenderer, number, cx + 25, cy + 57, 0x7f7f7f);
			}
		}
	}
	
	private void drawCard(int x, int y, int sizeX, int sizeY) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buff = tessellator.getBuffer();
		buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buff.pos(x, y + sizeY, 0).tex(0, 1).endVertex();
		buff.pos(x + sizeX, y + sizeY, 0).tex(1, 1).endVertex();
		buff.pos(x + sizeX, y, 0).tex(1, 0).endVertex();
		buff.pos(x, y, 0).tex(0, 0).endVertex();
		tessellator.draw();
	}
}