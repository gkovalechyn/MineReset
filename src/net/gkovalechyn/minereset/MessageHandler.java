
/*
 * File:   MessageHandler.java
 * Author: gkovalechyn
 *
 * Created on Feb 9, 2016, 9:28:33 PM
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.minereset;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author gkovalechyn
 */
public class MessageHandler {

    private static FileConfiguration config;
    private static String prefix;
    private static char colorCharacter;
    
    public static void initialize() {
        /*
        File f = new File(dataFolder, "Messages.yml");

        if (!f.exists()) {
            config = YamlConfiguration.loadConfiguration(MineReset.class.getClassLoader().getResourceAsStream("Messages.yml"));
            try {
                f.mkdir();
                config.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            config = YamlConfiguration.loadConfiguration(f);
        }
        */
        config = YamlFileLoader.load("Messages.yml", true);
        
        colorCharacter = config.getString("ColorChar", "&").charAt(0);
        prefix = config.getString("Prefix", "§4[§9MineReset§4]§r");
    }

    public static String getMessage(Message message) {
        return ChatColor.translateAlternateColorCodes(colorCharacter, Util.unescapeString(config.getString(message.getPath())));
    }

    public static String getPrefixedMessage(Message message) {
        return prefix + getMessage(message);
    }
}
