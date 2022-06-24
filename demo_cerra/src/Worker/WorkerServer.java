package Worker;

import java.rmi.Remote;
import java.rmi.RemoteException;

//Interfaccia per i Worker

public interface WorkerServer extends Remote {
    String doTask(String command) throws RemoteException;
}