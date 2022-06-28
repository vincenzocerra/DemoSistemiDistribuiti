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

	
	MasterThread(ServerCallback client,Job j, Object parameters, SynchroList<Job> jobQueue, SynchroList<WorkerServer> availableWorker){
		check = true;
		this.client = client;
		this.j =j;
		this.parameters=parameters;
		this.jobQueue = jobQueue;
		this.availableWorker = availableWorker;
		
	}
	
	public void run()	{	
   		while (check) {
			try {
				if(availableWorker.size()>0 && jobQueue.size()>0) {
					if (jobQueue.getFirst().equals(j)) {
						w = availableWorker.get();
						jobQueue.get();
						System.out.println("Master : assegno l'esecuzione dell'app "+j.getId()+" al worker "+w.getId());
						w.start(client, j, parameters);
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
	
