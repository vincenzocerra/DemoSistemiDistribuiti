package Master;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MasterStub extends Remote{
	
	String submitJob() throws RemoteException;

}
