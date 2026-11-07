package dev.tralala.module.impl;

import dev.tralala.module.Category;
import dev.tralala.module.Module;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class AutoTotem extends Module {
    private int cooldown = 0;

    public AutoTotem() {
        super("AutoTotem", "Тотем в левую руку", Category.COMBAT);
    }

    @Override
    public void onTick() {
        ClientPlayerEntity player = mc.player;
        if (player == null || mc.interactionManager == null || mc.currentScreen != null) {
            return;
        }
        if (cooldown > 0) {
            cooldown--;
            return;
        }
        if (player.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING)) {
            return;
        }
        for (int i = 0; i < 36; i++) {
            if (player.getInventory().getStack(i).isOf(Items.TOTEM_OF_UNDYING)) {
                int slot = i < 9 ? i + 36 : i;
                mc.interactionManager.clickSlot(player.playerScreenHandler.syncId, slot, 40, SlotActionType.SWAP, player);
                cooldown = 3;
                return;
            }
        }
    }
}
