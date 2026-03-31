package com.nutonmod.item.custom;

import com.nutonmod.sound.ModSoundEvents;
import com.nutonmod.tags.ModBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


import java.util.List;

public class Prospector extends Item {

    public Prospector(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();

        if (player == null) {
            // 非玩家触发：不处理
            return ActionResult.SUCCESS;
        }

        // 客户端直接返回，不执行逻辑
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }

        // player 在 useOnBlock 的上下文中应当存在；若不存在，这里会抛出 NPE，
        // 但大多数 Item 的 useOnBlock 在玩家交互时会传入非空 player。
        // 若你仍想对非玩家触发防守，请把前面的逻辑改为在 player==null 时直接返回。
        boolean foundBlock = false;
        boolean isPrecise = player.isSneaking();

        if (!isPrecise) {
            // 模糊搜索 - 3x3 范围
            for (int i = 0; i < 64; i++) {
                for (int j = -1; j <= 1; j++) {
                    for (int k = -1; k <= 1; k++) {
                        BlockPos posToCheck = pos.down(i).add(j, 0, k);
                        BlockState blockState = world.getBlockState(posToCheck);

                        if (isRightBlock(blockState)) {
                            String name = blockState.getBlock().getName().getString();
                            
                            // 聊天框显示详细信息（可历史记录）
                            player.sendMessage(Text.literal("§a§l探矿器报告：§r发现 §6" + name + "§r"), true);
                            
                            // ActionBar 显示简洁提示（醒目）
                            player.sendMessage(Text.literal("§e§l⚠ 发现矿藏！§r"), false);

                            world.playSound(null,pos, ModSoundEvents.PROSPECTOR_FOUND_ORE, SoundCategory.BLOCKS,1.0f,1.0f);
                            foundBlock = true;
                            break;
                        }
                    }
                    if (foundBlock) break;
                }
                if (foundBlock) break;
            }
        } else {
            // 精确搜索 - 正下方 1x1 范围
            for (int i = 0; i < 64; i++) {
                BlockPos posToCheck = pos.down(i);
                BlockState blockState = world.getBlockState(posToCheck);

                if (isRightBlock(blockState)) {
                    String name = blockState.getBlock().getName().getString();
                    
                    // 聊天框显示详细信息
                    player.sendMessage(Text.literal("§a§l探矿器报告：§r精确位置发现 §6" + name + "§r"), true);
                    
                    // ActionBar 显示提示
                    player.sendMessage(Text.literal("§b§l⬇ 正下方有矿！§r"), false);

                    world.playSound(null,pos, ModSoundEvents.PROSPECTOR_FOUND_ORE, SoundCategory.BLOCKS,1.0f,1.0f);
                    foundBlock = true;
                    break;
                }
            }
        }

        if (!foundBlock) {
            // 只在聊天框显示未找到消息
            player.sendMessage(Text.literal("§7§l探矿器扫描完成§r - §c未发现任何矿藏§r"), true);
        }

        // 损坏工具
        context.getStack().damage(1, player, EquipmentSlot.MAINHAND);
        
        return ActionResult.SUCCESS;
    }

    private boolean isRightBlock(BlockState blockState) {
        // 使用标签系统判断是否为可探测矿石
        return blockState.isIn(ModBlockTags.PROSPECTOR_ORES);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        addTooltipLines(tooltip);
    }

    private void addTooltipLines(List<Text> tooltip) {
        tooltip.add(Text.literal("").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("§7§l 探矿器 §r - 高科技矿石探测设备").formatted(Formatting.AQUA, Formatting.BOLD));
        tooltip.add(Text.literal("").formatted(Formatting.GRAY));
        
        if (Screen.hasShiftDown()) {
            // 按住 Shift 显示详细信息
            tooltip.add(Text.literal("§e§l⚙ 工作原理：").formatted(Formatting.YELLOW));
            tooltip.add(Text.literal("  • 右键点击方块表面进行扫描").formatted(Formatting.GRAY));
            tooltip.add(Text.literal("  • 向下探测最多 64 层深度").formatted(Formatting.GRAY));
            tooltip.add(Text.literal("  • 可检测 8 种基础矿石及其深层变种").formatted(Formatting.GRAY));
            tooltip.add(Text.literal("").formatted(Formatting.GRAY));
            
            tooltip.add(Text.literal("§b§l🎯 探测模式：").formatted(Formatting.AQUA));
            tooltip.add(Text.literal("  §a● 普通模式:").formatted(Formatting.GREEN));
            tooltip.add(Text.literal("    - 范围：3x3x64 格").formatted(Formatting.GRAY));
            tooltip.add(Text.literal("    - 适合快速大范围扫描").formatted(Formatting.GRAY));
            tooltip.add(Text.literal("  §c● 精确模式:").formatted(Formatting.RED));
            tooltip.add(Text.literal("    - 范围：1x1x64 格").formatted(Formatting.GRAY));
            tooltip.add(Text.literal("    - 精准定位正下方矿藏").formatted(Formatting.GRAY));
            tooltip.add(Text.literal("").formatted(Formatting.GRAY));
            
            tooltip.add(Text.literal("§d§l 使用技巧：").formatted(Formatting.LIGHT_PURPLE));
            tooltip.add(Text.literal("  • 配合地图标记，圈定矿区范围").formatted(Formatting.GRAY));
            tooltip.add(Text.literal("  • 潜行模式精确定位矿脉中心").formatted(Formatting.GRAY));
            tooltip.add(Text.literal("  • 注意工具耐久度，及时修复").formatted(Formatting.GRAY));
        } else {
            // 不按 Shift 显示简要信息
            tooltip.add(Text.literal("§7按 §eShift §7查看详细信息 §r").formatted(Formatting.GRAY, Formatting.ITALIC));
            tooltip.add(Text.literal("").formatted(Formatting.GRAY));
            
            tooltip.add(Text.literal("§a§l✓ 功能特性：").formatted(Formatting.GREEN));
            tooltip.add(Text.literal("  • 双重消息提示（聊天框 + 屏幕中央）").formatted(Formatting.GRAY));
            tooltip.add(Text.literal("  • 智能识别 16 种矿石类型").formatted(Formatting.GRAY));
            tooltip.add(Text.literal("  • 粒子特效指示").formatted(Formatting.GRAY));
        }
        
        tooltip.add(Text.literal("").formatted(Formatting.GRAY));
    }
}
