package Master;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SynchroList<T> extends Remote {

	void put(T t) throws RemoteException;

	T get() throws RemoteException;
	
	void remove(T t) throws RemoteException;
	
	T getFirst() throws RemoteException;

	int size() throws RemoteException;
}
