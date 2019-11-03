package br.com.ppd.tuplespace.commands;

public enum ECommand {
    ADD_ENV(new AddEnvironment()),
    ADD_USER(new AddUser()),
    ADD_DEV(new AddDevice()),
    LIST_ENV(new ListEnvironment());

    private Command command;

    ECommand(Command command) {
        this.command = command;
    }

    public void execute(String[] args) throws InvalidCommand { this.command.execute(args);}
}
