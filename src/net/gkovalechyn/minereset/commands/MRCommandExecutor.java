
/*
 * File:   MRCommandExecutor.java
 * Author: gkovalechyn
 *
 * Created on Feb 9, 2016, 9:43:48 PM
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.minereset.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.gkovalechyn.minereset.Message;
import net.gkovalechyn.minereset.MessageHandler;
import net.gkovalechyn.minereset.MineReset;
import net.gkovalechyn.minereset.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

/**
 *
 * @author gkovalechyn
 */
public class MRCommandExecutor implements CommandExecutor {

    private final MineReset plugin;
    private final Map<String, ICommand> commands = new HashMap<>();

    public MRCommandExecutor(MineReset plugin) {
        this.plugin = plugin;
        this.registerCommands();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] strings) {
        String[] args = this.fixArguments(strings);
        if (args.length == 0) {
            StringBuilder builder = new StringBuilder(64);
            
            sender.sendMessage(MessageHandler.getMessage(Message.GENERAL_HEADER));
            for (Map.Entry<String, ICommand> entry : this.commands.entrySet()) {
                builder.append("・").append(entry.getKey()).append(" - ").append(entry.getValue().getDescription());
                sender.sendMessage(builder.toString());
                builder.setLength(0);
            }
            sender.sendMessage(MessageHandler.getMessage(Message.GENERAL_FOOTER));
            
            return true;
        } else {
            ICommand icmd = this.commands.get(args[0]);
            if (icmd != null) {
                if (!icmd.supportsConsole() && (sender instanceof ConsoleCommandSender)) {
                    sender.sendMessage(MessageHandler.getPrefixedMessage(Message.ERROR_CONSOLE_NOT_SUPPORTED));
                    return true;
                }

                if (!sender.hasPermission(icmd.getPermission())) {
                    sender.sendMessage(MessageHandler.getPrefixedMessage(Message.ERROR_NO_PERMISSION).replace("{Permission}", icmd.getPermission()));
                    return true;
                }

                icmd.execute(sender, args, plugin);
                return true;
            } else {
                sender.sendMessage(MessageHandler.getPrefixedMessage(Message.ERROR_NO_COMMAND));
                return true;
            }
        }
    }

    private String[] fixArguments(String[] args) {
        List<String> result = new ArrayList<>(args.length);
        char[] full = Util.joinString(args).toCharArray();
        StringBuilder builder = new StringBuilder(12);
        String[] resultArray;

        if (full.length == 0){
            return new String[0];
        }
        
        for (int i = 0; i < full.length; i++) {
            switch (full[i]) {
                case '\\':
                    i++;
                    builder.append(readEscapedCharacter(full[i]));
                    break;
                case '\"':
                    i++;

                    while (i < full.length) {
                        if (full[i] == '\"') {
                            break;
                        } else if (full[i] == '\\') {
                            i++;
                            builder.append(readEscapedCharacter(full[i]));
                        } else {
                            builder.append(full[i]);
                        }
                        i++;
                    }
                    break;
                case ' ':
                    result.add(builder.toString());
                    builder.setLength(0);
                    break;
                default:
                    builder.append(full[i]);
            }
        }
        
        result.add(builder.toString());

        resultArray = new String[result.size()];

        for (int i = 0; i < resultArray.length; i++) {
            resultArray[i] = result.get(i);
        }

        return resultArray;
    }

    private char readEscapedCharacter(char c) {
        switch (c) {
            case 'n':
                return '\n';
            case '\"':
                return '\"';
            case '\'':
                return '\'';
            case 't':
                return '\t';
            case '\\':
                return '\\'; //Because you know... Reasons
            default:
                return c;
        }
    }

    public Map<String, ICommand> getCommands() {
        return commands;
    }

    private void registerCommands() {
        this.commands.put("create", new CreateCommand());
        this.commands.put("delete", new DeleteCommand());
        this.commands.put("disable", new DisableCommand());
        this.commands.put("enable", new EnableCommand());
        this.commands.put("list", new ListCommand());
        this.commands.put("info", new InfoCommand());
        this.commands.put("set", new SetCommand());
        this.commands.put("reset", new ResetCommand());
    }
}
