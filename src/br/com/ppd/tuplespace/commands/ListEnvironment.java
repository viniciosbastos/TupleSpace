package br.com.ppd.tuplespace.commands;

import br.com.ppd.tuplespace.models.Device;
import br.com.ppd.tuplespace.models.Environment;
import br.com.ppd.tuplespace.models.User;
import br.com.ppd.tuplespace.service.JavaSpaceService;
import br.com.ppd.tuplespace.service.ServiceUnavailable;

import static br.com.ppd.tuplespace.util.Util.println;

public class ListEnvironment implements Command {
    private JavaSpaceService service;

    public ListEnvironment() {
        this.service = JavaSpaceService.getInstance();
    }

    @Override
    public void execute(String[] args) throws InvalidCommand {
        if (args.length > 1) {
            throw new InvalidCommand("Correct usage: all_env");
        }

        try {
            Device env = (Device) this.service.read(new Device());
        } catch (ServiceUnavailable serviceUnavailable) {
            println("Could not execute command. Error: " + serviceUnavailable.getMessage());
        }
    }
}
