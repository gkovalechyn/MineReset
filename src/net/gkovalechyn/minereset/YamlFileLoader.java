/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.minereset;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author gkovalechyn
 */
public class YamlFileLoader {

    private static File dataFolder;

    public static FileConfiguration load(String file, boolean saveFile) {
        return YamlFileLoader.load(new File(file), saveFile);
    }

    public static FileConfiguration load(File file, boolean saveFile) {
        FileConfiguration result;
        InputStream stream = MineReset.class.getClassLoader().getResourceAsStream(file.getName());
        File fileInDataFolder = new File(YamlFileLoader.dataFolder.getPath() + "/" + file.toString());

        if (fileInDataFolder.exists()) {
            result = YamlConfiguration.loadConfiguration(fileInDataFolder);
            //Try to load the default file
            if (stream != null) {
                result.setDefaults(YamlConfiguration.loadConfiguration(stream));

                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return new YamlConfiguration();
                }
            }
        } else {
            if (stream != null) {
                result = YamlConfiguration.loadConfiguration(stream);

                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return new YamlConfiguration();
                }
            } else {
                System.err.println("Could not load a configuration from the jar.");
                result = new YamlConfiguration();
            }
        }

        if (saveFile && result != null) {
            try {
                result.save(fileInDataFolder);
            } catch (IOException e) {
                System.err.println("Could not save default file to plugin folder.");
                e.printStackTrace();
            }
        }

        return result;
    }

    public static void initializeDataFolder(File dataFolder) {
        if (YamlFileLoader.dataFolder == null) {
            YamlFileLoader.dataFolder = dataFolder;
        }
    }
}
