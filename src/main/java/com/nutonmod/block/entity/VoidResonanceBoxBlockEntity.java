package com.nutonmod.block.entity;

import com.nutonmod.block.custom.VoidResonanceBox;
import com.nutonmod.data.VoidResonanceBoxData;
import com.nutonmod.recipe.VoidResonanceRecipe;
import com.nutonmod.screen.VoidResonanceBoxScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class VoidResonanceBoxBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<VoidResonanceBoxData>, ImplementedInventory {
    private static final int INPUT_SLOT = 0;
    private static final int CATALYST_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;
    public static final int STATUS_CAN_PROCESS = 1;
    public static final int STATUS_MISSING_MATERIAL = 2;
    public static final int STATUS_OUTPUT_FULL = 3;
    public static final int STATUS_MISSING_CATALYST = 4;

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 200;
    private int machineStatus = STATUS_MISSING_MATERIAL;

    public VoidResonanceBoxBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.VOID_RESONANCE_BOX_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> VoidResonanceBoxBlockEntity.this.progress;
                    case 1 -> VoidResonanceBoxBlockEntity.this.maxProgress;
                    case 2 -> VoidResonanceBoxBlockEntity.this.machineStatus;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> VoidResonanceBoxBlockEntity.this.progress = value;
                    case 1 -> VoidResonanceBoxBlockEntity.this.maxProgress = value;
                    case 2 -> VoidResonanceBoxBlockEntity.this.machineStatus = value;
                }
            }

            @Override
            public int size() {
                return 3;
            }
        };
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("container.nutonmod.void_resonance_box");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new VoidResonanceBoxScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    public VoidResonanceBoxData getScreenOpeningData(ServerPlayerEntity player) {
        return new VoidResonanceBoxData(pos);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, this.inventory, false, registryLookup);
        nbt.putInt("progress", progress);
        nbt.putInt("max_progress", maxProgress);
        nbt.putInt("machine_status", machineStatus);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, this.inventory, registryLookup);
        progress = nbt.getInt("progress");
        maxProgress = nbt.contains("max_progress") ? nbt.getInt("max_progress") : 200;
        machineStatus = nbt.contains("machine_status") ? nbt.getInt("machine_status") : STATUS_MISSING_MATERIAL;
    }

    public static void tick(World world, BlockPos pos, BlockState state, VoidResonanceBoxBlockEntity blockEntity) {
        if (world.isClient()) {
            return;
        }

        Optional<RecipeEntry<VoidResonanceRecipe>> recipe = blockEntity.getCurrentRecipe();
        blockEntity.machineStatus = blockEntity.getStatus(recipe);
        blockEntity.maxProgress = recipe.map(value -> value.value().time()).orElse(200);

        boolean canProcess = blockEntity.machineStatus == STATUS_CAN_PROCESS;
        if (canProcess) {
            blockEntity.progress++;
            if (blockEntity.progress >= blockEntity.maxProgress && recipe.isPresent()) {
                blockEntity.craftItem(recipe.get());
                blockEntity.progress = 0;
            }
        } else {
            blockEntity.progress = 0;
        }

        blockEntity.setWorkingState(world, pos, state, canProcess);
        markDirty(world, pos, world.getBlockState(pos));
    }

    private Optional<RecipeEntry<VoidResonanceRecipe>> getCurrentRecipe() {
        if (this.world == null) {
            return Optional.empty();
        }

        SingleStackRecipeInput input = new SingleStackRecipeInput(this.getStack(INPUT_SLOT));
        return this.world.getRecipeManager()
                .getFirstMatch(VoidResonanceRecipe.Type.INSTANCE, input, this.world)
                .filter(entry -> entry.value().catalyst().test(this.getStack(CATALYST_SLOT)))
                .filter(entry -> !this.getStack(OUTPUT_SLOT).isEmpty() || this.canInsertIntoOutputSlot(entry.value().getResult(null)))
                .filter(entry -> this.canInsertAmountIntoOutputSlot(entry.value().getResult(null)));
    }

    private int getStatus(java.util.Optional<RecipeEntry<VoidResonanceRecipe>> recipe) {
        if (recipe.isEmpty()) {
            return this.getStack(INPUT_SLOT).isEmpty() ? STATUS_MISSING_MATERIAL : STATUS_MISSING_CATALYST;
        }
        ItemStack result = recipe.get().value().getResult(null);
        if (!canInsertIntoOutputSlot(result) || !canInsertAmountIntoOutputSlot(result)) {
            return STATUS_OUTPUT_FULL;
        }
        return STATUS_CAN_PROCESS;
    }

    private boolean canInsertIntoOutputSlot(ItemStack result) {
        return this.getStack(OUTPUT_SLOT).isEmpty() || ItemStack.areItemsAndComponentsEqual(this.getStack(OUTPUT_SLOT), result);
    }

    private boolean canInsertAmountIntoOutputSlot(ItemStack result) {
        ItemStack output = this.getStack(OUTPUT_SLOT);
        int maxOutputCount = output.isEmpty() ? Math.min(this.getMaxCountPerStack(), result.getMaxCount()) : Math.min(this.getMaxCountPerStack(), output.getMaxCount());
        return output.getCount() + result.getCount() <= maxOutputCount;
    }

    private void craftItem(RecipeEntry<VoidResonanceRecipe> recipe) {
        ItemStack result = recipe.value().getResult(null).copy();
        this.removeStack(INPUT_SLOT, 1);
        this.removeStack(CATALYST_SLOT, 1);
        ItemStack outputStack = this.getStack(OUTPUT_SLOT);
        if (outputStack.isEmpty()) {
            this.setStack(OUTPUT_SLOT, result);
        } else {
            outputStack.increment(result.getCount());
        }
    }

    private void setWorkingState(World world, BlockPos pos, BlockState state, boolean working) {
        if (state.contains(VoidResonanceBox.WORKING) && state.get(VoidResonanceBox.WORKING) != working) {
            world.setBlockState(pos, state.with(VoidResonanceBox.WORKING, working), Block.NOTIFY_LISTENERS);
        }
    }

    @Override
    public int getMaxCountPerStack() {
        return 64;
    }
}
