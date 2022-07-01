package Master;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import Client.Job;
import Client.ServerCallback;
import Worker.WorkerServer;

/**
 * 
 * @author Vincenzo
 *
 */

public class MasterImp extends UnicastRemoteObject implements MasterServer{
	
	List<WorkerServer> workers;
	SynchroListImp<Job> executionQueue;
	SynchroListImp<WorkerServer> availableWorkers;
	Map<WorkerServer, ExecInfo> inEsecuzione;
	private Registry registry;
	private String lineString;


	public MasterImp(int port) throws RemoteException {
		super();
		System.out.println("Avvio Master");		
		//aggiorno le informazioni di registro
		
		registry = LocateRegistry.createRegistry(port);
		registry.rebind("master", this);
		
		workers  = Collections.synchronizedList(new LinkedList<WorkerServer>());
		availableWorkers = new SynchroListImp<WorkerServer>();
		executionQueue = new SynchroListImp<Job>();
		inEsecuzione = Collections.synchronizedMap(new HashMap<WorkerServer,ExecInfo>());
		
		try {
			String addressString = (InetAddress.getLocalHost()).toString();
			System.out.println("Il Master è in esecuzione su " + addressString + ", port: " + port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}
	/**
	* Metodo privato utilizzato per migliorare l'esperienza utente. Viene invocato solo nel caso di esecuzione singola del Master
	* e consente di verificare il numero dei worker o disconnettersi. Tale dinamicità è possibile
	* grazie ad un'implementazione non bloccante dei metodi.
	*/
	
	private void startConsole() {
		System.out.println("CONSOLE");
		System.out.println("Premi 'q' per uscire o 's' per verificare il numero di WORKER registrati");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			
			// implementazione di una breve console attraverso un BufferedReader per visualizzare il numero di WORKER o uscire
			
			try {
				lineString = br.readLine();
				if (lineString != null) {
					if (lineString.equals("q")) {
						System.out.println("Master disconnesso!");
						break;
					} else if (lineString.equals("s")) {
						System.out.println("Il Master ha a disposizione " + workers.size() + " workers");
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(-1);
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Metodo Synchronized utilizzato per gestire eventuali disconnessioni dei Worker. Nel caso in cui il Worker disconnesso aveva
	 * preso in carico l'esecuzione di un'applicazione senza ancora produrre risultato, verrà avviata la procedura di riassegnamento
	 * del job ad un altro worker.
	 * @param w	Worker disconnesso
	 * @throws RemoteException
	 */
	
	synchronized void handleBadDisconnection(WorkerServer w) throws RemoteException {
		availableWorkers.remove(w);
		workers.remove(w);
		if(inEsecuzione.containsKey(w)) {
			System.out.println("Master: Avvio procedura riassegnazione applicazione");
			ExecInfo info = inEsecuzione.get(w);
			inEsecuzione.remove(w);
			Job j = info.getJ();
			executionQueue.put(j);
			MasterThread gestoreTurno = new MasterThread(info,this);
			gestoreTurno.start();
			
		}
		
	}
	
	/**
	 * Implementazione del metodo presente nell'interfaccia del Master che serve a registrare un nuovo Worker
	 * la registrazione avviene attraverso l'aggiunta del Worker a 2 liste e l'avvio di un Thread per il monitoraggio 
	 * dell'effettiva disponibilità del Worker.
	 */
	
	@Override
	public void connectWorker(WorkerServer w, int id) throws RemoteException {
		WorkerScanner ws= new WorkerScanner(w,id,this);
		ws.start();
		workers.add(w);
		availableWorkers.put(w);
		System.out.println("Master: Worker " +id+ " connesso!");
		System.out.println("Master: Attualmente sono disponibili: " + workers.size()+ " Worker");
	}
	
	/**
	 * Implementazione del metodo presente nell'interfaccia del Master che serve a disconnettere in maniera Controllata
	 * un Worker.
	 */

	@Override
	public void disconnectWorker(WorkerServer w,int id) throws RemoteException {
		workers.remove(w);
		availableWorkers.remove(w);
		System.out.println("Master: Worker " +id+ " Disconnesso!");
		System.out.println("Master: Attualmente sono disponibili: " + workers.size()+ " Worker");
	}
	
	/**
	 * Implementazione del metodo presente nell'interfaccia. Tale metodo viene utilizzato dai client per richiedere una
	 * nuova richiesta di esecuzione di un'applicazione. Tale metodo delegherà ogni richiesta ricevuta ad un nuovo MasterThread
	 * che procederà a gestirla.
	 */

	@Override
	public void startRequest(ServerCallback sc, Job j, Object parameters) throws RemoteException {
		System.out.println("Master: ho ricevuto la richiesta di esecuzione dell'app "+j.getId()+" dal Client "+sc.getId());		
		// qui tutto deve essere preso in carico da un thread 
		// che verifichi che ci sia il worker disponibile, accodi le richieste e che avvii il medoto start di w 
		
		ExecInfo info = new ExecInfo(sc,j,parameters);
		executionQueue.put(j);
		MasterThread gestoreTurno = new MasterThread(info,this);
		gestoreTurno.start();

	}
	
	/**
	 * Metodo presente nell'interfaccia che può essere invocato da un Worker per comunicare la risoluzione dell'applicazione assegnata.
	 * L'implementazione prevede che il Master, una volta ottenuti i risultati, procederà ad invocare la callback sul client inoltrando i risultati.
	 */
	@Override
	public void finishJob(ServerCallback sc,int iDJob, Object result, WorkerServer w, int wID) throws RemoteException {
		inEsecuzione.remove(w);
		availableWorkers.put(w);
		System.out.println("Master: Il worker "+wID+" mi ha comunicato il risultato dell'app "+iDJob+": "+result+", lo inoltro al client "+sc.getId());
		sc.getResult(iDJob,result);
		System.out.println("Master: App in coda: "+executionQueue.size()+" App in esecuzione: "+inEsecuzione.size());
		
	}
	
	 public static void main(String[] args) throws IOException {
		
		 BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		 boolean ok = false;
		 int port = 6000;
		 while (ok != true) {
			 System.out.println("Inserire la porta del master");
			 try {
					port = Integer.parseInt(br.readLine());
				}catch(NumberFormatException e) {
					System.out.println("Bisogna inserire un numero compreso tra !");
					continue;
				}
			 ok = true;
			 try {
					MasterImp mi= new MasterImp(port);
					mi.startConsole();
				} catch (RemoteException e) {
					System.out.println("Porta in uso da un altro Master");
					ok =false;
				}
			}// while	 
	 }
	

}
