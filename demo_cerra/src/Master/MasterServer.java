package Master;

import java.rmi.Remote;
import java.rmi.RemoteException;

import Worker.WorkerServer;

public interface MasterServer extends Remote{
	
	String submitJob() throws RemoteException;
	
	public void connectWorker(WorkerServer w) throws RemoteException;
	
	public void disconnectWorker(WorkerServer w) throws RemoteException;
}
