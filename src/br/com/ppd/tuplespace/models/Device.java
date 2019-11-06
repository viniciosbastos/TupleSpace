package br.com.ppd.tuplespace.models;

import net.jini.core.entry.Entry;

public class Device implements Entry {

    public String name;
    public Environment environment;

    public Device() {}
    public Device(String name, Environment environment) {
        this.name = name;
        this.environment = environment;
    }
    public Device(String name) {
        this.name = name;
    }
}
