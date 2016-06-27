
/*
 * File:   DisableCommand.java
 * Author: gkovalechyn
 *
 * Created on Feb 9, 2016, 11:06:48 PM
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
public class DisableCommand extends CommandBase {

    public DisableCommand() {
        super("MineReset.Command.Disable", Message.CMD_DISABLE_DESCRIPTION, true);
    }

    @Override
    public void execute(CommandSender sender, String[] args, MineReset plugin) {
        if (args.length < 2) {
            sender.sendMessage(MessageHandler.getMessage(Message.CMD_DISABLE_USAGE));
            return;
        }
        Mine mine = plugin.getMineManager().getMine(args[1]);

        if (mine != null) {
            Message m = Message.DUMMY;
            switch (mine.getState()) {
                case IDLE:
                    m = Message.INFO_DISABLED;
                    mine.setState(MineState.NOT_SET);
                    break;
                case NOT_SET:
                    m = Message.ERROR_NOT_ENABLED;
                    break;
                case RESETTING:
                    m = Message.INFO_DISABLED_RESET;
                    mine.setState(MineState.NOT_SET);
                    break;
            }
            
            sender.sendMessage(MessageHandler.getPrefixedMessage(m));
        } else {
            sender.sendMessage(MessageHandler.getPrefixedMessage(Message.ERROR_NO_MINE));
        }
    }

}
