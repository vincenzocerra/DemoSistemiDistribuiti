package Worker;

import java.rmi.Remote;
import java.rmi.RemoteException;
import Client.ServerCallback;

/**
 * Questa interfaccia contiene tutti i metodi che possono essere invocati sul Worker da Remoto.
 * @author VincenzoCerra
 *
 */

public interface WorkerServer extends Remote {	
	/** 
	 * Metodo utilizzato dal Master per richiedere al Worker di eseguire un programma Java
	 * @param sc riferimento all'interfaccia del Client
	 * @param j riferimento all'applicazione java
	 * @param parameters paramentri dell'applicazione
	 * @param type indica la tipologia dell'applicazione (Server o Client)
	 * @throws RemoteException Se generata indica che c è un problema di connessione
	 */
    void execute(ServerCallback sc, Object j, Object parameters,int type) throws RemoteException;
    /**
     * Metodo utilizzato per ottenere l'id del worker
     * @return id del worker
     * @throws RemoteException Se generata indica che c è un problema di connessione
     */
    
    int getId()throws RemoteException;
    /**
     * Metodo utilizzato per capire se il Worker è online o offline dal Master
     * @return restituisce True se il Worker è online
     * @throws RemoteException Se generata indica che c è un problema di connessione con il Worker
     */
    boolean isOn()throws RemoteException;
    
    /**
     * Metodo utilizzato per settare l'id del Worker a piacimento del Master
     * @param id nuovo id
     * @throws RemoteException Se generata indica che c è un problema di connessione con il Worker
     */
    
    void setId(int id)throws RemoteException;
}