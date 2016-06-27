
/*
 * File:   ResetCommand.java
 * Author: gkovalechyn
 *
 * Created on Feb 9, 2016, 11:13:35 PM
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
public class ResetCommand extends CommandBase {

    public ResetCommand() {
        super("MineReset.Command.Reset", Message.CMD_RESET_DESCRIPTION, true);
    }

    @Override
    public void execute(CommandSender sender, String[] args, MineReset plugin) {
        if (args.length < 2) {
            sender.sendMessage(MessageHandler.getPrefixedMessage(Message.CMD_RESET_USAGE));
            return;
        }
        Mine mine = plugin.getMineManager().getMine(args[1]);

        if (mine != null) {
            if (mine.getState() != MineState.RESETTING){
                plugin.getMineManager().queueMineReset(mine);
            }
        } else {
            sender.sendMessage(MessageHandler.getPrefixedMessage(Message.ERROR_NO_MINE));
        }
    }

}
