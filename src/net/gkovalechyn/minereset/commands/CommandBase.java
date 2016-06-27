
/*
 * File:   CommandBase.java
 * Author: gkovalechyn
 *
 * Created on Feb 9, 2016, 10:15:44 PM
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.gkovalechyn.minereset.commands;

import net.gkovalechyn.minereset.Message;
import net.gkovalechyn.minereset.MessageHandler;

/**
 *
 * @author gkovalechyn
 */
public abstract class CommandBase implements ICommand{
    private final String permission;
    private final String description;
    private final boolean supportsConsole;

    public CommandBase(String permission, Message description, boolean supportsConsole) {
        this.permission = permission;
        this.description = MessageHandler.getMessage(description);
        this.supportsConsole = supportsConsole;
    }

    @Override
    public boolean supportsConsole() {
        return this.supportsConsole;
    }

    @Override
    public String getPermission() {
        return this.permission;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
    
    

}
