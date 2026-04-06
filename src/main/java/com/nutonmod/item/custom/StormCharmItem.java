package com.nutonmod.item.custom;

import com.nutonmod.world.dimension.ModDimensions;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class StormCharmItem extends Item {
    private static final int COOLDOWN_TICKS = 20 * 30;

    public StormCharmItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!(world instanceof ServerWorld serverWorld)) {
            return TypedActionResult.success(stack);
        }

        if (!serverWorld.getRegistryKey().equals(ModDimensions.ENERGY_REALM_WORLD_KEY)) {
            user.sendMessage(Text.literal("Storm Charm only reacts in the Energy Realm"), true);
            return TypedActionResult.fail(stack);
        }

        user.removeStatusEffect(StatusEffects.BLINDNESS);
        user.removeStatusEffect(StatusEffects.SLOWNESS);
        user.removeStatusEffect(StatusEffects.WEAKNESS);
        user.removeStatusEffect(StatusEffects.MINING_FATIGUE);
        user.removeStatusEffect(StatusEffects.NAUSEA);
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20 * 20, 1, true, false, true));
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 20 * 20, 0, true, false, true));
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 20 * 20, 0, true, false, true));
        user.getItemCooldownManager().set(this, COOLDOWN_TICKS);

        if (!user.isCreative()) {
            stack.decrement(1);
        }
        return TypedActionResult.success(stack, world.isClient());
    }
}
