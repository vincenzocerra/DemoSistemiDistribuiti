package Worker;

import java.rmi.Remote;
import java.rmi.RemoteException;

import Client.Job;
import Client.ServerCallback;

//Interfaccia per i Worker

public interface WorkerServer extends Remote {
    void start(ServerCallback sc,Job j, Object parameters) throws RemoteException;
}