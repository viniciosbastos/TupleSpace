package br.com.ppd.tuplespace.service;

import br.com.ppd.tuplespace.models.Device;
import br.com.ppd.tuplespace.models.Environment;
import br.com.ppd.tuplespace.models.User;
import br.com.ppd.tuplespace.util.Lookup;
import net.jini.core.entry.Entry;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

public class JavaSpaceService {
    private static JavaSpaceService INSTANCE = null;

    private JavaSpace space;
    private long lease = 10*60*1000;

    private JavaSpaceService() {
        init();
    }

    private void init() {
        this.space = (JavaSpace) new Lookup(JavaSpace.class).getService();
    }

    public void send(Entry entry) throws ServiceUnavailable{
        try {
            this.space.write(entry, null, lease);
        } catch (RemoteException|TransactionException e) {
            e.printStackTrace();
            throw new ServiceUnavailable(e.getMessage());
        }
    }

    public Entry read(Entry template) throws ServiceUnavailable {
        Entry entry = null;
        try {
            entry = this.space.readIfExists(template, null, lease);
        } catch (RemoteException|
                TransactionException|
                InterruptedException|
                UnusableEntryException e) {
            e.printStackTrace();
            throw new ServiceUnavailable(e.getMessage());
        }
        return entry;
    }

    public Entry take(Entry template) throws ServiceUnavailable {
        Entry entry = null;
        try {
            entry = this.space.takeIfExists(template, null, lease);
        } catch (RemoteException|
                TransactionException|
                InterruptedException|
                UnusableEntryException e) {
            e.printStackTrace();
            throw new ServiceUnavailable(e.getMessage());
        }
        return entry;
    }

    private void write(List<? extends Entry> listEnv) throws ServiceUnavailable {
        for(Entry env : listEnv) {
            send(env);
        }
    }

    public List<Environment> listEnvironments() throws ServiceUnavailable {
        List<Environment> listEnv = new LinkedList<Environment>();

        Environment env = null;
        Environment template = new Environment();
        do {
            env = (Environment) take(template);
            if (env != null) listEnv.add(env);
        } while(env != null);
        write(listEnv);
        return listEnv;
    }

    public List<User> listUsersByEnv(String env) throws ServiceUnavailable {
        List<User> listEnv = new LinkedList<User>();

        User user = null;
        User template = new User();
        template.environment = new Environment(env);
        do {
            user = (User) take(template);
            if (user != null) listEnv.add(user);
        } while(user != null);
        write(listEnv);
        return listEnv;
    }

    public List<Device> listDevicesByEnv(String env) throws ServiceUnavailable {
        List<Device> listEnv = new LinkedList<Device>();

        Device device = null;
        Device template = new Device();
        template.environment = new Environment(env);
        do {
            device = (Device) take(template);
            if (device != null) listEnv.add(device);
        } while(device != null);
        write(listEnv);
        return listEnv;
    }

    public Environment findEnvironment(String arg) throws ServiceUnavailable{
        return (Environment) read(new Environment(arg));
    }

    public static JavaSpaceService getInstance() {
        if (INSTANCE == null)
            INSTANCE = new JavaSpaceService();
        return INSTANCE;
    }
}
