/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.minereset;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 *
 * @author gkovalechyn
 */
public class Mine {

    private final String id;
    private Location from;
    private Location to;
    private Location resetPosition;
    private long resetInterval = 0;
    private float resetTreshold;
    private List<Pair<IdDataWrapper, Float>> fillProbabilities;
    private MineState state = MineState.NOT_SET;
    private long lastReset = 0;
    private ResetCondition resetCondition = ResetCondition.TIME;

    public Mine(String id) {
        this.id = id;
    }

    public long getResetInterval() {
        return resetInterval;
    }

    public void setResetInterval(long resetInterval) {
        this.resetInterval = resetInterval;
    }

    public float getResetTreshold() {
        return resetTreshold;
    }

    public void setResetTreshold(float resetTreshold) {
        this.resetTreshold = resetTreshold;
    }

    public Location getFrom() {
        return from;
    }

    public void setFrom(Location from) {
        this.from = from;
        if (this.to != null) {
            this.fixCorners();
        }
    }

    public Location getTo() {
        return to;
    }

    public void setTo(Location to) {
        this.to = to;
        if (this.from != null) {
            this.fixCorners();
        }
    }

    private void fixCorners() {
        int minX = Math.min(this.from.getBlockX(), this.to.getBlockX());
        int maxX = Math.max(this.from.getBlockX(), this.to.getBlockX());
        int minY = Math.min(this.from.getBlockY(), this.to.getBlockY());
        int maxY = Math.max(this.from.getBlockY(), this.to.getBlockY());
        int minZ = Math.min(this.from.getBlockZ(), this.to.getBlockZ());
        int maxZ = Math.max(this.from.getBlockZ(), this.to.getBlockZ());

        this.from = new Location(this.from.getWorld(), minX, minY, minZ);
        this.to = new Location(this.to.getWorld(), maxX, maxY, maxZ);
    }

    public Location getResetPosition() {
        return resetPosition;
    }

    public void setResetPosition(Location resetPosition) {
        this.resetPosition = resetPosition;
    }

    public String getId() {
        return id;
    }

    public List<Pair<IdDataWrapper, Float>> getFillProbabilities() {
        return fillProbabilities;
    }

    public void setFillProbabilities(List<Pair<IdDataWrapper, Float>> fillProbabilities) {
        this.fillProbabilities = fillProbabilities;
    }

    public MineState getState() {
        return state;
    }

    public void setState(MineState state) {
        this.state = state;
    }

    public long getLastReset() {
        return lastReset;
    }

    public void setLastReset(long lastReset) {
        this.lastReset = lastReset;
    }

    public ResetCondition getResetCondition() {
        return resetCondition;
    }

    public void setResetCondition(ResetCondition resetCondition) {
        this.resetCondition = resetCondition;
    }

}
