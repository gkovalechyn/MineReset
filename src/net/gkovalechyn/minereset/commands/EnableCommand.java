
/*
 * File:   EnableCommand.java
 * Author: gkovalechyn
 *
 * Created on Feb 9, 2016, 10:46:12 PM
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
import net.gkovalechyn.minereset.ResetCondition;
import org.bukkit.command.CommandSender;

/**
 *
 * @author gkovalechyn
 */
public class EnableCommand extends CommandBase{
    public EnableCommand(){
        super("MineReset.Command.Enable", Message.CMD_ENABLE_DESCRIPTION, true);
    }
    
    @Override
    public void execute(CommandSender sender, String[] args, MineReset plugin) {
        if (args.length < 2){
            sender.sendMessage(MessageHandler.getPrefixedMessage(Message.CMD_ENABLE_USAGE));
            return;
        }
        Mine mine = plugin.getMineManager().getMine(args[1]);
        
        if (mine != null){
            if (mine.getFrom() == null){
                sender.sendMessage(MessageHandler.getPrefixedMessage(Message.ERROR_NO_VALUE).replace("{Value}", "From"));
                return;
            }
            
            if (mine.getTo() == null){
                sender.sendMessage(MessageHandler.getPrefixedMessage(Message.ERROR_NO_VALUE).replace("{Value}", "To"));
                return;
            }
            
            if (mine.getResetPosition() == null){
                sender.sendMessage(MessageHandler.getPrefixedMessage(Message.ERROR_NO_VALUE).replace("{Value}", "ResetPosition"));
                return;
            }
            
            if (mine.getResetInterval() <= 0 && mine.getResetCondition() == ResetCondition.TIME){
                sender.sendMessage(MessageHandler.getPrefixedMessage(Message.ERROR_NO_VALUE).replace("{Value}", "ResetInterval"));
                return;
            }
            
            if (mine.getResetTreshold() <= 0F && mine.getResetCondition() == ResetCondition.PERCENTAGE_LEFT){
                sender.sendMessage(MessageHandler.getPrefixedMessage(Message.ERROR_NO_VALUE).replace("{Value}", "ResetPercentage"));
                return;
            }
            
            if (mine.getFillProbabilities() == null){
                sender.sendMessage(MessageHandler.getPrefixedMessage(Message.ERROR_NO_VALUE).replace("{Value}", "Materials"));
                return;
            }
            
            if (mine.getState() == MineState.NOT_SET){
                mine.setLastReset(0);
                mine.setState(MineState.IDLE);
                sender.sendMessage(MessageHandler.getPrefixedMessage(Message.INFO_ENABLED).replace("{Mine}", mine.getId()));
            }else{
                sender.sendMessage(MessageHandler.getPrefixedMessage(Message.ERROR_ALREADY_ENABLED));
            }
        }else{
            sender.sendMessage(MessageHandler.getPrefixedMessage(Message.ERROR_NO_MINE));
        }
    }

}
