package Worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import Client.ServerCallback;
import Master.MasterServer;

/**
 * Questa Classe gestisce la creazione del Worker, la connessione con il Master 
 * e la corretta esecuzione dell'applicazione JAVA comunicatagli dal Master. Implementa l'interfaccia WorkerServer
 * definendo nel dettaglio le specifiche dei metodi accessibili da remoto.
 * @author VincenzoCerra
 *
 */

public class WorkerImp extends UnicastRemoteObject implements WorkerServer{
	
	private static final long serialVersionUID = 1L;
	/**
	 * default generated serialVersionUID
	 */
	
	private MasterServer master;
	private boolean running = true;
	int id;
	int port;
	private Executer executer;
	
	/**
	 * Costruttore di un Worker
	 * @param host indirizzo host
	 * @param port porta del registro
	 * @param id intero identificativo
	 * @throws RemoteException
	 * @throws NotBoundException
	 */

	public WorkerImp(String host, int port, int id) throws RemoteException, NotBoundException {
		super();
		this.id = id;
		this.port=port;
		
		System.out.println("Worker "+id+": Sto cercando di connettermi al server: " + host + " port: " + port);
		
		try {
			Registry registry = LocateRegistry.getRegistry(host, port);
			master = (MasterServer) registry.lookup("master");
			master.connectWorker(this,id);

		} catch (RemoteException e) {
			System.out.println("Nessun Master Disponibile sulla porta selezionata");
			System.exit(-1);
		}
		System.out.println("Connessione avvenuta con Successo");
		
	}
	
	/**
	* Metodo privato utilizzato per migliorare l'esperienza utente. Viene invocato solo nel caso di esecuzione singola del Worker
	* e consente di disconnettere il worker in maniera controllata o non controllata anche durante l'esecuzione di un'applicazione. Tale dinamicità è possibile
	* grazie ad un'implementazione non bloccante dei metodi.
	*/
	private void startConsole() {
		String line;
		System.out.println("CONSOLE");
		System.out.println("Digita  \"q\" per disconnetterti o  \"c\" per simulare un crash");
		BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
		
		while(running) {
			try {
				line = bReader.readLine();
				if(line.equals("q") && line != null) {
					try {
					master.disconnectWorker(this,id);
					}catch(RemoteException e) {
						System.out.println("il Master non e' piu' disponibile");
					}
					running = false;
					bReader.close();
					System.out.println("Worker"+id+" disconnesso!");
				}
				if(line.equals("c") && line != null) {
					System.out.println("Worker"+id+" Crash!");
					System.exit(-1);
				}
			} catch (IOException e) {           
				e.printStackTrace();
			}
		}
		System.exit(-1);
	}
	
	/**
	 * Implementazione del metodo dell'interfaccia che si occupa della esecuzione del programma ricevuto. Il tutto avviene attraverso
	 * la delega ad un Thread esecutore
	 */
	public void execute(ServerCallback sc, Object j, Object parameters, int type) throws RemoteException {
		System.out.println("M->W"+id+" execute java App");
		executer = new Executer( sc, j, parameters,master,this,id, type);
		executer.start();
		 
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
	@Override
	public boolean isOn() throws RemoteException {
		return true;
	}
	@Override
	public int getId() throws RemoteException {
		return id;
	}
	
	@Override
	public void setId(int id) throws RemoteException {
		this.id=id;
		System.out.println("W"+id+" new id");
	}
	
	 
	
	
}