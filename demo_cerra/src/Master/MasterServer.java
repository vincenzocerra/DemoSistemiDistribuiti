package Master;

import java.rmi.Remote;
import java.rmi.RemoteException;

import Client.Job;
import Client.ServerCallback;
import Worker.WorkerServer;

public interface MasterServer extends Remote{
	
	//public Object execute(Job j, Object parameters) throws RemoteException;
	
	public void startRequest(ServerCallback sc,Job j, Object parameters) throws RemoteException;
	
	public void finishJob(ServerCallback sc,int idJob,Object result, WorkerServer w, int wID) throws RemoteException;
	
	public void connectWorker(WorkerServer w, int id) throws RemoteException;
	
	public void disconnectWorker(WorkerServer w, int id) throws RemoteException;
}
