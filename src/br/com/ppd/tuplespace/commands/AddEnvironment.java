package br.com.ppd.tuplespace.commands;

import br.com.ppd.tuplespace.ServiceNotFound;
import br.com.ppd.tuplespace.models.Environment;
import br.com.ppd.tuplespace.service.JavaSpaceService;
import br.com.ppd.tuplespace.service.ServiceUnavailable;
import static br.com.ppd.tuplespace.util.Util.*;

public class AddEnvironment implements Command {

    private JavaSpaceService service;

    public AddEnvironment() {
        this.service = JavaSpaceService.getInstance();
    }

    @Override
    public void execute(String[] args) throws InvalidCommand{
        if (args.length != 2) {
            throw new InvalidCommand("Correct usage: add_env <environment name>");
        }
        String envName = args[1];
        try {
            this.service.send(new Environment(envName));
            println(String.format("Environment %s added", envName));
        } catch (ServiceUnavailable serviceUnavailable) {
            println("Could not execute command. Error: " + serviceUnavailable.getMessage());
        }
    }
}
