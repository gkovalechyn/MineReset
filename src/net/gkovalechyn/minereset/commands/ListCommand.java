
/*
 * File:   ListCommand.java
 * Author: gkovalechyn
 *
 * Created on Feb 9, 2016, 10:15:15 PM
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
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author gkovalechyn
 */
public class ListCommand extends CommandBase {

    private final StringBuilder builder = new StringBuilder(64);

    public ListCommand() {
        super("MineReset.Command.List", Message.CMD_LIST_DESCRIPTION, true);
    }

    @Override
    public void execute(CommandSender sender, String[] args, MineReset plugin) {

        sender.sendMessage(MessageHandler.getMessage(Message.GENERAL_HEADER));
        for (Mine mine : plugin.getMineManager().getMines()) {
            builder.append('・');
            builder.append(mine.getId());
            builder.append(' ').append(ChatColor.BLACK).append('[');
            switch (mine.getState()) {
                case IDLE:
                    builder.append(ChatColor.GREEN);
                    break;
                case NOT_SET:
                    builder.append(ChatColor.RED);
                    break;
                case RESETTING:
                    builder.append(ChatColor.DARK_PURPLE);
                    break;
            }
            builder.append(mine.getState());
            builder.append(ChatColor.BLACK).append(']');
            sender.sendMessage(builder.toString());
            
            builder.setLength(0);
        }
        sender.sendMessage(MessageHandler.getMessage(Message.GENERAL_FOOTER));
    }

}
