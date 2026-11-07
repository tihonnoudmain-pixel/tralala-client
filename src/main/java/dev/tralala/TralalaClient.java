package dev.tralala;

import dev.tralala.gui.ClickGuiScreen;
import dev.tralala.gui.Hud;
import dev.tralala.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class TralalaClient implements ClientModInitializer {
    public static ModuleManager modules;
    private boolean menuKeyWasDown = false;

    @Override
    public void onInitializeClient() {
        modules = new ModuleManager();
        modules.load();
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
        HudRenderCallback.EVENT.register(Hud::render);
    }

    private void onTick(MinecraftClient mc) {
        if (mc.getWindow() == null) {
            return;
        }
        long handle = mc.getWindow().getHandle();
        boolean down = GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
        if (down && !menuKeyWasDown) {
            if (mc.currentScreen instanceof ClickGuiScreen) {
                mc.setScreen(null);
            } else if (mc.currentScreen == null) {
                mc.setScreen(new ClickGuiScreen());
            }
        }
        menuKeyWasDown = down;

        if (mc.player != null && mc.world != null) {
            modules.tick(mc);
        }
    }
}
