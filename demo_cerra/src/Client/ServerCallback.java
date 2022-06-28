package Client;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerCallback extends Remote {
	
	// Viene implementata questa interfaccia per evitare che il Client Rimanga bloccato in attesa di una risposta
	//verra infatti ricontattato direttamente dal master quando l'applicazione sarà stata eseguita.
	
	public void getResult(int idJob, Object result) throws RemoteException;
	
	public void notifyInfo(String info) throws RemoteException;
	
	public int getId()throws RemoteException;

}
