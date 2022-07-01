package Master;

import Client.ClientApp;
import Client.ServerCallback;
import Worker.WorkerServer;

/**
 * Questa classe si occupa della creazione di un Thread lato Server. Ogni Thread ha il compito di associare un'applicazione
 * ad un worker richiedendone l'esecuzione rispettando un ordine FIFO e gestendo eventuali problemi di comunicazione.
 * @author VincenzoCerra
 *
 */

public class MasterThread extends Thread {
	
	private ServerCallback client;
	private ClientApp j;
	private Object parameters;
	private SynchroListImp<ClientApp> jobQueue;
	private SynchroListImp<WorkerServer> availableWorker;
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
		this.jobQueue = master.executionQueue;
		this.availableWorker = master.availableWorkers;		
	}
	
	/**
	 * 
	 */
	
	public void run()	{	
   		while (check) {
			try {
				if(availableWorker.size()>0 && jobQueue.size()>0) {
					if (jobQueue.getFirst().equals(j)) {
						w = availableWorker.get();
						jobQueue.get();
						master.inEsecuzione.put(w, info);
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
	
