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
import Master.MasterImp;
import Master.MasterServer;

public class WorkerImp extends UnicastRemoteObject implements WorkerServer{
	
	private static final long serialVersionUID = 1L;
	/**
	 * default generated serialVersionUID
	 */
	
	private MasterServer master;
	private boolean running = true;
	int id;

	public WorkerImp(String host, int port, int id) throws RemoteException, NotBoundException {
		super();
		this.id = id;
		
		System.out.println("Worker "+id+": Sto cercando di connettermi al server: " + host + " port: " + port);
		
		try {
			Registry registry = LocateRegistry.getRegistry(host, port);
			master = (MasterServer) registry.lookup("master");
			master.connectWorker(this);

		} catch (RemoteException e) {
			System.out.println("Nessun Master Disponibile sulla porta selezionata");
			System.exit(-1);
		}
		System.out.println("Connessione avvenuta con Successo");
		
	}
	public void startConsole() {
		String line;
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
	
	public int getId() {
		return id;
	}

	public void start(ServerCallback sc, Job j, Object parameters) throws RemoteException {
		System.out.println("Worker "+id+": Sto procedendo ad eseguire l'app "+j.getId());
		 Object result = j.run(parameters);
		 int jID = j.getId();
		 System.out.println("Worker "+id+" ho calcolato l'app "+jID+": "+result+" lo comunico al Master" );
		 master.finishJob(sc,jID,result,this);
		 
	}
	
	public static void main(String[] args) throws IOException, NotBoundException {
		 BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		 boolean ok = false;
		 int port = 6000;
		 int scelta=0;
		 int workers=1;
		 while (ok != true) {
			 System.out.println("Esecuzione Singola (1) o Esecuzione Multipla (2)");
			 try {
					scelta = Integer.parseInt(br.readLine());
				}catch(NumberFormatException e) {
					System.out.println("Bisogna inserire un numero tra 1 e 2 !");
					continue;
				}
			 if(scelta != 1 && scelta !=2)continue;
			 else {
				 ok = true; 
			 }
		 }
		 ok=false;
		 while (ok != true) {
			 System.out.println("Inserire la porta");
			 try {
					port = Integer.parseInt(br.readLine());
				}catch(NumberFormatException e) {
					System.out.println("Bisogna inserire un numero compreso tra !");
					continue;
				}
			 if(scelta ==2) {
				 System.out.println("Inserire il numero di Worker");
			 try {
					workers = Integer.parseInt(br.readLine());
				}catch(NumberFormatException e) {
					System.out.println("Bisogna inserire un numero tra 2 e n !");
					continue;
				}
			 }
			 try {
				 for(int i = 0; i< workers;i++) {
					WorkerImp wi= new WorkerImp("127.0.0.1",port,i);
					if(scelta==1)wi.startConsole();
					ok =true;
				 }
				} catch (RemoteException e) {
					ok =false;
				}
			}// while	 
	 }
}