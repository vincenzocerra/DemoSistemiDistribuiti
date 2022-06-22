package Client;

import java.rmi.RemoteException;

// non conviene estendere Unicast Remote object nel client perche poco pratico

public class ClientImp implements ClientStub {

	@Override
	public String sendToMaster(String command) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
