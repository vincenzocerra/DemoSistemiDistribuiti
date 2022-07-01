package Master;

import java.util.concurrent.BlockingQueue;

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
	private Object j;
	private Object parameters;
	private BlockingQueue<WorkerServer> availableWorker;
	private WorkerServer w;
	
	private ExecInfo info;
	private MasterImp master;
	
	MasterThread(ExecInfo info, MasterImp master){
		this.info=info;
		this.client = info.getSc();
		this.j =info.getJ();
		this.master=master;
		this.parameters=info.getParameters();
		this.availableWorker = master.availableWorkers2;		
	}
	
	/**
	 * 
	 */
	
	public void run()	{	
		try {
			w =availableWorker.take();
			master.inEsecuzione.put(w, info);
			try {
			System.out.println("Master: assegno l'esecuzione dell'app al worker "+w.getId());
			w.start(client, j, parameters, info.getType());
			}catch(Exception e){
				master.handleBadDisconnection(w);
			}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
