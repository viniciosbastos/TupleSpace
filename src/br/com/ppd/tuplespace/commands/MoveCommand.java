package br.com.ppd.tuplespace.commands;

import br.com.ppd.tuplespace.models.Device;
import br.com.ppd.tuplespace.models.Environment;
import br.com.ppd.tuplespace.models.User;
import br.com.ppd.tuplespace.service.JavaSpaceService;
import br.com.ppd.tuplespace.service.ServiceUnavailable;

import java.util.List;

import static br.com.ppd.tuplespace.util.Util.println;

public class MoveCommand implements ICommand {
    private JavaSpaceService service;
    private String[] args;

    public MoveCommand() {
        this.service = JavaSpaceService.getInstance();
    }

    @Override
    public void execute(String[] args) throws InvalidCommand {
        ETarget target = ETarget.valueOf(args[1].toUpperCase());
        this.args = args;
        switch(target) {
            case USER:
                listUserInEnv();
                break;
            case DEV:
                listDevicesInEnv();
                break;
            default:
                println("Target not supported.");
        }
    }

    private void listEnv() throws InvalidCommand {
        if (args.length != 2) throw new InvalidCommand("Correct usage: list env");
        try {
            List<Environment> listEnv = this.service.listEnvironments();
            println("+ Environments");
            for(Environment env : listEnv) {
                println("   - " + env.name);
            }
        } catch (ServiceUnavailable serviceUnavailable) {
            println("Could not execute command. Error: " + serviceUnavailable.getMessage());
        }
    }

    private void listDevicesInEnv() throws InvalidCommand {
        if (args.length != 3) throw new InvalidCommand("Correct usage: list env <environment_name>");
        try {
            List<Device> listDev = this.service.listDevicesByEnv(args[2]);
            println("+ Environment: " + args[2]);
            println("+ Devices");
            for(Device dev : listDev) {
                println("   - " + dev.name);
            }
        } catch (ServiceUnavailable serviceUnavailable) {
            println("Could not execute command. Error: " + serviceUnavailable.getMessage());
        }
    }

    private void listUserInEnv() throws InvalidCommand {
        if (args.length != 3) throw new InvalidCommand("Correct usage: list env <environment_name>");
        try {
            List<User> listUser = this.service.listUsersByEnv(args[2]);
            println("+ Environment: " + args[2]);
            println("+ Users");
            for(User env : listUser) {
                println("   - " + env.name);
            }
        } catch (ServiceUnavailable serviceUnavailable) {
            println("Could not execute command. Error: " + serviceUnavailable.getMessage());
        }
    }
}
