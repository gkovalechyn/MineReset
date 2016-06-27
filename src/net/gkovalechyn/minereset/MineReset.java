/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.minereset;

import net.gkovalechyn.minereset.commands.MRCommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author gkovalechyn
 */
public class MineReset {

    private MineManager mineManager;
    private MRCommandExecutor commandExecutor;
    private JavaPlugin plugin;

    public void onEnable(JavaPlugin plugin) {
        this.plugin = plugin;
        YamlFileLoader.initializeDataFolder(this.plugin.getDataFolder());
        MessageHandler.initialize();
        this.commandExecutor = new MRCommandExecutor(this);

        this.plugin.getCommand("mr").setExecutor(commandExecutor);

        this.mineManager = new MineManager(this);
    }

    public void onDisable() {
        this.mineManager.save();
    }

    public MineManager getMineManager() {
        return mineManager;
    }

    public MRCommandExecutor getCommandExecutor() {
        return commandExecutor;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

}
