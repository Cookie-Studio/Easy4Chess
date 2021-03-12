package cn.cookiestudio.easy4chess_server.command;

public abstract class Command {
    private String commandName;
    private int level;

    public Command(String name){
        this(name,1);
    }

    public Command(String name,int level){
        this.commandName = name;
        this.level = level;
    }

    public String getCommandName() {
        return commandName;
    }

    public int getLevel() {
        return level;
    }
}
