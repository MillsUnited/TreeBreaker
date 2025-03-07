package com.mills.treeBreaker;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.*;

public class TreeEvents implements Listener {

    private Main main;
    private TreeManager treeManager;

    public TreeEvents(Main main) {
        this.main = main;
        this.treeManager = main.getTreeManager();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        ItemStack tool = player.getInventory().getItemInMainHand();
        Block block = e.getBlock();

        if (!treeManager.getToggle(uuid)) return;
        if (!isAxe(tool)) return;
        if (!isLog(block)) return;
        if (player.getGameMode() != GameMode.SURVIVAL) return;

        Set<Block> treeBlocks = new HashSet<>();
        findTreeBlocks(block, player, treeBlocks);

        for (Block log : treeBlocks) {
            log.breakNaturally(tool);
            reduceDurability(tool, player);
        }
    }

    private boolean isAxe(ItemStack item) {
        if (item == null || item.getType().isAir()) return false;
        Material type = item.getType();
        return type.name().endsWith("_AXE");
    }

    private boolean isLog(Block block) {
        return Tag.LOGS.isTagged(block.getType());
    }

    private void findTreeBlocks(Block block, Player player, Set<Block> treeBlocks) {
        if (treeBlocks.size() > 100) return;
        if (!isLog(block) || treeBlocks.contains(block)) return;

        ItemStack tool = player.getInventory().getItemInMainHand();
        if (tool.getType().getMaxDurability() == 0) return;

        short maxDurability = tool.getType().getMaxDurability();
        short currentDurability = tool.getDurability();
        int remainingUses = maxDurability - currentDurability - 1;

        if (remainingUses <= 1) return;

        treeBlocks.add(block);
        block.breakNaturally(tool);

        tool.setDurability((short) (currentDurability + 1));

        if (tool.getDurability() >= maxDurability - 2) {
            return;
        }

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dy == 0 && dz == 0) continue;
                    Block nearby = block.getRelative(dx, dy, dz);
                    findTreeBlocks(nearby, player, treeBlocks);
                }
            }
        }
    }

    private void reduceDurability(ItemStack tool, Player player) {
        if (tool.getType().getMaxDurability() == 0) return;

        short maxDurability = tool.getType().getMaxDurability();
        short currentDurability = tool.getDurability();

        if (currentDurability >= maxDurability - 1) return;

        tool.setDurability((short) (currentDurability + 1));
    }
}