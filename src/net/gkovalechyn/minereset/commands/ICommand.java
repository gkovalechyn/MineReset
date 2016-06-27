
/*
 * File:   ICommand.java
 * Author: gkovalechyn
 *
 * Created on Feb 9, 2016, 9:50:16 PM
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.gkovalechyn.minereset.commands;

import net.gkovalechyn.minereset.MineReset;
import org.bukkit.command.CommandSender;

/**
 *
 * @author gkovalechyn
 */
public interface ICommand {
    
    public void execute(CommandSender sender, String[] args, MineReset plugin);
    
    public boolean supportsConsole();
    
    public String getPermission();
    
    public String getDescription();
}
