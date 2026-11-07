package dev.tralala.module.impl;

import dev.tralala.module.Category;
import dev.tralala.module.Module;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", "Автоматический бег", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.options.forwardKey.isPressed()) {
            mc.options.sprintKey.setPressed(true);
        }
    }
}
