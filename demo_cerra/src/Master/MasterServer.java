package Master;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MasterServer extends Remote{
	
	String submitJob() throws RemoteException;

}
