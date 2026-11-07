package dev.tralala.gui;

import net.minecraft.client.gui.DrawContext;

public final class Draw {
    private Draw() {
    }

    public static void rounded(DrawContext c, int x, int y, int w, int h, int r, int color) {
        int radius = Math.max(0, Math.min(r, Math.min(w, h) / 2));
        c.fill(x + radius, y, x + w - radius, y + h, color);
        c.fill(x, y + radius, x + radius, y + h - radius, color);
        c.fill(x + w - radius, y + radius, x + w, y + h - radius, color);
        for (int i = 0; i < radius; i++) {
            double d = radius - i - 0.5;
            int inset = radius - (int) Math.round(Math.sqrt(Math.max(0, radius * radius - d * d)));
            c.fill(x + inset, y + i, x + radius, y + i + 1, color);
            c.fill(x + w - radius, y + i, x + w - inset, y + i + 1, color);
            c.fill(x + inset, y + h - i - 1, x + radius, y + h - i, color);
            c.fill(x + w - radius, y + h - i - 1, x + w - inset, y + h - i, color);
        }
    }

    public static boolean inside(int mx, int my, int x, int y, int w, int h) {
        return mx >= x && mx < x + w && my >= y && my < y + h;
    }
}
