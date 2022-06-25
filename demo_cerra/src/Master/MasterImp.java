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
import java.util.ArrayList;

import Client.Job;
import Client.ServerCallback;
import Worker.WorkerServer;

public class MasterImp extends UnicastRemoteObject implements MasterServer{
	
	private static ArrayList<WorkerServer> workers;
	private static ArrayList<WorkerServer> busyWorkers;
	private static ArrayList<WorkerServer> availableWorkers;
	private Registry registry;
	private String lineString;


	public MasterImp(int port) throws RemoteException {
		super();
		System.out.println("Master avviato con successo");
		
		//aggiorno le informazioni di registro
		
		registry = LocateRegistry.createRegistry(port);
		registry.rebind("master", this);
		
		workers = new ArrayList<WorkerServer>();        
		availableWorkers = new ArrayList<WorkerServer>();  
		
		try {
			String addressString = (InetAddress.getLocalHost()).toString();
			System.out.println("Il Master è in esecuzione su " + addressString + ", port: " + port);
		} catch (UnknownHostException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		startConsole();
	}
	
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

	/*public Object execute(Job j, Object parameters) throws RemoteException {
		//devo controllare se ci sono worker liberi
		
		System.out.println("E'stata richiesta l'esecuzione di un'applicazione");
		System.out.println("Attualmente ci sono :"+ workers.size()+" Worker disponibili");

		if(availableWorkers.size()>0) {
			WorkerServer w = availableWorkers.remove(0);
				return w.start( j, parameters);	
		}
		return -1;
	}*/
	
	
	@Override
	public void connectWorker(WorkerServer w) throws RemoteException {
		workers.add(w);
		availableWorkers.add(w);
		System.out.println("Server: Worker" + w + " connesso!");
		System.out.println("Server: Attualmente sono disponibili: " + workers.size()+ " Worker");
	}

	@Override
	public void disconnectWorker(WorkerServer w) throws RemoteException {
		workers.remove(w);
		System.out.println("Server: Worker" + w + " Disconnesso!");
		System.out.println("Server: Attualmente sono disponibili: " + workers.size()+ " Worker");
	}

	@Override
	public void startRequest(ServerCallback sc, Job j, Object parameters) throws RemoteException {
		System.out.println("Il Client si è connesso al Master richiedento l'esecuzione del JOB");
		System.out.println("Attualmente ci sono :"+ workers.size()+" Worker disponibili");
		
		if(availableWorkers.size()>0) {
			WorkerServer w = availableWorkers.remove(0);
			// in questo modo il Client non è bloccato dalla richiesta che ha effettuato
				 sc.getResult(w.start( j, parameters));	
		}

	}


}
