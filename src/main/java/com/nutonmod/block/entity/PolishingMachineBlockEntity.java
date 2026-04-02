package com.nutonmod.block.entity;
import com.nutonmod.block.custom.PolishingMachine;
import com.nutonmod.data.PolishingMachineData;
import com.nutonmod.recipe.PolishingMachineRecipe;
import com.nutonmod.screen.PolishingMachineScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PolishingMachineBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<PolishingMachineData>, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    public static final int STATUS_CAN_PROCESS = 1;
    public static final int STATUS_MISSING_MATERIAL = 2;
    public static final int STATUS_OUTPUT_FULL = 3;

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = PolishingMachineRecipe.DEFAULT_TIME;
    private int machineStatus = STATUS_MISSING_MATERIAL;
    public PolishingMachineBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.POLISHING_MACHINE_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {

            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> PolishingMachineBlockEntity.this.progress;
                    case 1 -> PolishingMachineBlockEntity.this.maxProgress;
                    case 2 -> PolishingMachineBlockEntity.this.machineStatus;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> PolishingMachineBlockEntity.this.progress = value;
                    case 1 -> PolishingMachineBlockEntity.this.maxProgress = value;
                    case 2 -> PolishingMachineBlockEntity.this.machineStatus = value;
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
        return this.inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("container.polishing_machine");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new PolishingMachineScreenHandler(syncId, playerInventory, this.propertyDelegate, this);
    }

    @Override
    public PolishingMachineData getScreenOpeningData(ServerPlayerEntity player) {
        return new PolishingMachineData(pos);
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
        maxProgress = nbt.contains("max_progress") ? nbt.getInt("max_progress") : PolishingMachineRecipe.DEFAULT_TIME;
        machineStatus = nbt.contains("machine_status") ? nbt.getInt("machine_status") : STATUS_MISSING_MATERIAL;
    }

    @Override
    public int getMaxCountPerStack() {
        return 64;
    }

    public static void tick(World world, BlockPos pos, BlockState state, PolishingMachineBlockEntity blockEntity) {
        if (world.isClient()) {
            return;
        }

        Optional<RecipeEntry<PolishingMachineRecipe>> recipe = blockEntity.getCurrentRecipe();
        blockEntity.machineStatus = blockEntity.getStatus(recipe);
        blockEntity.maxProgress = recipe.map(value -> value.value().time()).orElse(PolishingMachineRecipe.DEFAULT_TIME);

        boolean canProcess = blockEntity.machineStatus == STATUS_CAN_PROCESS;
        if (canProcess) {
            if (blockEntity.progress == 0) {
                world.playSound(null, pos, SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS, 0.7F, 1.0F);
            }

            blockEntity.increaseCraftProgress();
            if (blockEntity.hasCraftingFinished() && recipe.isPresent()) {
                blockEntity.craftItem(recipe.get());
                blockEntity.resetProgress();
                world.playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.6F, 1.1F);
            }
        } else {
            blockEntity.resetProgress();
        }

        blockEntity.setWorkingState(world, pos, state, canProcess);
        markDirty(world, pos, world.getBlockState(pos));
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private void craftItem(RecipeEntry<PolishingMachineRecipe> recipe) {
        ItemStack result = recipe.value().getResult(null).copy();
        this.removeStack(INPUT_SLOT, 1);
        ItemStack outputStack = this.getStack(OUTPUT_SLOT);
        if (outputStack.isEmpty()) {
            this.setStack(OUTPUT_SLOT, result);
        } else {
            outputStack.increment(result.getCount());
        }
    }

    private Optional<RecipeEntry<PolishingMachineRecipe>> getCurrentRecipe() {
        SimpleInventory inventory = new SimpleInventory(this.size());
        for (int i = 0; i < this.size(); i++) {
            inventory.setStack(i, this.getStack(i));
        }
        if (this.world == null) {
            return Optional.empty();
        }
        return this.world.getRecipeManager().getFirstMatch(
                PolishingMachineRecipe.Type.INSTANCE,
                new SingleStackRecipeInput(inventory.getStack(INPUT_SLOT)),
                this.world
        );
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftProgress() {
        this.progress++;
    }

    private int getStatus(Optional<RecipeEntry<PolishingMachineRecipe>> recipe) {
        if (recipe.isEmpty()) {
            return STATUS_MISSING_MATERIAL;
        }

        ItemStack result = recipe.get().value().getResult(null);
        if (!canInsertIntoOutputSlot(result) || !canInsertAmountIntoOutputSlot(result)) {
            return STATUS_OUTPUT_FULL;
        }

        return STATUS_CAN_PROCESS;
    }

    private boolean canInsertIntoOutputSlot(ItemStack result) {
        return this.getStack(OUTPUT_SLOT).isEmpty() ||
                ItemStack.areItemsAndComponentsEqual(this.getStack(OUTPUT_SLOT), result);
    }

    private boolean canInsertAmountIntoOutputSlot(ItemStack result) {
        ItemStack output = this.getStack(OUTPUT_SLOT);
        int maxOutputCount = output.isEmpty()
                ? Math.min(this.getMaxCountPerStack(), result.getMaxCount())
                : Math.min(this.getMaxCountPerStack(), output.getMaxCount());
        return output.getCount() + result.getCount() <= maxOutputCount;
    }

    private void setWorkingState(World world, BlockPos pos, BlockState state, boolean working) {
        if (state.contains(PolishingMachine.WORKING) && state.get(PolishingMachine.WORKING) != working) {
            world.setBlockState(pos, state.with(PolishingMachine.WORKING, working), Block.NOTIFY_LISTENERS);
        }
    }
}
