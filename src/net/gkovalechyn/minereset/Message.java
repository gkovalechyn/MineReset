
/*
 * File:   Message.java
 * Author: gkovalechyn
 *
 * Created on Feb 9, 2016, 9:29:07 PM
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.minereset;

/**
 *
 * @author gkovalechyn
 */
public enum Message {
    ERROR_NO_PERMISSION("Error.NoPermission"),
    ERROR_CONSOLE_NOT_SUPPORTED("Error.consoleNotSupported"),
    ERROR_NO_MINE("Error.NoSuchMine"),
    ERROR_NO_VALUE("Error.MissingValue"),
    ERROR_ALREADY_ENABLED("Error.AlreadyEnabled"),
    ERROR_NOT_ENABLED("Error.NotEnabled"),
    ERROR_NOT_DISABLED("Error.NotDisabled"),
    ERROR_NO_TARGET("Error.NoTargetBlock"),
    ERROR_MINE_EXISTS("Error.MineAlreadyExists"),
    ERROR_NO_COMMAND("Error.NoCommand"),
    ERROR_NO_RESET_COND("Error.NoSuchResetCondition"),
    
    GENERAL_HEADER("General.Header"),
    GENERAL_FOOTER("General.Footer"),
    
    INFO_RESET("Info.Reset"),
    INFO_TIMERESET("Info.ResetTime"),
    INFO_ENABLED("Info.MineEnabled"),
    INFO_DISABLED("Info.MineDisabled"),
    INFO_DISABLED_RESET("Info.MineDisabledReset"),
    INFO_SET("Info.Set"),
    INFO_CREATED("Info.Created"),
    INFO_DELETED("Info.Deleted"),
    INFO_DELETED_RESET("Info.DeletedReset"),
    
    CMD_LIST_DESCRIPTION("Commands.List.Description"),
    
    CMD_INFO_DESCRIPTION("Commands.Info.Description"),
    CMD_INFO_USAGE("Commands.Info.Usage"),
    
    CMD_SET_DESCRIPTION("Commands.Set.Description"),
    CMD_SET_USAGE("Commands.Set.Usage"),
    
    CMD_RESET_DESCRIPTION("Commands.Reset.Description"),
    CMD_RESET_USAGE("Commands.Reset.Usage"),
    
    CMD_CREATE_DESCRIPTION("Commands.Create.Description"),
    CMD_CREATE_USAGE("Commands.Create.Usage"),
    
    CMD_DELETE_DESCRIPTION("Commands.Delete.Description"),
    CMD_DELETE_USAGE("Commands.Delete.Usage"),
    
    CMD_ENABLE_DESCRIPTION("Commands.Enable.Description"),
    CMD_ENABLE_USAGE("Commands.Enable.Usage"),
    
    CMD_DISABLE_DESCRIPTION("Commands.Disable.Description"),
    CMD_DISABLE_USAGE("Commands.Disable.Usage"),
    
    CMDSTR_SET_PARAMETERS("CommandStrings.Set.Parameters"),
    CMDSTR_SET_FROM("CommandStrings.Set.From"),
    CMDSTR_SET_TO("CommandStrings.Set.To"),
    CMDSTR_SET_RESET_POSITION("CommandStrings.Set.ResetPosition"),
    CMDSTR_SET_RESET_TYPE("CommandStrings.Set.ResetType"),
    CMDSTR_SET_RESET_INTERVAL("CommandStrings.Set.ResetInterval"),
    CMDSTR_SET_RESET_PERCENTAGE("CommandStrings.Set.ResetPercentage"),
    CMDSTR_SET_MATERIALS("CommandStrings.Set.Materials"),
    DUMMY("%Dummy%");
    private final String path;

    private Message(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
