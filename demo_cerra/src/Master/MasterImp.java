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

import Worker.WorkerServer;

public class MasterImp extends UnicastRemoteObject implements MasterServer{
	
	private static ArrayList<WorkerServer> workers;
	private Registry registry;
	private String lineString;


	public MasterImp(int port) throws RemoteException {
		super();
		System.out.println("Master avviato con successo");
		
		//aggiorno le informazioni di registro
		
		registry = LocateRegistry.createRegistry(port);
		registry.rebind("master", this);
		
		workers = new ArrayList<WorkerServer>();        
		
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

	@Override
	public String submitJob() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void addWorker(WorkerServer w) throws RemoteException {
		workers.add(w);
		System.out.println("Server: Worker" + w + " registrato!");
		System.out.println("Server: Attualmente sono disponibili: " + workers.size()+ " Worker");
	}

	@Override
	public void removeWorker(WorkerServer w) throws RemoteException {
		workers.remove(w);
		System.out.println("Server: Worker" + w + " rimosso!");
		System.out.println("Server: Attualmente sono disponibili: " + workers.size()+ " Worker");
	}
	
	

}
