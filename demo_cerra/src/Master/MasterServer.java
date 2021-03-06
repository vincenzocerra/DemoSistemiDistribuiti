package Master;

import java.rmi.Remote;
import java.rmi.RemoteException;
import Client.ClientApp;
import Client.ServerCallback;
import Worker.WorkerServer;

/**
 * Questa interfaccia contiene tutti i metodi che possono essere invocati da Remoto sia dal Client che dal Worker.
 *
 * @author VincenzoCerra
 *
 */

public interface MasterServer extends Remote{
	
	/**
	 * Metodo che viene utilizzato dal Client per richiedere al Master l'esecuzione di un'applicazione java Client
	 * @param sc riferimento al client per la callback
	 * @param j	applicazione Java da eseguire
	 * @param parameters parametri di input dell'applicazione
	 * @throws RemoteException
	 */
	public void execClientApp(ServerCallback sc, ClientApp j, Object parameters) throws RemoteException;
	
	/**
	 * Metodo che viene utilizzato dal Client per richiedere al Master l'esecuzione di un'applicazione java Server
	 * @param sc riferimento al client per la callback
	 * @param programma id programma da eseguire
	 * @param parameters parametri di input dell'applicazione
	 * @throws RemoteException
	 */
	
	public void execServerApp(ServerCallback sc, int programma, Object parameters) throws RemoteException;
	
	/**
	 * Metodo utilizzato dal Worker per comunicare al Master il risultato ottenuto dall'esecuzione dell'applicazione
	 * @param sc riferimento al client per la callback
	 * @param idJob	id dell'applicazione eseguita
	 * @param result risultato dell'applicazione eseguita
	 * @param w	riferimento al Worker che ha eseguito l'applicazione
	 * @param wID id del Worker che ha eseguito l'applicazione
	 * @throws RemoteException
	 */
	public void finishJob(ServerCallback sc,int idJob,Object result, WorkerServer w, int wID) throws RemoteException;
	
	/**
	 * Metodo utilizzato dal Worker per connettersi al Master
	 * @param w	riferimento al Worker che ha richiesto la connessione
	 * @param id id del Worker che ha richiesto la connessione
	 * @throws RemoteException
	 */
	public void connectWorker(WorkerServer w, int id) throws RemoteException;
	
	/**
	 * Metodo utilizzato dal Worker per disconnettersi dal Master in maniera controllata
	 * @param w	riferimento al Worker che ha richiesto la disconnessione
	 * @param id id del Worker che ha richiesto la disconnessione
	 * @throws RemoteException
	 */
	
	public void disconnectWorker(WorkerServer w, int id) throws RemoteException;
	
	/**
	 * Metodo che serve ai Clients per consocere le applicazioni java che il Master espone.
	 * @return id dei servizi disponibili
	 * @throws RemoteException
	 */
	
	public String getService()throws RemoteException;
}
