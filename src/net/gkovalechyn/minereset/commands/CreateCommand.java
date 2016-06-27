
/*
 * File:   CreateCommand.java
 * Author: gkovalechyn
 *
 * Created on Feb 10, 2016, 1:02:25 AM
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.gkovalechyn.minereset.commands;

import net.gkovalechyn.minereset.Message;
import net.gkovalechyn.minereset.MessageHandler;
import net.gkovalechyn.minereset.Mine;
import net.gkovalechyn.minereset.MineReset;
import org.bukkit.command.CommandSender;

/**
 *
 * @author gkovalechyn
 */
public class CreateCommand extends CommandBase{
    public CreateCommand(){
        super("MineReset.Command.Create", Message.CMD_CREATE_DESCRIPTION, true);
    }
    
    @Override
    public void execute(CommandSender sender, String[] args, MineReset plugin) {
        if (args.length < 2) {
            sender.sendMessage(MessageHandler.getMessage(Message.CMD_DISABLE_USAGE));
            return;
        }
        
        Mine mine = plugin.getMineManager().getMine(args[1]);

        if (mine != null) {
            sender.sendMessage(MessageHandler.getPrefixedMessage(Message.ERROR_MINE_EXISTS));
        } else {
            plugin.getMineManager().createMine(args[1]);
            sender.sendMessage(MessageHandler.getPrefixedMessage(Message.INFO_CREATED));
        }
    }

}
