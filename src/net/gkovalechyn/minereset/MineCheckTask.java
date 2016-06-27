/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.minereset;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 *
 * @author gkovalechyn
 */
public class MineCheckTask implements Runnable {

    private final MineReset plugin;
    private boolean toStop = false;
    private boolean running = false;

    public MineCheckTask(MineReset plugin) {
        this.plugin = plugin;
    }

    public void start() {
        if (!this.running) {
            new Thread(this).start();
        }
    }

    @Override
    public void run() {
        this.running = true;

        while (!this.toStop) {
            for (Mine mine : plugin.getMineManager().getMines()) {
                if (mine.getState() == MineState.IDLE) {
                    switch (mine.getResetCondition()) {
                        case PERCENTAGE_LEFT:
                            if (this.getMinePercentageDug(mine) > mine.getResetTreshold()) {
                                this.plugin.getMineManager().queueMineReset(mine);
                            }
                            break;
                        case TIME:
                            if (System.currentTimeMillis() - mine.getLastReset() > mine.getResetInterval()) {
                                this.plugin.getMineManager().queueMineReset(mine);
                            }
                            break;
                        default:
                            break;
                    }

                    try {
                        Thread.sleep(1000L);//Sleep for 1 second
                    } catch (InterruptedException e) {

                    }
                }
            }

            try {
                Thread.sleep(10000L); //Sleep for 10 seconds
            } catch (InterruptedException e) {

            }
        }

        this.running = false;
    }

    private float getMinePercentageDug(Mine mine) {
        int totalBlocks = (mine.getTo().getBlockX() - mine.getFrom().getBlockX())
                * (mine.getTo().getBlockY() - mine.getFrom().getBlockY())
                * (mine.getTo().getBlockZ() - mine.getFrom().getBlockZ());
        int dugBlocks = 0;
        World w = mine.getFrom().getWorld();

        for (int i = mine.getFrom().getBlockX(); i < mine.getTo().getBlockX(); i++) {
            for (int j = mine.getFrom().getBlockY(); j < mine.getTo().getBlockY(); j++) {
                for (int k = mine.getFrom().getBlockZ(); k < mine.getTo().getBlockZ(); k++) {
                    Block block = w.getBlockAt(i, j, k);

                    if (block == null || block.getType() == Material.AIR) {
                        dugBlocks++;
                    }
                }
            }
        }

        return dugBlocks / ((float) totalBlocks);
    }

    public void stop() {
        this.toStop = true;
    }

    public boolean isRunning() {
        return running;
    }

}
