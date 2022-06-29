package Worker;

import java.rmi.RemoteException;

import Client.Job;
import Client.ServerCallback;
import Master.MasterServer;

public class Executer extends Thread{
	
	private ServerCallback sc;
	private Job j;
	private Object parameters;
	private MasterServer master;
	private WorkerServer worker;
	private int id;
	
	public Executer(ServerCallback sc, Job j, Object parameters,MasterServer master,WorkerServer worker,int id) {
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
			e.printStackTrace();
		}
		
	}

}
