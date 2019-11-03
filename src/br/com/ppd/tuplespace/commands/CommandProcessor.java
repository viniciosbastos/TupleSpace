package br.com.ppd.tuplespace.commands;

public class CommandProcessor {

    public static void process(String commandLine) throws InvalidCommand{
        String[] args = commandLine.split(" ");
        ECommand command;
        try {
            command = ECommand.valueOf(args[0].toUpperCase());
        } catch(IllegalArgumentException ex) {
            throw new InvalidCommand("Command not found.");
        }
        command.execute(args);
    }
}
