package Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import Master.MasterServer;

public class ClientTest {
	private MasterServer master;

	public ClientTest(String host, int port) throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(host, port);
		master = (MasterServer) (registry.lookup("master"));
		System.out.println("Client started!");
	}

	public void execute() throws RemoteException {
		Job job = new JavaProgram();
		int result = (int) master.execute(job,1);
		System.out.println("The result is: " + result);
	}
}