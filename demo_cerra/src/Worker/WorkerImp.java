package Worker;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Worker extends UnicastRemoteObject implements WorkerStub{

	protected Worker() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String doTask(String command) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
}