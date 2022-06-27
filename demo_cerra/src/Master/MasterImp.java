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
import java.util.UUID;

import Client.Job;
import Client.ServerCallback;
import Worker.WorkerServer;

public class MasterImp extends UnicastRemoteObject implements MasterServer{
	
	private static ArrayList<WorkerServer> workers;
	private static SynchroListImp<Job> executionQueue;
	private static SynchroListImp<WorkerServer> availableWorkers;
	private Registry registry;
	private String lineString;


	public MasterImp(int port) throws RemoteException {
		super();
		System.out.println("Avvio Master");
		
		//aggiorno le informazioni di registro
		
		registry = LocateRegistry.createRegistry(port);
		registry.rebind("master", this);
		
		workers = new ArrayList<WorkerServer>();
		availableWorkers = new SynchroListImp<WorkerServer>();
		executionQueue = new SynchroListImp<Job>();
		
		try {
			String addressString = (InetAddress.getLocalHost()).toString();
			System.out.println("Il Master è in esecuzione su " + addressString + ", port: " + port);
		} catch (UnknownHostException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	public void startConsole() {
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
		availableWorkers.put(w);
		System.out.println("Master "+serialVersionUID+": Worker" + w + " connesso!");
		System.out.println("Master "+serialVersionUID+": Attualmente sono disponibili: " + workers.size()+ " Worker");
	}

	@Override
	public void disconnectWorker(WorkerServer w) throws RemoteException {
		workers.remove(w);
		availableWorkers.remove(w);
		System.out.println("Server: Worker" + w + " Disconnesso!");
		System.out.println("Server: Attualmente sono disponibili: " + workers.size()+ " Worker");
	}

	@Override
	public void startRequest(ServerCallback sc, Job j, Object parameters) throws RemoteException {
		String cId = "";
		System.out.println("Master : ho ricevuto una richiesta di esecuzione di un'applicazione da parte del Client");		
		// qui tutto deve essere preso in carico da un thread 
		// che verifichi che ci sia il worker disponibile, accodi le richieste e che avvii il medoto start di w 
		executionQueue.put(j);
		MasterThread gestoreTurno = new MasterThread(sc, j, parameters,executionQueue,availableWorkers);
		gestoreTurno.start();

	}
	@Override
	public void finishJob(ServerCallback sc, Object result, WorkerServer w) throws RemoteException {
		availableWorkers.put(w);
		System.out.println(result);
		sc.getResult(result);
		
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
