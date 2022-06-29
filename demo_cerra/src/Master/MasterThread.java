package Master;

import Client.Job;
import Client.ServerCallback;
import Worker.WorkerServer;

public class MasterThread extends Thread {
	
	private ServerCallback client;
	private Job j;
	private Object parameters;
	private SynchroList<Job> jobQueue;
	private SynchroList<WorkerServer> availableWorker;
	private WorkerServer w;
	
	private boolean check;
	private ExecInfo info;
	private MasterImp master;
	
	MasterThread(ExecInfo info, MasterImp master){
		check = true;
		this.info=info;
		this.client = info.getSc();
		this.j =info.getJ();
		this.master=master;
		this.parameters=info.getParameters();
		this.jobQueue = MasterImp.executionQueue;
		this.availableWorker = MasterImp.availableWorkers;		
	}
	
	public void run()	{	
   		while (check) {
			try {
				if(availableWorker.size()>0 && jobQueue.size()>0) {
					if (jobQueue.getFirst().equals(j)) {
						w = availableWorker.get();
						jobQueue.get();
						MasterImp.inEsecuzione.put(w, info);
						try {
						System.out.println("Master: assegno l'esecuzione dell'app "+j.getId()+" al worker "+w.getId());
						w.start(client, j, parameters);
						}catch(Exception e){
							master.handleBadDisconnection(w);
						}
						
						check = false;
					}
					else {
						//client.notifyInfo("In attesa del proprio turno");	
					}
				}
				else {
					//client.notifyInfo("In attesa del proprio turno: Non ci sono al momento Worker disponibili");
				}
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}//while
	}
}
	
