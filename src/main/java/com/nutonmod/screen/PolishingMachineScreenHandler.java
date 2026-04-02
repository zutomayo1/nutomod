package com.nutonmod.screen;

import com.nutonmod.block.entity.PolishingMachineBlockEntity;
import com.nutonmod.data.PolishingMachineData;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;


public class PolishingMachineScreenHandler extends ScreenHandler {
    private static final int MACHINE_INVENTORY_SIZE = 2;
    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    public final PolishingMachineBlockEntity blockEntity;
    public PolishingMachineScreenHandler(int syncId, PlayerInventory playerInventory, PropertyDelegate propertyDelegate, BlockEntity blockEntity) {
        super(ModScreenHandlers.POLISHING_MACHINE_SCREEN_HANDLER, syncId);
        checkSize((Inventory) blockEntity, MACHINE_INVENTORY_SIZE);
        this.inventory = (Inventory) blockEntity;
        inventory.onOpen(playerInventory.player);
        this.propertyDelegate = propertyDelegate;
        this.blockEntity = (PolishingMachineBlockEntity) blockEntity;

        this.addSlot(new Slot(inventory, 0, 80, 11));
        this.addSlot(new Slot(inventory, 1, 80, 59));

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addProperties(propertyDelegate);
    }

    public PolishingMachineScreenHandler(int syncId, PlayerInventory playerInventory, PolishingMachineData data) {
        this(syncId, playerInventory, new ArrayPropertyDelegate(2), playerInventory.player.getWorld().getBlockEntity(data.pos()));
    }
    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for(int i = 0; i < 9; i++){
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 9; j++){
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot invSlot = this.slots.get(slot);

        if (invSlot != null && invSlot.hasStack()) {
            ItemStack originalStack = invSlot.getStack();
            newStack = originalStack.copy();

            if (slot == OUTPUT_SLOT) {
                if (!this.insertItem(originalStack, MACHINE_INVENTORY_SIZE, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                invSlot.onQuickTransfer(originalStack, newStack);
            } else if (slot < MACHINE_INVENTORY_SIZE) {
                if (!this.insertItem(originalStack, MACHINE_INVENTORY_SIZE, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (originalStack.isOf(Items.ICE)) {
                if (!this.insertItem(originalStack, INPUT_SLOT, OUTPUT_SLOT, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slot < MACHINE_INVENTORY_SIZE + 27) {
                if (!this.insertItem(originalStack, MACHINE_INVENTORY_SIZE + 27, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, MACHINE_INVENTORY_SIZE, MACHINE_INVENTORY_SIZE + 27, false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                invSlot.setStack(ItemStack.EMPTY);
            } else {
                invSlot.markDirty();
            }

            if (originalStack.getCount() == newStack.getCount()) {
                return ItemStack.EMPTY;
            }

            invSlot.onTakeItem(player, originalStack);
        }

        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public boolean isCrafting(){
        return propertyDelegate.get(0) > 0;
    }
    public int getScaledProgress(){
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1);
        int progressArrowSize = 26;

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

}
