package com.nutonmod.block.entity;

import com.nutonmod.data.StabilizerBeaconData;
import com.nutonmod.screen.StabilizerBeaconScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class StabilizerBeaconBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<StabilizerBeaconData> {
    public static final int STATUS_OFFLINE = 0;
    public static final int STATUS_READY = 1;
    public static final int STATUS_ACTIVE = 2;

    private int status = STATUS_OFFLINE;
    private int remainingSeconds = 0;
    private int protectionRadius = 16;
    private int stormMultiplier = 1;
    private boolean stormActive = false;

    public StabilizerBeaconBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STABILIZER_BEACON_BLOCK_ENTITY, pos, state);
    }

    public void syncFromWorld(boolean active, int seconds, int radius, boolean storm) {
        this.status = active ? STATUS_ACTIVE : STATUS_READY;
        this.remainingSeconds = Math.max(0, seconds);
        this.protectionRadius = Math.max(1, radius);
        this.stormActive = storm;
        this.stormMultiplier = storm ? 2 : 1;
        markDirty();
        if (world != null && !world.isClient) {
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
        }
    }

    public void setOffline() {
        this.status = STATUS_OFFLINE;
        this.remainingSeconds = 0;
        this.stormActive = false;
        this.stormMultiplier = 1;
        markDirty();
        if (world != null && !world.isClient) {
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
        }
    }

    public int getStatus() { return status; }
    public int getRemainingSeconds() { return remainingSeconds; }
    public int getProtectionRadius() { return protectionRadius; }
    public int getStormMultiplier() { return stormMultiplier; }
    public boolean isStormActive() { return stormActive; }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.nutonmod.stabilizer_beacon");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new StabilizerBeaconScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public StabilizerBeaconData getScreenOpeningData(ServerPlayerEntity player) {
        return new StabilizerBeaconData(pos);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putInt("status", status);
        nbt.putInt("remaining_seconds", remainingSeconds);
        nbt.putInt("protection_radius", protectionRadius);
        nbt.putInt("storm_multiplier", stormMultiplier);
        nbt.putBoolean("storm_active", stormActive);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        status = nbt.getInt("status");
        remainingSeconds = nbt.getInt("remaining_seconds");
        protectionRadius = nbt.getInt("protection_radius");
        stormMultiplier = nbt.getInt("storm_multiplier");
        stormActive = nbt.getBoolean("storm_active");
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
}
