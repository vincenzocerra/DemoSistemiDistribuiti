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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import Client.ClientApp;
import Client.ServerCallback;
import Worker.WorkerServer;

/**
 * Questa Classe gestisce la creazione del Master, la connessione con i Client e con i worker, 
 * l'assegnamento dell'esecuzione delle applicazioni ai worker e la comunicazione dei risultati ai client.
 * Implementa l'interfaccia MasterServer definendo nel dettaglio le specifiche dei metodi accessibili da remoto.
 * @author VincenzoCerra
 *
 */

public class MasterImp extends UnicastRemoteObject implements MasterServer{
	
	BlockingQueue <WorkerServer>availableWorkers2;
	List<WorkerServer> workers;
	volatile Map<WorkerServer, ExecInfo> inEsecuzione;
	volatile Map<WorkerServer, Thread> scanner;
	volatile LinkedList<ServerProgram> serverProg;
	private Registry registry;
	private String lineString;
	private int id=0;
	volatile int wCount=0;
	
	/**
	 * 
	 * @param port
	 * @throws RemoteException
	 */

	public MasterImp(int port) throws RemoteException {
		super();
		System.out.println("Avvio Master");		
		
		registry = LocateRegistry.createRegistry(port);
		registry.rebind("master", this);
		
		serverProg = new LinkedList<ServerProgram>();
		serverProg.add(new ServerJavaProgram0());
		serverProg.add(new ServerJavaProgram1());
		serverProg.add(new ServerJavaProgram2());

		workers  = Collections.synchronizedList(new LinkedList<WorkerServer>());
		availableWorkers2 = new LinkedBlockingQueue<WorkerServer>(2048);
		inEsecuzione = Collections.synchronizedMap(new HashMap<WorkerServer,ExecInfo>());
		scanner = Collections.synchronizedMap(new HashMap<WorkerServer,Thread>());

		try {
			String addressString = (InetAddress.getLocalHost()).toString();
			System.out.println("Il Master e' in esecuzione su " + addressString + ", port: " + port);
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
		System.out.println("Premi 'q' per uscire o 'i' per ottenere informazioni relative ai worker e alle applicazioni");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			
			// implementazione di una breve console attraverso un BufferedReader per visualizzare il numero di WORKER o uscire
			
			try {
				lineString = br.readLine();
				if (lineString != null) {
					if (lineString.equals("q")) {
						System.out.println("Master disconnesso!");
						break;
					} else if (lineString.equals("i")) {
						System.out.println("Worker Connessi: " + workers.size() + " | App in esecuzione: "+inEsecuzione.size());
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
		availableWorkers2.remove(w);
		workers.remove(w);
		if(inEsecuzione.containsKey(w)) {
			System.out.println("M"+id+": Avvio procedura riassegnazione applicazione Worker ");
			ExecInfo info = inEsecuzione.get(w);
			inEsecuzione.remove(w);
			try {
			info.getSc().notifyInfo("Internal Problem, reprocessing your request");
			}catch(RemoteException e) {
				System.out.println("Client non disponibile");
			}
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
	public synchronized void connectWorker(WorkerServer w, int id) throws RemoteException {
		w.setId(wCount);
		WorkerScanner ws= new WorkerScanner(w,wCount,this);
		scanner.put(w, ws);
		ws.start();
		workers.add(w);
		try {
			availableWorkers2.put(w);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Master: Worker " +wCount+ " connesso!");
		wCount++;
	}
	
	/**
	 * Implementazione del metodo presente nell'interfaccia del Master che serve a disconnettere in maniera Controllata
	 * un Worker.
	 */

	@Override
	public void disconnectWorker(WorkerServer w,int id) throws RemoteException {
		workers.remove(w);
		availableWorkers2.remove(w);
		scanner.get(w).interrupt();
		System.out.println("M"+id+": W"+id+ " Disconnesso!");
		System.out.println("M"+id+": Attualmente sono disponibili: " + workers.size()+ " Worker");
		if(inEsecuzione.containsKey(w)) {
			System.out.println("M"+id+": Avvio procedura riassegnazione applicazione Worker ");
			ExecInfo info = inEsecuzione.get(w);
			inEsecuzione.remove(w);
			MasterThread gestoreTurno = new MasterThread(info,this);
			gestoreTurno.start();
			
		}
	}
	
	/**
	 * Implementazione del metodo presente nell'interfaccia. Tale metodo viene utilizzato dai client per richiedere una
	 * nuova richiesta di esecuzione di un'applicazione Client. Tale metodo delegherà ogni richiesta ricevuta ad un nuovo MasterThread
	 * che procederà a gestirla.
	 */

	@Override
	public void execClientApp(ServerCallback sc, ClientApp j, Object parameters) throws RemoteException {
		System.out.println("C"+sc.getId()+"->M"+id+" exec app Client "+j.getId());
		Object app = j;
		int type = 0;
		ExecInfo info = new ExecInfo(sc,app,parameters,type);
		MasterThread gestoreTurno = new MasterThread(info,this);
		gestoreTurno.start();

	}
	
	/**
	 * Implementazione del metodo presente nell'interfaccia. Tale metodo viene utilizzato dai client per richiedere una
	 * nuova richiesta di esecuzione di un'applicazione Server. Tale metodo delegherà ogni richiesta ricevuta ad un nuovo MasterThread
	 * che procederà a gestirla.
	 */
	
	@Override
	public void execServerApp(ServerCallback sc, int programma, Object parameters) throws RemoteException {
		System.out.println("C"+sc.getId()+"->M"+id+" exec app Server "+programma);		
		if (programma <0 || programma > serverProg.size()-1)sc.getResult(programma,"Programma non esistente");
		else{
			Object app = serverProg.get(programma);
			int type = 1;
			ExecInfo info = new ExecInfo(sc,app,parameters,type);
			MasterThread gestoreTurno = new MasterThread(info,this);
			gestoreTurno.start();
		}

	}
	
	/**
	 * Metodo presente nell'interfaccia che può essere invocato da un Worker per comunicare la risoluzione dell'applicazione assegnata.
	 * L'implementazione prevede che il Master, una volta ottenuti i risultati, procederà ad invocare la callback sul client inoltrando i risultati.
	 */
	
	@Override
	public void finishJob(ServerCallback sc,int iDJob, Object result, WorkerServer w, int wID) throws RemoteException {
		inEsecuzione.remove(w);
		try {
			availableWorkers2.put(w);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
		System.out.println("M"+id+"->C"+sc.getId()+": app "+iDJob+": "+result);
		sc.getResult(iDJob,result);
		}catch(Exception e) {
			System.out.println("Client non disponibile");
		}
	}
	
	 /**
	  * Implementazione del metodo presente nell'interfaccia che permette ai Client di chiedere quali sono le applicazioni server disponibili.
	  */
	 
	@Override
	public String getService() throws RemoteException {
		String services ="| ";
		for (ServerProgram sp : serverProg) {
			services+= sp.getId()+ " | ";
		}
		return services;
	}
	
	public void setId(int id) {
		this.id=id;
	}
	
	 public static void main(String[] args) throws IOException {
		
		 BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		 boolean ok = false;
		 int port = 6000;
		 int scelta= 0;
		 int masters=1;
		 while (ok != true) {
			 System.out.println("Esecuzione Singola da Terminale (1) o Esecuzione Multipla (2)");
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
		 if(scelta ==2) {
			 System.out.println("Inserire il numero di Master da connettere");
		 try {
				masters = Integer.parseInt(br.readLine());
			}catch(NumberFormatException e) {
				System.out.println("Bisogna inserire un numero tra 2 e n !");
				continue;
			}
		 	if( masters<2 ) {
		 		System.out.println("Bisogna inserire un numero maggiore di 2");
		 		continue;
		 	}
		 	else {
		 		ok=true;
		 	}
		 }
		 for(int i = 0; i< masters; i++) {
		 ok=false;
		 while (ok != true) {
			 System.out.println("Inserire la porta del Master "+i);
			 try {
					port = Integer.parseInt(br.readLine());
				}catch(NumberFormatException e) {
					System.out.println("Bisogna inserire un numero compreso tra !");
					continue;
				}
			 ok = true;
			 try {
					MasterImp mi= new MasterImp(port);
					mi.setId(i);
					if(scelta==1)mi.startConsole();
				} catch (RemoteException e) {
					System.out.println("Porta in uso da un altro Master");
					ok =false;
				}
			}// while	 
		 }
		}
	 }
	

}
