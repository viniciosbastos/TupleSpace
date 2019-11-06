package br.com.ppd.tuplespace;

import br.com.ppd.tuplespace.commands.AdminCommandProcessor;
import br.com.ppd.tuplespace.commands.InvalidCommand;

import java.util.Scanner;

import static br.com.ppd.tuplespace.util.Util.*;

class AdminApplication {

    private Scanner scanner;

    AdminApplication(){
        this.scanner = new Scanner(System.in);
    }

    void start() {
        boolean loop = true;
        showCommandsTable();
        String command;
        do {
            print(">> ");
            command = this.scanner.nextLine();
            try {
                AdminCommandProcessor.process(command);
            } catch (InvalidCommand|IllegalArgumentException invalidCommand) {
                println(invalidCommand.getMessage());
            }
        } while(loop);
    }

    private void showCommandsTable() {
        println("+-------------------------------------------+");
        println("+                  Commands                 +");
        println("+-------------------------------------------+");
        println("Add Command: ");
        println("   add env <environment name>");
        println("   add user <username> <environment name>");
        println("   add dev <device_name> <environment name>");
        println("List Command: ");
        println("   ls env");
        println("   ls user <environment name>");
        println("   ls dev <environment name>");
        println("Remove Command: ");
        println("   rm env <name>");
        println("   rm user <name>");
        println("   rm dev <name>");
        println("Move Command: ");
        println("   mv user <username> <new_env>");
        println("   mv dev <dev_name> <new_env>");
        println("+-------------------------------------------+");
    }

}
