package Master;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MasterImp extends UnicastRemoteObject implements MasterStub{

	protected MasterImp() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String submitJob() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
