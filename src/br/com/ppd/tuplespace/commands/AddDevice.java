package br.com.ppd.tuplespace.commands;

import br.com.ppd.tuplespace.models.Device;
import br.com.ppd.tuplespace.models.Environment;
import br.com.ppd.tuplespace.models.User;
import br.com.ppd.tuplespace.service.JavaSpaceService;
import br.com.ppd.tuplespace.service.ServiceUnavailable;

import static br.com.ppd.tuplespace.util.Util.println;

public class AddDevice implements Command {

    private JavaSpaceService service;

    public AddDevice() {
        this.service = JavaSpaceService.getInstance();
    }

    @Override
    public void execute(String[] args) throws InvalidCommand {
        if (args.length != 3) {
            throw new InvalidCommand("Correct usage: add_dev <device name> <environment name>");
        }
        try {
            Environment env = this.service.findEnvironment(args[2]);
            if (env == null) throw new IllegalArgumentException(String.format("Could not find environment with name %s", args[2]));

            this.service.send(new Device(args[1], env));
            println(String.format("Added device %s to environment %s", args[1], args[2]));
        } catch (ServiceUnavailable serviceUnavailable) {
            println("Could not execute command. Error: " + serviceUnavailable.getMessage());
        }
    }
}
