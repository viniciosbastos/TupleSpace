package br.com.ppd.tuplespace.service;

import br.com.ppd.tuplespace.ServiceNotFound;
import br.com.ppd.tuplespace.models.Environment;
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
    private long timeout = 60*1000;

    private JavaSpaceService() {
        init();
    }

    private void init() {
        this.space = (JavaSpace) new Lookup(JavaSpace.class).getService();
    }

    public void send(Entry entry) throws ServiceUnavailable{
        try {
            this.space.write(entry, null, timeout);
        } catch (RemoteException|TransactionException e) {
            e.printStackTrace();
            throw new ServiceUnavailable(e.getMessage());
        }
    }

    public Entry read(Entry template) throws ServiceUnavailable {
        Entry entry = null;
        try {
            entry = this.space.readIfExists(template, null, timeout);
        } catch (RemoteException|
                TransactionException|
                InterruptedException|
                UnusableEntryException e) {
            e.printStackTrace();
            throw new ServiceUnavailable(e.getMessage());
        }
        return entry;
    }

    public List<Environment> listEnvironments() {
        List envs = new LinkedList<Environment>();

        return envs;
    }

    public static JavaSpaceService getInstance() {
        if (INSTANCE == null)
            INSTANCE = new JavaSpaceService();
        return INSTANCE;
    }

    public Environment findEnvironment(String arg) throws ServiceUnavailable{
        return (Environment) read(new Environment(arg));
    }
}
