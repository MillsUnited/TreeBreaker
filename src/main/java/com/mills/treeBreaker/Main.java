package com.mills.treeBreaker;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private TreeManager treeManager;

    @Override
    public void onEnable() {

        treeManager = new TreeManager(this.getDataFolder());

        Bukkit.getPluginManager().registerEvents(new TreeEvents(this), this);
        getCommand("toggletreechopper").setExecutor(new ToggleCommand(this));
    }

    public TreeManager getTreeManager() {
        return treeManager;
    }
}
