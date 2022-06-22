package Worker;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WorkerStub extends Remote {
    String doTask(String command) throws RemoteException;
}