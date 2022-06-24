package Master;

import java.rmi.Remote;
import java.rmi.RemoteException;

import Worker.WorkerServer;

public interface MasterServer extends Remote{
	
	String submitJob() throws RemoteException;
	
	public void addWorker(WorkerServer w) throws RemoteException;
	
	public void removeWorker(WorkerServer w) throws RemoteException;
}
