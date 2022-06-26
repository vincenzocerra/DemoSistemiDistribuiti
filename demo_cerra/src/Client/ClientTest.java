package Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import Master.MasterServer;

public class ClientTest implements ServerCallback {
	private MasterServer master;
	private Job job;

	public ClientTest(String host, int port) throws RemoteException, NotBoundException {
		System.out.println("Client started!");
		Registry registry = LocateRegistry.getRegistry(host, port);
		UnicastRemoteObject.exportObject(this,1098);
		master = (MasterServer) (registry.lookup("master"));
		job = new JavaProgram();
		System.out.println("Client : Invio richiesta esecuzione Job");
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
		System.out.println("The result is: " + result);
	}
	@Override
	public void notifyInfo(String info) throws RemoteException {
		System.out.println(info);
	}
}