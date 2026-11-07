package dev.tralala.gui;

import dev.tralala.TralalaClient;
import dev.tralala.module.Category;
import dev.tralala.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class ClickGuiScreen extends Screen {
    private static final int W = 440;
    private static final int H = 270;
    private static final int SIDE = 112;

    private Category selected = Category.COMBAT;
    private boolean wasDown = true;

    public ClickGuiScreen() {
        super(Text.literal("Tralala client"));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        long handle = MinecraftClient.getInstance().getWindow().getHandle();
        boolean down = GLFW.glfwGetMouseButton(handle, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
        boolean click = down && !wasDown;
        wasDown = down;

        int x = (width - W) / 2;
        int y = (height - H) / 2;

        ctx.fill(0, 0, width, height, 0x66000814);
        Draw.rounded(ctx, x - 1, y - 1, W + 2, H + 2, 9, 0x664FC3F7);
        Draw.rounded(ctx, x, y, W, H, 8, 0xB0102A48);
        Draw.rounded(ctx, x + 6, y + 6, SIDE - 6, H - 12, 6, 0x30FFFFFF);

        ctx.drawText(textRenderer, "tralala", x + 16, y + 14, Theme.ACCENT, true);
        ctx.drawText(textRenderer, "client", x + 16 + textRenderer.getWidth("tralala "), y + 14, Theme.TEXT, true);

        Category[] categories = Category.values();
        for (int i = 0; i < categories.length; i++) {
            Category category = categories[i];
            int cx = x + 12;
            int cy = y + 36 + i * 24;
            int cw = SIDE - 18;
            boolean hover = Draw.inside(mouseX, mouseY, cx, cy, cw, 20);
            if (category == selected) {
                Draw.rounded(ctx, cx, cy, cw, 20, 5, 0x704FC3F7);
            } else if (hover) {
                Draw.rounded(ctx, cx, cy, cw, 20, 5, 0x30FFFFFF);
            }
            ctx.drawText(textRenderer, category.getTitle(), cx + 8, cy + 6, Theme.TEXT, true);
            if (click && hover) {
                selected = category;
            }
        }

        int mx = x + SIDE + 10;
        int my = y + 12;
        int mw = W - SIDE - 20;
        ctx.drawText(textRenderer, selected.getTitle(), mx, my, Theme.ACCENT, true);

        List<Module> list = TralalaClient.modules.getByCategory(selected);
        if (list.isEmpty()) {
            ctx.drawText(textRenderer, "Модули скоро появятся", mx, my + 24, Theme.TEXT_DIM, true);
        }
        int rowY = my + 22;
        for (Module m : list) {
            boolean hover = Draw.inside(mouseX, mouseY, mx, rowY, mw, 26);
            int bg = m.isEnabled() ? 0x504FC3F7 : (hover ? 0x40FFFFFF : 0x30FFFFFF);
            Draw.rounded(ctx, mx, rowY, mw, 26, 5, bg);
            ctx.drawText(textRenderer, m.getName(), mx + 8, rowY + 4, Theme.TEXT, true);
            ctx.drawText(textRenderer, m.getDescription(), mx + 8, rowY + 15, Theme.TEXT_DIM, false);

            int sx = mx + mw - 34;
            int sy = rowY + 7;
            Draw.rounded(ctx, sx, sy, 26, 12, 6, m.isEnabled() ? Theme.ACCENT : 0x60FFFFFF);
            int knobX = m.isEnabled() ? sx + 15 : sx + 2;
            ctx.fill(knobX, sy + 2, knobX + 9, sy + 10, 0xFFFFFFFF);

            if (click && hover) {
                m.toggle();
                TralalaClient.modules.save();
            }
            rowY += 30;
        }

        ctx.drawText(textRenderer, "Right Shift или Esc — закрыть", x + SIDE + 10, y + H - 16, Theme.TEXT_DIM, false);
    }
}
