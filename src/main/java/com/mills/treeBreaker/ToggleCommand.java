package com.mills.treeBreaker;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ToggleCommand implements CommandExecutor {

    private Main main;
    private TreeManager treeManager;

    public ToggleCommand(Main main) {
        this.main = main;
        this.treeManager = main.getTreeManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();

            if (treeManager.getToggle(uuid)) {
                treeManager.setToggle(uuid, false);
                player.sendMessage(ChatColor.YELLOW + "toggled off tree chopper!");
            } else {
                treeManager.setToggle(uuid, true);
                player.sendMessage(ChatColor.YELLOW + "toggled on tree chopper!");
            }

        }

        return false;
    }
}
