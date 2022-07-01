package Worker;

import java.rmi.RemoteException;
import Client.ClientApp;
import Client.ServerCallback;
import Master.MasterServer;

/**
 * Questa classe si occupa della creazione di un Thread lato Worker. Ogni Thread ha il compito di invocare il metodo
 * run dell'applicazione per esegurla seppure non conosce la sua reale implementazione.
 * Una volta ottenuto il risultato, lo comunica al master invocando il metodo finishJob
 * @author VincenzoCerra
 *
 */

public class Executer extends Thread{
	
	private ServerCallback sc;
	private ClientApp j;
	private Object parameters;
	private MasterServer master;
	private WorkerServer worker;
	private int id;
	
	public Executer(ServerCallback sc, ClientApp j, Object parameters,MasterServer master,WorkerServer worker,int id) {
		this.sc=sc;
		this.j=j;
		this.parameters=parameters;
		this.master=master;
		this.worker=worker;
		this.id=id;
	}
	
	public void run() {
		System.out.println("Worker "+id+": Sto procedendo ad eseguire l'app "+j.getId());
		 Object result = j.run(parameters);
		 int jID = j.getId();
		 System.out.println("Worker "+id+" ho calcolato l'app "+jID+": "+result+" lo comunico al Master" );
		 try {
			master.finishJob(sc,jID,result,worker,id);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			System.out.println("Worker "+id+" Master non disponibile" );
		}
		
	}

}
