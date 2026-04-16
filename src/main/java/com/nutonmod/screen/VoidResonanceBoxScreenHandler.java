package com.nutonmod.screen;

import com.nutonmod.block.entity.VoidResonanceBoxBlockEntity;
import com.nutonmod.data.VoidResonanceBoxData;
import com.nutonmod.item.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class VoidResonanceBoxScreenHandler extends ScreenHandler {
    private static final int INV_SIZE = 3;
    private static final int SLOT_INPUT = 0;
    private static final int SLOT_CATALYST = 1;
    private static final int SLOT_OUTPUT = 2;
    private static final Ingredient CATALYST = Ingredient.ofItems(ModItems.ENERGY_CRYSTAL, ModItems.ENERGY_ESSENCE);

    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;

    public VoidResonanceBoxScreenHandler(int syncId, PlayerInventory playerInventory, VoidResonanceBoxBlockEntity blockEntity, PropertyDelegate propertyDelegate) {
        super(ModScreenHandlers.VOID_RESONANCE_BOX_SCREEN_HANDLER, syncId);
        checkSize(blockEntity, INV_SIZE);
        this.inventory = blockEntity;
        this.propertyDelegate = propertyDelegate;
        inventory.onOpen(playerInventory.player);

        addSlot(new Slot(inventory, SLOT_INPUT, 18, 22));
        addSlot(new Slot(inventory, SLOT_CATALYST, 18, 44) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return VoidResonanceBoxScreenHandler.this.canInsertCatalyst(stack);
            }
        });
        addSlot(new Slot(inventory, SLOT_OUTPUT, 72, 34) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
        });

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addProperties(propertyDelegate);
    }

    public VoidResonanceBoxScreenHandler(int syncId, PlayerInventory playerInventory, VoidResonanceBoxData data) {
        this(syncId, playerInventory, data.getBlockEntity(VoidResonanceBoxBlockEntity.class, playerInventory.player), new ArrayPropertyDelegate(3));
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        if (slot < 0 || slot >= this.slots.size()) {
            return ItemStack.EMPTY;
        }

        Slot sourceSlot = this.slots.get(slot);
        if (!sourceSlot.hasStack()) {
            return ItemStack.EMPTY;
        }

        ItemStack sourceStack = sourceSlot.getStack();
        ItemStack movedStack = sourceStack.copy();

        if (slot == SLOT_OUTPUT) {
            if (!this.insertItem(sourceStack, INV_SIZE, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
            sourceSlot.onQuickTransfer(sourceStack, movedStack);
        } else if (slot < INV_SIZE) {
            if (!this.insertItem(sourceStack, INV_SIZE, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (canInsertCatalyst(sourceStack)) {
            if (!this.insertItem(sourceStack, SLOT_CATALYST, SLOT_CATALYST + 1, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (!this.insertItem(sourceStack, SLOT_INPUT, SLOT_INPUT + 1, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (sourceStack.isEmpty()) {
            sourceSlot.setStack(ItemStack.EMPTY);
        } else {
            sourceSlot.markDirty();
        }

        if (sourceStack.getCount() == movedStack.getCount()) {
            return ItemStack.EMPTY;
        }
        sourceSlot.onTakeItem(player, sourceStack);
        return movedStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    private boolean canInsertCatalyst(ItemStack stack) {
        return !stack.isEmpty() && CATALYST.test(stack);
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInventory, i, 8 + i * 18 - 5, 160 - 10));
        }
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18 - 5, 102 + i * 18 - 10));
            }
        }
    }

    public int getStatus() {
        return propertyDelegate.get(2);
    }

    public int getScaledProgress() {
        int progress = propertyDelegate.get(0);
        int max = propertyDelegate.get(1);
        return max > 0 ? progress * 24 / max : 0;
    }
}
