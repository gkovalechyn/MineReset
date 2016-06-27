/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.minereset;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 *
 * @author gkovalechyn
 */
public class MineManager {

    private final MineReset plugin;
    private final Map<String, Mine> mines = new HashMap<>();
    private final MineCheckTask mineCheckTask;

    public MineManager(MineReset plugin) {
        this.plugin = plugin;
        this.mineCheckTask = new MineCheckTask(plugin);
        this.load();
    }

    private void load() {
        File dataFile = new File(plugin.getPlugin().getDataFolder(), "Data.yml");

        if (dataFile.exists()) {
            FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
            List<String> loadLaterQueue = new ArrayList<>();
            
            if (!data.getString("version", "").equals("1.0")){
                this.plugin.getPlugin().getLogger().warning("Data file version was not 1.0, trying to load anyway.");
            }
            
            if (!data.contains("Mines")) {
                return;
            }

            for (String id : data.getConfigurationSection("Mines").getKeys(false)) {
                try {
                    Mine mine = this.loadSingleMine(id, data);

                    if (mine != null) {
                        this.mines.put(id, mine);
                    } else {
                        loadLaterQueue.add(id);
                    }
                } catch (Exception e) {
                    this.plugin.getPlugin().getLogger().log(Level.SEVERE, "Failed to load mine: {0}", id);
                    e.printStackTrace();
                }
            }
            
            for(String s : data.getStringList("EnabledMines")){
                Mine m = this.getMine(s);
                if (s != null){
                    m.setState(MineState.IDLE);
                }
            }

            if (loadLaterQueue.size() > 0) {
                this.plugin.getPlugin().getServer().getScheduler().runTaskLater(plugin.getPlugin(), new LoadLaterTask(this, loadLaterQueue), 15 * 20);
            } else {
                this.mineCheckTask.start();
            }
        }
    }

    private Mine loadSingleMine(String id, FileConfiguration data) {
        Mine mine = new Mine(id);
        Location loc;
        World w;
        double x;
        double y;
        double z;
        float yaw;
        float pitch;
        List<String> percentages;
        List<Pair<IdDataWrapper, Float>> resultList = new ArrayList<>();

        x = data.getDouble("Mines." + id + ".Locations.From.X");
        y = data.getDouble("Mines." + id + ".Locations.From.Y");
        z = data.getDouble("Mines." + id + ".Locations.From.Z");
        w = this.plugin.getPlugin().getServer().getWorld(data.getString("Mines." + id + ".Locations.From.World"));
        if (w != null) {
            loc = new Location(w, x, y, z);
            mine.setFrom(loc);

            x = data.getDouble("Mines." + id + ".Locations.To.X");
            y = data.getDouble("Mines." + id + ".Locations.To.Y");
            z = data.getDouble("Mines." + id + ".Locations.To.Z");
            w = this.plugin.getPlugin().getServer().getWorld(data.getString("Mines." + id + ".Locations.To.World"));
            mine.setTo(new Location(w, x, y, z));

            x = data.getDouble("Mines." + id + ".Locations.To.X");
            y = data.getDouble("Mines." + id + ".Locations.To.Y");
            z = data.getDouble("Mines." + id + ".Locations.To.Z");
            yaw = (float) data.getDouble("Mines." + id + ".Locations.To.Yaw");
            pitch = (float) data.getDouble("Mines." + id + ".Locations.To.Pitch");
            w = this.plugin.getPlugin().getServer().getWorld(data.getString("Mines." + id + ".Locations.To.World"));
            mine.setResetPosition(new Location(w, x, y, z, yaw, pitch));

            mine.setLastReset(0);
            mine.setResetCondition(ResetCondition.getByString(data.getString("Mines." + id + ".ResetCondition")));
            mine.setResetInterval(data.getLong("Mines." + id + ".ResetInterval"));
            mine.setResetTreshold((float) data.getDouble("Mines." + id + ".ResetThreshold"));

            percentages = data.getStringList("Mines." + id + ".Probabilities");

            for (String s : percentages) {
                String[] temp = s.split(":");
                IdDataWrapper wrapper = new IdDataWrapper();
                wrapper.id = Integer.parseInt(temp[0]);
                wrapper.data = Byte.parseByte(temp[1]);
                resultList.add(new Pair<>(wrapper, Float.parseFloat(temp[2])));
            }

            mine.setFillProbabilities(resultList);

            return mine;
        } else {
            return null;
        }

    }

    public void queueMineReset(Mine mine) {
        if (mine != null) {
            System.out.println("Mine reset queued for mine: " + mine.getId());
            Location from = mine.getFrom();
            Location to = mine.getTo();

            mine.setState(MineState.RESETTING);
            this.plugin.getPlugin().getServer().broadcastMessage(MessageHandler.getPrefixedMessage(Message.INFO_RESET).replace("{Mine}", mine.getId()));

            for (Player p : this.plugin.getPlugin().getServer().getOnlinePlayers()) {
                Location playerLoc = p.getLocation();
                if ((playerLoc.getX() >= from.getX() && playerLoc.getX() <= to.getX())
                        && (playerLoc.getY() >= from.getY() && playerLoc.getY() <= to.getY())
                        && (playerLoc.getZ() >= from.getZ() && playerLoc.getZ() <= to.getZ())) {
                    p.teleport(mine.getResetPosition());
                }
            }
            new Thread(new ResetTask(this.plugin, mine)).start();
            //this.plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new ResetTask(mine));
        }
    }

    public void createMine(String name) {
        if (this.mines.get(name) == null) {
            Mine mine = new Mine(name);
            this.mines.put(name, mine);
        }
    }

    public void deleteMine(String mine) {
        this.mines.remove(mine);
    }

    public void save() {
        FileConfiguration config = new YamlConfiguration();
        List<String> enabledMines = new ArrayList<>();
        
        config.set("Version", "1.0");
        
        for (Mine mine : this.mines.values()) {
            String id = mine.getId();
            try {
                Location loc = mine.getFrom();
                List<String> probabilities;

                if (mine.getFrom() == null || mine.getTo() == null || mine.getResetPosition() == null || mine.getFillProbabilities() == null) {
                    continue;
                }

                probabilities = new ArrayList<>(mine.getFillProbabilities().size());

                config.set("Mines." + id + ".Locations.From.X", loc.getBlockX());
                config.set("Mines." + id + ".Locations.From.Y", loc.getBlockY());
                config.set("Mines." + id + ".Locations.From.Z", loc.getBlockZ());
                config.set("Mines." + id + ".Locations.From.World", loc.getWorld().getName());

                loc = mine.getTo();
                config.set("Mines." + id + ".Locations.To.X", loc.getBlockX());
                config.set("Mines." + id + ".Locations.To.Y", loc.getBlockY());
                config.set("Mines." + id + ".Locations.To.Z", loc.getBlockZ());
                config.set("Mines." + id + ".Locations.To.World", loc.getWorld().getName());

                loc = mine.getResetPosition();
                config.set("Mines." + id + ".Locations.Reset.X", loc.getBlockX());
                config.set("Mines." + id + ".Locations.Reset.Y", loc.getBlockY());
                config.set("Mines." + id + ".Locations.Reset.Z", loc.getBlockZ());
                config.set("Mines." + id + ".Locations.Reset.World", loc.getWorld().getName());
                config.set("Mines." + id + ".Locations.Reset.Yaw", loc.getYaw());
                config.set("Mines." + id + ".Locations.Reset.Pitch", loc.getPitch());

                config.set("Mines." + id + ".ResetCondition", mine.getResetCondition().toString());
                config.set("Mines." + id + ".ResetInterval", mine.getResetInterval());
                config.set("Mines." + id + ".ResetThreshold", mine.getResetTreshold());

                for (Pair<IdDataWrapper, Float> pair : mine.getFillProbabilities()) {
                    probabilities.add(String.format("%d:%d:", pair.first.id, pair.first.data) + pair.second);
                }

                config.set("Mines." + id + ".Probabilities", probabilities);
                
                if (mine.getState() == MineState.IDLE || mine.getState() == MineState.RESETTING){
                    enabledMines.add(id);
                }
            } catch (Exception e) {
                e.printStackTrace();
                config.set("Mines." + id, null);
            }
        }
        
        config.set("EnabledMines", enabledMines);

        try {
            config.save(new File(this.plugin.getPlugin().getDataFolder(), "Data.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Mine> getMines() {
        return new ArrayList(this.mines.values());
    }

    public Mine getMine(String mine) {
        return this.mines.get(mine);
    }

    private class LoadLaterTask implements Runnable {

        private final List<String> mines;
        private final MineManager manager;

        public LoadLaterTask(MineManager manager, List<String> mines) {
            this.manager = manager;
            this.mines = mines;
        }

        @Override
        public void run() {
            FileConfiguration data = YamlConfiguration.loadConfiguration(new File(manager.plugin.getPlugin().getDataFolder(), "Data.yml"));
            
            for (String id : mines) {
                try {
                    Mine m = manager.loadSingleMine(id, data);
                    manager.mines.put(id, m);
                } catch (Exception e) {
                    manager.plugin.getPlugin().getLogger().log(Level.SEVERE, "Could not load mine: {0}", id);
                    e.printStackTrace();
                }
            }
            
            for(String s : data.getStringList("EnabledMines")){
                Mine m = this.manager.getMine(s);
                if (s != null && m.getState() == MineState.NOT_SET){
                    m.setState(MineState.IDLE);
                }
            }

            manager.mineCheckTask.start();
        }

    }
}
