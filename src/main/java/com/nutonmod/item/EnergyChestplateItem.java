package com.nutonmod.item;

import com.nutonmod.armor.EnergyArmor;
import com.nutonmod.block.EnergyCoreBlock;
import com.nutonmod.block.ModBlocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class EnergyChestplateItem extends BaseElementArmor{

    private static final int COOLDOWN_TICKS = 20; // 1秒冷却时间
    private static final int EFFECT_INTERVAL = 100; // 5秒效果间隔

    /**
     * 能量胸板
     *
     */
    public EnergyChestplateItem() {
        super(ArmorMaterials.NETHERITE, Type.CHESTPLATE, new Item.Settings()
            .maxDamage(2048)
            .rarity(Rarity.EPIC)
            .fireproof());
    }
    
    // 当玩家穿着胸甲时，每 tick 检测一次附近是否有激活的能量核心
    public void clientTick(ItemStack stack, PlayerEntity player) {
        if (player.getWorld().isClient) {  // 修正：只在客户端执行
            // 只有穿着全套能量盔甲时才激活效果
            if (hasFullSet(player)) {
                // 添加冷却时间，避免每 tick 都检测
                if (player.age % COOLDOWN_TICKS == 0) {
                    // 检查附近是否有激活的能量核心（10 格范围）
                    if (isNearActivatedCore(player.getWorld(), player.getBlockPos())) {
                        // 激活状态：提供强大增益效果（每 5 秒一次）
                        if (player.age % EFFECT_INTERVAL == 0) {
                            // 在客户端添加粒子效果，在服务端添加状态效果
                            if (!player.getWorld().isClient) {
                                // 1. 生命恢复 II
                                player.addStatusEffect(new StatusEffectInstance(
                                    StatusEffects.REGENERATION, 
                                    100, // 持续 5 秒
                                    1,   // 等级 II
                                    false, false, true
                                ));
                                
                                // 2. 防火效果
                                player.addStatusEffect(new StatusEffectInstance(
                                    StatusEffects.FIRE_RESISTANCE, 
                                    220, // 持续 11 秒
                                    0, 
                                    false, false, true
                                ));
                                
                                // 3. 速度提升 I
                                player.addStatusEffect(new StatusEffectInstance(
                                    StatusEffects.SPEED, 
                                    220, // 持续 11 秒
                                    0,   // 等级 I
                                    false, false, true
                                ));
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        addTooltipLines(tooltip);
    }
    
    private void addTooltipLines(List<Text> tooltip) {
        tooltip.add(Text.literal("").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("\u00a75\u00a7l🛡️ 能量胸甲").formatted(Formatting.DARK_PURPLE, Formatting.BOLD));
        tooltip.add(Text.literal("\u00a77 需要激活的能量核心才能发挥全部效果").formatted(Formatting.GRAY));
        
        if (Screen.hasShiftDown()) {
            tooltip.add(Text.literal("").formatted(Formatting.GRAY));
            tooltip.add(Text.literal("\u00a7e● 基础属性:").formatted(Formatting.YELLOW));
            tooltip.add(Text.literal("   - 护甲值：8 点").formatted(Formatting.GREEN));
            tooltip.add(Text.literal("   - 耐久度：2048").formatted(Formatting.GREEN));
            tooltip.add(Text.literal("   - 防火、防爆炸").formatted(Formatting.GREEN));
            tooltip.add(Text.literal("\u00a7d● 套装效果:").formatted(Formatting.LIGHT_PURPLE));
            tooltip.add(Text.literal("   - 需要穿着全套盔甲才能激活").formatted(Formatting.GRAY));
            tooltip.add(Text.literal("   - 需要能量核心激活状态").formatted(Formatting.GRAY));
            tooltip.add(Text.literal("\u00a7b● 全套激活时效果:").formatted(Formatting.AQUA));
            tooltip.add(Text.literal("   - ❤️ 生命恢复 II").formatted(Formatting.RED));
            tooltip.add(Text.literal("   - 🔥 防火效果").formatted(Formatting.DARK_RED));
            tooltip.add(Text.literal("   - 💨 速度提升 I").formatted(Formatting.GRAY));
            tooltip.add(Text.literal("\u00a7d● 检测范围：10 格").formatted(Formatting.LIGHT_PURPLE));
            tooltip.add(Text.literal("").formatted(Formatting.GRAY));
        } else {
            tooltip.add(Text.literal("§7 按 §eShift§7 查看详细信息 §r").formatted(Formatting.GRAY, Formatting.ITALIC));
        }
    }
    
    /**
     * 检查附近是否有激活的能量核心
     * 优化版本：减少检测范围，提前退出循环
     */
    private boolean isNearActivatedCore(World world, BlockPos pos) {
        // 只检测水平方向 6 格，垂直方向 5 格，减少检测数量
        for (int x = -6; x <= 6; x++) {
            for (int y = -5; y <= 5; y++) {
                for (int z = -6; z <= 6; z++) {
                    BlockPos checkPos = pos.add(x, y, z);
                    if (world.getBlockState(checkPos).getBlock() == ModBlocks.ENERGY_CORE) {
                        if (world.getBlockState(checkPos).get(EnergyCoreBlock.ACTIVATED)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}