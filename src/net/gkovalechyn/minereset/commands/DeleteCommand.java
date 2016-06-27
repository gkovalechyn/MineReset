
/*
 * File:   DeleteCommand.java
 * Author: gkovalechyn
 *
 * Created on Feb 10, 2016, 1:19:25 AM
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
import net.gkovalechyn.minereset.MineState;
import org.bukkit.command.CommandSender;

/**
 *
 * @author gkovalechyn
 */
public class DeleteCommand extends CommandBase{

    public DeleteCommand(){
        super("MineReset.Command.Delete", Message.CMD_DELETE_DESCRIPTION, true);
    }
    @Override
    public void execute(CommandSender sender, String[] args, MineReset plugin) {
        if (args.length < 2) {
            sender.sendMessage(MessageHandler.getMessage(Message.CMD_DISABLE_USAGE));
            return;
        }
        
        Mine mine = plugin.getMineManager().getMine(args[1]);

        if (mine != null) {
            if (mine.getState() == MineState.RESETTING){
                sender.sendMessage(MessageHandler.getPrefixedMessage(Message.INFO_DELETED));
            }else{
                sender.sendMessage(MessageHandler.getPrefixedMessage(Message.INFO_DELETED_RESET));
            }
            plugin.getMineManager().deleteMine(args[1]);
        } else {
            sender.sendMessage(MessageHandler.getPrefixedMessage(Message.ERROR_NO_MINE));
            
        }
    }

}
