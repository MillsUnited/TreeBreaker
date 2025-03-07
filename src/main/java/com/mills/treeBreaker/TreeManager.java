package com.mills.treeBreaker;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class TreeManager {

    private final File file;
    private FileConfiguration config;

    public TreeManager(File dataFolder) {
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        file = new File(dataFolder, "data.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void setToggle(UUID uuid, Boolean toggled) {
        config.set(uuid.toString(), toggled);
        saveConfig();
    }

    public boolean getToggle(UUID uuid) {
        return config.getBoolean(uuid.toString());
    }

    private void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
