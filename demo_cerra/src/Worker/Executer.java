package Worker;

import java.rmi.RemoteException;
import Client.ClientApp;
import Client.ServerCallback;
import Master.MasterServer;
import Master.ServerProgram;

/**
 * Questa classe si occupa della creazione di un Thread lato Worker. Ogni Thread ha il compito di invocare il metodo
 * run dell'applicazione per esegurla seppure non conosce la sua reale implementazione.
 * Una volta ottenuto il risultato, lo comunica al master invocando il metodo finishJob
 * @author VincenzoCerra
 *
 */

public class Executer extends Thread{
	
	private ServerCallback sc;
	private Object j;
	private Object parameters;
	private MasterServer master;
	private WorkerServer worker;
	private int id;
	private int type;
	
	public Executer(ServerCallback sc, Object j, Object parameters,MasterServer master,WorkerServer worker,int id, int type) {
		this.sc=sc;
		this.j=j;
		this.parameters=parameters;
		this.master=master;
		this.worker=worker;
		this.id=id;
		this.type=type;
	}
	
	public void run() {
		if (type == 0) {
			ClientApp app = (ClientApp) j;
			System.out.println("W"+id+" start ClientApp execution ....");
			 Object result = app.run(parameters);
			 int jID = app.getId();
			 System.out.println("W"+id+"->M app "+jID+": "+result);
			 try {
				master.finishJob(sc,jID,result,worker,id);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				System.out.println("Worker "+id+" Master non disponibile" );
			}
		}
		else{
			ServerProgram app = (ServerProgram) j;
			System.out.println("W"+id+" start ServerApp execution ....");
			 Object result = app.run(parameters);
			 int jID = app.getId();
			 System.out.println("W"+id+"->M app "+jID+": "+result);
			 try {
				master.finishJob(sc,jID,result,worker,id);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				System.out.println("Worker "+id+" Master non disponibile" );
			}
		}
		
	}

}
