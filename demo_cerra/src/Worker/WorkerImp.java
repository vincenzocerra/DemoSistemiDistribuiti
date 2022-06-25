package Worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import Client.Job;
import Client.ServerCallback;
import Master.MasterServer;

public class WorkerImp extends UnicastRemoteObject implements WorkerServer{
	
	private static final long serialVersionUID = 1L;
	/**
	 * default generated serialVersionUID
	 */
	
	private MasterServer master;
	private boolean running = true;

	public WorkerImp(String host, int port) throws RemoteException, NotBoundException {
		super();
		
		System.out.println("Worker: Sta cercando di connettersi al server: " + host + " port: " + port);
		
		try {
			Registry registry = LocateRegistry.getRegistry(host, port);
			master = (MasterServer) registry.lookup("master");
			master.connectWorker(this);

		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}
	
	public void startConsole() {
		String line;
		
		System.out.println("Connessione avvenuta con Successo");
		System.out.println("CONSOLE");
		System.out.println("Digita  \"q\" per disconnetterti");
		BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
		
		while(running) {
			try {
				line = bReader.readLine();
				if(line.equals("q") && line != null) {
					master.disconnectWorker(this);
					running = false;
					bReader.close();
					System.out.println("Worker disconnesso!");
				}
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		System.exit(-1);
	}

	public void start(ServerCallback sc, Job j, Object parameters) throws RemoteException {
		System.out.println("Worker: Sto procedendo ad eseguire l'applicazione");
		 Object result = j.run(parameters);
		 master.finishJob(sc,result,this);
		 
	}
}