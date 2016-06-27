
/*
 * File:   ResetTask.java
 * Author: gkovalechyn
 *
 * Created on Feb 9, 2016, 8:41:54 PM
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.minereset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 *
 * @author gkovalechyn
 */
public class ResetTask implements Runnable {

    private final Mine mine;
    private final Random random = new Random();
    private final MineReset plugin;

    public ResetTask(MineReset plugin, Mine mine) {
        this.plugin = plugin;
        this.mine = mine;
    }

    @Override
    public void run() {
        //128 blocks per tick
        Location from = mine.getFrom();
        Location to = mine.getTo();
        World world = from.getWorld();
        List<Pair<IdDataWrapper, Float>> probabilities = this.mine.getFillProbabilities();
        int runs = 0;
        int nowX = from.getBlockX();
        int nowY = from.getBlockY();
        int nowZ = from.getBlockZ();

        int dx = (to.getBlockX() - from.getBlockX());
        int dy = (to.getBlockX() - from.getBlockX());
        int dz = (to.getBlockX() - from.getBlockX());
        
        dx = (dx == 0) ? 1 : dx;
        dy = (dy == 0) ? 1 : dy;
        dz = (dz == 0) ? 1 : dz;
        
        runs = ((dx) * (dy) * (dz));
        runs = (int) Math.ceil(runs / 128D);
        
        for (int run = 0; run <= runs; run++) {
            int count = 0;
            List<Block> blocks = new ArrayList<>(128);
            List<IdDataWrapper> wrappers = new ArrayList<>(128);

            for (int i = nowX; i <= to.getX() && count < 128; i++) {
                for (int j = nowY; j <= to.getY() && count < 128; j++) {
                    for (int k = nowZ; k <= to.getZ() && count < 128; k++) {

                        Block b = world.getBlockAt(i, j, k);
                        IdDataWrapper wrapper = this.getNextBlock(probabilities);

                        if (wrapper != null && b.getTypeId() != wrapper.id && b.getData() != wrapper.data) {
                            //b.setTypeIdAndData(wrapper.id, wrapper.data, false);
                            blocks.add(b);
                            wrappers.add(wrapper);
                        }

                        nowZ = k;
                        count++;
                    }

                    nowZ = nowZ < to.getZ() ? nowZ : from.getBlockZ();
                    nowY = j;
                }

                nowY = nowY < to.getY() ? nowY : from.getBlockY();
                nowX = i;
            }

            this.plugin.getPlugin().getServer().getScheduler().runTask(plugin.getPlugin(), new SyncTask(blocks, wrappers));

            try {
                Thread.sleep(20 * 20L);//Wait 20 ticks
            } catch (InterruptedException e) {

            }
        }

        this.mine.setLastReset(System.currentTimeMillis());
        this.mine.setState(MineState.IDLE);
    }

    private IdDataWrapper getNextBlock(List<Pair<IdDataWrapper, Float>> list) {
        float f = this.random.nextFloat();
        float total = 0F;

        for (Pair<IdDataWrapper, Float> pair : list) {
            total += pair.second;

            if (f < total) {
                return pair.first;
            }
        }

        return null;
    }

    private class SyncTask implements Runnable {

        private final List<Block> blocks;
        private final List<IdDataWrapper> wrappers;
        private boolean complete = false;

        public SyncTask(List<Block> blocks, List<IdDataWrapper> wrappers) {
            this.blocks = blocks;
            this.wrappers = wrappers;
        }

        @Override
        public void run() {
            for (int i = 0; i < blocks.size(); i++) {
                IdDataWrapper wp = wrappers.get(i);
                blocks.get(i).setTypeIdAndData(wp.id, wp.data, true);
            }

            complete = true;
        }

    }

}

