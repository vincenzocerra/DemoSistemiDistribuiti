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

public class ClientTest implements ServerCallback {
	private MasterServer master;
	int id;
	int executeProgramCount;
	private int exportPort;

	public ClientTest(String host, int port,int id, int executeProgramCount) throws RemoteException, NotBoundException {
		this.id=id;
		this.executeProgramCount=executeProgramCount;
		exportPort=1100;
		
		System.out.println("Client "+id+": Esecuzione avviata, inviera' "+executeProgramCount+ " richieste di esecuzione al Master"	);
		Registry registry = LocateRegistry.getRegistry(host, port);
		exportClass();
		master = (MasterServer) (registry.lookup("master"));
		GestoreRichieste gestore = new GestoreRichieste(this,master,executeProgramCount);
		gestore.start();
	}
	
	private void exportClass() throws RemoteException {
		boolean export = false;
		while(export!=true) {
			int c=0;
		try {
			UnicastRemoteObject.exportObject(this,exportPort+c);
			}catch(ExportException e) {
				System.out.println("Errore nell'export della classe");
				c++;
			}
		export = true;
		}	
	}

	@Override
	public void getResult(int idJob, Object result) throws RemoteException {
		System.out.println("Client "+id+": il master mi ha comunicato il risultato dell'app "+idJob+" : " + result);
	}
	@Override
	public void notifyInfo(String info) throws RemoteException {
		System.out.println(info);
	}
	@Override
	public int getId() throws RemoteException {
		return this.id;
		
	}
	
	public void setId(int id) {
		this.id = id;
		
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
					 
					 new ClientTest("127.0.0.1",port,i,3);
					ok =true;
				 }
				} catch (RemoteException e) {
					System.out.println("Nessun Master connesso a questa porta");
					ok =false;
				}
			}// while	 
	 }
}