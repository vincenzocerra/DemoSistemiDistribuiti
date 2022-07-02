package Client;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Questa interfaccia è stata pensata per evitare che un Client rimanga bloccato in attesa di una risposta
 * da parte del MASTER. Tramite l'utilizzo di queste callback esso verrà direttamente ricontattato quando il risultato
 * è stato calcolato o il server 
 * 
 * @author VincenzoCerra
 *
 */

public interface ServerCallback extends Remote {
	
	/**
	* Metodo che consente al MASTER di contattare il Client per comunicare
	* il risultato dell' operazione di esecuzione richiesta. E' stato pensato per consentire al client di non restare in 
	* un'attesa bloccante del risultato e di poter svolgere altre operazioni.
	* 
	* @param idJob Indica l'id dell'applicazione eseguita dal master
	* @param result Indica il risultato prodotto dall'esecuzione dell'applicazione

	*/
	
	public void getResult(int idJob, Object result) throws RemoteException;
	
	/**
	 * Medoto che consente al Master di contattare il client per comunicare informazioni inerenti allo stato dell'esecuzione dell'applicazione
	 * @param info Stringa che contiene le informazioni che il master vuole comunicare al client
	 * @throws RemoteException
	 */
	
	public void notifyInfo(String info) throws RemoteException;
	
	/**
	 * Medoto che consente al Master di ottenere l'id del Client
	 * @return id del Client
	 * @throws RemoteException
	 */
	
	public int getId()throws RemoteException;

}
