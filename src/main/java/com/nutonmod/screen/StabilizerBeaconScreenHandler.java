package com.nutonmod.screen;

import com.nutonmod.block.entity.StabilizerBeaconBlockEntity;
import com.nutonmod.data.StabilizerBeaconData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

public class StabilizerBeaconScreenHandler extends ScreenHandler {
    private final StabilizerBeaconBlockEntity blockEntity;

    public StabilizerBeaconScreenHandler(int syncId, PlayerInventory playerInventory, StabilizerBeaconBlockEntity blockEntity) {
        super(ModScreenHandlers.STABILIZER_BEACON_SCREEN_HANDLER, syncId);
        this.blockEntity = blockEntity;
    }

    public StabilizerBeaconScreenHandler(int syncId, PlayerInventory playerInventory, StabilizerBeaconData data) {
        this(syncId, playerInventory, getBlockEntity(playerInventory, data));
    }

    private static StabilizerBeaconBlockEntity getBlockEntity(PlayerInventory playerInventory, StabilizerBeaconData data) {
        if (playerInventory == null || playerInventory.player.getWorld() == null) {
            return null;
        }
        var blockEntity = playerInventory.player.getWorld().getBlockEntity(data.pos());
        return blockEntity instanceof StabilizerBeaconBlockEntity stabilizerBeaconBlockEntity ? stabilizerBeaconBlockEntity : null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return blockEntity != null;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    public int getStatus() {
        return blockEntity == null ? 0 : blockEntity.getStatus();
    }

    public int getRemainingSeconds() {
        return blockEntity == null ? 0 : blockEntity.getRemainingSeconds();
    }

    public int getProtectionRadius() {
        return blockEntity == null ? 0 : blockEntity.getProtectionRadius();
    }

    public int getStormMultiplier() {
        return blockEntity == null ? 1 : blockEntity.getStormMultiplier();
    }

    public int getActivationHint() {
        return blockEntity != null && blockEntity.isStormActive() ? 1 : 0;
    }
}
