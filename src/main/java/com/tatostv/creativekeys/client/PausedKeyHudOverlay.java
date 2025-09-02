package com.tatostv.creativekeys.client;

import com.tatostv.creativekeys.CreativeKeys;
import com.tatostv.creativekeys.item.CreativeKeyItem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.world.item.ItemStack;

public class PausedKeyHudOverlay implements LayeredDraw.Layer {

    private ItemStack iconStack;

    @Override
    public void render(GuiGraphics gfx, DeltaTracker partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;

        if (iconStack == null) {
            iconStack = new ItemStack(CreativeKeys.CREATIVE_KEY.get());
        }

        long now = mc.player.level().getGameTime();
        long expires = mc.player.getPersistentData().getLong(CreativeKeyItem.NBT_EXPIRES);
        if (expires > now) return; // active timer handled by main overlay

        long remaining = mc.player.getPersistentData().getLong("creative_keys:paused_remaining");
        if (remaining <= 0) return; // not paused

        long mins = remaining / (20L * 60L);
        long secs = (remaining / 20L) % 60L;
        String timeStr = String.format("%d:%02d (paused)", mins, secs);

        Font font = mc.font;

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        int paddingX = 6;
        int paddingY = 4;
        int margin = 6;
        int iconSize = 16;
        int gap = 6;
        int textW = font.width(timeStr);
        int bgW = paddingX + iconSize + gap + textW + paddingX;
        int bgH = Math.max(iconSize, font.lineHeight) + paddingY * 2;

        int x = margin;
        int y = screenHeight - bgH - 36; // above hotbar

        int bgColor = 0x90000000; // ARGB, slightly more transparent
        int borderColor = 0x60FFFFFF;
        gfx.fill(x - 1, y - 1, x + bgW + 1, y + bgH + 1, borderColor);
        gfx.fill(x, y, x + bgW, y + bgH, bgColor);

        int iconX = x + paddingX;
        int iconY = y + (bgH - iconSize) / 2;
        gfx.renderItem(iconStack, iconX, iconY);

        int textX = iconX + iconSize + gap;
        int textY = y + (bgH - font.lineHeight) / 2;
        int color = 0xFFAAAAAA; // gray when paused
        gfx.drawString(font, timeStr, textX, textY, color, false);
    }
}

