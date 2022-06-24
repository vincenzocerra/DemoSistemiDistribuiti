package Worker;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import Master.MasterServer;

public class WorkerImp extends UnicastRemoteObject implements WorkerServer{
	
	private static final long serialVersionUID = 1L;
	/**
	 * default generated serialVersionUID
	 */
	
	private MasterServer master;
	private boolean running = true;

	protected WorkerImp(String host, int port) throws RemoteException, NotBoundException {
		super();
		
		System.out.println("Worker: Sta cercando di connettersi al server: " + host + " port: " + port);
		
		try {
			Registry registry = LocateRegistry.getRegistry(host, port);
			master = (MasterServer) registry.lookup("master");
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}


	@Override
	public String doTask(String command) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
}