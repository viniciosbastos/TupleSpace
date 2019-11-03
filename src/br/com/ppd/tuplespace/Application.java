package br.com.ppd.tuplespace;

import br.com.ppd.tuplespace.commands.CommandProcessor;
import br.com.ppd.tuplespace.commands.InvalidCommand;
import com.sun.javaws.exceptions.InvalidArgumentException;

import java.util.Scanner;

import static br.com.ppd.tuplespace.util.Util.*;

class Application {

    private Scanner scanner;

    Application(){
        this.scanner = new Scanner(System.in);
    }

    void start() {
        boolean loop = true;
        showCommandsTable();
        String command;
        do {
            command = this.scanner.nextLine();
            try {
                CommandProcessor.process(command);
            } catch (InvalidCommand|IllegalArgumentException invalidCommand) {
                println(invalidCommand.getMessage());
            }
        } while(loop);
    }

    private void showCommandsTable() {
        println("+-------------------------+");
        println("+         Commands        +");
        println("+-------------------------+");
        println("Add Environment: add_env <environment name>");
        println("Add User: add_user <username> <environment name>");
        println("Add Device: add_dev <device name> <environment name>");
        println("List all environments: list_env");
        println("+-------------------------+");
        println("");
    }

}
