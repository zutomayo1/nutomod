package com.nutonmod.block.custom;

import com.nutonmod.entity.ModEntities;
import com.nutonmod.item.ModItems;
import com.nutonmod.world.dimension.ModDimensions;
import com.nutonmod.world.system.EnergyRealmStormSystem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class SanctumGateBlock extends Block {
    public static final BooleanProperty OPENED = BooleanProperty.of("opened");
    private static final long ELITE_COOLDOWN_TICKS = 20L * 60L * 8L;
    private static final Map<String, Long> LAST_TRIGGER = new HashMap<>();

    public SanctumGateBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(OPENED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(OPENED);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        if (!world.getRegistryKey().equals(ModDimensions.ENERGY_REALM_WORLD_KEY)) {
            player.sendMessage(Text.literal("The Sanctum Gate only responds in the Energy Realm"), true);
            return ActionResult.SUCCESS;
        }
        boolean stormActive = world instanceof ServerWorld serverWorld
                && EnergyRealmStormSystem.isEnergyStormActive(serverWorld);

        String key = world.getRegistryKey().getValue() + ":" + pos.toShortString();
        long now = world.getTime();
        long last = LAST_TRIGGER.getOrDefault(key, Long.MIN_VALUE / 4L);
        long cooldown = stormActive ? (ELITE_COOLDOWN_TICKS * 2L / 3L) : ELITE_COOLDOWN_TICKS;
        long remain = cooldown - (now - last);
        if (remain > 0) {
            player.sendMessage(Text.literal("Gate stabilizing: " + Math.max(1L, remain / 20L) + "s"), true);
            return ActionResult.SUCCESS;
        }

        if (!consumeKey(player)) {
            player.sendMessage(Text.literal("Need sanctum_key to open the gate"), true);
            return ActionResult.SUCCESS;
        }

        LAST_TRIGGER.put(key, now);
        world.setBlockState(pos, state.with(OPENED, true));
        world.playSound(null, pos, SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.BLOCKS, 1.0f, 0.8f);
        triggerEliteEncounter(world, pos, player, stormActive);
        return ActionResult.SUCCESS;
    }

    private boolean consumeKey(PlayerEntity player) {
        ItemStack main = player.getMainHandStack();
        ItemStack off = player.getOffHandStack();
        if (main.isOf(ModItems.SANCTUM_KEY)) {
            if (!player.isCreative()) main.decrement(1);
            return true;
        }
        if (off.isOf(ModItems.SANCTUM_KEY)) {
            if (!player.isCreative()) off.decrement(1);
            return true;
        }
        return false;
    }

    private void triggerEliteEncounter(World world, BlockPos pos, PlayerEntity player, boolean stormActive) {
        if (!(world instanceof ServerWorld serverWorld)) {
            return;
        }
        spawnElite(serverWorld, pos.add(3, 1, 0), true);
        spawnElite(serverWorld, pos.add(-3, 1, 0), false);
        spawnElite(serverWorld, pos.add(0, 1, 3), true);
        if (stormActive) {
            spawnElite(serverWorld, pos.add(0, 1, -3), true);
        }
        player.giveItemStack(new ItemStack(ModItems.STORM_FRAGMENT, stormActive ? 4 : 2));
        player.giveItemStack(new ItemStack(ModItems.STORM_ALLOY, stormActive ? 2 : 1));
        if (stormActive) {
            player.giveItemStack(new ItemStack(ModItems.ALTAR_SHARD, 1));
            player.giveItemStack(new ItemStack(ModItems.CRYSTAL_MATRIX, 1));
        }
        player.addExperience(120);
        String suffix = stormActive ? " (storm surge)" : "";
        player.sendMessage(Text.literal("Sanctum elite encounter started" + suffix), false);
    }

    private void spawnElite(ServerWorld world, BlockPos pos, boolean preferRiftStalker) {
        var entity = preferRiftStalker ? ModEntities.RIFT_STALKER.create(world) : ModEntities.ENERGY_BEING.create(world);
        if (entity == null) return;
        entity.refreshPositionAndAngles(pos, world.random.nextFloat() * 360.0F, 0.0F);
        if (entity instanceof MobEntity mob) {
            mob.setHealth(mob.getMaxHealth() * 2.0F);
            mob.setCustomName(Text.literal(preferRiftStalker ? "Sanctum Rift Warden" : "Sanctum Warden"));
            mob.setCustomNameVisible(true);
        }
        world.spawnEntity(entity);
    }
}
