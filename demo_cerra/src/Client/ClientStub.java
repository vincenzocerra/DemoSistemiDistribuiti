package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientStub extends Remote{
	
	String sendToMaster(String command) throws RemoteException;

}
