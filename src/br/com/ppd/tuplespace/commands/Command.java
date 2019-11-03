package br.com.ppd.tuplespace.commands;

public interface Command {

    void execute(String[] args) throws InvalidCommand;
}
