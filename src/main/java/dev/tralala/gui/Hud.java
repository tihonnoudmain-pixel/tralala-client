package dev.tralala.gui;

import dev.tralala.TralalaClient;
import dev.tralala.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import java.util.Comparator;
import java.util.List;

public final class Hud {
    private Hud() {
    }

    public static void render(DrawContext ctx, RenderTickCounter tickCounter) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.options.hudHidden || TralalaClient.modules == null) {
            return;
        }
        TextRenderer tr = mc.textRenderer;

        String watermark = "tralala client";
        Draw.rounded(ctx, 6, 6, tr.getWidth(watermark) + 12, 18, 4, 0x90103050);
        ctx.drawText(tr, watermark, 12, 11, Theme.ACCENT, true);

        List<Module> enabled = TralalaClient.modules.getEnabled();
        enabled.sort(Comparator.comparingInt((Module m) -> -tr.getWidth(m.getName())));
        int screenWidth = mc.getWindow().getScaledWidth();
        int y = 6;
        for (Module m : enabled) {
            int textWidth = tr.getWidth(m.getName());
            int boxX = screenWidth - textWidth - 16;
            Draw.rounded(ctx, boxX, y, textWidth + 10, 14, 3, 0x90103050);
            ctx.fill(screenWidth - 6, y + 2, screenWidth - 4, y + 12, Theme.ACCENT);
            ctx.drawText(tr, m.getName(), boxX + 5, y + 3, Theme.TEXT, true);
            y += 16;
        }
    }
}
