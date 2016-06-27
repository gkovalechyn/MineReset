
/*
 * File:   SetCommand.java
 * Author: gkovalechyn
 *
 * Created on Feb 9, 2016, 11:30:30 PM
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.minereset.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import net.gkovalechyn.minereset.IdDataWrapper;
import net.gkovalechyn.minereset.Message;
import net.gkovalechyn.minereset.MessageHandler;
import net.gkovalechyn.minereset.Mine;
import net.gkovalechyn.minereset.MineReset;
import net.gkovalechyn.minereset.MineState;
import net.gkovalechyn.minereset.Pair;
import net.gkovalechyn.minereset.ResetCondition;
import net.gkovalechyn.minereset.Util;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author gkovalechyn
 */
public class SetCommand extends CommandBase {

    public SetCommand() {
        super("MineReset.Command.Set", Message.CMD_SET_DESCRIPTION, false);
    }

    //mr set <mine> <param> <value>
    @Override
    public void execute(CommandSender sender, String[] args, MineReset plugin) {
        Mine mine;

        if (args.length == 1) {
            sender.sendMessage(MessageHandler.getPrefixedMessage(Message.CMD_SET_USAGE));
            sender.sendMessage(MessageHandler.getMessage(Message.CMDSTR_SET_PARAMETERS));

            sender.sendMessage(MessageHandler.getMessage(Message.GENERAL_HEADER));
            sender.sendMessage("・From - " + MessageHandler.getMessage(Message.CMDSTR_SET_FROM));
            sender.sendMessage("・To - " + MessageHandler.getMessage(Message.CMDSTR_SET_TO));
            sender.sendMessage("・Materials - " + MessageHandler.getMessage(Message.CMDSTR_SET_MATERIALS));
            sender.sendMessage("・ResetPosition - " + MessageHandler.getMessage(Message.CMDSTR_SET_RESET_POSITION));
            sender.sendMessage("・ResetInterval - " + MessageHandler.getMessage(Message.CMDSTR_SET_RESET_INTERVAL));
            sender.sendMessage("・ResetPercentage - " + MessageHandler.getMessage(Message.CMDSTR_SET_RESET_PERCENTAGE));
            sender.sendMessage("・ResetType - " + MessageHandler.getMessage(Message.CMDSTR_SET_RESET_TYPE));
            sender.sendMessage(MessageHandler.getMessage(Message.GENERAL_FOOTER));
            return;
        }

        if (args.length < 3) {
            sender.sendMessage(MessageHandler.getPrefixedMessage(Message.CMD_SET_USAGE));
            return;
        }

        mine = plugin.getMineManager().getMine(args[1]);

        if (mine != null) {
            if (mine.getState() == MineState.NOT_SET) {
                Player p = (Player) sender;
                if (args[2].equalsIgnoreCase("From")) {

                    Block b = p.getTargetBlock((HashSet<Byte>) null, 100);

                    if (b != null) {
                        mine.setFrom(b.getLocation());
                        sender.sendMessage(MessageHandler.getPrefixedMessage(Message.INFO_SET).replace("{Param}", "From").replace("{Value}",
                                String.format(("(%d,%d,%d)"), b.getLocation().getBlockX(), b.getLocation().getBlockY(), b.getLocation().getBlockZ())));
                    } else {
                        sender.sendMessage(MessageHandler.getPrefixedMessage(Message.ERROR_NO_TARGET));
                    }
                } else if (args[2].equalsIgnoreCase("To")) {
                    Block b = p.getTargetBlock((HashSet<Byte>) null, 100);

                    if (b != null) {
                        mine.setTo(b.getLocation());
                        sender.sendMessage(MessageHandler.getPrefixedMessage(Message.INFO_SET).replace("{Param}", "To").replace("{Value}",
                                String.format(("(%d,%d,%d)"), b.getLocation().getBlockX(), b.getLocation().getBlockY(), b.getLocation().getBlockZ())));
                    } else {
                        sender.sendMessage(MessageHandler.getPrefixedMessage(Message.ERROR_NO_TARGET));
                    }
                } else if (args[2].equalsIgnoreCase("ResetType")) {
                    if (args.length < 4) {
                        sender.sendMessage(MessageHandler.getPrefixedMessage(Message.CMD_SET_USAGE));
                        return;
                    }

                    ResetCondition condition = ResetCondition.getByString(args[3]);
                    if (condition == null) {
                        sender.sendMessage(MessageHandler.getPrefixedMessage(Message.ERROR_NO_RESET_COND));
                    } else {
                        mine.setResetCondition(condition);
                    }
                } else if (args[2].equalsIgnoreCase("ResetPosition")) {
                    mine.setResetPosition(p.getLocation());
                    sender.sendMessage(MessageHandler.getPrefixedMessage(Message.INFO_SET).replace("{Param}", "ResetPosition").replace("{Value}",
                            String.format(("(%d,%d,%d)"), p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ())));
                } else if (args[2].equalsIgnoreCase("ResetInterval")) {
                    if (args.length < 4) {
                        sender.sendMessage(MessageHandler.getPrefixedMessage(Message.CMD_SET_USAGE));
                        return;
                    }

                    mine.setResetInterval(Util.timeStringToLong(args[3]));
                    sender.sendMessage(MessageHandler.getPrefixedMessage(Message.INFO_SET).replace("{Param}", "ResetInterval").replace("{Value}", "" + mine.getResetInterval()));
                } else if (args[2].equalsIgnoreCase("ResetPercentage")) {
                    if (args.length < 4) {
                        sender.sendMessage(MessageHandler.getPrefixedMessage(Message.CMD_SET_USAGE));
                        return;
                    }

                    mine.setResetTreshold(Float.parseFloat(args[3]));
                    sender.sendMessage(MessageHandler.getPrefixedMessage(Message.INFO_SET).replace("{Param}", "ResetPercentage").replace("{Value}", "" + mine.getResetTreshold()));
                } else if (args[2].equalsIgnoreCase("Materials")) {
                    StringBuilder builder = new StringBuilder(32);
                    List<Pair<IdDataWrapper, Float>> materials = new ArrayList<>();
                    float total = 0F;

                    for (int i = 3; i < args.length; i += 2) {
                        IdDataWrapper wrapper = new IdDataWrapper();

                        if (args[i].contains(":")) {
                            String[] temp = args[i].split(":");
                            wrapper.id = Integer.parseInt(temp[0]);
                            wrapper.data = Byte.parseByte(temp[1]);
                        } else {
                            wrapper.id = Integer.parseInt(args[i]);
                            wrapper.data = 0;
                        }

                        materials.add(new Pair<>(wrapper, Float.parseFloat(args[i + 1])));
                    }

                    for (Pair<IdDataWrapper, Float> pair : materials) {
                        total += pair.second;
                    }
                    for (Pair<IdDataWrapper, Float> pair : materials) {
                        pair.second /= total;
                    }

                    for (Pair<IdDataWrapper, Float> pair : materials) {
                        builder.append('(');
                        builder.append('[').append(pair.first.data).append(':').append(pair.first.data);
                        builder.append(']').append(String.format("%.3f", pair.second)).append(')');
                    }
                    
                    mine.setFillProbabilities(materials);
                    sender.sendMessage(MessageHandler.getPrefixedMessage(Message.INFO_SET).replace("{Param}", "Materials").replace("{Value}", builder.toString()));
                } else {
                    sender.sendMessage("Argument not recognized: " + args[2]);
                }
            } else {
                sender.sendMessage(MessageHandler.getPrefixedMessage(Message.ERROR_NOT_DISABLED));
            }
        } else {
            sender.sendMessage(MessageHandler.getPrefixedMessage(Message.ERROR_NO_MINE));
        }
    }

}
