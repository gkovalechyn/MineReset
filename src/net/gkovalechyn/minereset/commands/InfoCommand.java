
/*
 * File:   InfoCommand.java
 * Author: gkovalechyn
 *
 * Created on Feb 9, 2016, 10:30:41 PM
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.minereset.commands;

import java.text.SimpleDateFormat;
import java.util.Date;
import net.gkovalechyn.minereset.IdDataWrapper;
import net.gkovalechyn.minereset.Message;
import net.gkovalechyn.minereset.MessageHandler;
import net.gkovalechyn.minereset.Mine;
import net.gkovalechyn.minereset.MineReset;
import net.gkovalechyn.minereset.Pair;
import org.bukkit.command.CommandSender;

/**
 *
 * @author gkovalechyn
 */
public class InfoCommand extends CommandBase {

    private final StringBuilder builder = new StringBuilder(64);
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public InfoCommand() {
        super("MineReset.Command.Info", Message.CMD_INFO_DESCRIPTION, true);
    }

    @Override
    public void execute(CommandSender sender, String[] args, MineReset plugin) {
        if (args.length < 2) {
            sender.sendMessage(MessageHandler.getPrefixedMessage(Message.CMD_INFO_USAGE));
            return;
        }
        Mine mine = plugin.getMineManager().getMine(args[1]);

        if (mine != null) {
            sender.sendMessage(MessageHandler.getMessage(Message.GENERAL_HEADER));
            builder.append("Name: ").append(mine.getId()).append(". Status: ").append(mine.getState());
            sender.sendMessage(builder.toString());
            builder.setLength(0);

            builder.append("Last reset: ").append(format.format(new Date(mine.getLastReset())));
            builder.append(". Reset condition: ").append(mine.getResetCondition());
            sender.sendMessage(builder.toString());
            builder.setLength(0);

            builder.append("From (");
            if (mine.getFrom() != null) {
                builder.append(mine.getFrom().getBlockX()).append(',');
                builder.append(mine.getFrom().getBlockY()).append(',').append(mine.getFrom().getBlockZ());
            } else {
                builder.append("NOT SET");
            }

            builder.append(") to (");

            if (mine.getTo() != null) {
                builder.append(mine.getTo().getBlockX()).append(',').append(mine.getTo().getBlockY());
                builder.append(',').append(mine.getTo().getBlockZ());
            } else {
                builder.append("NOT SET");
            }
            builder.append(')');
            sender.sendMessage(builder.toString());
            builder.setLength(0);

            if (mine.getResetPosition() != null) {
                builder.append("Reset position: (").append(mine.getResetPosition().getBlockX());
                builder.append(',').append(mine.getResetPosition().getBlockY()).append(',');
                builder.append(',').append(mine.getResetPosition().getBlockZ()).append(')');
                sender.sendMessage(builder.toString());
                builder.setLength(0);
            }
            
            builder.append("Materials: ");
            for (Pair<IdDataWrapper, Float> pair : mine.getFillProbabilities()) {
                builder.append("([").append(pair.first.id).append(':').append(pair.first.data).append(']');
                builder.append(String.format("%.3f", pair.second)).append("), ");
            }
            sender.sendMessage(builder.toString());
            builder.setLength(0);
            
            sender.sendMessage(MessageHandler.getMessage(Message.GENERAL_FOOTER));
        } else {
            sender.sendMessage(MessageHandler.getPrefixedMessage(Message.ERROR_NO_MINE));
        }
    }

}
