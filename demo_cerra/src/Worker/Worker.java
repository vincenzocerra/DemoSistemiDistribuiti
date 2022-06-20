package Worker;

import java.rmi.RemoteException;

public class Worker extends java.rmi.server.UnicastRemoteObject implements WorkerStub{

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

	@Override
	public boolean saveFile(byte[] file, String filename) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public byte[] readFile(String filename) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
