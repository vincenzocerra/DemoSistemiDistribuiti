package Master;

import Client.Job;
import Client.ServerCallback;
import Worker.WorkerServer;

public class MasterThread extends Thread {
	
	private ServerCallback client;
	private Job j;
	private Object parameters;
	private SynchroList<Job> jobList;
	private SynchroList<WorkerServer> workerList;
	private WorkerServer w;
	
	private boolean check;

	
	MasterThread(ServerCallback client,Job j, Object parameters, SynchroList<Job> jobList, SynchroList<WorkerServer> workerList){
		check = true;
		this.client = client;
		this.j =j;
		this.parameters=parameters;
		this.jobList = jobList;
		this.workerList = workerList;
		
	}
	
	public void run()	{	
   		while (check) {
			try {
				if(workerList.size()>0) {
					if (jobList.getFirst().equals(j)) {
						w = workerList.get();
						jobList.get();
						//client.notifyInfo("La tua richiesta è stata presa in carico da un Worker");
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
	
