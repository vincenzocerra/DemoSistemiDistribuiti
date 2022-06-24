package Worker;

import java.rmi.Remote;
import java.rmi.RemoteException;

import Client.Job;

//Interfaccia per i Worker

public interface WorkerServer extends Remote {
    Object start(Job j, Object parameters) throws RemoteException;
}