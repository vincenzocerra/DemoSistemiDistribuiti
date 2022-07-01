package Worker;

import java.rmi.Remote;
import java.rmi.RemoteException;
import Client.Job;
import Client.ServerCallback;

/**
 * Questa interfaccia contiene tutti i metodi che possono essere invocati sul Worker da Remoto.
 * @author VincenzoCerra
 *
 */

public interface WorkerServer extends Remote {	
	/** 
	 * Metodo utilizzato dal Master per richiedere al Worker di eseguire il programma java comunicato dal Client
	 * @param sc riferimento all'interfaccia del Client
	 * @param j riferimento all'applicazione Client java
	 * @param parameters paramentri dell'applicazione
	 * @throws RemoteException
	 */
    void start(ServerCallback sc,Job j, Object parameters) throws RemoteException;
    /**
     * Metodo utilizzato per ottenere l'id del worker
     * @return id del worker
     * @throws RemoteException 
     */
    
    int getId()throws RemoteException;
    /**
     * Metodo utilizzato per capire se il Worker è online o offline
     * @return restituisce True se il Worker è online
     * @throws RemoteException Se generata indica che c è un problema di connessione con il Worker
     */
    boolean isOn()throws RemoteException;
}