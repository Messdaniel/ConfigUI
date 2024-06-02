package me.messdaniel.configui;

import me.messdaniel.configui.command.OpenGuiCommand;
import me.messdaniel.configui.command.ReloadCommand;
import me.messdaniel.configui.listener.InventoryEvent;
import me.messdaniel.configui.manager.ConfigUIManger;
import me.messdaniel.configui.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ConfigUI extends JavaPlugin {

    private static ConfigUI instance;

    private ConfigUIManger configUIManger;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultResource("config");
        saveDefaultResource("example");
        configUIManger = new ConfigUIManger();
        MessagesUtils.load();

        Bukkit.getPluginManager().registerEvents(new InventoryEvent(), this);

        getCommand("uiopengui").setExecutor(new OpenGuiCommand());
        getCommand("uireload").setExecutor(new ReloadCommand());
    }

    void saveDefaultResource(String resource) {
        String path = resource + ".yml";
        File file;
        if (resource.toLowerCase().contains("example")) {
            File folder = new File(getDataFolder(),"guis");
            if (!folder.exists()) folder.mkdir();
            file = new File(getDataFolder() + File.separator + "guis", path);
            if (!file.exists()) {
                this.saveResource("guis/" + path, false);
            }
        } else {
            file = new File(getDataFolder(), path);
            if (!file.exists()) {
                this.saveResource(path, false);
            }
        }
    }

    public static ConfigUI getInstance() {
        return instance;
    }

    public ConfigUIManger getConfigUIManger() {
        return configUIManger;
    }
}
