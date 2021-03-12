package cn.cookiestudio.easy4chess_server.command;

import java.util.HashMap;

public class CommandMap {

    private HashMap<String,Command> commands = new HashMap<>();

    public HashMap<String, Command> getCommands() {
        return commands;
    }

    public void registerCommand(Command command){
        this.commands.put(command.getCommandName(),command);
    }
}
