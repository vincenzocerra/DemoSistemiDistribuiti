package Client;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerCallback extends Remote {
	
	// viene implementata questa interfaccia per evitare che il Client Rimanga bloccato in attesa di una risposta
	//verra infatti ricontattato direttamente dal master
	
	public void getResult(Object result) throws RemoteException;

}
