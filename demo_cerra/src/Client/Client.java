package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import Master.MasterServer;

/**
 * La classe client gestisce la creazione dei client, delle loro richieste di esecuzione di applicazioni e ricezione dei risultati
 * Essa implementa l'interfaccia ServerCallback in quanto le richieste di esecuzioni non sono bloccanti e consentono al client di 
 * effettuare altre operazioni non rimanendo in attesa del risultato.
 *  
 * @author VincenzoCerra
 *
 */

public class Client implements ServerCallback {
	private MasterServer master;
	int id;
	private int exportPort;
	
	public Client(String host, int port, int id, int executeProgramCount) throws RemoteException, NotBoundException {
		this.id=id;
		exportPort=1100;
		System.out.println("C "+id+": Running - Request Count= "+executeProgramCount);
		Registry registry = LocateRegistry.getRegistry(host, port);
		exportClass();
		master = (MasterServer) (registry.lookup("master"));
		GestoreRichieste gestore = new GestoreRichieste(this,master,executeProgramCount);
		gestore.start();
	}
	
	
	/**
	* Metodo privato utilizzato dal client per esportare la classe tramite UnicastRemoteObject.
	* Si è deciso di utilizzare questo meccanismo per non vincolare il Client ad estendere la classe Remote.
	* Inoltre è stata gestita la possibilità di avviare più esecuzioni della classe gestendo la porta utilizzata per l'export
	* 
	* @exception ExportException se la porta è già occupata.
	*/
	
	private void exportClass() throws RemoteException {
		boolean export = false;
		int c=0;
		while(export!=true) {
		try {
			int port=exportPort+c;
			UnicastRemoteObject.exportObject(this,port);
			export = true;
			}catch(ExportException e) {
				System.out.println("Errore nell'export della classe");
				c++;
				continue;
			}
		}	
	}
	
	/**
	* Implementazione del metodo presente nell'intefaccia ServerCallback che consente al MASTER di contattare il Client per comunicare
	* il risultato dell' operazione di esecuzione richiesta. E' stato pensato per consentire al client di non restare in 
	* un'attesa bloccante del risultato e di poter svolgere altre operazioni. Nello specifico consiste in una stampa che riporta l'id
	* dell'applicazione richiesta e il risultato ottenuto
	* 
	* @param int	idJob 	Indica l'id dell'applicazione eseguita dal master
	* @param Object	result 	Indica il risultato prodotto dall'esecuzione dell'applicazione

	*/

	@Override
	public void getResult(int idJob, Object result) throws RemoteException {
		System.out.println("M->C"+id+" request: "+idJob+" result: " + result);
	}
	
	/**
	* Implementazione del metodo presente nell'intefaccia ServerCallback che consente al MASTER di contattare il Client per comunicare
	* eventuali notifiche sullo stato dell'esecuzione dell'applicazione inviata. Nello specifico consiste in una stampa che riporta la Stringa
	* info comunicata dal Master
	* 
	* @param String	info 	Informazioni che il Master vuole comunicare al client
	*/
	
	@Override
	public void notifyInfo(String info) throws RemoteException {
		System.out.println(info);
	}

	/**
	* Implementazione del metodo presente nell'intefaccia ServerCallback che consente al MASTER di contattare il Client per richiedere il sui id 
	* 
	* @return int	id	Restituisce al Master l'id del Client
	* 	*/
	
	@Override
	public int getId() throws RemoteException {
		return this.id;	
	}
	
	/**
	* Metodo privato utilizzato per migliorare l'esperienza utente. Viene invocato solo nel caso di esecuzione singola del Client
	* e consente di richiedere l'invio di una nuova richiesta di esecuzione o di disconnettere il client. Tale dinamicità è possibile
	* grazie all'implementazione non bloccante delle richieste client.
	*/
	
	private void startConsole() {
		String line;
		System.out.println("CONSOLE");
		System.out.println("Digita  \"e\" per mandare una nuova richiesta");
		BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
		boolean running = true;
		
		while(running) {
			try {
				line = bReader.readLine();
				if(line.equals("q") && line != null) {
					running = false;
					bReader.close();
					System.out.println("Client disconnesso!");
				}
				else if(line.equals("e") && line != null) {
					GestoreRichieste gr = new GestoreRichieste(this,master,1);
					gr.start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		System.exit(-1);
	}
	
	
	public static void main(String[] args) throws IOException, NotBoundException {
		 BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		 boolean ok = false;
		 int port = 6000;
		 int scelta=0;
		 int client=1;
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
				 System.out.println("Inserire il numero di Client da connettere");
			 try {
					client = Integer.parseInt(br.readLine());
				}catch(NumberFormatException e) {
					System.out.println("Bisogna inserire un numero tra 2 e n !");
					continue;
				}
			 }
			 try {
				 for(int i = 0; i< client;i++) {
					 int numProg=(int)(Math.random() * (5 - 1) + 1);
					 Client cl=new Client("127.0.0.1",port,i,numProg);
					 if(scelta==1)cl.startConsole();
					ok =true;
				 }
				} catch (RemoteException e) {
					System.out.println("Nessun Master connesso a questa porta");
					ok =false;
				}
			}// while	 
	 }
}