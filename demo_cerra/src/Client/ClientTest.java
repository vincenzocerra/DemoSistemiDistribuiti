package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import Master.MasterImp;
import Master.MasterServer;
import Worker.WorkerImp;

public class ClientTest implements ServerCallback {
	private MasterServer master;
	private Job job;
	String nome;

	public ClientTest(String host, int port,String nome) throws RemoteException, NotBoundException {
		this.nome=nome;
		System.out.println("Client started!");
		Registry registry = LocateRegistry.getRegistry(host, port);
		UnicastRemoteObject.exportObject(this,1098);
		master = (MasterServer) (registry.lookup("master"));
		job = new JavaProgram();
		System.out.println("Client "+nome+" Ha inviato alcune applicazioni da eseguire");
		master.startRequest(this,job,25000);
		master.startRequest(this,job,25000);
		master.startRequest(this,job,4000); 
		master.startRequest(this,job,12000); 
		master.startRequest(this,job,1000); 
		System.out.println("Verifica Callback non bloccante");
// implementato con Callback
	}

	/*public void execute() throws RemoteException {
		// implementazione bloccante senza callback
		int result = (int) master.execute(job,1);
		System.out.println("The result is: " + result);
	}*/ 

	@Override
	public void getResult(Object result) throws RemoteException {
		System.out.println(""+nome+" : risultato: " + result);
	}
	@Override
	public void notifyInfo(String info) throws RemoteException {
		System.out.println(info);
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
				 System.out.println("Inserire il numero di Worker");
			 try {
					client = Integer.parseInt(br.readLine());
				}catch(NumberFormatException e) {
					System.out.println("Bisogna inserire un numero tra 2 e n !");
					continue;
				}
			 }
			 try {
				 for(int i = 0; i< client;i++) {
					 ClientTest cli= new ClientTest("127.0.0.1",port,""+i);
					ok =true;
				 }
				} catch (RemoteException e) {
					System.out.println("Problema");
					System.out.println(e);

					ok =false;
				}
			}// while	 
	 }
}