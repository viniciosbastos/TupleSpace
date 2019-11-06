package br.com.ppd.tuplespace.commands;

import br.com.ppd.tuplespace.models.Device;
import br.com.ppd.tuplespace.models.Environment;
import br.com.ppd.tuplespace.models.User;
import br.com.ppd.tuplespace.service.JavaSpaceService;
import br.com.ppd.tuplespace.service.ServiceUnavailable;

import static br.com.ppd.tuplespace.util.Util.println;

public class AddCommand implements ICommand {

    private JavaSpaceService service;
    private String[] args;

    public AddCommand() {
        this.service = JavaSpaceService.getInstance();
    }

    @Override
    public void execute(String[] args) throws InvalidCommand {
        ETarget target = ETarget.valueOf(args[1].toUpperCase());
        this.args = args;
        switch(target) {
            case ENV:
                addEnv();
                break;
            case USER:
                addUser();
                break;
            case DEV:
                addDevice();
                break;
        }
    }

    private void addDevice() throws InvalidCommand{
        if (args.length != 4) throw new InvalidCommand("Correct usage: add dev <device name> <environment name>");
        try {
            Environment env = this.service.findEnvironment(args[3]);
            if (env == null) throw new IllegalArgumentException(String.format("Could not find environment with name %s", args[3]));

            Device device = new Device();
            device.name = args[2];
            if (this.service.searchDevice(device) == null) {
                device.environment = env;
                this.service.send(device);
                println(String.format("Added device %s to environment %s", args[2], args[3]));
            } else {
                println(String.format("Found device %s in another environment", args[2]));
            }
        } catch (ServiceUnavailable serviceUnavailable) {
            println("Could not execute command. Error: " + serviceUnavailable.getMessage());
        }
    }

    private void addEnv() throws InvalidCommand {
        if (args.length != 3) throw new InvalidCommand("Correct usage: add env <environment name>");
        String envName = args[2];
        try {
            this.service.send(new Environment(envName));
            println(String.format("Environment %s added", envName));
        } catch (ServiceUnavailable serviceUnavailable) {
            println("Could not execute command. Error: " + serviceUnavailable.getMessage());
        }
    }

    private void addUser() throws InvalidCommand {
        if (args.length != 4) throw new InvalidCommand("Correct usage: add user <username> <environment name>");
        try {
            Environment env = this.service.findEnvironment(args[3]);
            if (env == null) throw new IllegalArgumentException(String.format("Could not find environment with name %s", args[3]));

            User user = new User();
            user.name = args[2];
            if (this.service.searchUser(user) == null) {
                user.environment = env;
                this.service.send(new User(args[2], env));
                println(String.format("Added user %s to environment %s", args[2], args[3]));
            } else {
                println(String.format("Found user %s in another environment", args[2]));
            }
        } catch (ServiceUnavailable serviceUnavailable) {
            println("Could not execute command. Error: " + serviceUnavailable.getMessage());
        }
    }
}
