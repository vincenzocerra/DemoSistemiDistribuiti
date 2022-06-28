package Utils;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import Client.ClientTest;
import Master.MasterImp;
import Worker.WorkerImp;

public class Launcher {

	private static final String localHost = "127.0.0.1";

    public static void main(String[] args) {
        try {
            if (args[0].equals("Master")) {
                new MasterImp(Integer.parseInt(args[1])).startConsole();
                } 
            else if (args[0].equals("Worker")){
                new WorkerImp(localHost, Integer.parseInt(args[1]),1).startConsole();
            }else if (args[0].equals("Client")) {
            	new ClientTest(localHost, Integer.parseInt(args[1]),1,2);
            	new ClientTest(localHost, Integer.parseInt(args[1]),2,3);
            	new ClientTest(localHost, Integer.parseInt(args[1]),3,4);
            } else {
            	System.err.println("Digita \"Server\" o \"Worker\" seguiti dalla porta!");
            }
        } catch (RemoteException e) {
            System.err.println(e);
        } catch (NotBoundException e) {
            System.err.println(e);
        }
        
    }

}
